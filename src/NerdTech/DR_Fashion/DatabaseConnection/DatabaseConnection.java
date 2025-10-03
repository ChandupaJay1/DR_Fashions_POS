package NerdTech.DR_Fashion.DatabaseConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {

    private static Connection con;
    private static final Properties props = new Properties();
    private static Consumer<String> statusCallback;

    // Static block - runs when class is loaded
    static {
        loadConfig();
    }

    public static void setStatusCallback(Consumer<String> callback) {
        statusCallback = callback;
    }

    private static void updateStatus(String message) {
        System.out.println(message);
        if (statusCallback != null) {
            statusCallback.accept(message);
        }
    }

    private static void loadConfig() {
        updateStatus("📂 Loading configuration...");

        // Try loading from resources folder first (packaged in JAR)
        try (InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
                updateStatus("✅ Config loaded from resources");
                return;
            }
        } catch (IOException ex) {
            updateStatus("⚠️ Could not load from resources: " + ex.getMessage());
        }

        // Try loading from file system (development)
        try (FileInputStream input = new FileInputStream("config.properties")) {
            props.load(input);
            updateStatus("✅ Config loaded from file system");
        } catch (IOException ex) {
            updateStatus("⚠️ Could not load config.properties from file system");
            updateStatus("⚠️ Will use hardcoded values as fallback");
        }
    }

    private static void connection() throws ClassNotFoundException, SQLException {
        // Read from properties file with fallback values
        String onlineUrl = props.getProperty("db.online.url");
        String onlineUser = props.getProperty("db.online.user");
        String onlinePass = props.getProperty("db.online.password");

        String localUrl = props.getProperty("db.local.url",
                "jdbc:mysql://localhost:3306/dr_fashions");
        String localUser = props.getProperty("db.local.user", "root");
        String localPass = props.getProperty("db.local.password", "88222006");

        Class.forName("com.mysql.cj.jdbc.Driver");

        // Print what we're trying to connect to (for debugging)
        updateStatus("📋 Config values loaded");
        System.out.println("   Online URL: " + (onlineUrl != null ? onlineUrl : "NOT SET"));
        System.out.println("   Online User: " + (onlineUser != null ? onlineUser : "NOT SET"));
        System.out.println("   Online Pass: " + (onlinePass != null ? "***SET***" : "NOT SET"));

        try {
            // Try ONLINE DB if credentials are set
            if (onlineUrl != null && !onlineUrl.isEmpty()
                    && onlineUser != null && !onlineUser.isEmpty()
                    && onlinePass != null && !onlinePass.isEmpty()) {

                updateStatus("🔄 Connecting to online database...");
                System.out.println("   URL: " + onlineUrl);

                con = DriverManager.getConnection(onlineUrl, onlineUser, onlinePass);
                updateStatus("✅ Connected to Online Database!");
            } else {
                throw new SQLException("Online DB credentials not found in config.properties");
            }
        } catch (SQLException ex) {
            // Fallback to LOCAL DB
            updateStatus("❌ Online DB failed: " + ex.getMessage());
            updateStatus("🔄 Trying local database...");
            System.out.println("   URL: " + localUrl);

            try {
                con = DriverManager.getConnection(localUrl, localUser, localPass);
                updateStatus("✅ Connected to Local Database!");
            } catch (SQLException ex1) {
                updateStatus("❌ Both databases failed!");
                Logger.getLogger(DatabaseConnection.class.getName())
                        .log(Level.SEVERE, "Both online and local DB connections failed", ex1);
                throw ex1;
            }
        }
    }

    public static Connection getConnection() throws Exception {
        if (con == null || con.isClosed()) {
            connection();
        }
        return con;
    }

    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
                updateStatus("✅ Database connection closed");
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName())
                        .log(Level.SEVERE, "Error closing connection", ex);
            }
        }
    }
}
