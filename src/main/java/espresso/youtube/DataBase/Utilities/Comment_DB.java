package espresso.youtube.DataBase.Utilities;

import espresso.youtube.models.ServerResponse;

import java.sql.*;
import java.util.UUID;

public class Comment_DB {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void add_comment(UUID owner_id, UUID post_id, String content) {
        System.out.println("Adding comment to post "+post_id+" ...");
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO comments (id, owner_id, content, post_id, parent_comment_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, owner_id);
            preparedStatement.setString(3, content);
            preparedStatement.setObject(4, post_id);
            preparedStatement.setNull(5, Types.OTHER);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while adding a comment",e);
        }
    }

    public static void reply_to_comment(UUID owner_id, UUID post_id, String content, UUID parent_comment_id) {
        System.out.println("Replying to comment "+parent_comment_id+" ...");
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO comments (id, owner_id, content, post_id, parent_comment_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, owner_id);
            preparedStatement.setString(3, content);
            preparedStatement.setObject(4, post_id);
            preparedStatement.setObject(5, parent_comment_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while replying to a comment",e);
        }
    }

    public static void delete_comment(UUID comment_id) {
        System.out.println("Deleting comment "+comment_id+" ...");
        String query = "DELETE FROM comments WHERE id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, comment_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while deleting a comment",e);
        }
    }

    public static void like_comment(UUID comment_id, UUID user_id) {
        System.out.println("User "+user_id+" is liking comment "+comment_id+" ...");
        String query = "INSERT INTO comment_likes (comment_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, comment_id);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while liking a comment",e);
        }
    }

    public static void dislike_comment(UUID comment_id, UUID user_id) {
        System.out.println("User "+user_id+" is disliking comment "+comment_id+" ...");
        String query = "INSERT INTO comment_dislikes (comment_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, comment_id);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while disliking a comment",e);
        }
    }

    public static boolean check_user_likes_comment(UUID comment_id, UUID user_id) {
        System.out.println("Check if user "+user_id+" has liked comment "+comment_id+" ...");
        String query = "SELECT EXISTS (SELECT 1 FROM comment_likes WHERE user_id = ? AND comment_id = ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, comment_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Done");
                    return resultSet.getBoolean(1);
                } else {
                    System.out.println("Done");
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking if user liked a comment",e);
        }
    }

    public static boolean check_user_dislikes_comment(UUID comment_id, UUID user_id) {
        System.out.println("Check if user "+user_id+" has disliked comment "+comment_id+" ...");
        String query = "SELECT EXISTS (SELECT 1 FROM comment_dislikes WHERE user_id = ? AND comment_id = ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, comment_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Done");
                    return resultSet.getBoolean(1);
                } else {
                    System.out.println("Done");
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking if user disliked a comment",e);
        }
    }

    public static void remove_user_like_from_comment(UUID comment_id, UUID user_id) {
        System.out.println("Removing user "+user_id+" like from comment "+comment_id+" ...");
        String query = "DELETE FROM comment_likes WHERE user_id = ? AND comment_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, comment_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while removing user like from a comment",e);
        }
    }

    public static void remove_user_dislike_from_comment(UUID comment_id, UUID user_id) {
        System.out.println("Removing user "+user_id+" dislike from comment "+comment_id+" ...");
        String query = "DELETE FROM comment_dislikes WHERE user_id = ? AND comment_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, comment_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while removing user dislike from a comment",e);
        }
    }

    public static int number_of_likes(UUID comment_id) {
        String query = "SELECT COUNT(*) AS row_count FROM comment_likes WHERE comment_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, comment_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("row_count");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred getting number of comment likes",e);
        }
    }

    public static int number_of_dislikes(UUID comment_id) {
        String query = "SELECT COUNT(*) AS row_count FROM comment_dislikes WHERE comment_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, comment_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("row_count");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred getting number of comment dislikes",e);
        }
    }

    public static ServerResponse get_info(UUID id, int request_id){
        System.out.println("Getting info of comment "+id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query ="SELECT * FROM comments WHERE id = ?";
        try(Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1 , id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    serverResponse.add_part("id" , resultSet.getString("id"));
                    serverResponse.add_part("owner_id" , resultSet.getString("owner_id"));
                    serverResponse.add_part("content" , resultSet.getString("content"));
                    serverResponse.add_part("post_id" , resultSet.getString("post_id"));
                    serverResponse.add_part("parent_comment_id" , resultSet.getString("parent_comment_id"));
                    serverResponse.add_part("created_at" , resultSet.getString("created_at"));
                }
            }
            System.out.println("Done");
            return serverResponse;
        }catch (SQLException e){
            System.out.println("Database error occurred while getting comment info");
        }
        return null;
    }
    ///+++

    public static void main(String[] args) {

    }
}
