package model;
import java.time.LocalDate;

public class FoodItem {
    private String itemName;
    private int quantity;
    private LocalDate itemExpiryDate;
    private String category;
    private String unit;
    private int shelfLifeDays;

    public FoodItem(String itemName, int quantity, String category, String unit, int shelfLifeDays) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        if (shelfLifeDays <= 0) throw new IllegalArgumentException("Shelf life must be positive.");

        this.itemName = itemName;
        this.quantity = quantity;
        this.itemExpiryDate = LocalDate.now().plusDays(shelfLifeDays);
        this.category = category;
        this.unit = unit;
        this.shelfLifeDays = shelfLifeDays;
    }

    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public LocalDate getItemExpiryDate() { return itemExpiryDate; }
    public String getCategory() { return category; }
    public String getUnit() { return unit; }
    public int getShelfLifeDays() { return shelfLifeDays; }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
        this.quantity = quantity;
    }

    public void reduceQuantity(int amount) {
        if (amount <= quantity) {
            quantity -= amount;
        } else {
            throw new IllegalArgumentException("Not enough quantity available!");
        }
    }

    @Override
    public String toString() {
        return itemName + " - " + quantity + " " + unit + " (Expiry: " + itemExpiryDate + ")";
    }
}

