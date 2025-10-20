package gameshop;

import model.Inventory;
import model.Manager;




public class Main {
    public static void main(String[] args) {
        // Initialize inventory and manager
        Inventory inventory = new Inventory(1);
        Manager manager = new Manager(1, "Shop Manager", inventory);

        // Create and start the interactive service
        GameShopService gameShopService = new GameShopService(manager);
        gameShopService.start();
    }
}