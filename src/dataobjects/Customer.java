package dataobjects;

import java.util.concurrent.atomic.AtomicInteger;

public class Customer {
    private String name;
    private int customerID;
    private int bonusPoints;

    //AtomicInteger is used as it is thread safe and
    //the getAndIncrement method is atomic
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);


    public Customer(String name) {
        this.name = name;
        customerID = ID_GENERATOR.getAndIncrement();
        this.bonusPoints = 0;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(int bonusPoints) {
        this.bonusPoints += bonusPoints;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", customerID=" + customerID +
                ", bonusPoints=" + bonusPoints +
                '}';
    }
}
