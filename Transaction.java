
package com.mishra;

import java.sql.*;
import java.util.Random;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.Vector;

public class Transaction {
    // Instance Variables
    private String TransactionNumber, TransactionType, TransactionTime, TransactionDate, FromAccount, ToAccount, CustomerID;
    private float Amount = -1;
    private String Startdate, Enddate;
    private Vector<Vector<String>> TransStore = new Vector<>();

    public Transaction(String AccountType, String Cust_ID, String Amt) {
        ToAccount = AccountType;
        CustomerID = Cust_ID;
        Amount = Float.parseFloat(Amt);
    }

    public Transaction(String ToAcc, String FromAcc, String Cust_ID, String Amt, String Trans_type) {
        ToAccount = ToAcc;
        FromAccount = FromAcc;
        CustomerID = Cust_ID;
        TransactionType = Trans_type;
        Amount = Float.parseFloat(Amt);
    }

    public Transaction(String SD, String ED) {
        Startdate = SD;
        Enddate = ED;
    }

    public String recordTransaction() {
        boolean unique = false;
        int attempts = 0;
        final int MAX_ATTEMPTS = 10;
        try (DBConnection ToDB = new DBConnection();
             Connection DBConn = ToDB.openConn();
             Statement Stmt = DBConn.createStatement()) {

            while (!unique && attempts < MAX_ATTEMPTS) {
                Random rand = new Random();
                int n = rand.nextInt(9000) + 1000; // 1000-9999
                TransactionNumber = Integer.toString(n);
                String SQL_Command = "SELECT TransactionNumber FROM Transactions WHERE TransactionNumber ='" + TransactionNumber + "'";
                try (ResultSet Rslt = Stmt.executeQuery(SQL_Command)) {
                    unique = !Rslt.next();
                }
                attempts++;
            }

            if (unique) {
                LocalTime nowTime = LocalTime.now();
                LocalDate nowDate = LocalDate.now();
                DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                TransactionTime = nowTime.format(formatterTime);
                TransactionDate = nowDate.format(formatterDate);

                String SQL_Command = "INSERT INTO Transactions(TransactionNumber, TransactionType, TransactionAmount, TransactionTime, TransactionDate, FromAccount, ToAccount, CustomerID)"
                        + " VALUES ('" + TransactionNumber + "','" + TransactionType + "','" + Amount + "','" + TransactionTime + "', '" + TransactionDate + "', '" + FromAccount + "', '" + ToAccount + "', '" + CustomerID + "')" +
                        "ON DUPLICATE KEY UPDATE TransactionType = VALUES(TransactionType), TransactionAmount = VALUES(TransactionAmount), TransactionTime = VALUES(TransactionTime), TransactionDate = VALUES(TransactionDate), FromAccount = VALUES(FromAccount), ToAccount = VALUES(ToAccount), CustomerID = VALUES(CustomerID)";
                Stmt.executeUpdate(SQL_Command);
            } else {
                System.out.println("Failed to generate a unique TransactionNumber after " + MAX_ATTEMPTS + " attempts.");
                TransactionNumber = null;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e);
            while (e != null) {
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("Message: " + e.getMessage());
                System.out.println("Vendor: " + e.getErrorCode());
                e = e.getNextException();
                System.out.println("");
            }
            TransactionNumber = null;
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
            TransactionNumber = null;
        }
        return TransactionNumber;
    }

    public Vector<Vector<String>> searchTransaction(String UName) {
        TransStore.clear();
        if (Startdate == null || Enddate == null || Startdate.isEmpty() || Enddate.isEmpty()) {
            System.out.println("Startdate or Enddate is empty.");
            return TransStore;
        }
        try (DBConnection ToDB = new DBConnection();
             Connection DBConn = ToDB.openConn();
             Statement Stmt = DBConn.createStatement()) {

            String SQL_Command = "SELECT * FROM Transactions WHERE TransactionDate BETWEEN '" + Startdate + "' AND '" + Enddate + "'";
            try (ResultSet Rslt = Stmt.executeQuery(SQL_Command)) {
                while (Rslt.next()) {
                    Vector<String> Column_Names = new Vector<>();
                    Column_Names.add(Rslt.getString("TransactionNumber"));
                    Column_Names.add(Rslt.getString("TransactionAmount"));
                    Column_Names.add(Rslt.getString("TransactionType"));
                    Column_Names.add(Rslt.getString("TransactionTime"));
                    Column_Names.add(Rslt.getString("TransactionDate"));
                    Column_Names.add(Rslt.getString("FromAccount"));
                    Column_Names.add(Rslt.getString("ToAccount"));
                    TransStore.add(Column_Names);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e);
            while (e != null) {
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("Message: " + e.getMessage());
                System.out.println("Vendor: " + e.getErrorCode());
                e = e.getNextException();
                System.out.println("");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
        return TransStore;
    }
}