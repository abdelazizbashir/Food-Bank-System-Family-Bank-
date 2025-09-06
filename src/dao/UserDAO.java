package dao;

import db.DataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.*;

public class UserDAO {

    // Register Donor or Recipient
    public static void registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users(name, role, email, password, familySize, contact, address) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = DataBase.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getClass().getSimpleName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setInt(5, (user instanceof Recipient) ? ((Recipient) user).getFamilySize() : 0);
            ps.setString(6, user.getContact());
            ps.setString(7, user.getAddress());
            ps.executeUpdate();
        }
    }

    // Login and return proper User object
    public static User login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email=? AND password=?";
        try (Connection c = DataBase.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String role = rs.getString("role");
                String name = rs.getString("name");
                String contact = rs.getString("contact");
                String address = rs.getString("address");
                int famSize = rs.getInt("familySize");

                switch (role) {
                    case "Donor": return new Donor(name, contact, email, address, password);
                    case "Recipient": return new Recipient(name, contact, email, address, famSize, password);
                    case "Admin": return new Admin(name, contact, email, address, password);
                    default: return null;
                }
            }
        }
    }
    public static List<String> listDonors() throws SQLException {
    List<String> donors = new ArrayList<>();
    String sql = "SELECT id, name, email, contact FROM users WHERE role='Donor'";
    try (Connection c = DataBase.getConnection();
         Statement st = c.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
        while (rs.next()) {
            donors.add("ID: " + rs.getInt("id") + 
                       " | Name: " + rs.getString("name") + 
                       " | Email: " + rs.getString("email") + 
                       " | Contact: " + rs.getString("contact"));
        }
    }
    return donors;
}

public static List<String> listRecipients() throws SQLException {
    List<String> recipients = new ArrayList<>();
    String sql = "SELECT id, name, familySize, email, contact FROM users WHERE role='Recipient'";
    try (Connection c = DataBase.getConnection();
         Statement st = c.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
        while (rs.next()) {
            recipients.add("ID: " + rs.getInt("id") + 
                           " | Name: " + rs.getString("name") + 
                           " | Family Size: " + rs.getInt("familySize") + 
                           " | Email: " + rs.getString("email") + 
                           " | Contact: " + rs.getString("contact"));
        }
    }
    return recipients;
}
   

}
