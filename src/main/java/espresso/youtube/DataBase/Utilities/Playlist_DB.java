package espresso.youtube.DataBase.Utilities;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.playlist.Playlist;

import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

public class Playlist_DB {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public static ServerResponse create_playlist(UUID owner_id, String title, boolean is_public, String description, int request_id) {
        System.out.println("[DATABASE] Creating playlist as "+title+" for user "+owner_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
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
            serverResponse.add_part("isSuccessful", true);
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse make_playlist_public(UUID playlist_id, int request_id) {
        System.out.println("[DATABASE] Making playlist "+playlist_id+" public ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "UPDATE playlists SET is_public = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
            serverResponse.add_part("isSuccessful", true);
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse make_playlist_private(UUID playlist_id, int request_id) {
        System.out.println("[DATABASE] Making playlist "+playlist_id+" private ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "UPDATE playlists SET is_public = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, false);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
            serverResponse.add_part("isSuccessful", true);
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }
        return serverResponse;
    }

    public static void change_playlist_title(UUID playlist_id, String title) {
        //check if user is owner of playlist??
        System.out.println("[DATABASE] Changing title of playlist "+playlist_id+" to "+title+" ...");
        String query = "UPDATE playlists SET title = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, title);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void change_playlist_description(UUID playlist_id, String description) {
        //check if user is owner of playlist??
        System.out.println("[DATABASE] Changing description of playlist "+playlist_id+" to "+description+" ...");
        String query = "UPDATE playlists SET description = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, description);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

//    public static void subscribe_to_playlist(UUID playlist_id, UUID subscriber_id) {
//        //check if playlist is public?
//        System.out.println("[DATABASE] Subscribing user "+subscriber_id+" to playlist "+playlist_id+" ...");
//        String query = "INSERT INTO playlist_subscription (playlist_id, subscriber_id) VALUES (?, ?)";
//        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
//            connection.setAutoCommit(false);
//            preparedStatement.setObject(1, playlist_id);
//            preparedStatement.setObject(2, subscriber_id);
//            preparedStatement.executeUpdate();
//            connection.commit();
//            System.out.println("[DATABASE] Done");
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
//    }

//    public static void unsubscribe_to_playlist(UUID playlist_id, UUID subscriber_id) {
//        System.out.println("[DATABASE] Unsubscribing user "+subscriber_id+" to playlist "+playlist_id+" ...");
//        String query = "DELETE FROM playlist_subscription WHERE playlist_id = ? AND subscriber_id = ?";
//        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
//            connection.setAutoCommit(false);
//            preparedStatement.setObject(1, playlist_id);
//            preparedStatement.setObject(2, subscriber_id);
//            preparedStatement.executeUpdate();
//            connection.commit();
//            System.out.println("[DATABASE] Done");
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
//    }

//    public static boolean check_if_user_subscribed(UUID playlist_id, UUID user_id) {
//        System.out.println("[DATABASE] Checking if user "+user_id+" is subscribed to playlist "+playlist_id+" ...");
//        String query = "SELECT EXISTS (SELECT 1 FROM playlist_subscription WHERE playlist_id = ? AND subscriber_id = ? )";
//        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setObject(1, playlist_id);
//            preparedStatement.setObject(2, user_id);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
//                    System.out.println("[DATABASE] Done");
//                    return resultSet.getBoolean(1);
//                }
//            }
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
//        return false;
//    }

    //    public static int number_of_subscribers(UUID playlist_id) {
//        String query = "SELECT COUNT(*) AS row_count FROM playlist_subscription WHERE playlist_id = ?";
//        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setObject(1,playlist_id);
//            try(ResultSet resultSet = preparedStatement.executeQuery();){
//                if (resultSet.next()) {
//                    return resultSet.getInt("row_count");
//                } else {
//                    return 0;
//                }
//            }
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
//        return -1;
//    }

//    public static List<UUID> get_subscribers(UUID playlist_id) {
//        System.out.println("[DATABASE] Getting subscribers of playlist "+playlist_id+" ...");
//        List<UUID> subscriberIds = new ArrayList<>();
//        String sql = "SELECT subscriber_id FROM playlist_subscription WHERE playlist_id = ?";
//        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
//            preparedStatement.setObject(1, playlist_id);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    UUID subscriberId = (UUID) resultSet.getObject("subscriber_id");
//                    subscriberIds.add(subscriberId);
//                }
//            }
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
//        System.out.println("[DATABASE] Done");
//        return subscriberIds;
//    }

    public static void create_watch_later(UUID owner_id) {
        System.out.println("[DATABASE] Creating watch later playlist for user "+owner_id+" ...");
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
            printSQLException(e);
        }
    }

    public static ServerResponse number_of_posts(UUID playlist_id, int request_id) {
        String query = "SELECT COUNT(*) AS row_count FROM playlist_posts WHERE playlist_id = ?";
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, playlist_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    serverResponse.add_part("number_of_posts", resultSet.getInt("row_count"));
                } else {
                    return serverResponse;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse get_info(UUID id, int request_id){
        System.out.println("[DATABASE] Getting info of playlist "+id+" ...");
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
            System.out.println("[DATABASE] Done");
        }catch (SQLException e){
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse delete_playlist(UUID playlist_id, int request_id) {
        System.out.println("[DATABASE] Deleting playlist "+playlist_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<String> queries = new ArrayList<>();
        queries.add("DELETE FROM playlist_posts WHERE playlist_id = ?");
        queries.add("DELETE FROM playlist_subscription WHERE playlist_id = ?");
        queries.add("DELETE FROM playlists WHERE id = ?");
        try (Connection connection = create_connection()) {
            connection.setAutoCommit(false);
            for (String query : queries) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setObject(1, playlist_id);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    serverResponse.add_part("isSuccessful", false);
                    connection.rollback();
                    printSQLException(e);
                }
            }
            connection.commit();
            serverResponse.add_part("isSuccessful", true);
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse get_playlists_of_account(UUID owner_id, int request_id) {
        ArrayList<Playlist> playlists = new ArrayList<>();
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "SELECT id, title, owner_id, description, is_public, created_at FROM playlists WHERE owner_id = ?";

        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, owner_id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Playlist playlist = new Playlist();
                    playlist.setId(resultSet.getString("id"));
                    playlist.setTitle(resultSet.getString("title"));
                    playlist.setUser_id(resultSet.getString("owner_id"));
                    playlist.setDescription(resultSet.getString("description"));
                    playlist.setIs_public(resultSet.getBoolean("is_public"));
                    playlist.setCreated_at(resultSet.getTimestamp("created_at"));

                    ServerResponse sr = Post_DB.get_all_posts_of_a_playlist(UUID.fromString(playlist.getId()), request_id);
                    playlist.setVideos(sr.getVideos_list());

                    playlists.add(playlist);

                }
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
        serverResponse.setPlaylists_list(playlists);
        return serverResponse;
    }
    ///+++
    public static ServerResponse change_playlist_info(UUID playlist_id, String description, String title, int request_id){
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        change_playlist_title(playlist_id, title);
        change_playlist_description(playlist_id, description);
        serverResponse.add_part("isSuccessful", true);
        return serverResponse;
    }

    public static void main(String[] args) {


    }
}
