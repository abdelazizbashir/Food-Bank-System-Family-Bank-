package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Donation {
    private Donor donor;
    private List<FoodItem> foodItems;
    private int donationId;
    private String donationStatus;
    private LocalDateTime donationDate;

    public Donation(int donationId, Donor donor) {
        this.donor = donor;
        this.donationId = donationId;
        this.donationStatus = "Pending";
        this.donationDate = LocalDateTime.now();
        this.foodItems = new ArrayList<>();
    }

    public int getDonationId() { return donationId; }
    public Donor getDonor() { return donor; }
    public String getDonationStatus() { return donationStatus; }
    public List<FoodItem> getFoodItems() { return foodItems; }
    public LocalDateTime getDonationDate() { return donationDate; }

    public void setDonationStatus(String donationStatus) { this.donationStatus = donationStatus; }
    public void setDonationDate(LocalDateTime donationDate){ this.donationDate = donationDate; }
    public void setFoodItems(List<FoodItem> foodItems){ this.foodItems = foodItems; }

    // Return a message for GUI
    public String addFoodItem(FoodItem item) { // CHANGED
        foodItems.add(item);
        return item.getQuantity() + " " + item.getUnit() + " of " + item.getItemName()
                + " added to donation ID " + donationId + " (Expiry: " + item.getItemExpiryDate() + ").";
    }

    public String infoString() { // NEW: for GUI display
        StringBuilder sb = new StringBuilder();
        sb.append("Donation ID: ").append(donationId)
          .append(" | Status: ").append(donationStatus)
          .append(" | Date: ").append(donationDate)
          .append("\nDonor: ").append(donor != null ? donor.getName() : "N/A");
        for (FoodItem item : foodItems) {
            sb.append("\n - ").append(item);
        }
        return sb.toString();
    }
}
