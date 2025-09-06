package utility;

import db.DataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import model.FoodCatalog;

public class Seeder {

    public static void run() {
        seedAdmin();
        seedInventory();
    }

  private static void seedAdmin() {
    try (Connection c = DataBase.getConnection();
         PreparedStatement ps = c.prepareStatement(
            "INSERT OR IGNORE INTO users(name, role, email, password, familySize, contact, address) VALUES(?,?,?,?,?,?,?)"
         )) {
        ps.setString(1, "Abdelaziz Bashir");
        ps.setString(2, "Admin");
        ps.setString(3, "abdelaziz@FamilyBank.com");
        ps.setString(4, "admin123");
        ps.setInt(5, 0);
        ps.setString(6, "01228855732");
        ps.setString(7, "Cyberjaya");
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    

    private static void seedInventory() {
        try (Connection c = db.DataBase.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT OR IGNORE INTO inventory(itemName, category, unit, shelfLife, quantity) VALUES(?,?,?,?,?)"
             )) {

            for (Map.Entry<Integer, Object[]> e : FoodCatalog.catalog.entrySet()) {
                Object[] d = e.getValue();
                String name = (String) d[0];
                String category = (String) d[1];
                String unit = (String) d[2];
                int shelf = (int) d[3];

                ps.setString(1, name);
                ps.setString(2, category);
                ps.setString(3, unit);
                ps.setInt(4, shelf);
                ps.setInt(5, 50); // default quantity
                ps.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
