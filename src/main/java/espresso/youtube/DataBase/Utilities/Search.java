package espresso.youtube.DataBase.Utilities;

import espresso.youtube.models.ServerResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.*;


public class Search {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static List<UUID> search_accounts(String text, int request_id) {
        System.out.println("Searching for "+text+" in accounts ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        List<UUID> IDs = new ArrayList<>();

        String query = "SELECT id FROM accounts WHERE username LIKE ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + text + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID id = (UUID) resultSet.getObject("id");
                    IDs.add(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while searching in accounts", e);
        }
        System.out.println("Done");
        return IDs;
    }

    public static List<UUID> search_channels(String text) {
        System.out.println("Searching for "+text+" in channels ...");
        List<UUID> IDs = new ArrayList<>();
        String query = "SELECT id FROM channels WHERE title LIKE ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + text + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID id = (UUID) resultSet.getObject("id");
                    IDs.add(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while searching in channels", e);
        }
        System.out.println("Done");
        return IDs;
    }

    public static List<UUID> search_playlists(String text) {
        System.out.println("Searching for "+text+" in playlists ...");
        List<UUID> IDs = new ArrayList<>();
        String query = "SELECT id FROM channels WHERE title LIKE ? AND is_public = true";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + text + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID id = (UUID) resultSet.getObject("id");
                    IDs.add(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while searching in playlists", e);
        }
        System.out.println("Done");
        return IDs;
    }

    public static List<UUID> search_posts(String text) {
        System.out.println("Searching for "+text+" in posts ...");
        List<UUID> IDs = new ArrayList<>();
        String query = "SELECT id FROM posts WHERE title LIKE ? ";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + text + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID id = (UUID) resultSet.getObject("id");
                    IDs.add(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while searching in posts", e);
        }
        System.out.println("Done");
        return IDs;
    }
    //+++

}
