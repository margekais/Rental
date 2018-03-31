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

    public void setTotal(int total) {
        this.total = total;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
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

    public void addRegularRental(String name, int daysRentedFor){

        //try accessing copies of the film with the input name
        try{
            List<Film> copies = dao.findCopies(name);

            //check if at least one of the copies of this film is currently in the store
            Film rentable = null;
            for(Film f: copies){
                if(f.isInStore()){
                    rentable = f;
                    break;
                }
            }

            if(!(rentable==null)){
                Rental rental = new RegularRental(this.customer,rentable,daysRentedFor);
                rentals.add(rental);
                total += rental.getSum();
            }
            else{
                throw new RuntimeException("There currently are no copies of " + name  + " in the inventory.");
            }
        }
        catch (NullPointerException e){
            throw new RuntimeException("There are no films in the inventory named " + name + ".");
        }
    }

    public void addLateReturn(int filmID, int daysLate){

        Film film = dao.findFilm(filmID);

        /*check if the film exists in the store and it actually isn't in stock before initializing
            the return return to handle incorrect inputs*/
        if(!(film==null)){
            if(!film.isInStore()){
                Rental rental = new LateReturn(this.customer,film,daysLate);
                rentals.add(rental);
                total += rental.getSum();
            }
            else{
                throw new RuntimeException("The film with the ID " + filmID + " is in store.");
            }
        }
        else{
            throw new RuntimeException("There are no films in the inventory with the ID " + filmID + ".");
        }

    }


    public void payWithMoney(){
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

    public void payWithBonus(){

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
                throw new RuntimeException("Only New release films can be payed for with bonus points");
            }
        }
        else{
            throw new RuntimeException("Only one New release film can be purchased with bonus at a time.");

        }
        System.out.println(toStringWithBonus());
    }

}
