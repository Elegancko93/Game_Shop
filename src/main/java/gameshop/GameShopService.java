package gameshop;


import model.*;
import java.util.Scanner;
import java.util.List;

public class GameShopService {
    private Scanner scanner;
    private Manager manager;
    private Customer currentCustomer;

    public GameShopService(Manager manager) {
        this.scanner = new Scanner(System.in);
        this.manager = manager;
        this.currentCustomer = new Customer(1, "Default Customer", "123 Main St");
    }

    public void start() {
        System.out.println("=== Welcome to Game Shop Management System ===");

        while (true) {
            displayMainMenu();
            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1:
                    manageInventory();
                    break;
                case 2:
                    handlePurchase();
                    break;
                case 3:
                    handleTradeIn();
                    break;
                case 4:
                    viewInventory();
                    break;
                case 5:
                    viewCustomerInfo();
                    break;
                case 6:
                    System.out.println("Thank you for using Game Shop Management System!");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Manage Inventory");
        System.out.println("2. Purchase Game");
        System.out.println("3. Trade-in Game");
        System.out.println("4. View Inventory");
        System.out.println("5. View Customer Info");
        System.out.println("6. Exit");
    }

    private void manageInventory() {
        while (true) {
            System.out.println("\n=== INVENTORY MANAGEMENT ===");
            System.out.println("1. Add New Game");
            System.out.println("2. Update Game Quantity");
            System.out.println("3. Remove Game");
            System.out.println("4. Back to Main Menu");

            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1:
                    addNewGame();
                    break;
                case 2:
                    updateGameQuantity();
                    break;
                case 3:
                    removeGame();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void addNewGame() {
        System.out.println("\n=== ADD NEW GAME ===");

        System.out.print("Enter game name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Platform: ");
        String console = scanner.nextLine();

        int year = getIntInput("Enter release year: ");
        int quantity = getIntInput("Enter quantity: ");
        double price = getDoubleInput("Enter price: ");

        if (!Stock.checkLimit(quantity)) {
            System.out.println("Error: Quantity exceeds maximum limit of " + Stock.getMaxLimit());
            return;
        }

        Game newGame = new Game(0, name, console, year, quantity, price);
        if (manager.getInventory().addGame(newGame)) {
            System.out.println("Game added successfully!");
        } else {
            System.out.println("Error: Game with same name and console already exists!");
        }
    }

    private void updateGameQuantity() {
        System.out.println("\n=== UPDATE GAME QUANTITY ===");
        manager.displayInventory();

        int gameID = getIntInput("Enter Game ID to update: ");
        Game game = manager.getInventory().getGameByID(gameID);

        if (game == null) {
            System.out.println("Game not found!");
            return;
        }

        System.out.println("Current quantity: " + game.getQuantity());
        int newQuantity = getIntInput("Enter new quantity: ");

        if (manager.updateStock(gameID, newQuantity)) {
            System.out.println("Quantity updated successfully!");
        } else {
            System.out.println("Error: Quantity exceeds maximum limit of " + Stock.getMaxLimit());
        }
    }

    private void removeGame() {
        System.out.println("\n=== REMOVE GAME ===");
        manager.displayInventory();

        int gameID = getIntInput("Enter Game ID to remove: ");

        if (manager.getInventory().removeGame(gameID)) {
            System.out.println("Game removed successfully!");
        } else {
            System.out.println("Game not found!");
        }
    }

    private void handlePurchase() {
        System.out.println("\n=== PURCHASE GAME ===");
        manager.displayInventory();

        List<Game> availableGames = manager.getInventory().getGames();
        if (availableGames.isEmpty()) {
            System.out.println("No games available for purchase!");
            return;
        }

        int gameID = getIntInput("Enter Game ID to purchase: ");
        Game game = manager.getInventory().getGameByID(gameID);

        if (game == null) {
            System.out.println("Game not found!");
            return;
        }

        if (game.getQuantity() <= 0) {
            System.out.println("Sorry, this game is out of stock!");
            return;
        }

        double finalPrice = game.getPrice();

        // Apply discount if available
        if (currentCustomer.isDiscountAvailable()) {
            Purchase tempPurchase = new Purchase(game, currentCustomer);
            Discount discount = new Discount(currentCustomer);
            finalPrice = discount.applyToPurchase(tempPurchase);
            System.out.printf("10%% discount applied! Original price: $%.2f, Final price: $%.2f\n",
                    game.getPrice(), finalPrice);
        } else {
            System.out.printf("Price: $%.2f\n", finalPrice);
        }

        System.out.print("Confirm purchase? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            Purchase purchase = currentCustomer.purchaseGame(game);
            purchase.completedPurchase();

            // Update inventory
            manager.updateStock(gameID, game.getQuantity() - 1);
            System.out.println("Purchase completed successfully!");
        } else {
            System.out.println("Purchase cancelled.");
        }
    }

    private void handleTradeIn() {
        System.out.println("\n=== TRADE-IN GAME ===");

        System.out.print("Enter game name for trade-in: ");
        String name = scanner.nextLine();

        System.out.print("Enter console: ");
        String console = scanner.nextLine();

        int year = getIntInput("Enter release year: ");
        double estimatedValue = getDoubleInput("Enter estimated value: ");

        Game tradeInGame = new Game(0, name, console, year, 1, estimatedValue);
        TradeIn tradeIn = currentCustomer.tradeIn(tradeInGame);
        tradeIn.processTradeIn();

        System.out.println("Trade-in completed! You now have a 10% discount for your next purchase.");
    }

    private void viewInventory() {
        manager.displayInventory();
    }

    private void viewCustomerInfo() {
        System.out.println("\n=== CUSTOMER INFORMATION ===");
        System.out.println("Name: " + currentCustomer.getName());
        System.out.println("Discount Available: " + (currentCustomer.isDiscountAvailable() ? "Yes" : "No"));

        List<Purchase> history = currentCustomer.getHistory();
        if (history.isEmpty()) {
            System.out.println("No purchase history.");
        } else {
            System.out.println("Purchase History:");
            for (Purchase purchase : history) {
                System.out.println(" - " + purchase.getPurchaseDetails());
            }
        }
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
}