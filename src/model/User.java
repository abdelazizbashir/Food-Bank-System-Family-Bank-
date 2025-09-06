package model;




// Abstract base class for all system users (Donor, Recipient, Admin)
public abstract class User {
    protected String name;
    protected String contact;
    protected String email;
    protected String address;
    protected String password;

    // Constructor with validation
    public User(String name, String contact, String email, String address, String password) {
        if (name == null || !name.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Invalid name! Only letters and spaces are allowed.");
        }
        if (contact == null || !contact.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid contact number! Only digits are allowed.");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format!");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty!");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long!");
        }
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    // Getters
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPassword() { return password; }

    // Simple authentication
    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Abstract
    public abstract void displayInfo(); // CHANGED: kept abstract; GUI won't call this to print
}
