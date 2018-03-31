package dao;

import dataobjects.*;

import java.util.ArrayList;
import java.util.List;

public class InMemoryRentalDAO implements RentalDAO {
    private List<Film> films;
    private List<Customer> customers;
    private List<Rental> rentals;

    public InMemoryRentalDAO() {
        List<Film> films = new ArrayList<>();
        films.add(new Film("Matrix 11", FilmType.NEWRELEASE));
        films.add(new Film("Matrix 11", FilmType.NEWRELEASE));
        films.add(new Film("Spider man", FilmType.REGULARRENTAL));
        films.add(new Film("Spider man", FilmType.REGULARRENTAL));
        films.add(new Film("Spider man 2",FilmType.REGULARRENTAL));
        films.add(new Film("Out of africa", FilmType.OLDFILM));
        this.films = films;
        this.customers = new ArrayList<>();
        this.rentals = new ArrayList<>();
    }

    @Override
    public List<Film> findAllFilms() {
        return films;
    }

    @Override
    public List<Film> findFilmsInStore() {
        List<Film> filmsInStore = new ArrayList<>();
        for(Film f: films){
            if(f.isInStore()){
                filmsInStore.add(f);
            }
        }
        return filmsInStore;
    }

    @Override
    public Film findFilm(int filmID) {
        for(Film f: films){
            if(f.getFilmID()==filmID){
                return f;
            }
        }
        return null;
    }

    @Override
    public List<Film> findCopies(String name) {
        List<Film> copies = new ArrayList<>();
        for(Film f: films){
            if(f.getName().equals(name)){
                copies.add(f);
            }
        }
        return copies;
    }

    @Override
    public Customer findCustomer(int customerID) {
        for(Customer c: customers){
            if(c.getCustomerID()==customerID){
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Customer> findCustomers() {
        return customers;
    }

    @Override
    public Rental findRental(int rentalID) {
        for(Rental r: rentals){
            if(r.getRentalID()==rentalID){
                return r;
            }
        }
        return null;
    }

    @Override
    public List<Rental> findRentals() {
        return rentals;
    }

    @Override
    public void saveRental(Rental rental) {
        if(rental instanceof RegularRental){
            rental.getFilm().setInStore(false);
            rentals.add(rental);
        }
        else{
            rental.getFilm().setInStore(true);
            rentals.add(rental);
        }
    }

    @Override
    public void saveCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public void saveFilm(Film film) {
        films.add(film);
    }

    @Override
    public void removeFilm(Film film) {
        films.remove(film);
    }

    @Override
    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }
}
