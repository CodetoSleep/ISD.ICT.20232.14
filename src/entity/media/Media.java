package entity.media;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import entity.db.AIMSDB;
import utils.Utils;

/**
 * The general media class, for another media it can be done by inheriting this class
 * @author nguyenlm
 */
public class Media {

    private static Logger LOGGER = Utils.getLogger(Media.class.getName());

    protected Statement stm;
    protected int id;//
    protected String title;
    protected String category;
    protected int value; // the real price of product (eg: 450)
    protected int price;// // the price which will be displayed on browser (eg: 500)
    protected int quantity;//
    protected String type;//
    protected String imageURL;
    protected int rushOrder;

    public Statement getStm() {
        return stm;
    }

    public void setStm(Statement stm) {
        this.stm = stm;
    }

    public int getValue() {
        return value;
    }

    public Media setValue(int value) {
        this.value = value;
        return this;
    }

    public Media setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public int getRushOrder() {
        return rushOrder;
    }

    public Media(int id, String title, String category, int value, int price, int quantity, String type, String imageURL, int rushOrder) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.value = value;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.imageURL = imageURL;
        this.rushOrder = rushOrder;
    }

    public Media setRushOrder(int rushOrder) {
        this.rushOrder = rushOrder;
        return this;
    }

    public Media() throws SQLException{
        stm = AIMSDB.getConnection().createStatement();
    }

    public Media (int id, String title, String category, int price, int quantity, String type) throws SQLException{
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.type = type;

        //stm = AIMSDB.getConnection().createStatement();
    }

    public int getQuantity() throws SQLException{
        int updated_quantity = getMediaById(id).quantity;
        this.quantity = updated_quantity;
        return updated_quantity;
    }

    public Media getMediaById(int id) throws SQLException {
        String sql = "SELECT * FROM Media WHERE id = ?;";
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;

        connection = AIMSDB.getConnection();
        pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        res = pstmt.executeQuery();

            if (res.next()) {
                return new Media()
                    .setId(res.getInt("id"))
                    .setTitle(res.getString("title"))
                    .setQuantity(res.getInt("quantity"))
                    .setCategory(res.getString("category"))
                    .setImageUrl(res.getString("imageUrl"))
                    .setValue(res.getInt("value"))
                    .setPrice(res.getInt("price"))
                    .setType(res.getString("type"))
                    .setImageURL(res.getString("imageUrl")) // This line is redundant with .setImageUrl above
                    .setRushOrder(res.getInt("rushOrder"));
            }
        return null;
    }

    public List getAllMedia() throws SQLException{
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery("select * from Media");
        ArrayList medium = new ArrayList<>();
        while (res.next()) {
            Media media = new Media()
                .setId(res.getInt("id"))
                .setTitle(res.getString("title"))
                .setQuantity(res.getInt("quantity"))
                .setCategory(res.getString("category"))
                .setImageUrl(res.getString("imageUrl"))
                .setValue(res.getInt("value"))
                .setPrice(res.getInt("price"))
                .setType(res.getString("type"));
            medium.add(media);
        }
        return medium;
    }

    public void updateMediaFieldById(int id, String field, Object value) throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        String tableName = "Media";
        if (value instanceof String){
            value = "\"" + value + "\"";
        }
        stm.executeUpdate(" update " + tableName + " set" + " "
                          + field + "=" + value + " " 
                          + "where id=" + id + ";");
    }

    public void insertMedia(String title, String category, int price, int value, int quantity, String type, String imageUrl, int rushOrder) throws SQLException {
        Connection connection = AIMSDB.getConnection();
        String sql = "INSERT INTO Media (title, category, price, value, quantity, type, imageUrl, rushOrder) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);

        pstmt.setString(1, title);
        pstmt.setString(2, category);
        pstmt.setInt(3, price);
        pstmt.setInt(4, value);
        pstmt.setInt(5, quantity);
        pstmt.setString(6, type);
        pstmt.setString(7, imageUrl);
        pstmt.setInt(8, rushOrder);

        pstmt.executeUpdate();
    }

    public void removeMedia(int id) throws SQLException {
        String sql = "DELETE FROM Media WHERE id = ?";
        PreparedStatement pstmt = AIMSDB.getConnection().prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    public void updateMediaById(int id, Media updatedMedia) throws SQLException {
        String sql = "UPDATE Media SET title = ?, category = ?, price = ?, value = ?, quantity = ?, type = ?, imageUrl = ?, rushOrder = ? WHERE id = ?";
        Connection connection = AIMSDB.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);

        pstmt.setString(1, updatedMedia.getTitle());
        pstmt.setString(2, updatedMedia.getCategory());
        pstmt.setInt(3, updatedMedia.getPrice());
        pstmt.setInt(4, updatedMedia.getValue());
        pstmt.setInt(5, updatedMedia.getQuantity());
        pstmt.setString(6, updatedMedia.getType());
        pstmt.setString(7, updatedMedia.getImageURL());
        pstmt.setInt(8, updatedMedia.getRushOrder());
        pstmt.setInt(9, id);

        pstmt.executeUpdate();
    }


    // getter and setter 
    public int getId() {
        return this.id;
    }

    private Media setId(int id){
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Media setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCategory() {
        return this.category;
    }

    public Media setCategory(String category) {
        this.category = category;
        return this;
    }

    public int getPrice() {
        return this.price;
    }

    public Media setPrice(int price) {
        this.price = price;
        return this;
    }

    public String getImageURL(){
        return this.imageURL;
    }

    public Media setImageUrl(String url){
        this.imageURL = url;
        return this;
    }

    public Media setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public Media setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + id + "'" +
            ", title='" + title + "'" +
            ", category='" + category + "'" +
            ", price='" + price + "'" +
            ", quantity='" + quantity + "'" +
            ", type='" + type + "'" +
            ", imageURL='" + imageURL + "'" +
            "}";
    }    

}