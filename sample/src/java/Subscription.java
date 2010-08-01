public class Subscription {

    private int price;
    private int length;

    public Subscription(int price, int length) {
        this.price = price;
        this.length = length;
    }

    public double getPricePerMonth() {
        double r = (double) price / (double) length;
        return r ;
    }

}
