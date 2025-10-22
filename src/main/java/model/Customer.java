package model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerID;
    private String name;
    private String surname;
    private String address;
    private boolean discountAvailable;
    private List<Purchase> purchaseHistory;

    public Customer(int customerID, String name, String surname, String address) {
        this.customerID = customerID;
        this.name = name;
        this.surname = surname;
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
    public int getCustomerID() { return customerID; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public boolean isDiscountAvailable() { return discountAvailable; }
    public void setDiscountAvailable(boolean discountAvailable) { this.discountAvailable = discountAvailable; }

    // Display customer info
    public String getCustomerInfo() {
        return String.format("Customer ID: %d | Name: %s %s | Address: %s | Discount Available: %s",
                customerID, name, surname, address, discountAvailable ? "Yes" : "No");
    }
}