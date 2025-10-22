package gameshop;


import model.*;
import java.util.Scanner;
import java.util.List;

public class GameShopService {
    private Scanner scanner;
    private Manager manager;
    private Customer currentCustomer;

    public GameShopService() {
        this.scanner = new Scanner(System.in);
        setupManager();
        setupCustomer();
    }

    private void setupManager() {
        System.out.println("=== MANAGER SETUP ===");
        System.out.print("Enter manager name: ");
        String name = scanner.nextLine();

        System.out.print("Enter manager surname: ");
        String surname = scanner.nextLine();

        System.out.print("Enter manager address: ");
        String address = scanner.nextLine();

        Inventory inventory = new Inventory(1);
        this.manager = new Manager(1, name, surname, address, inventory);

        System.out.println(" Manager registered: " + manager.getManagerInfo());
    }

    private void setupCustomer() {
        System.out.println("\n=== CUSTOMER REGISTRATION ===");
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter customer surname: ");
        String surname = scanner.nextLine();

        System.out.print("Enter customer address: ");
        String address = scanner.nextLine();

        this.currentCustomer = new Customer(1, name, surname, address);

        System.out.println(" Customer registered: " + currentCustomer.getCustomerInfo());
    }

    public void start() {
        System.out.println("\n=== Welcome to Game Shop Management System ===");
        System.out.println("Manager: " + manager.getName() + " " + manager.getSurname());
        System.out.println("Customer: " + currentCustomer.getName() + " " + currentCustomer.getSurname());

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
                    viewManagerInfo();
                    break;
                case 7:
                    searchGamesByPlatform();
                    break;
                case 8:
                    System.out.println("Thank you for using Game Shop Management System!");
                    scanner.close();
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
        System.out.println("6. View Manager Info");
        System.out.println("7. Search Games by Platform");
        System.out.println("8. Exit");
    }

    private void viewManagerInfo() {
        System.out.println("\n=== MANAGER INFORMATION ===");
        System.out.println(manager.getManagerInfo());
    }

    private void viewCustomerInfo() {
        System.out.println("\n=== CUSTOMER INFORMATION ===");
        System.out.println(currentCustomer.getCustomerInfo());

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

        String platform = selectPlatform();
        if (platform == null) return;

        int year = getIntInput("Enter release year: ");
        int quantity = getIntInput("Enter quantity: ");
        double price = getDoubleInput("Enter price: ");

        if (!Stock.checkLimit(quantity)) {
            System.out.println("Error: Quantity exceeds maximum limit of " + Stock.getMaxLimit());
            return;
        }

        Game newGame = new Game(0, name, platform, year, quantity, price);
        if (manager.getInventory().addGame(newGame)) {
            System.out.println("Game added successfully!");
        } else {
            System.out.println("Error: Game with same name and platform already exists!");
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

        System.out.println("Selected: " + game.getDetails());
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

        String platform = selectPlatform();
        if (platform == null) return;

        int year = getIntInput("Enter release year: ");
        double estimatedValue = getDoubleInput("Enter estimated value: ");

        Game tradeInGame = new Game(0, name, platform, year, 1, estimatedValue);
        TradeIn tradeIn = currentCustomer.tradeIn(tradeInGame);
        tradeIn.processTradeIn();

        System.out.println("Trade-in completed! You now have a 10% discount for your next purchase.");
    }

    private void viewInventory() {
        manager.displayInventory();
    }

    private void searchGamesByPlatform() {
        System.out.println("\n=== SEARCH GAMES BY PLATFORM ===");
        String platform = selectPlatform();
        if (platform == null) return;

        List<Game> allGames = manager.getInventory().getGames();
        List<Game> platformGames = allGames.stream()
                .filter(game -> game.getConsole().equalsIgnoreCase(platform))
                .toList();

        if (platformGames.isEmpty()) {
            System.out.println("No games found for platform: " + platform);
        } else {
            System.out.println("\nGames for " + platform + ":");
            for (Game game : platformGames) {
                System.out.println(" - " + game.getDetails());
            }
        }
    }

    private String selectPlatform() {
        String[] PLATFORMS = {
                "PC", "PlayStation 5", "PlayStation 4", "Xbox Series X",
                "Xbox One", "Nintendo Switch", "Nintendo 3DS", "Mobile"
        };

        System.out.println("\nAvailable Platforms:");
        for (int i = 0; i < PLATFORMS.length; i++) {
            System.out.println((i + 1) + ". " + PLATFORMS[i]);
        }
        System.out.println((PLATFORMS.length + 1) + ". Other (enter custom platform)");

        int choice = getIntInput("Select platform: ");

        if (choice >= 1 && choice <= PLATFORMS.length) {
            return PLATFORMS[choice - 1];
        } else if (choice == PLATFORMS.length + 1) {
            System.out.print("Enter custom platform: ");
            return scanner.nextLine();
        } else {
            System.out.println("Invalid platform selection!");
            return null;
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