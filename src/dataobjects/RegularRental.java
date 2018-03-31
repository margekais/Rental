package dataobjects;

public class RegularRental extends Rental {
    private int daysRentedFor;

    public RegularRental(Customer customer, Film film, int daysRentedFor) {
        super(customer, film);
        this.daysRentedFor = daysRentedFor;
        this.sum = calculateSum();
    }

    public int calculateSum(){
        int sum;
        switch (film.getType()){
            case NEWRELEASE:
                sum = PREMIUM_PRICE * daysRentedFor;
                break;

            case REGULARRENTAL:
                if(daysRentedFor<=3){
                    sum = BASIC_PRICE;
                    break;
                }
                else{
                    sum = BASIC_PRICE * (daysRentedFor-2);
                    break;
                }

            case OLDFILM:
                if(daysRentedFor<=5){
                    sum = BASIC_PRICE;
                    break;
                }
                else{
                    sum = BASIC_PRICE * (daysRentedFor - 4);
                    break;
                }
            default: throw new RuntimeException();
        }
        return sum;
    }

    public String toString(){
        return film + " " + daysRentedFor + " days " + sum + " EUR";
    }
}
