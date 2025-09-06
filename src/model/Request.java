package model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Request {
    private String requestId;
    private Recipient recipient;
    private int quantity;
    private LocalDate requestDate;
    private String status;
    private List<FoodItem> foodItems;
    private Map<String, Integer> requestedItems;

    public Request(String requestId, Recipient recipient, LocalDate requestDate, String status, int quantity, List<FoodItem> foodItems) {
        if (quantity <= 0) throw new IllegalArgumentException("Requested quantity must be positive.");
        this.requestId = requestId;
        this.recipient = recipient;
        this.requestDate = requestDate;
        this.status = status != null ? status : "Pending";
        this.quantity = quantity;
        this.foodItems = new ArrayList<>(foodItems);
        this.requestedItems = new java.util.HashMap<>();
    }

    public String getRequestId() { return requestId; }
    public Recipient getRecipient() { return recipient; }
    public LocalDate getRequestDate() { return requestDate; }
    public String getStatus() { return status; }
    public int getQuantity() { return quantity; }
    public List<FoodItem> getFoodItems() { return foodItems; }
    public Map<String, Integer> getRequestedItems() { return requestedItems; }

    public void setStatus(String status) { this.status = status; }

    public void addItemRequest(String category, int quantity) { // CHANGED: no prints
        requestedItems.put(category, quantity);
    }

    public String infoString() { // NEW: for GUI
        StringBuilder sb = new StringBuilder();
        sb.append("Request ID: ").append(requestId)
          .append(" | Recipient: ").append(recipient.getName())
          .append("\nStatus: ").append(status)
          .append(" | Date: ").append(requestDate);
        for (FoodItem item : foodItems) {
            sb.append("\n - ").append(item);
        }
        return sb.toString();
    }
}
