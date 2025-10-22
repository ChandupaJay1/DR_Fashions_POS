package NerdTech.DR_Fashion.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BidirectionalDatabaseSync {

    public interface SyncStatusCallback {

        void onStatusChange(String status);
    }

    private static SyncStatusCallback statusCallback;

    public static void setStatusCallback(SyncStatusCallback callback) {
        statusCallback = callback;
        DatabaseConnection.setStatusCallback(new DatabaseConnection.StatusCallback() {
            @Override
            public void onStatusChange(String status) {
                if (statusCallback != null) {
                    statusCallback.onStatusChange(status);
                }
            }
        });
    }

    private static void updateStatus(String status) {
        if (statusCallback != null) {
            statusCallback.onStatusChange(status);
        }
        System.out.println("[SYNC] " + status);
    }

    // Primary key mapping - ONLY actual tables from database
    private static final Map<String, String> primaryKeyMap = new HashMap<>() {
        {
            // Core tables
            put("user", "id");
            put("employee", "id");
            put("designation", "id");
            put("section", "id");
            put("bank_details", "id");

            // Attendance & Salary tables
            put("attendence", "id");
            put("salary", "id");
            put("salary_details", "id");
            put("per_day_salary", "id");
            put("monthly_payment", "id");
            put("resignation", "id");

            // Inventory tables
            put("type", "type_id");  // Special case - different primary key
            put("accesories", "id");
            put("stock", "id");

            // Buyer & Shipment tables (මේවා තියෙනවා database එකේ)
            put("buyer", "id");
            put("registration_buyer", "id");
            put("registration_buyers", "id");  // Duplicate table
            put("bstock", "id");
            put("baccesories", "id");
        }
    };

    /**
     * Get ONLY the tables that actually exist in the database මේ tables විතරයි
     * database schema එකේ තියෙන්නේ
     */
    private static String[] getSyncTables() {
        return new String[]{
            // Core tables - මේවා priority
            "user",
            "designation",
            "section",
            "employee", // designation & section වලට depend වෙනවා
            "bank_details", // employee එකට depend වෙනවා

            // Attendance & Salary
            "attendence",
            "salary",
            "salary_details",
            "per_day_salary",
            "monthly_payment",
            "resignation",
            // Inventory - type එක first
            "type", // accesories එකට depend වෙනවා
            "accesories",
            "stock",
            // Buyer tables
            "buyer",
            "registration_buyer",
            "registration_buyers",
            "bstock", // registration_buyer එකට depend වෙනවා
            "baccesories" // registration_buyer & type එකට depend වෙනවා
        };
    }

    /**
     * Safe bidirectional sync - doesn't throw exceptions
     */
    public static void syncBothSafe() {
        try {
            updateStatus("Starting safe bidirectional sync...");
            DatabaseConnection.syncOnlineToLocalSafe();
            DatabaseConnection.syncLocalToOnlineSafe();
            updateStatus("✅ Safe sync completed!");
        } catch (Exception e) {
            updateStatus("⚠️ Safe sync completed with errors: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Full bidirectional sync with proper error handling
     */
    public static boolean performFullSync() {
        try {
            updateStatus("Starting bidirectional sync...");

            // Step 1: Test online connection
            updateStatus("Testing online database connection...");
            Connection testConn = null;
            boolean onlineAvailable = false;

            try {
                testConn = DatabaseConnection.tryOnlineConnection();
                if (testConn != null && !testConn.isClosed()) {
                    onlineAvailable = true;
                    updateStatus("✓ Online database is reachable");
                    testConn.close();
                }
            } catch (Exception e) {
                updateStatus("✗ Online database not available: " + e.getMessage());
            }

            if (!onlineAvailable) {
                updateStatus("⚠️ Sync aborted - Online database not available");
                updateStatus("Working in offline mode");
                return false;
            }

            // Step 2: Sync from online to local (pull latest)
            updateStatus("Pulling updates from online database...");
            try {
                DatabaseConnection.syncOnlineToLocal();
                Thread.sleep(500);
            } catch (Exception e) {
                updateStatus("⚠️ Pull from online failed: " + e.getMessage());
                throw e;
            }

            // Step 3: Sync from local to online (push changes)
            updateStatus("Pushing local changes to online database...");
            try {
                DatabaseConnection.syncLocalToOnline();
                Thread.sleep(500);
            } catch (Exception e) {
                updateStatus("⚠️ Push to online failed: " + e.getMessage());
                throw e;
            }

            // Step 4: Verification
            updateStatus("Verifying data consistency...");
            verifyDataConsistency();

            updateStatus("✅ Sync completed successfully!");
            return true;

        } catch (Exception e) {
            updateStatus("❌ Sync failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sync online database → local database
     */
    public static void syncOnlineToLocal() throws Exception {
        DatabaseConnection.syncOnlineToLocal();
    }

    /**
     * Sync local database → online database
     */
    public static void syncLocalToOnline() throws Exception {
        DatabaseConnection.syncLocalToOnline();
    }

    /**
     * Safe sync methods
     */
    public static void syncOnlineToLocalSafe() throws Exception {
        DatabaseConnection.syncOnlineToLocalSafe();
    }

    public static void syncLocalToOnlineSafe() throws Exception {
        DatabaseConnection.syncLocalToOnlineSafe();
    }

    /**
     * Verify data consistency between databases
     */
    private static void verifyDataConsistency() {
        try {
            updateStatus("Verifying data integrity...");

            try (Connection onlineConn = DatabaseConnection.getOnlineConnection(); Connection localConn = DatabaseConnection.getLocalConnection()) {

                String[] tables = getSyncTables();
                boolean allConsistent = true;
                int consistentCount = 0;
                int inconsistentCount = 0;
                int missingCount = 0;

                for (String table : tables) {
                    try {
                        // Check if table exists in both databases
                        if (!tableExists(table, onlineConn)) {
                            updateStatus("⚠ Table '" + table + "' not found in online database");
                            missingCount++;
                            continue;
                        }

                        if (!tableExists(table, localConn)) {
                            updateStatus("⚠ Table '" + table + "' not found in local database");
                            missingCount++;
                            continue;
                        }

                        long onlineCount = getRecordCount(table, onlineConn);
                        long localCount = getRecordCount(table, localConn);

                        if (onlineCount == localCount) {
                            updateStatus("✓ " + table + " (" + localCount + " records)");
                            consistentCount++;
                        } else {
                            updateStatus("⚠ " + table + " - Mismatch (Online: "
                                    + onlineCount + ", Local: " + localCount + ")");
                            allConsistent = false;
                            inconsistentCount++;
                        }
                    } catch (SQLException e) {
                        updateStatus("⚠ Cannot verify '" + table + "': " + e.getMessage());
                        inconsistentCount++;
                    }
                }

                // Summary
                if (allConsistent && missingCount == 0) {
                    updateStatus("✅ All " + consistentCount + " tables are consistent");
                } else {
                    String summary = String.format("⚠ Summary: %d consistent, %d mismatch",
                            consistentCount, inconsistentCount);
                    if (missingCount > 0) {
                        summary += String.format(", %d missing", missingCount);
                    }
                    updateStatus(summary);
                }
            }
        } catch (Exception e) {
            updateStatus("Verification warning: " + e.getMessage());
        }
    }

    /**
     * Get record count for a table
     */
    private static long getRecordCount(String table, Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM `" + table + "`";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getLong(1) : 0;
        }
    }

    /**
     * Check if column exists in table
     */
    private static boolean columnExistsInTable(String tableName, String columnName, Connection conn) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(null, null, tableName, columnName);
            return rs.next();
        } catch (SQLException e) {
            System.err.println("⚠️ Error checking column existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get primary key for a table
     */
    public static String getPrimaryKey(String tableName) {
        return primaryKeyMap.getOrDefault(tableName, "id");
    }

    /**
     * Check if table exists in database
     */
    public static boolean tableExists(String tableName, Connection conn) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
            boolean exists = rs.next();
            rs.close();
            return exists;
        } catch (SQLException e) {
            System.err.println("⚠️ Error checking table existence: " + e.getMessage());
            return false;
        }
    }
}
