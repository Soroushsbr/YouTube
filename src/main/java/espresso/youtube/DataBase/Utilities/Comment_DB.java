package espresso.youtube.DataBase.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Comment_DB {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void add_comment(UUID owner_id, UUID post_id, String content) {
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO comments (id, owner_id, content, post_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, owner_id);
            preparedStatement.setString(3, content);
            preparedStatement.setObject(4, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while adding comment",e);
        }
    }

    public static void reply(UUID owner_id, String content, UUID super_comment_id) {
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO replies (id, owner_id, content, super_comment_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, owner_id);
            preparedStatement.setString(3, content);
            preparedStatement.setObject(4, super_comment_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while replying to a comment",e);
        }
    }
    ///+++
    public static void main(String[] args) {

    }
}
