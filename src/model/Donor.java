package model;
import java.util.ArrayList;
import java.util.List;

public class Donor extends User {
    private final List<Donation> donations;

    public Donor(String name, String contact, String email, String address, String password) {
        super(name, contact, email, address, password);
        this.donations = new ArrayList<>();
    }

    public List<Donation> getDonations() { return donations; }

    // Make donation and update inventory — return message (no prints)
    public String makeDonation(Donation donation, Inventory inventory) { // CHANGED
        if (donation == null || donation.getFoodItems().isEmpty())
            return "Donation has no items.";
        donations.add(donation);
        String invMsg = inventory.addDonation(donation); // CHANGED: returns message
        return getName() + " made a donation (ID: " + donation.getDonationId() + "). " + invMsg;
    }

    public List<Donation> viewDonationHistory() { // CHANGED
        return new ArrayList<>(donations);
    }

    @Override
    public void displayInfo() { /* no console output */ } // CHANGED
}
