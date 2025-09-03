import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class Main {
    private static JFrame frame;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;

    private static final UserManager userManager = new UserManager();
    private static final Inventory inventory = new Inventory("Main Branch", "INV001", "Central Inventory");

    private static final AtomicInteger donationCounter = new AtomicInteger(1000);
    private static final AtomicInteger requestCounter = new AtomicInteger(5000);

    private static final Color BG_GREEN = new Color(235, 250, 235);
    private static final Color ORANGE = new Color(255, 140, 0);

    public static void main(String[] args) {
        // preload admin (your info)
        Admin adminDefault = new Admin("Abdelaziz Bashir", "01228855732",
                "abdelaziz@FamilyBank.com", "Cyberjaya", "admin123");
        userManager.registerUser(adminDefault);

        // preload inventory from FoodCatalog
        FoodCatalog.catalog.forEach((id, data) -> {
            String name = (String) data[0];
            String category = (String) data[1];
            String unit = (String) data[2];
            int shelf = (int) data[3];
            // starting qty 50 for demo
            FoodItem fi = new FoodItem(name, 50, category, unit, shelf);
            inventory.addFoodItem(fi);
        });

        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Food Bank System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(980, 720);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // initial pages: role choice
        mainPanel.add(roleChoicePage(), "RoleChoice");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // ---------- UI helpers ----------
    private static JPanel basePanel() {
        JPanel p = new JPanel(new BorderLayout(12, 12));
        p.setBackground(BG_GREEN);
        p.setBorder(new EmptyBorder(12, 12, 12, 12));
        return p;
    }

    private static JLabel heading(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(ORANGE);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setBorder(new EmptyBorder(12, 12, 12, 12));
        return lbl;
    }

    private static JButton bigButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(ORANGE);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setPreferredSize(new Dimension(260, 56));
        return b;
    }

    private static JButton smallButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(ORANGE);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(160, 42));
        return b;
    }

    private static JTextField inputField(int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBackground(Color.WHITE);
        f.setForeground(Color.BLACK);
        return f;
    }

    private static JPasswordField inputPassword(int cols) {
        JPasswordField f = new JPasswordField(cols);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBackground(Color.WHITE);
        f.setForeground(Color.BLACK);
        return f;
    }

    private static JPanel navPanel(String backTo, boolean showExit) {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nav.setBackground(BG_GREEN);
        JButton back = smallButton("Back");
        back.addActionListener(e -> cardLayout.show(mainPanel, backTo));
        nav.add(back);
        if (showExit) {
            JButton exit = smallButton("Exit");
            exit.addActionListener(e -> System.exit(0));
            nav.add(exit);
        }
        return nav;
    }

    private static void showTextAreaPage(String title, String content, String backTo) {
        JPanel p = basePanel();
        p.add(heading(title), BorderLayout.NORTH);
        JTextArea area = new JTextArea(content);
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(area);
        p.add(sp, BorderLayout.CENTER);
        p.add(navPanel(backTo, false), BorderLayout.SOUTH);
        mainPanel.add(p, title + "Page");
        cardLayout.show(mainPanel, title + "Page");
    }

    // ---------- Role choice page ----------
    private static JPanel roleChoicePage() {
        JPanel p = basePanel();
        p.add(heading("Welcome to Family Bank"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG_GREEN);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(16, 16, 16, 16);
        g.gridx = 0; g.gridy = 0;

        JLabel pick = new JLabel("Select your role:");
        pick.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        center.add(pick, g);

        g.gridy++;
        JButton donorBtn = bigButton("Donor");
        donorBtn.addActionListener(e -> {
            mainPanel.add(choicePage("Donor"), "DonorChoice");
            cardLayout.show(mainPanel, "DonorChoice");
        });
        center.add(donorBtn, g);

        g.gridy++;
        JButton recipientBtn = bigButton("Recipient");
        recipientBtn.addActionListener(e -> {
            mainPanel.add(choicePage("Recipient"), "RecipientChoice");
            cardLayout.show(mainPanel, "RecipientChoice");
        });
        center.add(recipientBtn, g);

        g.gridy++;
        JButton adminBtn = bigButton("Admin");
        adminBtn.addActionListener(e -> {
            mainPanel.add(loginPage("Admin"), "AdminLogin");
            cardLayout.show(mainPanel, "AdminLogin");
        });
        center.add(adminBtn, g);

        p.add(center, BorderLayout.CENTER);
        p.add(navPanel("RoleChoice", true), BorderLayout.SOUTH);
        return p;
    }

    // ---------- Role choice (login/register) ----------
    private static JPanel choicePage(String role) {
        JPanel p = basePanel();
        p.add(heading(role + " — Login or Register"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG_GREEN);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(14, 14, 14, 14);
        g.gridx = 0; g.gridy = 0;

        JButton loginBtn = bigButton("Login (Existing " + role + ")");
        loginBtn.addActionListener(e -> {
            mainPanel.add(loginPage(role), role + "Login");
            cardLayout.show(mainPanel, role + "Login");
        });
        center.add(loginBtn, g);

        g.gridy++;
        JButton regBtn = bigButton("Register (New " + role + ")");
        regBtn.addActionListener(e -> {
            mainPanel.add(registerPage(role), role + "Register");
            cardLayout.show(mainPanel, role + "Register");
        });
        center.add(regBtn, g);

        p.add(center, BorderLayout.CENTER);
        p.add(navPanel("RoleChoice", false), BorderLayout.SOUTH);
        return p;
    }

    // ---------- Register page ----------
    private static JPanel registerPage(String role) {
        JPanel p = basePanel();
        p.add(heading("Register as " + role), BorderLayout.NORTH);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_GREEN);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8,8,8,8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameF = inputField(20);
        JTextField contactF = inputField(20);
        JTextField emailF = inputField(20);
        JTextField addressF = inputField(20);
        JPasswordField pwF = inputPassword(20);
        JTextField familyF = inputField(6);

        int row = 0;
        g.gridx = 0; g.gridy = row; wrapper.add(new JLabel("Name:"), g);
        g.gridx = 1; wrapper.add(nameF, g); row++;

        g.gridx = 0; g.gridy = row; wrapper.add(new JLabel("Contact:"), g);
        g.gridx = 1; wrapper.add(contactF, g); row++;

        g.gridx = 0; g.gridy = row; wrapper.add(new JLabel("Email:"), g);
        g.gridx = 1; wrapper.add(emailF, g); row++;

        g.gridx = 0; g.gridy = row; wrapper.add(new JLabel("Address:"), g);
        g.gridx = 1; wrapper.add(addressF, g); row++;

        g.gridx = 0; g.gridy = row; wrapper.add(new JLabel("Password:"), g);
        g.gridx = 1; wrapper.add(pwF, g); row++;

        if (role.equals("Recipient")) {
            g.gridx = 0; g.gridy = row; wrapper.add(new JLabel("Family size:"), g);
            g.gridx = 1; wrapper.add(familyF, g); row++;
        }

        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG_GREEN);
        outer.add(wrapper);
        p.add(outer, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bottom.setBackground(BG_GREEN);
        JButton submit = smallButton("Submit Registration");
        submit.addActionListener(e -> {
            try {
                String name = nameF.getText().trim();
                String contact = contactF.getText().trim();
                String email = emailF.getText().trim();
                String address = addressF.getText().trim();
                String pw = new String(pwF.getPassword());

                if (role.equals("Donor")) {
                    Donor d = new Donor(name, contact, email, address, pw);
                    userManager.registerUser(d);
                    JOptionPane.showMessageDialog(frame, "Donor registered. Please log in.");
                    cardLayout.show(mainPanel, "DonorLogin");
                } else {
                    int fam = 1;
                    try { fam = Integer.parseInt(familyF.getText().trim()); } catch (Exception ex) { fam = 1; }
                    Recipient r = new Recipient(name, contact, email, address, fam, pw);
                    userManager.registerUser(r);
                    JOptionPane.showMessageDialog(frame, "Recipient registered. Please log in.");
                    cardLayout.show(mainPanel, "RecipientLogin");
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, "Validation error: " + ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });
        bottom.add(submit);

        p.add(bottom, BorderLayout.SOUTH);
        p.add(navPanel("RoleChoice", false), BorderLayout.EAST);
        return p;
    }

    // ---------- Login page ----------
    private static JPanel loginPage(String role) {
        JPanel p = basePanel();
        p.add(heading(role + " Login"), BorderLayout.NORTH);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_GREEN);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField emailF = inputField(24);
        JPasswordField pwF = inputPassword(24);

        g.gridx = 0; g.gridy = 0; wrapper.add(new JLabel("Email:"), g);
        g.gridx = 1; wrapper.add(emailF, g);
        g.gridx = 0; g.gridy = 1; wrapper.add(new JLabel("Password:"), g);
        g.gridx = 1; wrapper.add(pwF, g);

        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG_GREEN);
        outer.add(wrapper);
        p.add(outer, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bottom.setBackground(BG_GREEN);
        JButton loginBtn = smallButton("Login");
        loginBtn.addActionListener(e -> {
            String email = emailF.getText().trim();
            String pw = new String(pwF.getPassword());

            // admin login special-case (also exists in userManager if added)
            if (role.equals("Admin") && "abdelaziz@FoodBank.com".equalsIgnoreCase(email) && "admin123".equals(pw)) {
                showAdminMenu(new Admin("Abdelaziz Bashir", "01228855732", email, "Cyberjaya", pw));
                return;
            }

            User u = userManager.login(email, pw);
            if (u == null) {
                JOptionPane.showMessageDialog(frame, "Login failed. Check email and password.");
                return;
            }
            if (u instanceof Donor) showDonorMenu((Donor) u);
            else if (u instanceof Recipient) showRecipientMenu((Recipient) u);
            else if (u instanceof Admin) showAdminMenu((Admin) u);
        });
        bottom.add(loginBtn);

        p.add(bottom, BorderLayout.SOUTH);
        p.add(navPanel("RoleChoice", false), BorderLayout.EAST);
        return p;
    }

    // ---------- Donor flow ----------
    private static void showDonorMenu(Donor donor) {
        JPanel p = basePanel();
        p.add(heading("Donor Dashboard - " + donor.getName()), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG_GREEN);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(14, 14, 14, 14);
        g.gridx = 0; g.gridy = 0;

        JButton donateBtn = bigButton("Donate");
        donateBtn.addActionListener(e -> showDonationPage(donor));
        center.add(donateBtn, g);

        g.gridy++;
        JButton histBtn = bigButton("Donation History");
        histBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            List<Donation> list = donor.viewDonationHistory();
            if (list.isEmpty()) sb.append("No donations yet.");
            else for (Donation d : list) sb.append(d.infoString()).append("\n\n");
            showTextAreaPage("Donation History", sb.toString(), "DonorMenu");
        });
        center.add(histBtn, g);

        p.add(center, BorderLayout.CENTER);
        p.add(navPanel("RoleChoice", false), BorderLayout.SOUTH);

        mainPanel.add(p, "DonorMenu");
        cardLayout.show(mainPanel, "DonorMenu");
    }

    private static void showDonationPage(Donor donor) {
        JPanel p = basePanel();
        p.add(heading("Make Donation"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_GREEN);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8,8,8,8);
        g.fill = GridBagConstraints.HORIZONTAL;

        // build catalog dropdown from FoodCatalog.catalog
        JComboBox<String> catalogBox = new JComboBox<>();
        for (Map.Entry<Integer, Object[]> e : FoodCatalog.catalog.entrySet()) {
            int id = e.getKey();
            Object[] data = e.getValue();
            String name = (String) data[0];
            String unit = (String) data[2];
            catalogBox.addItem(id + " - " + name + " (" + unit + ")");
        }

        JTextField qtyF = inputField(8);

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Item:"), g);
        g.gridx = 1; form.add(catalogBox, g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Quantity:"), g);
        g.gridx = 1; form.add(qtyF, g);

        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG_GREEN);
        outer.add(form);
        p.add(outer, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bottom.setBackground(BG_GREEN);
        JButton submit = smallButton("Submit Donation");
        submit.addActionListener(e -> {
            try {
                int selectedId = Integer.parseInt(((String)catalogBox.getSelectedItem()).split(" - ")[0]);
                Object[] data = FoodCatalog.getItem(selectedId);
                String itemName = (String) data[0];
                String category = (String) data[1];
                String unit = (String) data[2];
                int shelf = (int) data[3];

                int qty = Integer.parseInt(qtyF.getText().trim());
                if (qty <= 0) { JOptionPane.showMessageDialog(frame, "Quantity must be positive."); return; }

                int id = donationCounter.incrementAndGet();
                Donation donation = new Donation(id, donor);
                FoodItem fi = new FoodItem(itemName, qty, category, unit, shelf);
                donation.addFoodItem(fi);

                donor.makeDonation(donation, inventory);

                JOptionPane.showMessageDialog(frame, "Donation submitted. ID: D" + id);
                showDonorMenu(donor);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Enter a valid quantity.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });
        bottom.add(submit);

        p.add(bottom, BorderLayout.SOUTH);
        p.add(navPanel("DonorMenu", false), BorderLayout.EAST);

        mainPanel.add(p, "DonationPage");
        cardLayout.show(mainPanel, "DonationPage");
    }

    // ---------- Recipient flow ----------
    private static void showRecipientMenu(Recipient recipient) {
        JPanel p = basePanel();
        p.add(heading("Recipient Dashboard - " + recipient.getName()), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG_GREEN);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(12,12,12,12);
        g.gridx = 0; g.gridy = 0;

        JLabel limits = new JLabel("Your limits: " + recipient.getLimitsSummary());
        limits.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        center.add(limits, g);

        g.gridy++;
        JButton viewBtn = bigButton("View Available Items");
        viewBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (FoodItem fi : inventory.getFoodItems()) sb.append(fi.toString()).append("\n");
            showTextAreaPage("Inventory", sb.toString(), "RecipientMenu");
        });
        center.add(viewBtn, g);

        g.gridy++;
        JButton reqBtn = bigButton("Make Request");
        reqBtn.addActionListener(e -> showRequestPage(recipient));
        center.add(reqBtn, g);

        g.gridy++;
        JButton histBtn = bigButton("My Requests");
        histBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            List<Request> list = recipient.viewMyRequests();
            if (list.isEmpty()) sb.append("No requests yet.");
            else for (Request r : list) sb.append(r.infoString()).append("\n\n");
            showTextAreaPage("My Requests", sb.toString(), "RecipientMenu");
        });
        center.add(histBtn, g);

        p.add(center, BorderLayout.CENTER);
        p.add(navPanel("RoleChoice", false), BorderLayout.SOUTH);

        mainPanel.add(p, "RecipientMenu");
        cardLayout.show(mainPanel, "RecipientMenu");
    }

    private static void showRequestPage(Recipient recipient) {
        JPanel p = basePanel();
        p.add(heading("Make a Request"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_GREEN);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8,8,8,8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> catalogBox = new JComboBox<>();
        for (Map.Entry<Integer, Object[]> e : FoodCatalog.catalog.entrySet()) {
            int id = e.getKey();
            Object[] data = e.getValue();
            String name = (String) data[0];
            String unit = (String) data[2];
            catalogBox.addItem(id + " - " + name + " (" + unit + ")");
        }

        JTextField qtyF = inputField(8);

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Item:"), g);
        g.gridx = 1; form.add(catalogBox, g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Quantity:"), g);
        g.gridx = 1; form.add(qtyF, g);

        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG_GREEN);
        outer.add(form);
        p.add(outer, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bottom.setBackground(BG_GREEN);
        JButton submit = smallButton("Submit Request");
        submit.addActionListener(e -> {
            try {
                int selectedId = Integer.parseInt(((String)catalogBox.getSelectedItem()).split(" - ")[0]);
                Object[] data = FoodCatalog.getItem(selectedId);
                String itemName = (String) data[0];
                String category = (String) data[1];
                String unit = (String) data[2];
                int shelf = (int) data[3];

                int qty = Integer.parseInt(qtyF.getText().trim());
                if (qty <= 0) { JOptionPane.showMessageDialog(frame, "Quantity must be positive."); return; }

                FoodItem requested = new FoodItem(itemName, qty, category, unit, shelf);
                String reqId = "REQ" + requestCounter.incrementAndGet();

                String msg = recipient.makeLimitedRequest(reqId, requested, inventory);
                JOptionPane.showMessageDialog(frame, msg);
                showRecipientMenu(recipient);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Enter a valid quantity.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });
        bottom.add(submit);

        p.add(bottom, BorderLayout.SOUTH);
        p.add(navPanel("RecipientMenu", false), BorderLayout.EAST);

        mainPanel.add(p, "RequestPage");
        cardLayout.show(mainPanel, "RequestPage");
    }

    // ---------- Admin flow ----------
    private static void showAdminMenu(Admin admin) {
        JPanel p = basePanel();
        p.add(heading("Admin Dashboard - " + admin.getName()), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG_GREEN);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(12,12,12,12);
        g.gridx = 0; g.gridy = 0;

        JButton manageReqBtn = bigButton("View & Manage Requests");
        manageReqBtn.addActionListener(e -> manageRequestsPage(admin));
        center.add(manageReqBtn, g);

        g.gridy++;
        JButton donationsBtn = bigButton("View All Donations");
        donationsBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (Donor d : userManager.getAllDonors()) {
                for (Donation dn : d.viewDonationHistory()) sb.append(dn.infoString()).append("\n\n");
            }
            if (sb.length() == 0) sb.append("No donations yet.");
            showTextAreaPage("All Donations", sb.toString(), "AdminMenu");
        });
        center.add(donationsBtn, g);

        g.gridy++;
        JButton usersBtn = bigButton("View Users");
        usersBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (User u : userManager.getUsers()) sb.append(u.getName()).append(" - ").append(u.getClass().getSimpleName()).append("\n");
            showTextAreaPage("All Users", sb.toString(), "AdminMenu");
        });
        center.add(usersBtn, g);

        g.gridy++;
        JButton invBtn = bigButton("View Inventory");
        invBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (FoodItem fi : inventory.getFoodItems()) sb.append(fi.toString()).append("\n");
            showTextAreaPage("Inventory", sb.toString(), "AdminMenu");
        });
        center.add(invBtn, g);

        p.add(center, BorderLayout.CENTER);
        p.add(navPanel("RoleChoice", false), BorderLayout.SOUTH);

        mainPanel.add(p, "AdminMenu");
        cardLayout.show(mainPanel, "AdminMenu");
    }

    private static void manageRequestsPage(Admin admin) {
        JPanel p = basePanel();
        p.add(heading("Manage Requests"), BorderLayout.NORTH);

        // show all requests
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        boolean foundAny = false;
        for (Recipient r : userManager.getAllRecipients()) {
            for (Request req : r.viewMyRequests()) {
                area.append(req.infoString() + "\n\n");
                foundAny = true;
            }
        }
        if (!foundAny) area.setText("No requests available.");

        JScrollPane sp = new JScrollPane(area);
        p.add(sp, BorderLayout.CENTER);

        // bottom panel: enter request ID and approve/reject + Back button to AdminMenu
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bottom.setBackground(BG_GREEN);

        bottom.add(new JLabel("Request ID:"));
        JTextField idField = new JTextField(12);
        bottom.add(idField);

        JButton approveBtn = smallButton("Approve");
        JButton rejectBtn = smallButton("Reject");

        approveBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter a Request ID."); return; }
            Request found = findRequestById(id);
            if (found == null) { JOptionPane.showMessageDialog(frame, "Request ID not found: " + id); return; }
            String res = admin.reviewRequest(found, true, inventory);
            JOptionPane.showMessageDialog(frame, res);
            // refresh page
            manageRequestsPage(admin);
        });

        rejectBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(frame, "Please enter a Request ID."); return; }
            Request found = findRequestById(id);
            if (found == null) { JOptionPane.showMessageDialog(frame, "Request ID not found: " + id); return; }
            String res = admin.reviewRequest(found, false, inventory);
            JOptionPane.showMessageDialog(frame, res);
            manageRequestsPage(admin);
        });

        bottom.add(approveBtn);
        bottom.add(rejectBtn);

        // Back to AdminMenu button
        JButton backToAdmin = smallButton("Back to Admin Menu");
        backToAdmin.addActionListener(e -> cardLayout.show(mainPanel, "AdminMenu"));
        bottom.add(backToAdmin);

        p.add(bottom, BorderLayout.SOUTH);

        mainPanel.add(p, "ManageRequests");
        cardLayout.show(mainPanel, "ManageRequests");
    }

    // helper: find request by id across recipients
    private static Request findRequestById(String id) {
        for (Recipient r : userManager.getAllRecipients()) {
            for (Request req : r.viewMyRequests()) {
                if (req.getRequestId().equalsIgnoreCase(id)) return req;
            }
        }
        return null;
    }
}
