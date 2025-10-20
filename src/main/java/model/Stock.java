package model;

public class Stock {
    private static final int MAX_LIMIT = 10;

    public static boolean checkLimit(int quantity) {
        return quantity <= MAX_LIMIT;
    }

    public static boolean canAddMore(int currentQuantity, int additionalQuantity) {
        return (currentQuantity + additionalQuantity) <= MAX_LIMIT;
    }

    public static int getMaxLimit() { return MAX_LIMIT; }
}