package espresso.youtube.DataBase.Utilities;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import espresso.youtube.models.ServerResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Post_DB {
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

    public static void add_post(UUID owner_id, String title, UUID channel_id, String description, Boolean is_public, Boolean is_short) {
        System.out.println("[DATABASE] User "+owner_id+" adding post to channel "+channel_id+" ...");
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO posts (id, name, owner_id, channel_id, description, is_public, is_short) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setObject(3, owner_id);
            preparedStatement.setObject(4, channel_id);
            preparedStatement.setString(5, description);
            preparedStatement.setBoolean(6, is_public);
            preparedStatement.setBoolean(7, is_short);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void delete_post_from_playlist(UUID post_id, UUID playlist_id) {
        System.out.println("[DATABASE] Delete post "+post_id+" from playlist "+playlist_id+"...");
        String query = "DELETE FROM playlist_posts WHERE post_id = ? AND playlist_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void add_post_to_playlist(UUID post_id, UUID playlist_id) {
        System.out.println("[DATABASE] Adding post "+post_id+" to playlist "+playlist_id+"...");
        String query = "INSERT INTO playlist_posts (playlist_id, post_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlist_id);
            preparedStatement.setObject(2, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void like_post(UUID post_id, UUID user_id) {
        System.out.println("[DATABASE] User "+user_id+" liking post "+post_id+" ...");
        String query = "INSERT INTO post_likes (post_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void dislike_post(UUID post_id, UUID user_id) {
        System.out.println("[DATABASE] User "+user_id+" disliking post "+post_id+" ...");
        String query = "INSERT INTO post_dislikes (post_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static boolean check_user_likes_post(UUID post_id, UUID user_id) {
        System.out.println("[DATABASE] Checking if user "+user_id+" has liked post "+post_id+" ...");
        String query = "SELECT EXISTS (SELECT 1 FROM post_likes WHERE user_id = ? AND post_id = ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("[DATABASE] Done");
                    return resultSet.getBoolean(1);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static boolean check_user_dislikes_post(UUID post_id, UUID user_id) {
        System.out.println("[DATABASE] Checking if user "+user_id+" has disliked post "+post_id+" ...");
        String query = "SELECT EXISTS (SELECT 1 FROM post_likes WHERE user_id = ? AND post_id = ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("[DATABASE] Done");
                    return resultSet.getBoolean(1);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static void remove_user_like_from_post(UUID post_id, UUID user_id) {
        System.out.println("[DATABASE] Removing like of user "+user_id+" from post "+post_id+" ...");
        String query = "DELETE FROM post_likes WHERE user_id = ? AND post_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void remove_user_dislike_from_post(UUID post_id, UUID user_id) {
        System.out.println("[DATABASE] Removing dislike of user "+user_id+" from post "+post_id+" ...");
        String query = "DELETE FROM post_dislikes WHERE user_id = ? AND post_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void add_to_post_viewers(UUID post_id, UUID user_id) {
        System.out.println("[DATABASE] Adding a viewer to post "+post_id+" ...");
        String query = "INSERT INTO views (post_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static boolean check_if_user_viewed_post(UUID post_id, UUID user_id) {
        System.out.println("[DATABASE] Checking if user "+user_id+" has viewed the post "+post_id+" ...");
        String query = "SELECT EXISTS (SELECT 1 FROM views WHERE post_id = ? AND user_id = ? )";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, post_id);
            preparedStatement.setObject(2, user_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("[DATABASE] Done");
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }

    public static void restart_post_views(UUID post_id) {
        System.out.println("[DATABASE] Restarting views of post "+post_id+" ...");
        String query = "DELETE FROM views WHERE post_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void change_post_title(UUID post_id, String title) {
        //check if user is owner of post??
        System.out.println("[DATABASE] Changing title of post "+post_id+" to "+title+" ...");
        String query = "UPDATE posts SET title = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, title);
            preparedStatement.setObject(2, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void change_post_description(UUID post_id, String description) {
        //check if user is owner of post??
        System.out.println("[DATABASE] Changing description of post "+post_id+" to "+description+" ...");
        String query = "UPDATE posts SET description = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, description);
            preparedStatement.setObject(2, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static int number_of_views(UUID post_id) {
        String query = "SELECT COUNT(*) AS row_count FROM views WHERE post_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, post_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("row_count");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return -1;
    }

    public static int number_of_likes(UUID post_id) {
        String query = "SELECT COUNT(*) AS row_count FROM post_likes WHERE post_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, post_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("row_count");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return -1;
    }

    public static int number_of_dislikes(UUID post_id) {
        String query = "SELECT COUNT(*) AS row_count FROM post_dislikes WHERE post_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, post_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("row_count");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return -1;
    }

    public static int number_of_comments(UUID post_id) {
        String query = "SELECT COUNT(*) AS row_count FROM comments WHERE post_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, post_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("row_count");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return -1;
    }

    public static ServerResponse get_info(UUID id, int request_id){
        System.out.println("[DATABASE] Getting info of post "+id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query ="SELECT * FROM posts WHERE id = ?";
        try(Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1 , id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    serverResponse.add_part("id" , resultSet.getString("id"));
                    serverResponse.add_part("title" , resultSet.getString("title"));
                    serverResponse.add_part("owner_id" , resultSet.getString("owner_id"));
                    serverResponse.add_part("channel_id" , resultSet.getString("channel_id"));
                    serverResponse.add_part("description" , resultSet.getString("description"));
                    serverResponse.add_part("is_public" , resultSet.getString("is_public"));
                    serverResponse.add_part("is_short" , resultSet.getString("is_short"));
                    serverResponse.add_part("created_at" , resultSet.getString("created_at"));
                }
            }
            System.out.println("[DATABASE] Done");
            return serverResponse;
        }catch (SQLException e){
            printSQLException(e);
        }
        return null;
    }

    public static void delete_post(UUID post_id) {
        System.out.println("[DATABASE] Deleting post "+ post_id +" ...");
        ArrayList<String> queries = new ArrayList<>();
        queries.add("DELETE FROM post_likes WHERE post_id = ?");
        queries.add("DELETE FROM post_dislikes WHERE post_id = ?");
        queries.add("DELETE FROM comment_likes WHERE comment_id IN (SELECT id FROM comments WHERE post_id = ?)");
        queries.add("DELETE FROM comment_dislikes WHERE comment_id IN (SELECT id FROM comments WHERE post_id = ?)");
        queries.add("DELETE FROM comments WHERE post_id = ?");
        queries.add("DELETE FROM playlist_posts WHERE post_id = ?");
        queries.add("DELETE FROM posts WHERE id = ?");

        try (Connection connection = create_connection()) {
            connection.setAutoCommit(false);
            for (String query : queries) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setObject(1, post_id);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    connection.rollback();
                    printSQLException(e);
                }
            }
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static ServerResponse get_all_posts(int request_id){
        System.out.println("[DATABASE] Getting IDs of all Posts...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "SELECT id FROM posts";
        try(Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ArrayList<String> IDs = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    IDs.add(((UUID) resultSet.getObject("id")).toString());
                }
            }
            serverResponse.add_part("videos_id", String.join(", ", IDs));
        } catch (SQLException e){
            printSQLException(e);
        }
        System.out.println("[DATABASE] Done");
        return serverResponse;
    }

    public static List<UUID> get_all_Posts_of_a_account(UUID account_id) {
        System.out.println("[DATABASE] Getting Posts of account "+account_id+" ...");
        List<UUID> IDs = new ArrayList<>();
        String sql = "SELECT id FROM posts WHERE owner_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setObject(1, account_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("id");
                IDs.add(id);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        System.out.println("[DATABASE] Done");
        return IDs;
    }

    public static List<UUID> get_all_posts_of_a_channel(UUID channel_id) {
        System.out.println("[DATABASE] Getting Posts of channel "+channel_id+" ...");
        List<UUID> IDs = new ArrayList<>();
        String sql = "SELECT id FROM posts WHERE channel_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setObject(1, channel_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID postId = (UUID) resultSet.getObject("id");
                    IDs.add(postId);
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        System.out.println("[DATABASE] Done");
        return IDs;
    }

    public static List<UUID> get_all_posts_of_a_playlist(UUID playlist_id) {
        System.out.println("[DATABASE] Getting Posts of playlist "+playlist_id+" ...");
        List<UUID> IDs = new ArrayList<>();
        String sql = "SELECT post_id FROM playlist_posts WHERE playlist_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setObject(1, playlist_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID postId = (UUID) resultSet.getObject("post_id");
                    IDs.add(postId);
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        System.out.println("[DATABASE] Done");
        return IDs;
    }

    public static List<UUID> get_all_viewers_of_a_post(UUID post_id) {
        System.out.println("[DATABASE] Getting viewers of post "+post_id+" ...");
        List<UUID> IDs = new ArrayList<>();
        String sql = "SELECT user_id FROM views WHERE post_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setObject(1, post_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID userId = (UUID) resultSet.getObject("user_id");
                    IDs.add(userId);
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        System.out.println("[DATABASE] Done");
        return IDs;
    }
    ///+++

    public static void main(String[] args) {

    }
}





