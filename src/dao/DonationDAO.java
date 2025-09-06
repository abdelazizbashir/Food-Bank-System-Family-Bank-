package dao;

import db.DataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonationDAO {

    public static void save(String donorName, String item, String category, String unit, int shelfLife, int qty) throws SQLException {
        String sql = "INSERT INTO donations(donorName, itemName, category, unit, shelfLife, quantity, timestamp) VALUES(?,?,?,?,?,?,?)";
        try(Connection c = DataBase.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, donorName);
            ps.setString(2, item);
            ps.setString(3, category);
            ps.setString(4, unit);
            ps.setInt(5, shelfLife);
            ps.setInt(6, qty);
            ps.setString(7, java.time.LocalDate.now().toString());
            ps.executeUpdate();
        }
        InventoryDAO.addStock(item, category, unit, shelfLife, qty);
    }

    public static ResultSet listByDonor(String donorName) throws SQLException {
        String sql = "SELECT * FROM donations WHERE donorName=?";
        Connection c = DataBase.getConnection();
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, donorName);
        return ps.executeQuery();
    }

    public static List<String> listAll() throws SQLException {
        List<String> out = new ArrayList<>();
        String sql = "SELECT * FROM donations ORDER BY timestamp DESC";
        try(Connection c = DataBase.getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            while(rs.next()) {
                out.add(rs.getString("donorName")+" donated "+rs.getString("itemName")+" x"+rs.getInt("quantity")+" on "+rs.getString("timestamp"));
            }
        }
        return out;
    }
}
