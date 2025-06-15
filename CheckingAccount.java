import java.sql.*;

public class CheckingAccount {
    private String CheckingAccountNumber, CustomerName, CustomerID;
    private float Balance = -1, Amount = -1;

    // Constructor with 4 parameters
    public CheckingAccount(String CA_Num, String Cust_Name, String Cust_ID, String Amt) {
        CheckingAccountNumber = CA_Num;
        CustomerName = Cust_Name;
        CustomerID = Cust_ID;
        Amount = Float.parseFloat(Amt);
    }

    // Constructor with 1 parameter
    public CheckingAccount(String CA_Num) {
        CheckingAccountNumber = CA_Num;
    }

    // Default constructor
    public CheckingAccount() {
    }

    public boolean openAcct() {
        boolean done = false;
        try {
            DBConnection ToDB = new DBConnection();
            Connection DBConn = ToDB.openConn();
            Statement Stmt = DBConn.createStatement();

            String SQL_Command = "SELECT CheckingAccountNumber FROM CheckingAccount WHERE CheckingAccountNumber ='" + CheckingAccountNumber + "'";
            ResultSet Rslt = Stmt.executeQuery(SQL_Command);
            done = !Rslt.next();

            if (done) {
                SQL_Command = "INSERT INTO CheckingAccount(CheckingAccountNumber, CustomerName, Balance, CustomerID) VALUES ('"
                        + CheckingAccountNumber + "','" + CustomerName + "'," + Amount + ", '" + CustomerID + "')";
                Stmt.executeUpdate(SQL_Command);
            }

            Stmt.close();
            ToDB.closeConn();
        } catch (SQLException e) {
            done = false;
            printSQLException(e);
        } catch (Exception e) {
            done = false;
            e.printStackTrace();
        }
        return done;
    }

    public String getAccno(String C_ID) {
        try {
            DBConnection ToDB = new DBConnection();
            Connection DBConn = ToDB.openConn();
            Statement Stmt = DBConn.createStatement();

            String SQL_Command = "SELECT CheckingAccountNumber FROM CheckingAccount WHERE CustomerID ='" + C_ID + "'";
            ResultSet Rslt = Stmt.executeQuery(SQL_Command);

            if (Rslt.next()) {
                CheckingAccountNumber = Rslt.getString("CheckingAccountNumber");
            }

            Stmt.close();
            ToDB.closeConn();
        } catch (SQLException e) {
            printSQLException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CheckingAccountNumber;
    }

    public float getBalance() {
        return getBalance(CheckingAccountNumber);
    }

    public float getBalance(String ChkAcctNumber) {
        try {
            DBConnection ToDB = new DBConnection();
            Connection DBConn = ToDB.openConn();
            Statement Stmt = DBConn.createStatement();

            String SQL_Command = "SELECT Balance FROM CheckingAccount WHERE CheckingAccountNumber ='" + ChkAcctNumber + "'";
            ResultSet Rslt = Stmt.executeQuery(SQL_Command);

            if (Rslt.next()) {
                Balance = Rslt.getFloat(1);
            }

            Stmt.close();
            ToDB.closeConn();
        } catch (SQLException e) {
            printSQLException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Balance;
    }

    public boolean deposit(String C_ID) {
        boolean done = !CheckingAccountNumber.equals("") && !CustomerID.equals("");
        try {
            if (done) {
                DBConnection DBconn = new DBConnection();
                Connection conn = DBconn.openConn();
                Statement stat = conn.createStatement();

                String SQL_Command = "SELECT Balance FROM CheckingAccount WHERE CheckingAccountNumber = '" + CheckingAccountNumber + "' AND CustomerID ='" + C_ID + "'";
                ResultSet reslt = stat.executeQuery(SQL_Command);

                if (reslt.next()) {
                    Balance = reslt.getFloat(1);
                }

                Balance = Balance + Amount;
                SQL_Command = "UPDATE CheckingAccount SET Balance = '" + Balance + "' WHERE CheckingAccountNumber = '" + CheckingAccountNumber + "'";
                stat.executeUpdate(SQL_Command);

                stat.close();
                DBconn.closeConn();
            }
        } catch (SQLException e) {
            printSQLException(e);
            done = false;
        } catch (Exception e) {
            e.printStackTrace();
            done = false;
        }
        return done;
    }

    public boolean withdraw(String C_ID) {
        boolean done = !CheckingAccountNumber.equals("") && !CustomerID.equals("");
        try {
            if (done) {
                DBConnection DBconn = new DBConnection();
                Connection conn = DBconn.openConn();
                Statement stat = conn.createStatement();

                String SQL_Command = "SELECT Balance FROM CheckingAccount WHERE CheckingAccountNumber = '" + CheckingAccountNumber + "' AND CustomerID= '" + C_ID + "'";
                ResultSet rslt = stat.executeQuery(SQL_Command);

                if (rslt.next()) {
                    Balance = rslt.getFloat(1);
                }

                if (Balance >= Amount) {
                    Balance = Balance - Amount;
                    SQL_Command = "UPDATE CheckingAccount SET Balance = '" + Balance + "' WHERE CheckingAccountNumber = '" + CheckingAccountNumber + "'";
                    stat.executeUpdate(SQL_Command);
                } else {
                    System.out.println("Insufficient funds");
                    done = false;
                }

                stat.close();
                DBconn.closeConn();
            }
        } catch (SQLException e) {
            printSQLException(e);
            done = false;
        } catch (Exception e) {
            e.printStackTrace();
            done = false;
        }
        return done;
    }

    private void printSQLException(SQLException e) {
        while (e != null) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            e = e.getNextException();
            System.out.println();
        }
    }
}
