package main;

import dao.*;
import db.DataBase;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import model.*;

/**
 
 *
 * Updates:
 * - Smooth workflow for Donor, Recipient, Admin.
 * - Approve/Reject requests by requestId input.
 * - Inventory updates correctly after donations & approvals.
 * - Back buttons everywhere, consistent button styling.
 * - DAO method names and parameters unchanged.
 */
public class Main {
    private JFrame frame;
    private User currentUser;

    // Theme / style
    private final Color bgColor = new Color(220, 255, 220); // light green
    private final Color primaryButtonColor = new Color(50, 180, 50); // green
    private final Color accentColor = new Color(255, 140, 0); // orange
    private final Color dangerColor = new Color(200, 40, 40); // red
    private final Font titleFont = new Font("Arial", Font.BOLD, 26);
    private final Font btnFont = new Font("Arial", Font.BOLD, 16);
    private final Dimension btnSize = new Dimension(200, 45);

    public Main() {
        frame = new JFrame("Family Food Bank");
        frame.setSize(700, 520);
        frame.setMinimumSize(new Dimension(650, 480));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(bgColor);
        frame.setLocationRelativeTo(null);
        showWelcomePage();
        frame.setVisible(true);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(btnFont);
        btn.setPreferredSize(btnSize);
        btn.setMaximumSize(btnSize);
        btn.setMinimumSize(btnSize);
    }

    /** ------------------- WELCOME PAGE ------------------- **/
    private void showWelcomePage() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel("Family Food Bank System", SwingConstants.CENTER);
        title.setFont(titleFont);
        title.setForeground(accentColor);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton registerBtn = new JButton("Register");
        JButton loginBtn = new JButton("Login");
        JButton exitBtn = new JButton("Exit");

