package entity.payment;

import entity.db.AIMSDB;
import entity.invoice.Invoice;

import java.sql.*;
import java.util.Date;

public class PaymentTransaction {
    private String errorCode;
    private String transactionId;
    private String transactionContent;
    private int amount;
    private Integer orderID;
    private Timestamp createdAt;

    public PaymentTransaction(String errorCode, String transactionId, String transactionContent,
                              int amount, Timestamp createdAt) {
        super();
        this.errorCode = errorCode;


        this.transactionId = transactionId;
        this.transactionContent = transactionContent;
        this.amount = amount;
        this.createdAt = createdAt;
    }


    /**
     * @return String
     */
    public String getErrorCode() {
        return errorCode;
    }

    public String getTransactionContent() {
        return transactionContent;
    }

    public void save(int orderId) throws SQLException {
        this.orderID = orderId;
        Statement stm = AIMSDB.getConnection().createStatement();
        String query = "INSERT INTO \"Transaction\" ( orderID, createAt, content) " +
                "VALUES ( ?, ?, ?)";
        try (PreparedStatement preparedStatement = AIMSDB.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, orderID);
            preparedStatement.setTimestamp(2, createdAt);
            preparedStatement.setString(3, transactionContent);
            preparedStatement.executeUpdate();
        }
    }

}