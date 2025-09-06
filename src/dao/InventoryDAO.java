package dao;

import db.DataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public static void addStock(String item, String category, String unit, int shelfLife, int qty) throws SQLException {
        String select = "SELECT quantity FROM inventory WHERE itemName=?";
        String update = "UPDATE inventory SET quantity=? WHERE itemName=?";
        String insert = "INSERT INTO inventory(itemName, category, unit, shelfLife, quantity) VALUES(?,?,?,?,?)";

        try (Connection c = DataBase.getConnection()) {
            c.setAutoCommit(false);
            int current = 0;
            boolean exists = false;
            try (PreparedStatement ps = c.prepareStatement(select)) {
                ps.setString(1, item);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) { current = rs.getInt("quantity"); exists = true; }
                }
            }
            if (exists) {
                try (PreparedStatement ps = c.prepareStatement(update)) {
                    ps.setInt(1, current + qty);
                    ps.setString(2, item);
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = c.prepareStatement(insert)) {
                    ps.setString(1, item);
                    ps.setString(2, category);
                    ps.setString(3, unit);
                    ps.setInt(4, shelfLife);
                    ps.setInt(5, qty);
                    ps.executeUpdate();
                }
            }
            c.commit();
        }
    }

    public static boolean reduceStock(String item, int qty) throws SQLException {
        String select = "SELECT quantity FROM inventory WHERE itemName=?";
        String update = "UPDATE inventory SET quantity=quantity-? WHERE itemName=?";
        try (Connection c = DataBase.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(select)) {
                ps.setString(1, item);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt("quantity") >= qty) {
                        try (PreparedStatement up = c.prepareStatement(update)) {
                            up.setInt(1, qty);
                            up.setString(2, item);
                            up.executeUpdate();
                        }
                        c.commit();
                        return true;
                    } else {
                        return false; // not enough stock
                    }
                }
            }
        }
    }

    public static List<String> listAll() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT itemName, category, unit, shelfLife, quantity FROM inventory ORDER BY itemName";
        try (Connection c = DataBase.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("itemName") + " | " +
                         rs.getString("category") + " | " +
                         rs.getString("unit") + " | Shelf:" +
                         rs.getInt("shelfLife") + " | Qty:" +
                         rs.getInt("quantity"));
            }
        }
        return list;
    }
}
