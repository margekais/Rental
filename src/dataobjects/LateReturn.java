package dataobjects;

public class LateReturn extends Rental {
    private int daysLate;

    public LateReturn(Customer customer, Film film, int daysLate) {
        super(customer, film);
        this.daysLate = daysLate;
        this.sum = calculateSum();
    }

    public int calculateSum(){
        int sum;
        switch (film.getType()){

            case NEWRELEASE:
                sum = PREMIUM_PRICE * daysLate;
                break;

            case REGULARRENTAL:
                    sum = BASIC_PRICE * daysLate;
                    break;

            case OLDFILM:
                    sum =  BASIC_PRICE * daysLate;
                    break;

            default: throw new RuntimeException();
        }
        return sum;
    }

    public String toString(){
        return film + " " + daysLate + " extra days " + sum + " EUR";
    }
}
