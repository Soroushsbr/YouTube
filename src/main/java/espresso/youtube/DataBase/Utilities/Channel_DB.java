package espresso.youtube.DataBase.Utilities;

import espresso.youtube.models.ServerResponse;

import java.sql.*;
import java.util.UUID;

public class Channel_DB {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void create_channel(UUID owner_id, String title, String description) {
        System.out.println("Creating channel as " + title + " ...");
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO channels (id, title, owner_id, description) VALUES (?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setObject(3, owner_id);
            preparedStatement.setString(4, description);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while creating channel",e);
        }
    }

    public static void subscribe_to_channel(UUID channel_id, UUID subscriber_id) {
        System.out.println("Subscribing user " + subscriber_id + "to channel "+channel_id + " ...");
        String query = "INSERT INTO channel_subscription (channel_id, subscriber_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, channel_id);
            preparedStatement.setObject(2, subscriber_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while subscribing to channel",e);
        }
    }

    public static void create_user_default_channel(UUID user_id) {
        System.out.println("Creating default channel of user " + user_id + " ...");
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO channels (id, title, owner_id) VALUES (?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, Account_DB.get_username_by_id(user_id));
            preparedStatement.setObject(3, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while creating user default channel",e);
        }
    }

    public static boolean check_if_user_subscribed(UUID channel_id, UUID user_id) {
        System.out.println("Checking if user " + user_id + " is subscribed to channel "+channel_id+" ...");
        String query = "SELECT EXISTS (SELECT 1 FROM channel_subscription WHERE channel_id = ? AND subscriber_id = ? )";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, channel_id);
            preparedStatement.setObject(2, user_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Done");
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking if user subscribed to channel", e);
        }
        return false;
    }

    public static void unsubscribe_to_channel(UUID channel_id, UUID subscriber_id) {
        System.out.println("Unsubscribing user " + subscriber_id + "from channel "+channel_id + " ...");
        String query = "DELETE FROM channel_subscription WHERE channel_id = ? AND subscriber_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, channel_id);
            preparedStatement.setObject(2, subscriber_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while unsubscribing from channel",e);
        }
    }

    public static void change_channel_title(UUID channel_id, String title) {
        //check if user is owner of channel??
        System.out.println("Changing title of channel " + channel_id + "to "+title + " ...");
        String query = "UPDATE channels SET title = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, title);
            preparedStatement.setObject(2, channel_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing channel title",e);
        }
    }

    public static void change_channel_description(UUID channel_id, String description) {
        //check if user is owner of channel??
        System.out.println("Changing description of channel " + channel_id + "to "+description + " ...");
        String query = "UPDATE channels SET description = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, description);
            preparedStatement.setObject(2, channel_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing channel description",e);
        }
    }

    public static int number_of_subscribers(UUID channel_id) {
        String query = "SELECT COUNT(*) AS row_count FROM channel_subscription WHERE channel_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1,channel_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("row_count");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred getting number of channel subscribers",e);
        }
    }

    public static int number_of_posts(UUID channel_id) {
        String query = "SELECT COUNT(*) AS row_count FROM posts WHERE channel_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1,channel_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("row_count");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred getting number of channel posts",e);
        }
    }

    public static ServerResponse get_info(UUID id, int request_id){
        System.out.println("Getting info of channel "+id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query ="SELECT * FROM channels WHERE id = ?";
        try(Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1 , id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    serverResponse.add_part("id" , resultSet.getString("id"));
                    serverResponse.add_part("title" , resultSet.getString("title"));
                    serverResponse.add_part("owner_id" , resultSet.getString("owner_id"));
                    serverResponse.add_part("description" , resultSet.getString("description"));
                    serverResponse.add_part("created_at" , resultSet.getString("created_at"));
                }
            }
            System.out.println("Done");
            return serverResponse;
        }catch (SQLException e){
            System.out.println("Database error occurred while getting channel info");
        }
        return null;
    }
    /////+++


    public static void main(String[] args) {

    }


}



