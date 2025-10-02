package NerdTech.DR_Fashion.DatabaseConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static Connection con;
    
    private static void connection() throws ClassNotFoundException {
        String local_url = "jdbc:mysql://localhost:3306/dr_fashions";
        // Use environment variables for online DB
        String online_url = System.getenv("DB_ONLINE_URL");
        String online_user = System.getenv("DB_ONLINE_USER");
        String online_pass = System.getenv("DB_ONLINE_PASSWORD");
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        try {
            // Try ONLINE DB if credentials are set
            if (online_url != null && online_user != null && online_pass != null) {
                con = DriverManager.getConnection(online_url, online_user, online_pass);
                System.out.println("✅ Connected to Online DB");
            } else {
                throw new SQLException("Online DB credentials not configured");
            }
        } catch (SQLException ex) {
            // Fallback to LOCAL DB
            System.err.println("❌ Online DB failed: " + ex.getMessage());
            try {
                con = DriverManager.getConnection(local_url, "root", "88222006");
                System.out.println("✅ Connected to Local DB");
            } catch (SQLException ex1) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
    public static Connection getConnection() throws Exception {
        if (con == null || con.isClosed()) {  
            connection();
        }
        return con;
    }
}