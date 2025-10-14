package NerdTech.DR_Fashion.DatabaseConnection;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class DatabaseSync {

    // ✅ Correct order: Parent tables first
    private static final List<String> TABLES = Arrays.asList(
            "role", // No dependencies
            "type", // No dependencies
            "user", // May depend on role
            "employee", // Depends on role
            "stock", // May depend on type
            "accesories", // Depends on type (FK: type_id)
            "attendence", // Depends on employee
            "monthly_payment", // Depends on role
            "per_day_salary" // Minimal dependencies
    );

    public static void syncAll() {
        Connection localConn = null;
        Connection onlineConn = null;

        try {
            localConn = DatabaseConnection.getLocalConnection();
            onlineConn = DatabaseConnection.getOnlineConnection();

            if (localConn == null || onlineConn == null) {
                System.out.println("❌ Database connection failed!");
                return;
            }

            System.out.println("🔄 Two-way sync started...");

            // Disable foreign key checks on both databases
            localConn.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");
            onlineConn.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");

            for (String table : TABLES) {
                syncTable(localConn, onlineConn, table);
            }

            // Re-enable foreign key checks
            localConn.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");
            onlineConn.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");

            System.out.println("✅ Two-way sync completed successfully!");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("⚠️ Sync failed: " + ex.getMessage());
        } finally {
            try {
                if (localConn != null) {
                    localConn.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");
                    localConn.close();
                }
                if (onlineConn != null) {
                    onlineConn.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");
                    onlineConn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void syncTable(Connection localConn, Connection onlineConn, String tableName) throws SQLException {
        System.out.println("➡️ Syncing table: " + tableName);

        // Local → Online
        syncDirection(localConn, onlineConn, tableName);

        // Online → Local
        syncDirection(onlineConn, localConn, tableName);
    }

    private static void syncDirection(Connection source, Connection target, String table) throws SQLException {
        Statement stmt = source.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);

        ResultSetMetaData meta = rs.getMetaData();
        int colCount = meta.getColumnCount();
        String pk = meta.getColumnName(1); // first column assumed primary key

        int syncedCount = 0;

        while (rs.next()) {
            Object pkValue = rs.getObject(1);

            // Check if exists in target
            PreparedStatement check = target.prepareStatement(
                    "SELECT COUNT(*) FROM " + table + " WHERE " + pk + "=?"
            );
            check.setObject(1, pkValue);
            ResultSet rsCheck = check.executeQuery();
            rsCheck.next();
            int count = rsCheck.getInt(1);
            rsCheck.close();
            check.close();

            if (count == 0) {
                insertRecord(target, table, rs, colCount);
                syncedCount++;
            } else {
                updateRecord(target, table, rs, colCount, pk, pkValue);
            }
        }

        System.out.println("   ✓ " + table + " synced (" + syncedCount + " new records)");

        rs.close();
        stmt.close();
    }

    private static void insertRecord(Connection conn, String table, ResultSet rs, int colCount) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(table).append(" VALUES(");
        for (int i = 1; i <= colCount; i++) {
            sql.append("?");
            if (i < colCount) {
                sql.append(",");
            }
        }
        sql.append(")");

        PreparedStatement ps = conn.prepareStatement(sql.toString());
        for (int i = 1; i <= colCount; i++) {
            ps.setObject(i, rs.getObject(i));
        }

        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            // Ignore duplicate key errors
            if (!e.getMessage().contains("Duplicate")
                    && !e.getMessage().contains("duplicate")) {
                throw e;
            }
        }
        ps.close();
    }

    private static void updateRecord(Connection conn, String table, ResultSet rs, int colCount, String pkName, Object pkValue) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE ").append(table).append(" SET ");
        ResultSetMetaData meta = rs.getMetaData();

        for (int i = 1; i <= colCount; i++) {
            String col = meta.getColumnName(i);
            if (!col.equalsIgnoreCase(pkName)) {
                sql.append(col).append("=?,");
            }
        }
        sql.deleteCharAt(sql.length() - 1); // remove last comma
        sql.append(" WHERE ").append(pkName).append("=?");

        PreparedStatement ps = conn.prepareStatement(sql.toString());
        int index = 1;
        for (int i = 1; i <= colCount; i++) {
            String col = meta.getColumnName(i);
            if (!col.equalsIgnoreCase(pkName)) {
                ps.setObject(index++, rs.getObject(i));
            }
        }
        ps.setObject(index, pkValue);
        ps.executeUpdate();
        ps.close();
    }
}
