package espresso.youtube.DataBase.Utilities;

import java.sql.*;
import java.util.UUID;

public class Channel_DB {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void create_channel(UUID owner_id, String title, String description) {
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO channels (id, title, owner_id, description) VALUES (?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setObject(3, owner_id);
            preparedStatement.setString(4, description);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while creating channel",e);
        }
    }

    public static void subscribe_to_channel(UUID channel_id, UUID subscriber_id) {
        String query = "INSERT INTO channel_subscription (channel_id, subscriber_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, channel_id);
            preparedStatement.setObject(2, subscriber_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while subscribing to channel",e);
        }
    }





    //newww
    public static void create_user_default_channel(UUID owner_id, String username) {
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO channels (id, title, owner_id) VALUES (?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, username);
            preparedStatement.setObject(3, owner_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while creating user default channel",e);
        }
    }

    public static boolean check_if_user_subscribed(UUID channel_id, UUID user_id) {
        String query = "SELECT EXISTS (SELECT 1 FROM channel_subscription WHERE channel_id = ? AND subscriber_id = ? )";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, channel_id);
            preparedStatement.setObject(2, user_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking if user subscribed to channel", e);
        }
        return false;
    }

    public static void unsubscribe_to_channel(UUID channel_id, UUID subscriber_id) {
        String query = "DELETE FROM channel_subscription WHERE channel_id = ? AND subscriber_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, channel_id);
            preparedStatement.setObject(2, subscriber_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while unsubscribing from channel",e);
        }
    }

    public static void change_channel_title(UUID channel_id, String title) {
        //check if user is owner of channel??
        String query = "UPDATE channels SET title = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, title);
            preparedStatement.setObject(2, channel_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing channel title",e);
        }
    }

    public static void change_channel_description(UUID channel_id, String description) {
        //check if user is owner of channel??
        String query = "UPDATE channels SET description = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, description);
            preparedStatement.setObject(2, channel_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing channel description",e);
        }
    }
    /////+++
    public static void delete_channel(UUID post_id) {
        //check if
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

        //delete all its posts and their comments. also posts shoudl be delete from playlists
    }

    public static void main(String[] args) {

    }


    //??
//in deleeting post, comments shoudld be deleted

}



