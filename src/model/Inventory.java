package model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private String location;
    private String inventoryId;
    private String inventoryName;
    private List<FoodItem> foodItems;

    public Inventory(String location, String inventoryId, String inventoryName) {
        this.location = location;
        this.inventoryId = inventoryId;
        this.inventoryName = inventoryName;
        this.foodItems = new ArrayList<>();
    }

    public List<FoodItem> getFoodItems() { return foodItems; }

    // --- FIXED: Add or merge food items; return message for GUI ---
    public String addFoodItem(FoodItem newItem) { 
        for (FoodItem item : foodItems) {
            if (item.getItemName().equalsIgnoreCase(newItem.getItemName()) &&
                item.getCategory().equalsIgnoreCase(newItem.getCategory()) &&
                item.getUnit().equalsIgnoreCase(newItem.getUnit()) &&
                item.getItemExpiryDate().equals(newItem.getItemExpiryDate())) {

                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return "Updated " + item.getItemName() + " | New Qty: " + item.getQuantity() + " " + item.getUnit() + ".";
            }
        }
        foodItems.add(newItem);
        return newItem.getItemName() + " added to the inventory.";
    }

    // --- FIXED: Add donation to inventory; return message ---
    public String addDonation(Donation donation) { 
        StringBuilder sb = new StringBuilder();
        for (FoodItem item : donation.getFoodItems()) {
            String msg = addFoodItem(item);
            if (sb.length() > 0) sb.append(" ");
            sb.append(msg);
        }
        return "Donation ID " + donation.getDonationId() + " processed. " + sb;
    }

    // --- FIXED: Fulfill a request; set status and return message ---
    public String fulfillRequest(Request request) { 
        if (checkAvailability(request.getFoodItems())) {
            for (FoodItem req : request.getFoodItems()) {
                for (FoodItem stock : foodItems) {
                    if (stock.getItemName().equalsIgnoreCase(req.getItemName())) {
                        stock.reduceQuantity(req.getQuantity());
                        break;
                    }
                }
            }
            request.setStatus("Approved");
            return "Request " + request.getRequestId() + " approved.";
        } else {
            request.setStatus("Rejected - Not Enough Stock");
            return "Request " + request.getRequestId() + " rejected: Not enough stock.";
        }
    }

    // --- NEW FIX: boolean version for GUI approveBtn ---
    public boolean canFulfillRequest(Request request) { 
        if (checkAvailability(request.getFoodItems())) {
            for (FoodItem req : request.getFoodItems()) {
                for (FoodItem stock : foodItems) {
                    if (stock.getItemName().equalsIgnoreCase(req.getItemName())) {
                        stock.reduceQuantity(req.getQuantity());
                        break;
                    }
                }
            }
            request.setStatus("Approved");
            return true;
        } else {
            request.setStatus("Rejected - Not Enough Stock");
            return false;
        }
    }

    // --- HELPER: Check stock availability ---
    private boolean checkAvailability(List<FoodItem> requestedItems) {
        for (FoodItem req : requestedItems) {
            boolean found = false;
            for (FoodItem stock : foodItems) {
                if (stock.getItemName().equalsIgnoreCase(req.getItemName()) &&
                    stock.getQuantity() >= req.getQuantity()) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    // --- REMOVE EXPIRED ITEMS (NO PRINTS) ---
    public void removeExpiredItems() { 
        foodItems.removeIf(item -> item.getItemExpiryDate().isBefore(LocalDate.now()));
    }

    // --- INVENTORY NAME FOR GUI ---
    public String inventoryName() { return inventoryName; }
}
