package model;

public class Manager {
    private int managerID;
    private String name;
    private Inventory inventory;

    public Manager(int managerID, String name, Inventory inventory) {
        this.managerID = managerID;
        this.name = name;
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
    public Inventory getInventory() {
        return inventory;
    }
}