package model;
import java.util.List;

public class Admin extends User {
    public Admin(String name, String contact, String email, String address, String password) {
        super(name, contact, email, address, password);
    }

    // Review request, return a message for GUI
    public String reviewRequest(Request request, boolean approve, Inventory inventory) { // CHANGED
        if (request == null) return "No request selected.";
        if (approve) {
            String result = inventory.fulfillRequest(request); // returns message and sets status
            return "Admin action: " + result;
        } else {
            request.setStatus("Rejected by Admin");
            return "Admin rejected request: " + request.getRequestId();
        }
    }

    // Data fetchers for GUI (no printing)
    public List<Request> viewRequests(List<Request> requests) { 
        return requests;
    }

    public List<Donation> viewAllDonations(List<Donation> donations) { 
        return donations;
    }

    public void viewInventory(Inventory inventory) {  } // GUI will render

    @Override
    public void displayInfo() { /* no console output */ } // CHANGED
}

