package espresso.youtube.DataBase.Utilities;

import java.sql.*;
import java.util.UUID;

public class Post_DB {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void add_post(UUID owner_id, String title, UUID channel_id, String description) {
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO posts (id, name, owner_id, channel_id, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setObject(3, owner_id);
            preparedStatement.setObject(4, channel_id);
            preparedStatement.setString(5, description);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while adding a post",e);
        }
    }

    public static void delete_post(UUID post_id) {
        String query1 = "DELETE FROM posts WHERE id = ?";
        String query2 = "DELETE FROM playlist_posts WHERE post_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement1 = connection.prepareStatement(query1); PreparedStatement preparedStatement2 = connection.prepareStatement(query2)){
            connection.setAutoCommit(false);
            preparedStatement1.setObject(1, post_id);
            preparedStatement1.executeUpdate();
            preparedStatement2.setObject(1, post_id);
            preparedStatement2.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while deleting a post",e);
        }
    }

    public static void delete_post_from_playlist(UUID post_id) {
        String query = "DELETE FROM playlist_posts WHERE post_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while deleting a post from a playlist",e);
        }
    }

    public static void add_post_to_playlist(UUID post_id, UUID playlist_id) {
        String query = "INSERT INTO playlist_posts (playlist_id, post_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlist_id);
            preparedStatement.setObject(2, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while adding a post to a playlist",e);
        }
    }

    public static void like_post(UUID post_id, UUID user_id) {
        String query = "INSERT INTO post_likes (post_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while liking a post",e);
        }
    }

    public static void dislike_post(UUID post_id, UUID user_id) {
        String query = "INSERT INTO post_dislikes (post_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while disliking a post",e);
        }
    }

    public static boolean check_user_likes_post(UUID post_id, UUID user_id) {
        String query = "SELECT EXISTS (SELECT 1 FROM post_likes WHERE user_id = ? AND post_id = ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking if user liked a post",e);
        }
    }

    public static boolean check_user_dislikes_post(UUID post_id, UUID user_id) {
        String query = "SELECT EXISTS (SELECT 1 FROM post_likes WHERE user_id = ? AND post_id = ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking if user disliked a post",e);
        }
    }

    public static void remove_user_like_from_post(UUID post_id, UUID user_id) {
        String query = "DELETE FROM post_likes WHERE user_id = ? AND post_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while removing user like from a post",e);
        }
    }

    public static void remove_user_dislike_from_post(UUID post_id, UUID user_id) {
        String query = "DELETE FROM post_dislikes WHERE user_id = ? AND post_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while removing user dislike from a post",e);
        }
    }

    public static void add_to_post_viewers(UUID post_id, UUID user_id) {
        String query = "INSERT INTO views (post_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while adding post viewers",e);
        }
    }






    //newwwwwwwwwww
    public static boolean check_if_user_viewed_post(UUID post_id, UUID user_id) {
        String query = "SELECT EXISTS (SELECT 1 FROM views WHERE post_id = ? AND user_id = ? )";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, post_id);
            preparedStatement.setObject(2, user_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking if user viewed the post", e);
        }
        return false;
    }

    public static void restart_post_views(UUID post_id) {
        String query = "DELETE FROM views WHERE post_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while restarting post views",e);
        }
    }

    public static void change_post_title(UUID post_id, String title) {
        //check if user is owner of post??
        String query = "UPDATE posts SET title = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, title);
            preparedStatement.setObject(2, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing post title",e);
        }
    }
    ///+++
    public static void change_post_description(UUID post_id, String description) {
        //check if user is owner of post??
        String query = "UPDATE posts SET description = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, description);
            preparedStatement.setObject(2, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing post description",e);
        }
    }

    public static void main(String[] args) {

    }
}





