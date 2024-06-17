package controller;

import entity.media.Media;

import java.sql.SQLException;

public class MediaController extends BaseController {

    /*
    TODO Example:
          insertMedia("cd1", "science", 100, 20, 10, "cd", "assets/images/cd/cd11.jpg", 1);
          rushOder = 1 mean support, 0 is non;
     */
    public void insertMedia(String title, String category, int price, int value, int quantity, String type, String imageURL, int rushOrder) throws SQLException {
        new Media().insertMedia(title, category, price, value, quantity, type, imageURL, rushOrder);
    }

    /*
    TODO Example:
          deleteById(40);
     */
    public void deleteById(int id) throws SQLException {
        new Media().removeMedia(id);
    }

    /*
    TODO Example:
          getMediaById(40);
     */
    public Media getMediaById(int id) throws SQLException {
        return new Media().getMediaById(id);
    }

    /*
    TODO Example:
          updateMedia(40, "title", "Phạm Nhật Minh");
     */
    public void updateFieldMediaById(int id, String field, Object value) throws SQLException {
        new Media().updateMediaFieldById(id, field, value);
    }

    public void updateMediaById(int id, Media media) throws SQLException {
        new Media().updateMediaById(id, media);
    }
}
