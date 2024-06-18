package entity.order;

import entity.db.AIMSDB;
import entity.media.Media;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OrderMedia {

    private int id;
    private Media media;
    private int orderId;
    private int quantity;
    private static Logger LOGGER = Utils.getLogger(Media.class.getName());

    protected Statement stm;

    public OrderMedia() {
    }

    public OrderMedia(int id, Media media, int orderId, int quantity) {
        this.id = id;
        this.media = media;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public OrderMedia(Media media, int orderId, int quantity) {
        this.media = media;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    public OrderMedia(int id, Media media, int quantity) {
        this.id = id;
        this.media = media;
        this.quantity = quantity;
    }

    public OrderMedia(Statement stm) {
        this.stm = stm;
    }

    public OrderMedia(Media media, int quantity) {
        this.media = media;
        this.quantity = quantity;
    }
    public void insertOrderMeia(Media media, int orderId, int quantity) throws SQLException {
        Connection connection = AIMSDB.getConnection();
        String sql = "INSERT INTO `OrderMedia` (mediaId, orderId, quantity) VALUES (?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);

        pstmt.setInt(1, media.getId());
        pstmt.setInt(2, orderId);
        pstmt.setInt(3, quantity);

        pstmt.executeUpdate();
    }

    public void insertOrderMedia(List<OrderMedia> orderMediaList, int orderId) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;

        connection = AIMSDB.getConnection();
        String sql = "INSERT INTO `OrderMedia` (mediaId, orderId, quantity) VALUES (?,?,?)";
        pstmt = connection.prepareStatement(sql);

        for (int i = 0; i < orderMediaList.size(); i++) {
            OrderMedia orderMedia = orderMediaList.get(i);
            pstmt.setInt(1, orderMedia.getMedia().getId());
            pstmt.setInt(2, orderId);
            pstmt.setInt(3, orderMedia.getQuantity());
            pstmt.addBatch();
        }

        pstmt.executeBatch();
    }

    public List<OrderMedia> getAllOrderMediaByOrderId(int orderId) throws SQLException {
        List<OrderMedia> orderMediaList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        connection = AIMSDB.getConnection();
        String sql = "SELECT om.id, om.quantity, m.id AS mediaId, m.title, m.quantity AS mediaQuantity, m.category, m.imageUrl, m.value, m.price, m.type, m.imageUrl AS mediaImageUrl, m.rushOrder " +
                "FROM `OrderMedia` om " +
                "INNER JOIN `Media` m ON om.mediaId = m.id " +
                "WHERE om.orderId = ?";
        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, orderId);
        rs = pstmt.executeQuery();

        while (rs.next()) {
            int orderMediaId = rs.getInt("id");
            int quantity = rs.getInt("quantity");

            int mediaId = rs.getInt("mediaId");
            String title = rs.getString("title");
            int mediaQuantity = rs.getInt("quantity");
            String category = rs.getString("category");
            String imageUrl = rs.getString("imageUrl");
            int value = rs.getInt("value");
            int price = rs.getInt("price");
            String type = rs.getString("type");
            int rushOrder = rs.getInt("rushOrder");

            Media media = new Media(mediaId, title, category, mediaQuantity, value, price, type, imageUrl, rushOrder);
            OrderMedia orderMedia = new OrderMedia(orderMediaId,  media, orderId, quantity);
            orderMediaList.add(orderMedia);
        }

        return orderMediaList;
    }

    
    public Media getMedia() {
        return this.media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
