package model;

import java.util.Date;

public class Purchase {
    private int purchaseID;
    private Date date;
    private Game purchasedGame;
    private Customer customer;
    private static int purchaseCounter = 1;

    public Purchase(Game purchasedGame, Customer customer) {
        this.purchaseID = purchaseCounter++;
        this.date = new Date();
        this.purchasedGame = purchasedGame;
        this.customer = customer;
    }

    public void completedPurchase() {
        System.out.println("Purchase completed for customer: " + customer.getName() + ", Game: " + purchasedGame.getName());
    }

    public String getPurchaseDetails() {
        return "Purchase ID: " + purchaseID + " - Game: " + purchasedGame.getName() + " - Date: " + date;
    }

    // Getters
    public Game getPurchasedGame() {
        return purchasedGame;
    }

    public Customer getCustomer() {
        return customer;
    }
}