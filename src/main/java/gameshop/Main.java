package gameshop;

import model.Inventory;
import model.Manager;

public class Main {
    public static void main(String[] args) {
        GameShopService gameShopService = new GameShopService();
        gameShopService.start();
    }
}