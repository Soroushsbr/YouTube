package espresso.youtube.DataBase.Utilities;

import java.sql.*;
import java.util.UUID;

public class Playlist_DB {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void create_playlist(UUID owner_id, String title, boolean is_public, String description) {
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO playlists (id, title, owner_id, is_public, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setObject(3, owner_id);
            preparedStatement.setBoolean(4, is_public);
            preparedStatement.setString(5, description);
            preparedStatement.executeUpdate();
           connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while creating playlist",e);
        }
    }

    public static void make_playlist_public(UUID playlist_id) {
        String query = "UPDATE playlists SET is_public = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while making playlist public",e);
        }
    }

    public static void make_playlist_private(UUID playlist_id) {
        String query = "UPDATE playlists SET is_public = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, false);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while making playlist private",e);
        }
    }




    //newwwww
    public static void delete_playlist(UUID playlist_id) {
        //check if user is owner of channel?
        String query = "DELETE FROM playlists WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while deleting playlist",e);
        }
    }

    public static void change_playlist_title(UUID playlist_id, String title) {
        //check if user is owner of playlist??
        String query = "UPDATE playlists SET title = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, title);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing playlist title",e);
        }
    }

    public static void change_playlist_description(UUID playlist_id, String title) {
        //check if user is owner of playlist??
        String query = "UPDATE playlists SET description = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, title);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing playlist description",e);
        }
    }

    public static void subscribe_to_playlist(UUID playlist_id, UUID subscriber_id) {
        //check if playlist is public?
        String query = "INSERT INTO playlist_subscription (playlist_id, subscriber_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlist_id);
            preparedStatement.setObject(2, subscriber_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while subscribing to playlist",e);
        }
    }

    public static void unsubscribe_to_playlist(UUID playlist_id, UUID subscriber_id) {
        String query = "DELETE FROM playlist_subscription WHERE playlist_id = ? AND subscriber_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlist_id);
            preparedStatement.setObject(2, subscriber_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while unsubscribing from playlist",e);
        }
    }

    public static boolean check_if_user_subscribed(UUID playlist_id, UUID user_id) {
        String query = "SELECT EXISTS (SELECT 1 FROM playlist_subscription WHERE playlist_id = ? AND subscriber_id = ? )";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, playlist_id);
            preparedStatement.setObject(2, user_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking if user subscribed to playlist", e);
        }
        return false;
    }
    ///+++
    public static void create_watch_later(UUID owner_id) {
        //!!!user shouldnt be able to change watch later title or make it public or add description
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO playlists (id, title, owner_id, is_public) VALUES (?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, "Watch Later");
            preparedStatement.setObject(3, owner_id);
            preparedStatement.setBoolean(4, false);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while creating watch later playlist",e);
        }
    }

    public static void main(String[] args) {

    }
}
