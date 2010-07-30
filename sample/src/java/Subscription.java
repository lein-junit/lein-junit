public class Subscription {

    private int price;
    private int length;

    public Subscription(int p, int n) {
        price = p ;
        length = n ;
    }

    public double pricePerMonth() {
        double r = (double) price / (double) length;
        return r ;
    }

    public void cancel() { 
        length = 0 ; 
    }

}
