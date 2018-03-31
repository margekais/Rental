package dao;

import dataobjects.Customer;
import dataobjects.Film;
import dataobjects.Rental;

import java.util.List;

public interface RentalDAO {

    List<Film> findAllFilms();

    List<Film> findFilmsInStore();

    /*searches from all the films in the store not only from the
        ones in store at the moment*/
    Film findFilm(int filmID);

    //used for finding copies of the same film
    List<Film> findCopies(String name);

    Customer findCustomer(int customerID);

    List<Customer> findCustomers();

    Rental findRental(int rentalID);

    List<Rental> findRentals();

    void saveRental(Rental rental);

    void saveCustomer(Customer customer);

    void saveFilm(Film film);

    void removeFilm(Film film);

    void removeCustomer(Customer customer);

}
