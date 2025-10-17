package NerdTech.DR_Fashion.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class BidirectionalDatabaseSync {

    public static void syncBothSafe() {
        try {
            syncLocalToOnlineSafe();
            syncOnlineToLocalSafe();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Sync failed: " + e.getMessage());
        }
    }

    public static void syncLocalToOnlineSafe() throws Exception {
        DatabaseConnection.syncLocalToOnlineSafe();
    }

    public static void syncOnlineToLocalSafe() throws Exception {
        DatabaseConnection.syncOnlineToLocalSafe();
    }

    private static final String SYNC_LOG_TABLE = "sync_log";
    private static SyncStatusCallback statusCallback;

    public interface SyncStatusCallback {

        void onStatusChange(String status);
    }

    public static void setStatusCallback(SyncStatusCallback callback) {
        statusCallback = callback;
    }

    private static void updateStatus(String status) {
        if (statusCallback != null) {
            statusCallback.onStatusChange(status);
        }
        System.out.println("[SYNC] " + status);
    }

    public static void syncBoth() throws Exception {
        // මෙතනදී ලොකල් → online සහ online → local sync logic එකක් තියෙනවා
        syncLocalToOnline();
        syncOnlineToLocal();
    }

    /**
     * Full bidirectional sync - local <-> online
     */
    public static boolean performFullSync() {
        try {
            updateStatus("Starting bidirectional sync...");

            // Step 1: Create sync log table if not exists
            createSyncLogTable();

            // Step 2: Sync from online to local (get latest updates)
            updateStatus("Pulling updates from online database...");
            syncOnlineToLocal();
            Thread.sleep(500);

            // Step 3: Sync from local to online (push local changes)
            updateStatus("Pushing local changes to online database...");
            syncLocalToOnline();
            Thread.sleep(500);

            // Step 4: Final verification
            updateStatus("Verifying data consistency...");
            verifyDataConsistency();

            updateStatus("Sync completed successfully!");
            return true;

        } catch (Exception e) {
            updateStatus("Sync failed: " + e.getMessage());
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
     * Sync online database → local database
     */
    public static void syncOnlineToLocal() throws Exception {
        updateStatus("Syncing: Online → Local");

        try (Connection onlineConn = DatabaseConnection.getOnlineConnection(); Connection localConn = DatabaseConnection.getLocalConnection()) {

            if (onlineConn == null || localConn == null) {
                throw new Exception("Database connections failed");
            }

            String[] tables = {
                "user", "employee", "accesories", "attendence", "bank_details", "designation",
                "monthly_payment", "per_day_salary", "resignation", "salary", "salary_details",
                "section", "stock", "type"
            };

            for (String table : tables) {
                syncTableOnlineToLocal(table, onlineConn, localConn);
                updateStatus("Synced table: " + table + " (Online → Local)");
            }
        }
    }

    /**
     * Sync local database → online database
     */
    public static void syncLocalToOnline() throws Exception {
        updateStatus("Syncing: Local → Online");

        try (Connection onlineConn = DatabaseConnection.getOnlineConnection(); Connection localConn = DatabaseConnection.getLocalConnection()) {

            if (onlineConn == null || localConn == null) {
                throw new Exception("Database connections failed");
            }

            String[] tables = {
                "user", "employee", "accesories", "attendence", "bank_details", "designation",
                "monthly_payment", "per_day_salary", "resignation", "salary", "salary_details",
                "section", "stock", "type"
            };

            for (String table : tables) {
                syncTableLocalToOnline(table, onlineConn, localConn);
                updateStatus("Synced table: " + table + " (Local → Online)");
            }
        }
    }

    /**
     * Sync individual table: Online → Local
     */
    private static void syncTableOnlineToLocal(String tableName, Connection onlineConn, Connection localConn) throws SQLException {
        String selectSql = "SELECT * FROM " + tableName;

        try (Statement onlineStmt = onlineConn.createStatement(); ResultSet rs = onlineStmt.executeQuery(selectSql)) {

            while (rs.next()) {
                // Get primary key (assuming 'id' is always first column)
                int id = rs.getInt(1);

                // Check if record exists in local
                if (!recordExistsInLocal(tableName, id, localConn)) {
                    // Insert new record
                    insertRecordToLocal(tableName, rs, localConn);
                } else {
                    // Update existing record
                    updateRecordInLocal(tableName, rs, localConn);
                }
            }
        }
    }

    /**
     * Sync individual table: Local → Online
     */
    private static void syncTableLocalToOnline(String tableName, Connection onlineConn, Connection localConn) throws SQLException {
        String selectSql = "SELECT * FROM " + tableName;

        try (Statement localStmt = localConn.createStatement(); ResultSet rs = localStmt.executeQuery(selectSql)) {

            while (rs.next()) {
                int id = rs.getInt(1);

                if (!recordExistsInOnline(tableName, id, onlineConn)) {
                    insertRecordToOnline(tableName, rs, onlineConn);
                } else {
                    updateRecordInOnline(tableName, rs, onlineConn);
                }
            }
        }
    }

    /**
     * Check if record exists in local DB
     */
    private static boolean recordExistsInLocal(String table, int id, Connection conn) throws SQLException {
        String primaryKey = primaryKeyMap.get(table);
        if (primaryKey == null) {
            throw new SQLException("Primary key not defined for table: " + table);
        }
        String sql = "SELECT " + primaryKey + " FROM " + table + " WHERE " + primaryKey + " = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeQuery().next();
        }
    }

    /**
     * Check if record exists in online DB
     */
    private static boolean recordExistsInOnline(String table, int id, Connection conn) throws SQLException {
        String primaryKey = primaryKeyMap.get(table);
        if (primaryKey == null) {
            throw new SQLException("Primary key not defined for table: " + table);
        }
        String sql = "SELECT " + primaryKey + " FROM " + table + " WHERE " + primaryKey + " = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeQuery().next();
        }
    }

    /**
     * Insert record to local database
     */
    private static void insertRecordToLocal(String tableName, ResultSet sourceRs, Connection localConn) throws SQLException {
        int columnCount = sourceRs.getMetaData().getColumnCount();
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (int i = 1; i <= columnCount; i++) {
            columns.append(sourceRs.getMetaData().getColumnName(i));
            placeholders.append("?");
            if (i < columnCount) {
                columns.append(", ");
                placeholders.append(", ");
            }
        }

        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

        try (PreparedStatement pstmt = localConn.prepareStatement(sql)) {
            for (int i = 1; i <= columnCount; i++) {
                pstmt.setObject(i, sourceRs.getObject(i));
            }
            pstmt.executeUpdate();
        }
    }

    /**
     * Insert record to online database
     */
    private static void insertRecordToOnline(String tableName, ResultSet sourceRs, Connection onlineConn) throws SQLException {
        int columnCount = sourceRs.getMetaData().getColumnCount();
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (int i = 1; i <= columnCount; i++) {
            columns.append(sourceRs.getMetaData().getColumnName(i));
            placeholders.append("?");
            if (i < columnCount) {
                columns.append(", ");
                placeholders.append(", ");
            }
        }

        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

        try (PreparedStatement pstmt = onlineConn.prepareStatement(sql)) {
            for (int i = 1; i <= columnCount; i++) {
                pstmt.setObject(i, sourceRs.getObject(i));
            }
            pstmt.executeUpdate();
        }
    }

    /**
     * Update record in local database
     */
    /**
     * Update record in local database
     */
    private static void updateRecordInLocal(String tableName, ResultSet sourceRs, Connection localConn) throws SQLException {
        int columnCount = sourceRs.getMetaData().getColumnCount();
        StringBuilder setClause = new StringBuilder();

        // Build SET part of query (skip primary key column)
        for (int i = 2; i <= columnCount; i++) {
            setClause.append(sourceRs.getMetaData().getColumnName(i)).append(" = ?");
            if (i < columnCount) {
                setClause.append(", ");
            }
        }

        // ✅ Use correct primary key
        String primaryKey = primaryKeyMap.getOrDefault(tableName, "id");
        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + primaryKey + " = ?";

        try (PreparedStatement pstmt = localConn.prepareStatement(sql)) {
            int paramIndex = 1;
            for (int i = 2; i <= columnCount; i++) {
                pstmt.setObject(paramIndex++, sourceRs.getObject(i));
            }
            pstmt.setObject(paramIndex, sourceRs.getObject(1)); // Primary key value
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("⚠️ [Local Update Failed] Table: " + tableName + " | Key: " + primaryKey + " | " + e.getMessage());
        }
    }

    /**
     * Update record in online database
     */
    private static void updateRecordInOnline(String tableName, ResultSet sourceRs, Connection onlineConn) throws SQLException {
        int columnCount = sourceRs.getMetaData().getColumnCount();
        StringBuilder setClause = new StringBuilder();

        // Build SET part of query (skip primary key column)
        for (int i = 2; i <= columnCount; i++) {
            setClause.append(sourceRs.getMetaData().getColumnName(i)).append(" = ?");
            if (i < columnCount) {
                setClause.append(", ");
            }
        }

        // ✅ Use correct primary key
        String primaryKey = primaryKeyMap.getOrDefault(tableName, "id");
        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + primaryKey + " = ?";

        try (PreparedStatement pstmt = onlineConn.prepareStatement(sql)) {
            int paramIndex = 1;
            for (int i = 2; i <= columnCount; i++) {
                pstmt.setObject(paramIndex++, sourceRs.getObject(i));
            }
            pstmt.setObject(paramIndex, sourceRs.getObject(1)); // Primary key value
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("⚠️ [Online Update Failed] Table: " + tableName + " | Key: " + primaryKey + " | " + e.getMessage());
        }
    }

    /**
     * Create sync log table for tracking
     */
    private static void createSyncLogTable() {
        String sql = "CREATE TABLE IF NOT EXISTS sync_log ("
                + "sync_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "table_name VARCHAR(100),"
                + "record_id INT,"
                + "action VARCHAR(50),"
                + "sync_direction VARCHAR(20),"
                + "sync_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "status VARCHAR(50)"
                + ")";

        try (Connection conn = DatabaseConnection.getLocalConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Sync log table already exists or error: " + e.getMessage());
        }
    }

    /**
     * Verify data consistency between databases
     */
    private static void verifyDataConsistency() {
        try {
            updateStatus("Verifying data integrity...");

            try (Connection onlineConn = DatabaseConnection.getOnlineConnection(); Connection localConn = DatabaseConnection.getLocalConnection()) {

                String[] tables = {
                    "user", "employee", "accesories", "attendence", "bank_details", "designation",
                    "monthly_payment", "per_day_salary", "resignation", "salary", "salary_details",
                    "section", "stock", "type"
                };

                for (String table : tables) {
                    long onlineCount = getRecordCount(table, onlineConn);
                    long localCount = getRecordCount(table, localConn);

                    if (onlineCount == localCount) {
                        updateStatus("✓ Table '" + table + "' - Consistent (" + localCount + " records)");
                    } else {
                        updateStatus("⚠ Table '" + table + "' - Count mismatch (Online: " + onlineCount + ", Local: " + localCount + ")");
                    }
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
}
