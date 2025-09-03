import java.util.ArrayList;
import java.util.List;

public class Recipient extends User {
    private int familySize;
    private final List<Request> requests;

    public Recipient(String name, String contact, String email, String address, int familySize, String password) {
        super(name, contact, email, address, password);
        if (familySize < 1)
            throw new IllegalArgumentException("Family size must be at least 1");
        this.familySize = familySize;
        this.requests = new ArrayList<>();
    }

    public int getFamilySize() { return familySize; }
    public List<Request> getRequests() { return new ArrayList<>(requests); }

    // NEW: Provide limits summary for GUI
    public String getLimitsSummary() {
        return "You may request up to: "
                + calculateAllowedQuantity("kg") + " kg, "
                + calculateAllowedQuantity("liters") + " liters, "
                + calculateAllowedQuantity("cans") + " cans, "
                + calculateAllowedQuantity("bags") + " bags.";
    }

    // Available items (GUI will render from inventory list)
    public List<FoodItem> viewAvailableItems(Inventory inventory) { // CHANGED
        return new ArrayList<>(inventory.getFoodItems());
    }

    public String checkItemAvailability(String itemName, int requiredQuantity, Inventory inventory) { // CHANGED
        for (FoodItem item : inventory.getFoodItems()) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                if (item.getQuantity() >= requiredQuantity) {
                    return itemName + " is available in requested quantity (" + requiredQuantity + ").";
                } else {
                    return itemName + " is available, but not enough stock.";
                }
            }
        }
        return itemName + " is not available in the inventory.";
    }

    public int calculateAllowedQuantity(String unit) {
        switch (unit.toLowerCase()) {
            case "kg": return (int) Math.ceil(familySize * 2.0);
            case "liters": return (int) Math.ceil(familySize * 1.0);
            case "cans": return familySize * 3;
            case "bags": return familySize * 1;
            default: return familySize;
        }
    }

    // Make request and store it — return a message for GUI (no prints)
    public String makeLimitedRequest(String requestId, FoodItem requestedItem, Inventory inventory) { // CHANGED
        int allowedQuantity = calculateAllowedQuantity(requestedItem.getUnit());
        if (requestedItem.getQuantity() > allowedQuantity) {
            return "Request denied: You can only request up to "
                    + allowedQuantity + " " + requestedItem.getUnit()
                    + " of " + requestedItem.getItemName() + ".";
        }

        String availabilityMessage = checkItemAvailability(requestedItem.getItemName(), requestedItem.getQuantity(), inventory);
        // Basic positive check:
        if (availabilityMessage.toLowerCase().contains("available")) {
            Request request = new Request(
                    requestId,
                    this,
                    java.time.LocalDate.now(),
                    "Pending",
                    requestedItem.getQuantity(),
                    List.of(requestedItem)
            );
            requests.add(request);
            return "Request submitted: " + requestId + " (" + requestedItem.getQuantity() + " "
                    + requestedItem.getUnit() + " of " + requestedItem.getItemName() + ").";
        } else {
            return "Request failed: " + availabilityMessage;
        }
    }

    public List<Request> viewMyRequests() { // CHANGED
        return new ArrayList<>(requests);
    }

    @Override
    public void displayInfo() { /* no console output */ } // CHANGED
}
