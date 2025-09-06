package dao;

import db.DataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {

    // ------------------- SAVE REQUEST -------------------
    public static int save(String recipientName, String itemName, String category,
                           String unit, int shelfLife, int quantity) throws SQLException {
        String sql = "INSERT INTO requests (recipientName, itemName, category, unit, shelfLife, quantity, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DataBase.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, recipientName);
            ps.setString(2, itemName);
            ps.setString(3, category);
            ps.setString(4, unit);
            ps.setInt(5, shelfLife);
            ps.setInt(6, quantity);
            ps.setString(7, "Pending");
            return ps.executeUpdate();
        }
    }

    // ------------------- GET ALREADY REQUESTED (per item) -------------------
    /** Returns how much of a given item a recipient has already requested (pending + approved). */
    public static int getAlreadyRequested(String recipientName, String itemName) throws SQLException {
        String sql = "SELECT SUM(quantity) as total FROM requests " +
                     "WHERE recipientName=? AND itemName=? AND status IN ('Pending','Approved')";
        try (Connection c = DataBase.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, recipientName);
            ps.setString(2, itemName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0; // no requests yet
    }

    // ------------------- LIST REQUESTS -------------------
    public static List<String> listAllPending() throws SQLException {
        List<String> requests = new ArrayList<>();
        String sql = "SELECT requestId, recipientName, itemName, quantity " +
                     "FROM requests WHERE status='Pending'";
        try (Connection c = DataBase.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                requests.add(rs.getInt("requestId") + " | " +
                             rs.getString("recipientName") + " | " +
                             rs.getString("itemName") + " x" +
                             rs.getInt("quantity"));
            }
        }
        return requests;
    }

    public static List<String> listByRecipient(String recipientName) throws SQLException {
        List<String> requests = new ArrayList<>();
        String sql = "SELECT requestId, itemName, quantity, status FROM requests WHERE recipientName=?";
        try (Connection c = DataBase.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, recipientName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(rs.getInt("requestId") + " | " +
                                 rs.getString("itemName") + " x" +
                                 rs.getInt("quantity") + " | Status: " +
                                 rs.getString("status"));
                }
            }
        }
        return requests;
    }

    // ------------------- APPROVE / REJECT -------------------
    public static void approveRequest(int requestId) throws SQLException {
        Request req = getById(requestId);
        if (req != null) {
            boolean success = dao.InventoryDAO.reduceStock(req.itemName, req.quantity);
            if (!success) throw new SQLException("Not enough stock for " + req.itemName);
            updateRequestStatus(requestId, "Approved");
        }
    }

    public static void rejectRequest(int requestId) throws SQLException {
        updateRequestStatus(requestId, "Rejected");
    }

    private static void updateRequestStatus(int requestId, String status) throws SQLException {
        String sql = "UPDATE requests SET status=? WHERE requestId=?";
        try (Connection c = DataBase.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, requestId);
            ps.executeUpdate();
        }
    }

    // ------------------- GET REQUEST BY ID -------------------
    public static Request getById(int requestId) throws SQLException {
        String sql = "SELECT * FROM requests WHERE requestId=?";
        try (Connection c = DataBase.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Request(
                        rs.getInt("requestId"),
                        rs.getString("recipientName"),
                        rs.getString("itemName"),
                        rs.getString("category"),
                        rs.getString("unit"),
                        rs.getInt("shelfLife"),
                        rs.getInt("quantity"),
                        rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    // ------------------- INTERNAL REQUEST CLASS -------------------
    public static class Request {
        public int requestId;
        public String recipientName;
        public String itemName;
        public String category;
        public String unit;
        public int shelfLife;
        public int quantity;
        public String status;

        public Request(int requestId, String recipientName, String itemName, String category,
                       String unit, int shelfLife, int quantity, String status) {
            this.requestId = requestId;
            this.recipientName = recipientName;
            this.itemName = itemName;
            this.category = category;
            this.unit = unit;
            this.shelfLife = shelfLife;
            this.quantity = quantity;
            this.status = status;
        }
    }
}
