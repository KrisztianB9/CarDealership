package ro.umfst.oop.cardealership;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private static final String DB_URL = "jdbc:sqlite:dealership.db";

    public static void initializeDB() {
        String sql = "CREATE TABLE IF NOT EXISTS vehicles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT NOT NULL, " +
                "make TEXT NOT NULL, " +
                "model TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "extra_attribute INTEGER" +
                ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database initialized.");
        } catch (SQLException e) {
            System.err.println("DB Init Error: " + e.getMessage());
        }
    }

    public static void addVehicle(Vehicle v) {
        String sql = "INSERT INTO vehicles(type, make, model, price, extra_attribute) VALUES(?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(2, v.make);
            pstmt.setString(3, v.model);
            pstmt.setDouble(4, v.getPrice());

            if (v instanceof Car) {
                pstmt.setString(1, "Car");
                pstmt.setInt(5, ((Car) v).getDoors());
            } else if (v instanceof Motorcycle) {
                pstmt.setString(1, "Motorcycle");
                pstmt.setInt(5, ((Motorcycle) v).getHasSidecar());
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Insert Error: " + e.getMessage());
        }
    }
    //konnyu metodus, nem kell get metodusokat hivjon
    public static void addVehicleRaw(String type, String make, String model, double price, int extra) {
        String sql = "INSERT INTO vehicles(type, make, model, price, extra_attribute) VALUES(?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.setString(2, make);
            pstmt.setString(3, model);
            pstmt.setDouble(4, price);
            pstmt.setInt(5, extra);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Insert Error: " + e.getMessage());
        }
    }

    public static void deleteVehicle(String make, String model) {
        String sql = "DELETE FROM vehicles WHERE make = ? AND model = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, make);
            pstmt.setString(2, model);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Delete Error: " + e.getMessage());
        }
    }

    public static List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String type = rs.getString("type");
                String make = rs.getString("make");
                String model = rs.getString("model");
                double price = rs.getDouble("price");
                int extra = rs.getInt("extra_attribute");

                try {
                    if (type.equals("Car")) {
                        list.add(new Car(make, model, price, extra)); // Extra is doors
                    } else if (type.equals("Motorcycle")) {
                        boolean hasSidecar = (extra == 1);
                        list.add(new Motorcycle(make, model, price, hasSidecar));
                    }
                } catch (InvalidPriceException e) {
                    System.err.println("Skipping invalid vehicle in DB: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Query Error: " + e.getMessage());
        }
        return list;
    }
}