
import dao.InMemoryRentalDAO;
import dao.RentalDAO;
import dataobjects.*;
import logic.CustomerInteraction;
import logic.Inventory;

public class Main {

    public static void main(String[] args) {
        final RentalDAO dao = new InMemoryRentalDAO();
        final Inventory inventory = new Inventory(dao);

        Customer customer1 = new Customer("Marge Käis");
        Customer customer2 = new Customer("Mari Maasikas");
        Customer customer3 = new Customer("Jüri Juurikas");
        dao.saveCustomer(customer1);
        dao.saveCustomer(customer2);
        dao.saveCustomer(customer3);


        CustomerInteraction ci1 = new CustomerInteraction(dao,customer1);
        ci1.addRegularRental("Matrix 11", 1);
        ci1.addRegularRental("Spider man", 5);
        ci1.addRegularRental("Spider man 2", 2);
        ci1.addRegularRental("Out of africa",7);
        ci1.payWithMoney();


        System.out.println("----------------");


        CustomerInteraction ci2 = new CustomerInteraction(dao,customer2);
        ci2.addLateReturn(1000,2);
        ci2.addRegularRental("Spider man",1);
        ci2.payWithMoney();


        System.out.println("----------------");


        CustomerInteraction ci3 = new CustomerInteraction(dao,customer3);
        ci3.getCustomer().setBonusPoints(30);
        ci3.addRegularRental("Matrix 11", 1);
        ci3.payWithBonus();


        System.out.println("----------------");


        Film f1 = new Film("Interstellar", FilmType.REGULARRENTAL);
        Film f2 = new Film("Red Sparrow", FilmType.NEWRELEASE);
        Film f3 = new Film("Titanic", FilmType.OLDFILM);
        inventory.addFilm(f1);
        inventory.addFilm(f2);
        inventory.addFilm(f3);
        inventory.removeFilm(f3);
        inventory.changeFilmType(f2,FilmType.REGULARRENTAL);
        System.out.println(inventory.toStringListAllFilms());
        System.out.println();
        System.out.println(inventory.toStringListFilmsInStore());

        System.out.println("----------------");

        System.out.println(dao.findCustomers());
        System.out.println(dao.findCustomer(1000));
    }


}
