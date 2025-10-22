package model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerID;
    private String name;
    private String address;
    private boolean discountAvailable;
    private List<Purchase> purchaseHistory;

    public Customer(int customerID, String name, String address) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.discountAvailable = false;
        this.purchaseHistory = new ArrayList<>();
    }

    public TradeIn tradeIn(Game game) {
        this.discountAvailable = true;
        return new TradeIn(game, this);
    }

    public Purchase purchaseGame(Game game) {
        Purchase purchase = new Purchase(game, this);
        purchaseHistory.add(purchase);
        return purchase;
    }

    public List<Purchase> getHistory() {
        return new ArrayList<>(purchaseHistory);
    }

    // Getters and setters
    public int getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public boolean isDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(boolean discountAvailable) {
        this.discountAvailable = discountAvailable;
    }
}
