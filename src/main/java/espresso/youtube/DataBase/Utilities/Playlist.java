package espresso.youtube.DataBase.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Playlist {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void create_playlist(UUID owner_id, String name, boolean is_public) {
        UUID id = UUID.randomUUID();
        try {
            Connection connection = create_connection();
            String query = "INSERT INTO playlists (id, name, owner_id, is_public) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setObject(3, owner_id);
            preparedStatement.setBoolean(4, is_public);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void make_playlist_public(UUID playlist_id) {
        try {
            Connection connection = create_connection();
            String query = "UPDATE playlists SET is_public = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setObject(2, playlist_id);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {throw new RuntimeException(e);}
    }
}
