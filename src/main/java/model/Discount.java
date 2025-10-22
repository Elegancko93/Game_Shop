package model;

public class Discount {
    private int discountID;
    private double discountRate = 10.0;
    private boolean used;
    private Customer customer;
    private static int discountCounter = 1;

    public Discount(Customer customer) {
        this.discountID = discountCounter++;
        this.customer = customer;
        this.used = false;
    }

    public double applyToPurchase(Purchase purchase) {
        if (!used) {
            this.used = true;
            customer.setDiscountAvailable(false);
            double originalPrice = purchase.getPurchasedGame().getPrice();
            double discountAmount = originalPrice * (discountRate / 100);
            return originalPrice - discountAmount;
        }
        return purchase.getPurchasedGame().getPrice();
    }

    public void markUsed() {
        this.used = true;
        customer.setDiscountAvailable(false);
    }

    // Getters
    public double getDiscountRate() {
        return discountRate;
    }

    public boolean isUsed() {
        return used;
    }
}