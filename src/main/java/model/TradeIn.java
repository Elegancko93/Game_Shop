package model;

import java.util.Date;

public class TradeIn {
    private int tradeInID;
    private Date date;
    private Game tradedGame;
    private Customer customer;
    private static int tradeInCounter = 1;

    public TradeIn(Game tradedGame, Customer customer) {
        this.tradeInID = tradeInCounter++;
        this.date = new Date();
        this.tradedGame = tradedGame;
        this.customer = customer;
    }

    public void processTradeIn() {
        System.out.println("Trade-in processed for customer: " + customer.getName() + ", Game: " + tradedGame.getName());
        customer.setDiscountAvailable(true);
    }

    public Discount applyDiscount() {
        return new Discount(customer);
    }

    // Getters
    public Game getTradedGame() {
        return tradedGame;
    }

    public Customer getCustomer() {
        return customer;
    }
}