        styleButton(registerBtn, primaryButtonColor);
        styleButton(loginBtn, accentColor);
        styleButton(exitBtn, dangerColor);

        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerBtn.addActionListener(e -> showChooseRolePage());
        loginBtn.addActionListener(e -> showLoginPage());
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(title);
        panel.add(Box.createVerticalStrut(40));
        panel.add(registerBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loginBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(exitBtn);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /** ------------------- CHOOSE ROLE PAGE ------------------- **/
    private void showChooseRolePage() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel("Register - Choose Role", SwingConstants.CENTER);
        title.setFont(titleFont);
        title.setForeground(accentColor);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton donorBtn = new JButton("Register as Donor");
        JButton recipientBtn = new JButton("Register as Recipient");
        JButton backBtn = new JButton("Back");

        styleButton(donorBtn, primaryButtonColor);
        styleButton(recipientBtn, primaryButtonColor.darker());
        styleButton(backBtn, accentColor);

        donorBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        recipientBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        donorBtn.addActionListener(e -> showDonorRegistrationPage());
        recipientBtn.addActionListener(e -> showRecipientRegistrationPage());
        backBtn.addActionListener(e -> showWelcomePage());

        panel.add(title);
        panel.add(Box.createVerticalStrut(30));
        panel.add(donorBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(recipientBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(backBtn);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /** ------------------- DONOR REGISTRATION ------------------- **/
    private void showDonorRegistrationPage() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField addressField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");

        styleButton(registerBtn, primaryButtonColor);
        styleButton(backBtn, accentColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        panel.add(contactField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(registerBtn, gbc);
        gbc.gridx = 1;
        panel.add(backBtn, gbc);

        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String contact = contactField.getText().trim();
            String address = addressField.getText().trim();
            String password = new String(passField.getPassword());
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Name, email and password are required.");
                return;
            }
            try {
                UserDAO.registerUser(new Donor(name, contact, email, address, password));
                JOptionPane.showMessageDialog(frame, "Donor registered successfully. You can now login.");
                showWelcomePage();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error during registration: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> showChooseRolePage());

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /** ------------------- RECIPIENT REGISTRATION ------------------- **/
    private void showRecipientRegistrationPage() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField familyField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");

        styleButton(registerBtn, primaryButtonColor);
        styleButton(backBtn, accentColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        panel.add(contactField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Family Size:"), gbc);
        gbc.gridx = 1;
        panel.add(familyField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(registerBtn, gbc);
        gbc.gridx = 1;
        panel.add(backBtn, gbc);

        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String contact = contactField.getText().trim();
            String address = addressField.getText().trim();
            String familyText = familyField.getText().trim();
            String password = new String(passField.getPassword());
            if (name.isEmpty() || email.isEmpty() || familyText.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required.");
                return;
            }
            int familySize;
            try {
                familySize = Integer.parseInt(familyText);
                if (familySize < 1) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid family size.");
                return;
            }
            try {
                UserDAO.registerUser(new Recipient(name, contact, email, address, familySize, password));
                JOptionPane.showMessageDialog(frame, "Recipient registered successfully. You can now login.");
                showWelcomePage();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error during registration: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> showChooseRolePage());

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /** ------------------- LOGIN PAGE ------------------- **/
    private void showLoginPage() {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");

        styleButton(loginBtn, primaryButtonColor);
        styleButton(backBtn, accentColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(loginBtn, gbc);
        gbc.gridx = 1;
        panel.add(backBtn, gbc);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String pass = new String(passField.getPassword());
            try {
                currentUser = UserDAO.login(email, pass);
                if (currentUser == null) {
                    if (email.equals("admin@foodbank.com") && pass.equals("admin123")) {
                        currentUser = new Admin("Admin", "000", "admin@foodbank.com", "Cyberjaya", "admin123");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid credentials.");
                        return;
                    }
                }
                if (currentUser instanceof Donor) showDonorMenu((Donor) currentUser);
                else if (currentUser instanceof Recipient) showRecipientMenu((Recipient) currentUser);
                else if (currentUser instanceof Admin) showAdminMenu((Admin) currentUser);
                else JOptionPane.showMessageDialog(frame, "Unknown role.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> showWelcomePage());

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /** ------------------- DONOR MENU ------------------- **/
    private void showDonorMenu(Donor donor) {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel label = new JLabel("Welcome Donor " + donor.getName(), SwingConstants.CENTER);
        label.setFont(titleFont);
        label.setForeground(accentColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton donateBtn = new JButton("Donate (Catalog)");
        JButton historyBtn = new JButton("Donation History");
        JButton logoutBtn = new JButton("Logout");

        styleButton(donateBtn, primaryButtonColor);
        styleButton(historyBtn, primaryButtonColor.darker());
        styleButton(logoutBtn, accentColor);

        donateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        historyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        donateBtn.addActionListener(e -> donorDonatePage(donor));
        historyBtn.addActionListener(e -> showDonorHistory(donor));
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            showWelcomePage();
        });

        panel.add(label);
        panel.add(Box.createVerticalStrut(30));
        panel.add(donateBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(historyBtn);
        panel.add(Box.createVerticalStrut(15));
        panel.add(logoutBtn);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    // DONOR DONATION PAGE
    private void donorDonatePage(Donor donor) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        try {
            for (Integer id : model.FoodCatalog.catalog.keySet()) {
                Object[] d = model.FoodCatalog.catalog.get(id);
                listModel.addElement(d[0] + " | " + d[1] + " | " + d[2] + " | Shelf:" + d[3]);
            }
        } catch (Exception e) { e.printStackTrace(); }

        JList<String> catalogList = new JList<>(listModel);
        catalogList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(catalogList);
        int option = JOptionPane.showConfirmDialog(frame, scrollPane, "Select Item to Donate", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION && !catalogList.isSelectionEmpty()) {
            String selected = catalogList.getSelectedValue();
            String itemName = selected.split("\\|")[0].trim();
            String qtyText = JOptionPane.showInputDialog(frame, "Enter quantity to donate:");
            if (qtyText == null) { showDonorMenu(donor); return; }
            int qty;
            try { qty = Integer.parseInt(qtyText.trim()); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(frame, "Invalid quantity."); showDonorMenu(donor); return; }

            try {
                Object[] data = null;
                for (Integer id : model.FoodCatalog.catalog.keySet()) {
                    Object[] d = model.FoodCatalog.catalog.get(id);
                    if (d[0].toString().equalsIgnoreCase(itemName)) { data = d; break; }
                }
                String category = data != null ? data[1].toString() : "General";
                String unit = data != null ? data[2].toString() : "Unit";
                int shelfDays = data != null ? (int) data[3] : 30;

                DonationDAO.save(donor.getName(), itemName, category, unit, shelfDays, qty);
                JOptionPane.showMessageDialog(frame, "Donation successful!");
            } catch (Exception e) { e.printStackTrace(); JOptionPane.showMessageDialog(frame, "Error donating: " + e.getMessage()); }
        }
        showDonorMenu(donor);
    }

    private void showDonorHistory(Donor donor) {
        DefaultListModel<String> model = new DefaultListModel<>();
        try (ResultSet rs = DonationDAO.listByDonor(donor.getName())) {
            while (rs.next()) model.addElement(rs.getString("itemName") + " x" + rs.getInt("quantity") + " | " + rs.getString("timestamp"));
        } catch (Exception e) { e.printStackTrace(); }
        JList<String> historyList = new JList<>(model);
        JScrollPane scrollPane = new JScrollPane(historyList);
        JOptionPane.showMessageDialog(frame, scrollPane, "Donation History", JOptionPane.INFORMATION_MESSAGE);
        showDonorMenu(donor);
    }

    /** ------------------- RECIPIENT MENU ------------------- **/
private void showRecipientMenu(Recipient recipient) {
    frame.getContentPane().removeAll();
    JPanel panel = new JPanel();
    panel.setBackground(bgColor);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

    JLabel label = new JLabel("Welcome Recipient " + recipient.getName(), SwingConstants.CENTER);
    label.setFont(titleFont);
    label.setForeground(accentColor);
    label.setAlignmentX(Component.CENTER_ALIGNMENT);

    JButton requestBtn = new JButton("Make Request");
    JButton historyBtn = new JButton("Request History");
    JButton logoutBtn = new JButton("Logout");

    styleButton(requestBtn, primaryButtonColor);
    styleButton(historyBtn, primaryButtonColor.darker());
    styleButton(logoutBtn, accentColor);

    requestBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    historyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

    // ---------- BUTTON LISTENERS ----------
    requestBtn.addActionListener(e -> recipientRequestPage(recipient)); // Make Request
    historyBtn.addActionListener(e -> showRecipientHistory(recipient)); // Show History
    logoutBtn.addActionListener(e -> { currentUser = null; showWelcomePage(); }); // Logout

    panel.add(label);
    panel.add(Box.createVerticalStrut(30));
    panel.add(requestBtn);
    panel.add(Box.createVerticalStrut(15));
    panel.add(historyBtn);
    panel.add(Box.createVerticalStrut(15));
    panel.add(logoutBtn);

    frame.add(panel);
    frame.revalidate();
    frame.repaint();
}





/** ------------------- RECIPIENT REQUEST PAGE ------------------- **/
    private void recipientRequestPage(Recipient recipient) {
    // Show entitlement per item (based on family size & catalog)
    StringBuilder entitlementMsg = new StringBuilder(
        "Based on your family size (" + recipient.getFamilySize() + " members),\n" +
        "your maximum entitlements are:\n\n"
    );

    try {
        for (Integer id : FoodCatalog.catalog.keySet()) {
            Object[] data = FoodCatalog.getItem(id);
            String itemName = data[0].toString();
            String unit = data[2].toString();
            int perPersonLimit = (int) data[4];
            int totalAllowed = perPersonLimit * recipient.getFamilySize();

            // Check how much already requested
            int alreadyRequested = RequestDAO.getAlreadyRequested(recipient.getName(), itemName);
            int remaining = Math.max(totalAllowed - alreadyRequested, 0);

            entitlementMsg.append(itemName)
                .append(": ").append(remaining).append(" ").append(unit)
                .append(" (of ").append(totalAllowed).append(" total)\n");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame,
            "Error calculating request limits: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        showRecipientMenu(recipient);
        return;
    }

    JOptionPane.showMessageDialog(frame, entitlementMsg.toString(),
        "Your Request Limits", JOptionPane.INFORMATION_MESSAGE);

    // Display available stock
    DefaultListModel<String> listModel = new DefaultListModel<>();
    try {
        for (String s : InventoryDAO.listAll()) {
            listModel.addElement(s); // Format: itemName | category | unit | Shelf:x | Qty:y
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Error loading available stock: " + e.getMessage());
        showRecipientMenu(recipient);
        return;
    }

    JList<String> inventoryList = new JList<>(listModel);
    inventoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(inventoryList);

    int option = JOptionPane.showConfirmDialog(frame, scrollPane,
        "Select Item to Request", JOptionPane.OK_CANCEL_OPTION);

    if (option == JOptionPane.OK_OPTION && !inventoryList.isSelectionEmpty()) {
        String selected = inventoryList.getSelectedValue();
        String itemName = selected.split("\\|")[0].trim();
        int availableQty = Integer.parseInt(selected.split("Qty:")[1].trim());

        // Lookup entitlement
        Object[] data = null;
        for (Integer id : FoodCatalog.catalog.keySet()) {
            Object[] d = FoodCatalog.catalog.get(id);
            if (d[0].toString().equalsIgnoreCase(itemName)) { data = d; break; }
        }
        if (data == null) {
            JOptionPane.showMessageDialog(frame, " Item not found in catalog.");
            showRecipientMenu(recipient);
            return;
        }

        String unit = data[2].toString();
        int perPersonLimit = (int) data[4];
        int totalAllowed = perPersonLimit * recipient.getFamilySize();

        int alreadyRequested = 0;
        try {
            alreadyRequested = RequestDAO.getAlreadyRequested(recipient.getName(), itemName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int remaining = Math.max(totalAllowed - alreadyRequested, 0);

        if (remaining == 0) {
            JOptionPane.showMessageDialog(frame,
                " You have already reached your limit for " + itemName +
                ".\nAllowed: " + totalAllowed + " " + unit +
                " | Already requested: " + alreadyRequested,
                "Limit Reached", JOptionPane.WARNING_MESSAGE);
            showRecipientMenu(recipient);
            return;
        }

        //  Ask user for quantity
        String qtyText = JOptionPane.showInputDialog(frame,
            "Enter quantity to request:\n" +
            "Remaining entitlement: " + remaining + " " + unit +
            " | Available stock: " + availableQty);
        if (qtyText == null) { showRecipientMenu(recipient); return; }

        int qty;
        try { qty = Integer.parseInt(qtyText.trim()); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, " Invalid quantity entered.");
            showRecipientMenu(recipient);
            return;
        }

        //  Validate
        if (qty > remaining) {
            JOptionPane.showMessageDialog(frame,
                " Request exceeds your entitlement!\n" +
                "Remaining limit: " + remaining + " " + unit);
            showRecipientMenu(recipient);
            return;
        }

        if (qty > availableQty) {
            JOptionPane.showMessageDialog(frame,
                " Request exceeds available stock!\n" +
                "Only " + availableQty + " " + unit + " available.");
            showRecipientMenu(recipient);
            return;
        }

        //  Save request
        try {
            String category = data[1].toString();
            int shelfDays = (int) data[3];

            RequestDAO.save(recipient.getName(), itemName, category, unit, shelfDays, qty);
            JOptionPane.showMessageDialog(frame,
                " Request submitted successfully!\n" +
                qty + " " + unit + " of " + itemName + " requested.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, " Error submitting request: " + e.getMessage());
        }
    }

    showRecipientMenu(recipient); // back to menu
}

/** ------------------- RECIPIENT REQUEST HISTORY ------------------- **/
private void showRecipientHistory(Recipient recipient) {
    DefaultListModel<String> model = new DefaultListModel<>();
    try {
        for (String r : RequestDAO.listByRecipient(recipient.getName())) {
            model.addElement(r); // shows status (Pending, Approved, Rejected)
        }
    } catch (Exception e) { e.printStackTrace(); }

    JList<String> historyList = new JList<>(model);
    JScrollPane scrollPane = new JScrollPane(historyList);
    JOptionPane.showMessageDialog(frame, scrollPane, "Request History", JOptionPane.INFORMATION_MESSAGE);
    showRecipientMenu(recipient); // back to menu
}



   /** ------------------- ADMIN MENU ------------------- **/
private void showAdminMenu(Admin admin) {
    frame.getContentPane().removeAll();
    JPanel panel = new JPanel();
    panel.setBackground(bgColor);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

    JLabel label = new JLabel("Welcome Admin", SwingConstants.CENTER);
    label.setFont(titleFont);
    label.setForeground(accentColor);
    label.setAlignmentX(Component.CENTER_ALIGNMENT);

    JButton viewRequestsBtn = new JButton("View Requests");
    JButton viewInventoryBtn = new JButton("View Inventory");
    JButton viewUsersBtn = new JButton("View Donors/Recipients");
    JButton logoutBtn = new JButton("Logout");

    styleButton(viewRequestsBtn, primaryButtonColor);
    styleButton(viewInventoryBtn, primaryButtonColor.darker());
    styleButton(viewUsersBtn, primaryButtonColor.darker());
    styleButton(logoutBtn, accentColor);

    viewRequestsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    viewInventoryBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    viewUsersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

    // ---------------- ACTIONS ----------------
    viewRequestsBtn.addActionListener(e -> adminApproveRejectPage(admin));
    viewInventoryBtn.addActionListener(e -> showInventory(admin));
    viewUsersBtn.addActionListener(e -> showUsers(admin));
    logoutBtn.addActionListener(e -> { currentUser = null; showWelcomePage(); });

    // ---------------- ADD TO PANEL ----------------
    panel.add(label);
    panel.add(Box.createVerticalStrut(30));
    panel.add(viewRequestsBtn);
    panel.add(Box.createVerticalStrut(15));
    panel.add(viewInventoryBtn);
    panel.add(Box.createVerticalStrut(15));
    panel.add(viewUsersBtn);
    panel.add(Box.createVerticalStrut(15));
    panel.add(logoutBtn);

    frame.add(panel);
    frame.revalidate();
    frame.repaint();
}

/** ------------------- ADMIN VIEW REQUESTS ------------------- **/
private void adminApproveRejectPage(Admin admin) {
    StringBuilder pendingRequests = new StringBuilder();
    try {
        for (String r : RequestDAO.listAllPending()) {
            pendingRequests.append(r).append("\n");
        }
    } catch (Exception e) { e.printStackTrace(); }

    String input = JOptionPane.showInputDialog(frame,
            "Pending Requests:\n" + pendingRequests + "\nEnter requestId to approve/reject:");

    if (input == null) return;

    int requestId;
    try {
        requestId = Integer.parseInt(input.trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(frame, "Invalid requestId.");
        return;
    }

    String[] options = {"Approve", "Reject"};
    int choice = JOptionPane.showOptionDialog(frame,
            "Approve or Reject request " + requestId + "?",
            "Request Action", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);

    if (choice == JOptionPane.CLOSED_OPTION) return;

    try {
        if (choice == 0) {
            RequestDAO.approveRequest(requestId);  //  Updates DB, inventory, recipient history
            JOptionPane.showMessageDialog(frame, "Request approved. Inventory updated.");
        } else if (choice == 1) {
            RequestDAO.rejectRequest(requestId);   //  Updates DB & recipient history
            JOptionPane.showMessageDialog(frame, "Request rejected.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Error processing request: " + e.getMessage());
    }

    showAdminMenu(admin);
}

/** ------------------- VIEW USERS ------------------- **/
private void showUsers(Admin admin) {
    DefaultListModel<String> model = new DefaultListModel<>();
    try {
        model.addElement("---- Donors ----");
        for (String d : UserDAO.listDonors()) model.addElement(d);

        model.addElement("---- Recipients ----");
        for (String r : UserDAO.listRecipients()) model.addElement(r);
    } catch (Exception e) { e.printStackTrace(); }

    JList<String> userList = new JList<>(model);
    JScrollPane scrollPane = new JScrollPane(userList);
    JOptionPane.showMessageDialog(frame, scrollPane, "Users", JOptionPane.INFORMATION_MESSAGE);

    showAdminMenu(admin);
}

/** ------------------- VIEW INVENTORY ------------------- **/
private void showInventory(Admin admin) {
    DefaultListModel<String> model = new DefaultListModel<>();
    try {
        for (String r : InventoryDAO.listAll()) model.addElement(r);
    } catch (Exception e) { e.printStackTrace(); }

    JList<String> inventoryList = new JList<>(model);
    JScrollPane scrollPane = new JScrollPane(inventoryList);
    JOptionPane.showMessageDialog(frame, scrollPane, "Inventory", JOptionPane.INFORMATION_MESSAGE);

    showAdminMenu(admin);
}



public static void main(String[] args) {
    DataBase.initialize();       // sets up DB connection, creates tables if needed
    utility.Seeder.run();        // populate initial data (admin, food catalog, etc.)
    SwingUtilities.invokeLater(Main::new);  // start GUI safely
}

}
