package model;

public class Manager {
    private int managerID;
    private String name;
    private String surname;
    private String address;
    private Inventory inventory;

    public Manager(int managerID, String name, String surname, String address, Inventory inventory) {
        this.managerID = managerID;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.inventory = inventory;
    }

    public boolean updateStock(int gameID, int qty) {
        return inventory.updateGameQuantity(gameID, qty);
    }

    public void displayInventory() {
        System.out.println("\n=== Inventory ===");
        if (inventory.listAllGames().isEmpty()) {
            System.out.println("No games in inventory.");
        } else {
            for (Game game : inventory.listAllGames()) {
                System.out.println(game.getDetails());
            }
        }
    }

    public Game displayGame(String name) {
        return inventory.getGame(name);
    }

    // Getters
    public int getManagerID() { return managerID; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public Inventory getInventory() { return inventory; }

    // Display manager info
    public String getManagerInfo() {
        return String.format("Manager ID: %d | Name: %s %s | Address: %s",
                managerID, name, surname, address);
    }
}