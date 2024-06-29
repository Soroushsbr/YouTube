package espresso.youtube.DataBase.Utilities;

import espresso.youtube.models.ServerResponse;

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
        System.out.println("Creating playlist as "+title+" for user "+owner_id+" ...");
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
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while creating playlist",e);
        }
    }

    public static void make_playlist_public(UUID playlist_id) {
        System.out.println("Making playlist "+playlist_id+" public ...");
        String query = "UPDATE playlists SET is_public = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while making playlist public",e);
        }
    }

    public static void make_playlist_private(UUID playlist_id) {
        System.out.println("Making playlist "+playlist_id+" private ...");
        String query = "UPDATE playlists SET is_public = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, false);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while making playlist private",e);
        }
    }

    public static void delete_playlist(UUID playlist_id) {
        //check if user is owner of channel?
        System.out.println("Deleting playlist "+playlist_id+" ...");
        String query = "DELETE FROM playlists WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while deleting playlist",e);
        }
    }

    public static void change_playlist_title(UUID playlist_id, String title) {
        //check if user is owner of playlist??
        System.out.println("Changing title of playlist "+playlist_id+" to "+title+" ...");
        String query = "UPDATE playlists SET title = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, title);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing playlist title",e);
        }
    }

    public static void change_playlist_description(UUID playlist_id, String description) {
        //check if user is owner of playlist??
        System.out.println("Changing description of playlist "+playlist_id+" to "+description+" ...");
        String query = "UPDATE playlists SET description = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, description);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing playlist description",e);
        }
    }

    public static void subscribe_to_playlist(UUID playlist_id, UUID subscriber_id) {
        //check if playlist is public?
        System.out.println("Subscribing user "+subscriber_id+" to playlist "+playlist_id+" ...");
        String query = "INSERT INTO playlist_subscription (playlist_id, subscriber_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlist_id);
            preparedStatement.setObject(2, subscriber_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while subscribing to playlist",e);
        }
    }

    public static void unsubscribe_to_playlist(UUID playlist_id, UUID subscriber_id) {
        System.out.println("Unsubscribing user "+subscriber_id+" to playlist "+playlist_id+" ...");
        String query = "DELETE FROM playlist_subscription WHERE playlist_id = ? AND subscriber_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlist_id);
            preparedStatement.setObject(2, subscriber_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while unsubscribing from playlist",e);
        }
    }

    public static boolean check_if_user_subscribed(UUID playlist_id, UUID user_id) {
        System.out.println("Checking if user "+user_id+" is subscribed to playlist "+playlist_id+" ...");
        String query = "SELECT EXISTS (SELECT 1 FROM playlist_subscription WHERE playlist_id = ? AND subscriber_id = ? )";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, playlist_id);
            preparedStatement.setObject(2, user_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Done");
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking if user subscribed to playlist", e);
        }
        return false;
    }

    public static void create_watch_later(UUID owner_id) {
        //!!!user shouldnt be able to change watch later title or make it public or add description
        System.out.println("Creating watch later playlist for user "+owner_id+" ...");
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
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while creating watch later playlist",e);
        }
    }

    public static int number_of_subscribers(UUID playlist_id) {
        String query = "SELECT COUNT(*) AS row_count FROM playlist_subscription WHERE playlist_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1,playlist_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("row_count");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred getting number of playlist subscribers",e);
        }
    }

    public static int number_of_posts(UUID playlist_id) {
        String query = "SELECT COUNT(*) AS row_count FROM playlist_posts WHERE playlist_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, playlist_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("row_count");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred getting number of playlist posts",e);
        }
    }

    public static ServerResponse get_info(UUID id, int request_id){
        System.out.println("Getting info of playlist "+id+" ...");
        System.out.println("Getting info of playlist "+id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query ="SELECT * FROM playlists WHERE id = ?";
        try(Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1 , id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    serverResponse.add_part("id" , resultSet.getString("id"));
                    serverResponse.add_part("title" , resultSet.getString("title"));
                    serverResponse.add_part("owner_id" , resultSet.getString("owner_id"));
                    serverResponse.add_part("description" , resultSet.getString("description"));
                    serverResponse.add_part("is_public" , resultSet.getString("is_public"));
                    serverResponse.add_part("created_at" , resultSet.getString("created_at"));
                }
            }
            System.out.println("Done");
            return serverResponse;
        }catch (SQLException e){
            System.out.println("Database error occurred while getting playlist info");
        }
        return null;
    }
    ///+++

    public static void main(String[] args) {

    }
}
