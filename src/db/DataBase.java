package db;

import java.sql.*;

public class DataBase {
    private static final String URL = "jdbc:sqlite:foodbank.db";

    // Initialize DB and create tables if not exist
    public static void initialize() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // Users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    contact TEXT,
                    email TEXT UNIQUE,
                    address TEXT,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL,  -- Donor, Recipient, Admin
                    familySize INTEGER
                );
            """);

            // Donations table (flat structure for DAO)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS donations (
                    donationId INTEGER PRIMARY KEY AUTOINCREMENT,
                    donorName TEXT NOT NULL,
                    itemName TEXT NOT NULL,
                    category TEXT,
                    unit TEXT,
                    shelfLife INTEGER,
                    quantity INTEGER,
                    timestamp TEXT
                );
            """);

            // Requests table (flat structure for DAO)
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS requests (
            requestId INTEGER PRIMARY KEY AUTOINCREMENT,
            recipientName TEXT NOT NULL,
            itemName TEXT NOT NULL,
            category TEXT,
            unit TEXT,
            shelfLife INTEGER,
            quantity INTEGER,
            status TEXT DEFAULT 'Pending',
            requestDate TEXT DEFAULT (datetime('now','localtime'))
            );
            """ );


            // Inventory table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS inventory (
                    itemId INTEGER PRIMARY KEY AUTOINCREMENT,
                    itemName TEXT NOT NULL,
                    category TEXT,
                    unit TEXT,
                    shelfLife INTEGER,
                    quantity INTEGER
                );
            """);

            System.out.println(" Database initialized successfully at: " + new java.io.File("foodbank.db").getAbsolutePath());

        } catch (SQLException e) {
            System.out.println(" Database initialization failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            // 🔑 Force load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found. Make sure the jar is in the classpath.", e);
        }
        return DriverManager.getConnection(URL);
    }
}
