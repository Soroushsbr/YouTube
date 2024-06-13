package espresso.youtube.DataBase.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Channel {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void create_channel(UUID owner_id, String name, String description) {
        UUID id = UUID.randomUUID();
        try {
            Connection connection = create_connection();
            String query = "INSERT INTO channels (id, name, owner_id, description) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setObject(3, owner_id);
            preparedStatement.setString(4, description);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();

        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void subscribe_to_channel(UUID channel_id, UUID subscriber_id) {
        try {
            Connection connection = create_connection();
            String query = "INSERT INTO channel_subscription (channel_id, subscriber_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, channel_id);
            preparedStatement.setObject(2, subscriber_id);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }


}
