package logic;

import dao.RentalDAO;
import dataobjects.*;

import java.util.ArrayList;
import java.util.List;

public class CustomerInteraction {
    private final RentalDAO dao;
    private Customer customer;
    private List<Rental> rentals;
    private int total;

    public CustomerInteraction(RentalDAO dao, Customer customer) {
        this.dao = dao;
        this.customer = customer;
        this.rentals = new ArrayList<>();
        this.total = 0;
    }


    public int getTotal() {
        return total;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public String toStringWithMoney(){
        StringBuilder sb = new StringBuilder();
        boolean containsLateRental = false;

        for(Rental r: rentals){
            if(r instanceof LateReturn){
                containsLateRental = true;
            }
            sb.append(r.toString() + System.getProperty("line.separator"));
        }

        if(containsLateRental){
            sb.append("Total late charge : " + total + " EUR");
        }
        else{
            sb.append("Total price : " + total + " EUR");
        }

        return sb.toString();
    }

    public String toStringWithBonus(){
        StringBuilder sb = new StringBuilder();
        sb.append(rentals.get(0));
        sb.append(" Paid with 25 Bonus points" + System.lineSeparator());
        sb.append("Total price: 0 EUR" + System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Remaining Bonus points: " + customer.getBonusPoints());
        return sb.toString();
    }

    public void addRegularRental(String filmName, int daysRentedFor) throws IllegalArgumentException{

        if(daysRentedFor<=0){
            throw new IllegalArgumentException("A film can't be rented for less than one day.");
        }

        //try accessing copies of the film with the input filmName
        List<Film> copies = dao.findCopies(filmName);

        try{
            //throw a NullPointerException if the list of copies is empty
            Film copy = copies.get(0);

            //check if at least one of the copies of this film is currently in the store
            Film rentable = null;
            for(Film f: copies){
                if(f.isInStore()){
                    rentable = f;
                    break;
                }
            }

            //check if a copy was found
            if(!(rentable==null)){
                Rental rental = new RegularRental(this.customer,rentable,daysRentedFor);
                total += rental.getSum();
                rentals.add(rental);
                //rental is saved only when paying so it is presumed
                //that one customer won't rent several copies of the same film
                dao.saveRental(rental);
            }
            else{
                throw new IllegalArgumentException("There currently are no copies of " + filmName  + " in the inventory.");
            }
        }
        catch(NullPointerException e){
            throw new IllegalArgumentException("There are no films in the inventory named " + filmName + ".");
        }
    }

    public void addLateReturn(int filmID, int daysLate) throws IllegalArgumentException{

        if(daysLate<=0){
            throw new IllegalArgumentException("A film can't be late for less than one day.");
        }

        Film film = dao.findFilm(filmID);

        //check if the film exists in the store and it actually isn't in stock before initializing
        //the return to handle incorrect inputs
        if(!(film==null)){
            if(!film.isInStore()){
                Rental rental = new LateReturn(this.customer,film,daysLate);
                this.total += rental.getSum();
                this.rentals.add(rental);
                dao.saveRental(rental);
            }
            else{
                throw  new IllegalArgumentException("The film with the ID " + filmID + " is already in store.");
            }
        }
        else{
            throw new IllegalArgumentException("There are no films in the inventory with the ID " + filmID + ".");
        }

    }

    public void payWithMoney(){

        if(rentals.size()==0){
            System.out.println("Nothing to pay for (no rentals added).");
        }
        else{
            for(Rental r: rentals){
                if(r instanceof RegularRental){

                    dao.saveRental(r);

                    if(r.getFilm().getType()==FilmType.NEWRELEASE){
                        customer.setBonusPoints(2);
                    }
                    else{
                        customer.setBonusPoints(1);
                    }
                }
            }
        System.out.println(toStringWithMoney());
        }
    }

    public void payWithBonus(){

        if(rentals.size()==0){
            System.out.println("Nothing to pay for (no rentals added).");
        }
        else{
            //check  if only one film is wished to pay for
            if(rentals.size()==1){
                Rental rental = rentals.get(0);
                Film rentable = rental.getFilm();

                //check if the film is of type New release
                if(rentable.getType()==FilmType.NEWRELEASE){
                    if(customer.getBonusPoints()>=25){
                        dao.saveRental(rental);
                        customer.setBonusPoints(-25);
                    }
                    else{
                        throw new RuntimeException("The customer doesn't have sufficient bonus points.");
                    }
                }
                else{
                    throw new RuntimeException("Only New release films can be payed for with bonus points.");
                }
            }
            else{
                throw new RuntimeException("Only one New release film can be purchased with bonus at a time.");

            }
            System.out.println(toStringWithBonus());
        }
    }

}
