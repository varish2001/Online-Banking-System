import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private Connection conn;

    // Modify these as per your DB setup
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bankdb"; // Change 'bankdb' to your DB name
    private static final String USER = "root"; // Change to your DB username
    private static final String PASS = "password"; // Change to your DB password

    public Connection openConn() throws SQLException {
        try {
            // Load the JDBC driver (optional for newer versions)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }

        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        return conn;
    }

    public void closeConn() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection.");
            e.printStackTrace();
        }
    }
}
