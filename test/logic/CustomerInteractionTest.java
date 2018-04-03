package logic;

import dao.InMemoryRentalDAO;
import dao.RentalDAO;
import dataobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerInteractionTest {
    private RentalDAO dao;
    private CustomerInteraction customerInteraction;
    private Customer customer1;
    @BeforeEach
    void setUp() {
        dao = new InMemoryRentalDAO();
        customer1 = new Customer("Mari Maasikas");
        customerInteraction = new CustomerInteraction(dao,customer1);
    }


    @Test
    void addRegularRentalValid() {
        String filmName = "Matrix 11";
        int daysRentedFor = 3;
        int expectedResult = 1;

        customerInteraction.addRegularRental(filmName,daysRentedFor);

        assertEquals(expectedResult, customerInteraction.getRentals().size());
    }

    @Test
    void addRegularRentalUnknownFilm() {
        String filmName = "Matrix 12";
        int daysRentedFor = 3;
        final String expectedResult = "There are no films in the inventory named " + filmName + ".";


        try {
        customerInteraction.addRegularRental(filmName,daysRentedFor);
        }
        catch (IllegalArgumentException e){
            assertEquals(expectedResult, e.getMessage());
        }
    }

    @Test
    void addRegularRentalPositiveDays() {
        String filmName = "Matrix 11";
        int daysRentedFor = 35;
        int expectedResult = 1;

        customerInteraction.addRegularRental(filmName,daysRentedFor);

        assertEquals(expectedResult, customerInteraction.getRentals().size());
    }

    @Test
    void addRegularRentalNegativeDays() {
        String filmName = "Matrix 11";
        int daysRentedFor = -3;
        final String expectedResult = "A film can't be rented for less than one day.";

        try {
            customerInteraction.addRegularRental(filmName,daysRentedFor);
        }
        catch (IllegalArgumentException e){
            assertEquals(expectedResult, e.getMessage());
        }

    }

    @Test
    void addRegularRentalRentedOutFilm() throws IOException {
        String filmName = "Matrix 11";
        int daysRentedFor = 3;
        String expectedResult = "There currently are no copies of " + filmName  + " in the inventory.";

        //setting the availability of all the copies to "false"
        List<Film> copiesInStore = dao.findCopies(filmName);
        for(Film f: copiesInStore){
            f.setInStore(false);
        }

        try {
            customerInteraction.addRegularRental(filmName,daysRentedFor);
        }
        catch (IllegalArgumentException e){
            assertEquals(expectedResult, e.getMessage());
        }
    }

    @Test
    void addRegularRentalCalculateTotal() {
        String filmName = "Matrix 11";
        int daysRentedFor = 3;
        int expectedResult = 12;

        customerInteraction.addRegularRental(filmName,daysRentedFor);

        assertEquals(expectedResult, customerInteraction.getTotal());
    }

    @Test
    void addLateReturnValidID() {
        Film film = dao.findAllFilms().get(0);
        int filmID = film.getFilmID();
        film.setInStore(false);
        int daysLate = 3;
        int expectedResult = 1;

        customerInteraction.addLateReturn(filmID,daysLate);

        assertEquals(expectedResult, customerInteraction.getRentals().size());
    }

    @Test
    void addLateReturnUnknownID() {
        int filmID = Integer.MAX_VALUE;
        int daysLate = 3;
        final String expectedResult = "There are no films in the inventory with the ID " + filmID + ".";

        try {
            customerInteraction.addLateReturn(filmID,daysLate);
        }
        catch (IllegalArgumentException e){
            assertEquals(expectedResult, e.getMessage());
        }
    }

    @Test
    void addLateReturnAlreadyInStore() throws IOException {
        Film film = dao.findAllFilms().get(0);
        int filmID = film.getFilmID();
        film.setInStore(true);
        int daysLate = 3;
        String expectedResult = "The film with the ID " + filmID + " is already in store.";

        try {
            customerInteraction.addLateReturn(filmID,daysLate);
        }
        catch (IllegalArgumentException e){
            assertEquals(expectedResult, e.getMessage());
        }
    }

    @Test
    void addLateReturnNegativeDays() {
        Film film = dao.findAllFilms().get(0);
        int filmID = film.getFilmID();
        film.setInStore(false);
        int daysLate = -3;
        final String expectedResult = "A film can't be late for less than one day.";

        try {
            customerInteraction.addLateReturn(filmID,daysLate);
        }
        catch (IllegalArgumentException e){
            assertEquals(expectedResult, e.getMessage());
        }
    }

    @Test
    void payWithMoneyValid() {
        //it is known that Spider man is a regular rental, therefore the amount of added bonus points
        //should be 1
        String name = "Spider man";
        int daysRentedFor = 3;
        customer1.setBonusPoints(0);
        int expectedResult = 1;

        customerInteraction.addRegularRental(name,daysRentedFor);
        customerInteraction.payWithMoney();

        assertEquals(expectedResult,customer1.getBonusPoints());
    }

    @Test
    void payWithMoneyNoRentals() throws IOException {
        String expectedResult = "Nothing to pay for (no rentals added)." + System.lineSeparator();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        customerInteraction.payWithMoney();

        outContent.close();
        assertEquals(expectedResult, outContent.toString());
    }

    @Test
    void payWithMoneyNewReleaseBonus() {
        //it is known that Matrix 11 is a new release, therefore the amount of added bonus points
        //should be 2
        String name = "Matrix 11";
        int daysRentedFor = 3;
        customer1.setBonusPoints(0);
        int expectedResult = 2;

        customerInteraction.addRegularRental(name,daysRentedFor);
        customerInteraction.payWithMoney();

        assertEquals(expectedResult,customer1.getBonusPoints());
    }

    @Test
    void payWithBonusValid() {
        String name = "Matrix 11";
        int daysRentedFor = 3;
        customer1.setBonusPoints(25);
        int expectedResult = 0;

        customerInteraction.addRegularRental(name,daysRentedFor);
        customerInteraction.payWithBonus();

        assertEquals(expectedResult,customer1.getBonusPoints());
    }

    @Test
    void payWithBonusNoRentals() {
        customer1.setBonusPoints(25);
        int expectedResult = 25;

        customerInteraction.payWithBonus();

        assertEquals(expectedResult,customer1.getBonusPoints());
    }

    @Test
    void payWithBonusMultipleFilms() throws IOException {
        String name1 = "Matrix 11";
        String name2 = "Matrix 11";
        int daysRentedFor1 = 3;
        int daysRentedFor2 = 3;
        customer1.setBonusPoints(50);
        String expectedResult = "Only one New release film can be purchased with bonus at a time.";

        try {
            customerInteraction.addRegularRental(name1,daysRentedFor1);
            customerInteraction.addRegularRental(name2,daysRentedFor2);
            customerInteraction.payWithBonus();
        }
        catch (RuntimeException e){
            assertEquals(expectedResult, e.getMessage());
        }


    }

    @Test
    void payWithBonusWrongType() throws IOException {
        //Spider man is known to be regular rental
        String name = "Spider man";
        int daysRentedFor = 3;
        customer1.setBonusPoints(25);
        String expectedResult = "Only New release films can be payed for with bonus points.";

        try {
            customerInteraction.addRegularRental(name,daysRentedFor);
            customerInteraction.payWithBonus();
        }
        catch (RuntimeException e){
            assertEquals(expectedResult, e.getMessage());
        }

    }

    @Test
    void payWithBonusNotEnoughBonus() throws IOException {
        String name = "Matrix 11";
        int daysRentedFor = 3;
        customer1.setBonusPoints(20);
        String expectedResult = "The customer doesn't have sufficient bonus points.";

        try {
            customerInteraction.addRegularRental(name,daysRentedFor);
            customerInteraction.payWithBonus();
        }
        catch (RuntimeException e){
            assertEquals(expectedResult, e.getMessage());
        }
    }

    @Test
    void toStringWithMoneyLateReturn(){
        Film film = dao.findAllFilms().get(0);
        int filmID = film.getFilmID();
        film.setInStore(false);
        int daysLate = 3;
        customerInteraction.addLateReturn(filmID,daysLate);
        String expectedOutput = film + " " + daysLate + " extra days 12 EUR" + System.lineSeparator() + "Total late charge : " +
                customerInteraction.getTotal() + " EUR";

        assertEquals(expectedOutput, customerInteraction.toStringWithMoney());

    }

    @Test
    void toStringWithMoneyRegularRental(){
        String filmName = "Matrix 11";
        int daysRentedFor = 3;
        customerInteraction.addRegularRental(filmName,daysRentedFor);
        String expectedOutput = filmName + "(New Release) " + daysRentedFor + " days 12 EUR" + System.lineSeparator() + "Total price : " +
                customerInteraction.getTotal() + " EUR";

        assertEquals(expectedOutput, customerInteraction.toStringWithMoney());
    }


}