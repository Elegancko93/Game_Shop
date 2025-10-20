package model;

public class Game {
    private int gameID;
    private String name;
    private String console;
    private int year;
    private int quantity;
    private double price;

    public Game(int gameID, String name, String console, int year, int quantity, double price) {
        this.gameID = gameID;
        this.name = name;
        this.console = console;
        this.year = year;
        this.quantity = quantity;
        this.price = price;
    }

    public String getDetails() {
        return String.format("ID: %d | %s | %s | %d | Qty: %d | £%.2f",
                gameID, name, console, year, quantity, price);
    }

    public void updateQuantity(int qty) {
        this.quantity = qty;
    }

    // Getters and setters
    public int getGameID() { return gameID; }
    public String getName() { return name; }
    public String getConsole() { return console; }
    public int getYear() { return year; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
}