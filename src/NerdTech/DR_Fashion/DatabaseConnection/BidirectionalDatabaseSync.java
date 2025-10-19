package NerdTech.DR_Fashion.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BidirectionalDatabaseSync {

    // Use the same callback interface as DatabaseConnection
    public interface SyncStatusCallback {

        void onStatusChange(String status);
    }

    private static SyncStatusCallback statusCallback;

    public static void setStatusCallback(SyncStatusCallback callback) {
        statusCallback = callback;
        // Also set it in DatabaseConnection so all messages go through same callback
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

    private static final Map<String, String> primaryKeyMap = new HashMap<>() {
        {
            put("user", "id");
            put("employee", "id");
            put("accesories", "id");
            put("attendence", "id");
            put("bank_details", "id");
            put("designation", "id");
            put("monthly_payment", "id");
            put("per_day_salary", "id");
            put("resignation", "id");
            put("salary", "id");
            put("salary_details", "id");
            put("section", "id");
            put("stock", "id");
            put("type", "type_id");
        }
    };

    /**
     * Sync online database → local database (uses DatabaseConnection methods)
     */
    public static void syncOnlineToLocal() throws Exception {
        DatabaseConnection.syncOnlineToLocal();
    }

    /**
     * Sync local database → online database (uses DatabaseConnection methods)
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

                String[] tables = {
                    "user", "employee", "accesories", "attendence", "type", "stock"
                };

                boolean allConsistent = true;

                for (String table : tables) {
                    try {
                        long onlineCount = getRecordCount(table, onlineConn);
                        long localCount = getRecordCount(table, localConn);

                        if (onlineCount == localCount) {
                            updateStatus("✓ Table '" + table + "' consistent (" + localCount + " records)");
                        } else {
                            updateStatus("⚠ Table '" + table + "' - Count mismatch (Online: " + onlineCount + ", Local: " + localCount + ")");
                            allConsistent = false;
                        }
                    } catch (SQLException e) {
                        updateStatus("⚠ Cannot verify table '" + table + "': " + e.getMessage());
                    }
                }

                if (allConsistent) {
                    updateStatus("✓ All tables are consistent");
                } else {
                    updateStatus("⚠ Some tables have inconsistencies");
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
        String sql = "SELECT COUNT(*) FROM " + table;
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
}
