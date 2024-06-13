package espresso.youtube.DataBase.Utilities;

import java.sql.*;
import java.util.UUID;

public class Post {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void add_post(UUID owner_id, String title, UUID channel_id, boolean is_public) {
        UUID id = UUID.randomUUID();
        try {
            Connection connection = create_connection();
            String query = "INSERT INTO posts (id, name, owner_id, is_public,channel_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setObject(3, owner_id);
            preparedStatement.setBoolean(4, is_public);
            preparedStatement.setObject(5, channel_id);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void delete_post(UUID post_id) {
        //delete from channel and playlist

        try {
            Connection connection = create_connection();
            String query = "DELETE FROM posts WHERE post_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, post_id);
            preparedStatement.executeUpdate();

            query = "DELETE FROM playlist_posts WHERE post_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, post_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }

        //delete file of video???????
    }

    public static void delete_post_from_playlist(UUID post_id) {
        //delete from only playlist
        try {
            Connection connection = create_connection();
            String query = "DELETE FROM playlist_posts WHERE post_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, post_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void add_post_to_playlist(UUID post_id, UUID playlist_id) {
        try {
            Connection connection = create_connection();
            String query = "INSERT INTO playlist_posts (playlist_id, post_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, playlist_id);
            preparedStatement.setObject(2, post_id);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void like_post(UUID post_id, UUID user_id) {
        try {
            Connection connection = create_connection();
            String query = "INSERT INTO likes (post_id, user_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, post_id);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void dislike_post(UUID post_id, UUID user_id) {
        try {
            Connection connection = create_connection();
            String query = "INSERT INTO dislikes (post_id, user_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, post_id);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean check_user_likes_post(UUID post_id, UUID user_id) {
        try {
            Connection connection = create_connection();
            String query = "SELECT EXISTS (SELECT 1 FROM likes WHERE user_id = ? AND post_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            connection.close();
            preparedStatement.close();
            return resultSet.getBoolean(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean check_user_dislikes_post(UUID post_id, UUID user_id) {
        try {
            Connection connection = create_connection();
            String query = "SELECT EXISTS (SELECT 1 FROM dislikes WHERE user_id = ? AND post_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            connection.close();
            preparedStatement.close();
            return resultSet.getBoolean(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
