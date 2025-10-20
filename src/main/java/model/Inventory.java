package model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private int inventoryID;
    private List<Game> games;
    private int nextGameID = 1;

    public Inventory(int inventoryID) {
        this.inventoryID = inventoryID;
        this.games = new ArrayList<>();
    }

    public boolean addGame(Game game) {
        if (games.stream().anyMatch(g -> g.getName().equals(game.getName()) && g.getConsole().equals(game.getConsole()))) {
            return false; // Game already exists
        }
        Game newGame = new Game(nextGameID++, game.getName(), game.getConsole(), game.getYear(), game.getQuantity(), game.getPrice());
        return games.add(newGame);
    }

    public boolean removeGame(int gameID) {
        return games.removeIf(g -> g.getGameID() == gameID);
    }

    public Game getGame(String name) {
        return games.stream()
                .filter(g -> g.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Game getGameByID(int gameID) {
        return games.stream()
                .filter(g -> g.getGameID() == gameID)
                .findFirst()
                .orElse(null);
    }

    public List<Game> listAllGames() {
        return new ArrayList<>(games);
    }

    public boolean updateGameQuantity(int gameID, int newQuantity) {
        Game game = getGameByID(gameID);
        if (game != null && Stock.checkLimit(newQuantity)) {
            game.setQuantity(newQuantity);
            return true;
        }
        return false;
    }

    // Getters
    public List<Game> getGames() { return games; }
    public int getInventoryID() { return inventoryID; }
}
