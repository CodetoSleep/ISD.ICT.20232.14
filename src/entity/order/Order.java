package entity.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import entity.db.AIMSDB;
import utils.Utils;

import utils.Configs;

public class Order {
    private int id;
    private String email;
    private String city;
    private String address;
    private String phone;
    private int rushhOrder;
    private int shippingFee;
    private int isPaid;
    private String status;
    private String name;
    private Date time;
    private String shippingInstruction;


    public Order() {
    }

    public Order(int id, String email, String city, String address, String phone, int rushhOrder, int shippingFee, int isPaid, String status, String name, Date time, String shippingInstruction) {
        this.id = id;
        this.email = email;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.rushhOrder = rushhOrder;
        this.shippingFee = shippingFee;
        this.isPaid = isPaid;
        this.status = status;
        this.name = name;
        this.time = time;
        this.shippingInstruction = shippingInstruction;
    }

    public Order(String email, String city, String address, String phone, int rushhOrder, int shippingFee, int isPaid, String status, String name, Date time, String shippingInstruction) {
        this.email = email;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.rushhOrder = rushhOrder;
        this.shippingFee = shippingFee;
        this.isPaid = isPaid;
        this.status = status;
        this.name = name;
        this.time = time;
        this.shippingInstruction = shippingInstruction;
    }

    public int insertOrder(String email, String city, String address, String phone, int rushOrder, int shippingFee, int isPaid, String status, String name, Date time, String shippingInstruction) throws SQLException {
        Connection connection = AIMSDB.getConnection();
        String sql = "INSERT INTO `Order` (email, city, address, phone, rushOrder, shippingFee, isPaid, status, name, time, shippingInstruction) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);

        pstmt.setString(1, email);
        pstmt.setString(2, city);
        pstmt.setString(3, address);
        pstmt.setString(4, phone);
        pstmt.setInt(5, rushOrder);
        pstmt.setInt(6, shippingFee);
        pstmt.setInt(7, isPaid);
        pstmt.setString(8, status);
        pstmt.setString(9, name);
        pstmt.setDate(10, time);
        pstmt.setString(11, shippingInstruction);

        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            throw new SQLException("Failed to retrieve the generated orderId.");
        }
    }

    public List<Order> getAllOrder() throws SQLException {
        List<Order> orders = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        connection = AIMSDB.getConnection();
        String sql = "SELECT * FROM `Order`";
        pstmt = connection.prepareStatement(sql);
        rs = pstmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String email = rs.getString("email");
            String city = rs.getString("city");
            String address = rs.getString("address");
            String phone = rs.getString("phone");
            int rushhOrder = rs.getInt("rushOrder");
            int shippingFee = rs.getInt("shippingFee");
            int isPaid = rs.getInt("isPaid");
            String status = rs.getString("status");
            String name = rs.getString("name");
            Date time = rs.getDate("time");
            String shippingInstruction = rs.getString("shippingInstruction");

            Order order = new Order(id, email, city, address, phone, rushhOrder, shippingFee, isPaid, status, name, time, shippingInstruction);
            orders.add(order);
        }

        return orders;
    }
    
    public void updateIsPaidByOrderId(int orderId) throws SQLException {
        Connection connection = AIMSDB.getConnection();
        String sql = "UPDATE `Order` SET isPaid = 1 WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);

        pstmt.setInt(1, orderId);

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException("Updating order failed, no rows affected.");
        }
    }

    public String getName() {
        return name;
    }

    public Order setName(String name) {
        this.name = name;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public Order setTime(Date time) {
        this.time = time;
        return this;
    }

    public String getShippingInstruction() {
        return shippingInstruction;
    }

    public Order setShippingInstruction(String shippingInstruction) {
        this.shippingInstruction = shippingInstruction;
        return this;
    }

    public Order setShippingFee(int shippingFee) {
        this.shippingFee = shippingFee;
        return this;
    }

    public Order setId(int id) {
        this.id = id;
        return this;
    }

    public Order setEmail(String email) {
        this.email = email;
        return this;
    }

    public Order setCity(String city) {
        this.city = city;
        return this;
    }

    public Order setAddress(String address) {
        this.address = address;
        return this;
    }

    public Order setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Order setRushhOrder(int rushhOrder) {
        this.rushhOrder = rushhOrder;
        return this;
    }

    public Order setIsPaid(int isPaid) {
        this.isPaid = isPaid;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public int getRushhOrder() {
        return rushhOrder;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public int getIsPaid() {
        return isPaid;
    }


    public String getStatus() {
        return status;
    }

    public Order setStatus(String status) {
        this.status = status;
        return this;
    }
}
