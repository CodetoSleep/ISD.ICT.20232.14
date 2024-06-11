package controller;

import entity.media.Media;

import java.sql.SQLException;

public class MediaController extends BaseController {

    public void insertMedia(String title, String category, int price, int value, int quantity, String type, String imageURL) throws SQLException {
        new Media().insertMedia(title, category, price, value, quantity, type, imageURL);
    }
}
