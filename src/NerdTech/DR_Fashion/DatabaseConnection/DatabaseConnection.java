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

            try (FileInputStream input = new FileInputStream("config.properties")) {
                props.load(input);
                updateStatus("✅ Config loaded from file system");
            } catch (IOException ex) {
                updateStatus("⚠️ Could not load config.properties from file system");
                updateStatus("⚠️ Will use hardcoded values as fallback");
            }
        }

        private static Connection createConnection() throws ClassNotFoundException, SQLException {
            String onlineUrl = props.getProperty("db.online.url");
            String onlineUser = props.getProperty("db.online.user");
            String onlinePass = props.getProperty("db.online.password");

            String localUrl = props.getProperty("db.local.url",
                    "jdbc:mysql://localhost:3306/dr_fashions");
            String localUser = props.getProperty("db.local.user", "root");
            String localPass = props.getProperty("db.local.password", "88222006");

            Class.forName("com.mysql.cj.jdbc.Driver");

            updateStatus("📋 Config values loaded");

            try {
                if (onlineUrl != null && !onlineUrl.isEmpty()
                        && onlineUser != null && !onlineUser.isEmpty()
                        && onlinePass != null && !onlinePass.isEmpty()) {

                    updateStatus("🔄 Connecting to online database...");
                    return DriverManager.getConnection(onlineUrl, onlineUser, onlinePass);
                } else {
                    throw new SQLException("Online DB credentials not found");
                }
            } catch (SQLException ex) {
                updateStatus("❌ Online DB failed: " + ex.getMessage());
                updateStatus("🔄 Trying local database...");

                try {
                    return DriverManager.getConnection(localUrl, localUser, localPass);
                } catch (SQLException ex1) {
                    updateStatus("❌ Both databases failed!");
                    Logger.getLogger(DatabaseConnection.class.getName())
                            .log(Level.SEVERE, "Both DB connections failed", ex1);
                    throw ex1;
                }
            }
        }

        public static Connection getConnection() throws Exception {
            // always return a NEW connection
            return createConnection();
        }
    }
