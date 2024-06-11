package controller;

import entity.media.Media;

import java.sql.SQLException;

public class MediaController extends BaseController {

    public void insertMedia(String title, String category, int price, int value, int quantity, String type, String imageURL, int rushOrder) throws SQLException {
        new Media().insertMedia(title, category, price, value, quantity, type, imageURL, rushOrder);
    }

    public void deleteById(int id) throws SQLException {
        new Media().removeMedia(id);
    }
}
