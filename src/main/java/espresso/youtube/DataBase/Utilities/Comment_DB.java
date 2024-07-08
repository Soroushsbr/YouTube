package espresso.youtube.DataBase.Utilities;

import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.comment.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Comment_DB {
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

    public static ServerResponse add_comment(UUID owner_id, UUID post_id, String content, int request_id) {
        System.out.println("[DATABASE] Adding comment to post "+post_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
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
            serverResponse.add_part("isSuccessful", true);
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse reply_to_comment(UUID owner_id, UUID post_id, String content, UUID parent_comment_id, int request_id) {
        System.out.println("[DATABASE] Replying to comment "+parent_comment_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
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
            serverResponse.add_part("isSuccessful", true);
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse like_comment(UUID comment_id, UUID user_id, int request_id) {
        System.out.println("[DATABASE] User "+user_id+" is liking comment "+comment_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "INSERT INTO comment_likes (comment_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, comment_id);
            preparedStatement.setObject(2, user_id);
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

    public static ServerResponse dislike_comment(UUID comment_id, UUID user_id, int request_id) {
        System.out.println("[DATABASE] User "+user_id+" is disliking comment "+comment_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "INSERT INTO comment_dislikes (comment_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, comment_id);
            preparedStatement.setObject(2, user_id);
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

    public static ServerResponse check_user_likes_comment(UUID comment_id, UUID user_id, int request_id) {
        System.out.println("[DATABASE] Check if user "+user_id+" has liked comment "+comment_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "SELECT EXISTS (SELECT 1 FROM comment_likes WHERE user_id = ? AND comment_id = ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, comment_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    serverResponse.add_part("user_likes_comment", resultSet.getBoolean(1));
                    System.out.println("Done");

                } else {
                    System.out.println("[DATABASE] Done");
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse check_user_dislikes_comment(UUID comment_id, UUID user_id, int request_id) {
        System.out.println("[DATABASE] Check if user "+user_id+" has disliked comment "+comment_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "SELECT EXISTS (SELECT 1 FROM comment_dislikes WHERE user_id = ? AND comment_id = ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, comment_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    serverResponse.add_part("user_dislikes_comment", resultSet.getBoolean(1));
                    System.out.println("[DATABASE] Done");
                } else {
                    System.out.println("[DATABASE] Done");
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse remove_user_like_from_comment(UUID comment_id, UUID user_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        System.out.println("[DATABASE] Removing user "+user_id+" like from comment "+comment_id+" ...");
        String query = "DELETE FROM comment_likes WHERE user_id = ? AND comment_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, comment_id);
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

    public static ServerResponse remove_user_dislike_from_comment(UUID comment_id, UUID user_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        System.out.println("[DATABASE] Removing user "+user_id+" dislike from comment "+comment_id+" ...");
        String query = "DELETE FROM comment_dislikes WHERE user_id = ? AND comment_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, comment_id);
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

    public static ServerResponse number_of_likes(UUID comment_id, int request_id) {
        String query = "SELECT COUNT(*) AS row_count FROM comment_likes WHERE comment_id = ?";
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, comment_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    serverResponse.add_part("number_of_likes", resultSet.getInt("row_count"));

                } else {
                    return serverResponse;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse number_of_dislikes(UUID comment_id, int request_id) {
        String query = "SELECT COUNT(*) AS row_count FROM comment_dislikes WHERE comment_id = ?";
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, comment_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    serverResponse.add_part("number_of_dislikes", resultSet.getInt("row_count"));
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
        System.out.println("[DATABASE] Getting info of comment "+id+" ...");
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
            System.out.println("[DATABASE] Done");
        } catch (SQLException e){
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse delete_comment(UUID comment_id, int request_id) {
        System.out.println("[DATABASE] Deleting comment "+comment_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<String> queries = new ArrayList<>();
        queries.add("UPDATE comments SET parent_comment_id = NULL WHERE parent_comment_id = ?");
        queries.add("DELETE FROM comment_likes WHERE comment_id = ?");
        queries.add("DELETE FROM comment_dislikes WHERE comment_id = ?");
        queries.add("DELETE FROM comments WHERE id = ?");

        try (Connection connection = create_connection()) {
            connection.setAutoCommit(false);
            for (String query : queries) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setObject(1, comment_id);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
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

    public static ServerResponse get_all_comments_of_a_post(UUID post_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<Comment> comments = new ArrayList<>();
        String query = "SELECT id, owner_id, content, post_id, parent_comment_id, created_at FROM comments WHERE post_id = ?";

        try (Connection connection = create_connection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, post_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Comment comment = new Comment();
                    comment.setComment_id(resultSet.getString("id"));
                    comment.setUser_id(resultSet.getString("owner_id"));
                    comment.setMessage(resultSet.getString("content"));
                    comment.setCreated_at(resultSet.getTimestamp("created_at"));
                    if (resultSet.getObject("parent_comment_id") == null){
                        comment.setParent_comment_id(null);
                    } else {
                        comment.setParent_comment_id(resultSet.getObject("parent_comment_id").toString());
                    }
                    ServerResponse sr ;
                    sr = Channel_DB.get_info(UUID.fromString(comment.getUser_id()) , request_id);
                    comment.setUsername((String) sr.get_part("title"));
                    comments.add(comment);
                }
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
        serverResponse.setComments_list(comments);
        return serverResponse;
    }
    ///+++
    public static ServerResponse edit_comment(UUID comment_id, String content, int request_id) {
        System.out.println("[DATABASE] Editing content of comment " + comment_id + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.add_part("request_id", request_id);
        String query = "UPDATE comments SET content = ? WHERE id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, content);
            preparedStatement.setObject(2, comment_id);
            preparedStatement.executeUpdate();
            serverResponse.add_part("isSuccessful", true);
        } catch (SQLException e) {
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }
        return serverResponse;
    }

    public static void main(String[] args) {

    }
}
