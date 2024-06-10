package espresso.youtube.DataBase.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Comment {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void add_comment(UUID owner_id, UUID post_id, String content) {
        UUID id = UUID.randomUUID();
        try {
            Connection connection = create_connection();
            String query = "INSERT INTO comments (id, owner_id, content, post_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, owner_id);
            preparedStatement.setObject(3, post_id);
            preparedStatement.setString(4, content);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void reply(UUID owner_id, String content, UUID super_comment_id) {
        UUID id = UUID.randomUUID();
        try {
            Connection connection = create_connection();
            String query = "INSERT INTO replies (id, owner_id, content, super_comment_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, owner_id);
            preparedStatement.setString(3, content);
            preparedStatement.setObject(4, super_comment_id);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {throw new RuntimeException(e);}
    }
}
