package dataobjects;

import java.util.concurrent.atomic.AtomicInteger;

public class Rental {
    private int rentalID;
    private Customer customer;
    Film film;
    int sum;

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);

    final int PREMIUM_PRICE = 4;
    final int BASIC_PRICE = 3;


    public Rental(Customer customer, Film film) {
        this.rentalID = ID_GENERATOR.getAndIncrement();
        this.customer = customer;
        this.film = film;
    }

    public int getRentalID() {
        return rentalID;
    }

    public void setRentalID(int rentalID) {
        this.rentalID = rentalID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

}
