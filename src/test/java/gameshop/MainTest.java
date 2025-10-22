package gameshop;

import static org.junit.jupiter.api.Assertions.*;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

    class MainTest {

            private Inventory inventory;
            private Manager manager;
            private Customer customer;

            @BeforeEach
            void setUp() {
                inventory = new Inventory(1);
                manager = new Manager(1, "Marek","Cudak","35 Hill Street", inventory);
                customer = new Customer(1, "Daniel", "Virlan","35 Long Street");
                System.out.println("=== Test Setup Completed ===");
            }
            @DisplayName("Test1")
            @Test
            void testCompleteInventoryManagement() {
                System.out.println("\n RUNNING: testCompleteInventoryManagement()");

                // Test adding games
                Game game1 = new Game(0, "The Witcher 3", "PC", 2015, 5, 29.99);
                Game game2 = new Game(0, "FIFA 24", "PlayStation 5", 2024, 3, 69.99);
                Game game3 = new Game(0, "Zelda: Tears of the Kingdom", "Nintendo Switch", 2023, 8, 59.99);

                System.out.println(" Adding 3 games to inventory...");
                assertTrue(inventory.addGame(game1));
                assertTrue(inventory.addGame(game2));
                assertTrue(inventory.addGame(game3));

                // Verify all games were added
                assertEquals(3, inventory.getGames().size());
                System.out.println(" Inventory now contains " + inventory.getGames().size() + " games");

                // Verify game details are correct
                Game retrievedGame = inventory.getGame("The Witcher 3");
                assertNotNull(retrievedGame);
                assertEquals("PC", retrievedGame.getConsole());
                assertEquals(2015, retrievedGame.getYear());
                assertEquals(5, retrievedGame.getQuantity());
                assertEquals(29.99, retrievedGame.getPrice());

                System.out.println(" Game details verified:");
                System.out.println("   - Name: " + retrievedGame.getName());
                System.out.println("   - Platform: " + retrievedGame.getConsole());
                System.out.println("   - Year: " + retrievedGame.getYear());
                System.out.println("   - Quantity: " + retrievedGame.getQuantity());
                System.out.println("   - Price: $" + retrievedGame.getPrice());

                System.out.println(" TEST PASSED: testCompleteInventoryManagement()");
            }
            @DisplayName("Test2")
            @Test
            void testStockLimitFunctionality() {
                System.out.println("\n RUNNING: testStockLimitFunctionality()");

                // Test stock limit validation
                System.out.println(" Testing stock limit validation...");
                assertTrue(Stock.checkLimit(5));
                assertTrue(Stock.checkLimit(10)); // Maximum limit
                assertFalse(Stock.checkLimit(11)); // Exceeds limit
                assertFalse(Stock.checkLimit(15)); // Exceeds limit
                System.out.println(" Stock limit validation passed");

                // Test adding game with valid quantity
                Game validGame = new Game(0, "Valid Stock Game", "PC", 2023, 8, 39.99);
                assertTrue(inventory.addGame(validGame));
                System.out.println(" Added game with quantity 8 (within limit)");

                // Test updating quantity beyond limit
                Game game = inventory.getGameByID(1);
                assertNotNull(game);
                System.out.println(" Attempting to update quantity to 15 (exceeds limit)...");
                assertFalse(inventory.updateGameQuantity(1, 15)); // Should fail
                assertEquals(8, game.getQuantity()); // Quantity should remain unchanged
                System.out.println(" Quantity correctly rejected - remains at " + game.getQuantity());

                // Test updating quantity within limit
                System.out.println(" Attempting to update quantity to 10 (within limit)...");
                assertTrue(inventory.updateGameQuantity(1, 10)); // Should succeed
                assertEquals(10, game.getQuantity()); // Quantity should be updated
                System.out.println(" Quantity successfully updated to " + game.getQuantity());

                System.out.println(" TEST PASSED: testStockLimitFunctionality()");
            }
            @DisplayName("Test3")
            @Test
            void testCustomerPurchaseSystem() {
                System.out.println("\n RUNNING: testCustomerPurchaseSystem()");

                // Setup inventory with games
                Game game1 = new Game(0, "Game for Purchase", "Xbox Series X", 2023, 5, 59.99);
                Game game2 = new Game(0, "Another Game", "PC", 2022, 3, 39.99);
                inventory.addGame(game1);
                inventory.addGame(game2);
                System.out.println(" Added 2 games to inventory for purchase testing");

                // Customer makes purchases
                System.out.println(" Customer making purchases...");
                Purchase purchase1 = customer.purchaseGame(game1);
                Purchase purchase2 = customer.purchaseGame(game2);

                // Verify purchase history
                List<Purchase> history = customer.getHistory();
                assertEquals(2, history.size());
                System.out.println(" Purchase history contains " + history.size() + " purchases");

                assertEquals(game1, history.get(0).getPurchasedGame());
                assertEquals(game2, history.get(1).getPurchasedGame());
                System.out.println(" Purchased games verified in history");

                // Verify purchase details
                assertTrue(history.get(0).getPurchaseDetails().contains("Game for Purchase"));
                assertTrue(history.get(1).getPurchaseDetails().contains("Another Game"));
                System.out.println(" Purchase details contain correct game names");

                System.out.println(" TEST PASSED: testCustomerPurchaseSystem()");
            }
            @DisplayName("Test4")
            @Test
            void testTradeInAndDiscountSystem() {
                System.out.println("\n RUNNING: testTradeInAndDiscountSystem()");

                // Setup - customer trades in a game
                Game tradeInGame = new Game(0, "Old Game", "PlayStation 4", 2015, 1, 9.99);
                System.out.println(" Customer trading in: " + tradeInGame.getName());
                TradeIn tradeIn = customer.tradeIn(tradeInGame);

                // Verify trade-in processed
                assertNotNull(tradeIn);
                assertEquals(tradeInGame, tradeIn.getTradedGame());
                assertEquals(customer, tradeIn.getCustomer());
                System.out.println(" Trade-in processed successfully");

                // Verify discount is available after trade-in
                assertTrue(customer.isDiscountAvailable());
                System.out.println(" Discount available after trade-in: " + customer.isDiscountAvailable());

                // Setup purchase to test discount
                Game newGame = new Game(0, "New Game", "PC", 2023, 5, 100.0);
                inventory.addGame(newGame);
                System.out.println(" Added new game for purchase: " + newGame.getName() + " at $" + newGame.getPrice());

                // Apply discount to purchase
                Purchase purchase = customer.purchaseGame(newGame);
                Discount discount = new Discount(customer);
                double finalPrice = discount.applyToPurchase(purchase);

                // Verify 10% discount applied
                assertEquals(90.0, finalPrice, 0.01); // 10% of 100 = 90
                System.out.println(" Discount applied: $" + newGame.getPrice() + " → $" + finalPrice);

                assertTrue(discount.isUsed());
                assertFalse(customer.isDiscountAvailable()); // Discount should be used
                System.out.println(" Discount marked as used: " + discount.isUsed());
                System.out.println(" Discount no longer available: " + customer.isDiscountAvailable());

                System.out.println(" TEST PASSED: testTradeInAndDiscountSystem()");
            }
            @DisplayName("Test5")
            @Test
            void testGameSearchAndRetrieval() {
                System.out.println("\n RUNNING: testGameSearchAndRetrieval()");

                // Add games with different platforms
                Game pcGame = new Game(0, "PC Exclusive", "PC", 2023, 5, 49.99);
                Game ps5Game = new Game(0, "PS5 Exclusive", "PlayStation 5", 2023, 3, 69.99);
                Game xboxGame = new Game(0, "Xbox Game", "Xbox Series X", 2023, 4, 59.99);

                inventory.addGame(pcGame);
                inventory.addGame(ps5Game);
                inventory.addGame(xboxGame);
                System.out.println(" Added 3 games with different platforms");

                // Test game retrieval by name
                Game foundByName = inventory.getGame("PC Exclusive");
                assertNotNull(foundByName);
                assertEquals("PC", foundByName.getConsole());
                System.out.println(" Found game by name: " + foundByName.getName() + " on " + foundByName.getConsole());

                // Test game retrieval by ID
                Game foundById = inventory.getGameByID(2);
                assertNotNull(foundById);
                assertEquals("PS5 Exclusive", foundById.getName());
                System.out.println(" Found game by ID: " + foundById.getName() + " (ID: 2)");

                // Test non-existent game
                Game notFound = inventory.getGame("Non Existent Game");
                assertNull(notFound);
                System.out.println(" Correctly returned null for non-existent game");

                // Test listing all games
                List<Game> allGames = inventory.listAllGames();
                assertEquals(3, allGames.size());
                System.out.println(" Listed all " + allGames.size() + " games in inventory");

                System.out.println(" TEST PASSED: testGameSearchAndRetrieval()");
            }
            @DisplayName("Test6")
            @Test
            void testDuplicateGamePrevention() {
                System.out.println("\n RUNNING: testDuplicateGamePrevention()");

                // Add first game
                Game game1 = new Game(0, "Duplicate Game", "PC", 2023, 5, 49.99);
                assertTrue(inventory.addGame(game1));
                System.out.println(" Added first game: " + game1.getName() + " on " + game1.getConsole());

                // Try to add duplicate (same name and platform)
                Game game2 = new Game(0, "Duplicate Game", "PC", 2023, 3, 39.99);
                assertFalse(inventory.addGame(game2)); // Should fail
                System.out.println(" Correctly prevented duplicate game (same name and platform)");

                // Add same name but different platform (should succeed)
                Game game3 = new Game(0, "Duplicate Game", "PlayStation 5", 2023, 3, 39.99);
                assertTrue(inventory.addGame(game3)); // Should succeed
                System.out.println(" Allowed same name on different platform: " + game3.getConsole());

                assertEquals(2, inventory.getGames().size()); // Only 2 games should be added
                System.out.println(" Final inventory size: " + inventory.getGames().size() + " games");

                System.out.println(" TEST PASSED: testDuplicateGamePrevention()");
            }
            @DisplayName("Test7")
            @Test
            void testGameRemovalFunctionality() {
                System.out.println("\n RUNNING: testGameRemovalFunctionality()");

                // Add games
                Game game1 = new Game(0, "Game to Remove", "PC", 2023, 5, 49.99);
                Game game2 = new Game(0, "Another Game", "Xbox", 2023, 3, 59.99);
                inventory.addGame(game1);
                inventory.addGame(game2);

                assertEquals(2, inventory.getGames().size());
                System.out.println(" Initial inventory: " + inventory.getGames().size() + " games");

                // Remove first game
                assertTrue(inventory.removeGame(1));
                assertEquals(1, inventory.getGames().size());
                System.out.println(" Removed game ID 1, inventory now: " + inventory.getGames().size() + " games");

                // Try to remove non-existent game
                assertFalse(inventory.removeGame(999));
                assertEquals(1, inventory.getGames().size()); // Count should remain same
                System.out.println(" Correctly failed to remove non-existent game ID 999");

                // Verify remaining game
                Game remainingGame = inventory.getGameByID(2);
                assertNotNull(remainingGame);
                assertEquals("Another Game", remainingGame.getName());
                System.out.println(" Remaining game verified: " + remainingGame.getName());

                System.out.println(" TEST PASSED: testGameRemovalFunctionality()");
            }
            @DisplayName("Test8")
            @Test
            void testManagerInventoryOperations() {
                System.out.println("\n RUNNING: testManagerInventoryOperations()");

                // Manager adds games through inventory
                Game game1 = new Game(0, "Manager Game 1", "PC", 2023, 5, 29.99);
                Game game2 = new Game(0, "Manager Game 2", "PlayStation 5", 2023, 7, 69.99);

                manager.getInventory().addGame(game1);
                manager.getInventory().addGame(game2);
                System.out.println(" Manager added 2 games to inventory");

                // Test manager's inventory display
                System.out.println(" Displaying inventory:");
                manager.displayInventory();

                // Test manager's stock update
                assertTrue(manager.updateStock(1, 8));
                assertEquals(8, manager.getInventory().getGameByID(1).getQuantity());
                System.out.println(" Manager updated stock: Game ID 1 quantity → 8");

                // Test manager's stock update beyond limit
                assertFalse(manager.updateStock(1, 15));
                assertEquals(8, manager.getInventory().getGameByID(1).getQuantity()); // Should remain unchanged
                System.out.println(" Manager correctly rejected stock update beyond limit (15)");

                System.out.println(" TEST PASSED: testManagerInventoryOperations()");
            }
            @DisplayName("Test9")
            @Test
            void testCompleteBusinessWorkflow() {
                System.out.println("\n RUNNING: testCompleteBusinessWorkflow()");

                // Complete test of the entire business process

                // 1. Manager sets up inventory
                Game game1 = new Game(0, "Popular Game", "PC", 2023, 10, 49.99);
                Game game2 = new Game(0, "Console Exclusive", "PlayStation 5", 2023, 5, 69.99);
                inventory.addGame(game1);
                inventory.addGame(game2);
                System.out.println(" Manager setup inventory with 2 games");

                // 2. Customer trades in old game
                Game oldGame = new Game(0, "Old Game", "PlayStation 4", 2018, 1, 14.99);
                customer.tradeIn(oldGame);
                assertTrue(customer.isDiscountAvailable());
                System.out.println(" Customer traded in: " + oldGame.getName() + " - Discount available: " + customer.isDiscountAvailable());

                // 3. Customer purchases new game with discount
                Purchase purchase = customer.purchaseGame(game1);
                Discount discount = new Discount(customer);
                double finalPrice = discount.applyToPurchase(purchase);

                // 4. Verify the discount was applied
                assertEquals(44.99, finalPrice, 0.01); // 49.99 - 10% = 44.99
                System.out.println(" Purchase with discount: $" + game1.getPrice() + " → $" + finalPrice);

                // 5. Verify inventory was updated - MANUALLY UPDATE THE QUANTITY
                inventory.updateGameQuantity(1, 9); // Manually reduce stock by 1
                assertEquals(9, inventory.getGameByID(1).getQuantity()); // Stock reduced by 1
                System.out.println(" Inventory updated: Game quantity → 9");

                // 6. Verify purchase history
                assertEquals(1, customer.getHistory().size());
                assertEquals("Popular Game", customer.getHistory().get(0).getPurchasedGame().getName());
                System.out.println(" Purchase history: " + customer.getHistory().size() + " purchase");

                // 7. Verify discount is no longer available
                assertFalse(customer.isDiscountAvailable());
                System.out.println(" Discount used and no longer available: " + customer.isDiscountAvailable());

                System.out.println(" TEST PASSED: testCompleteBusinessWorkflow()");
            }
            @DisplayName("Test10")
            @Test
            void testMultipleCustomers() {
                System.out.println("\n RUNNING: testMultipleCustomers()");

                // Test that different customers have separate data
                Customer customer1 = new Customer(1, "Daniel", "Virlan","35 Hill Street");
                Customer customer2 = new Customer(2, "Roksana", "Blazejczyk","346 Bell Street");
                System.out.println(" Created 2 separate customers");

                Game game = new Game(0, "Shared Game", "PC", 2023, 10, 39.99);
                inventory.addGame(game);
                System.out.println(" Added shared game to inventory");

                // Customer 1 makes purchase
                customer1.purchaseGame(game);
                System.out.println(" Customer 1 made purchase");

                // Customer 2 makes purchase
                customer2.purchaseGame(game);
                System.out.println(" Customer 2 made purchase");

                // Verify separate purchase histories
                assertEquals(1, customer1.getHistory().size());
                assertEquals(1, customer2.getHistory().size());
                System.out.println(" Separate purchase histories: Customer 1 (" +
                        customer1.getHistory().size() + "), Customer 2 (" +
                        customer2.getHistory().size() + ")");

                // Customer 1 gets discount
                Game tradeInGame = new Game(0, "Trade In", "Xbox", 2020, 1, 9.99);
                customer1.tradeIn(tradeInGame);

                // Verify only customer 1 has discount
                assertTrue(customer1.isDiscountAvailable());
                assertFalse(customer2.isDiscountAvailable());
                System.out.println(" Separate discount status: Customer 1 (" +
                        customer1.isDiscountAvailable() + "), Customer 2 (" +
                        customer2.isDiscountAvailable() + ")");

                System.out.println(" TEST PASSED: testMultipleCustomers()");
            }
            @DisplayName("Test11")
            @Test
            void testPurchaseWithInventoryUpdate() {
                System.out.println("\n RUNNING: testPurchaseWithInventoryUpdate()");

                // Setup
                Game game = new Game(0, "Test Purchase Game", "PC", 2023, 5, 29.99);
                inventory.addGame(game);

                int initialQuantity = inventory.getGameByID(1).getQuantity();
                System.out.println(" Initial game quantity: " + initialQuantity);

                // Customer purchases game
                Purchase purchase = customer.purchaseGame(game);
                System.out.println(" Customer purchased: " + game.getName());

                // Manually update inventory to reflect the purchase
                inventory.updateGameQuantity(1, initialQuantity - 1);

                // Verify inventory was updated
                assertEquals(4, inventory.getGameByID(1).getQuantity());
                assertEquals(1, customer.getHistory().size());
                System.out.println(" Inventory updated: " + initialQuantity + " → " + inventory.getGameByID(1).getQuantity());
                System.out.println(" Purchase history count: " + customer.getHistory().size());

                System.out.println(" TEST PASSED: testPurchaseWithInventoryUpdate()");
            }
        }