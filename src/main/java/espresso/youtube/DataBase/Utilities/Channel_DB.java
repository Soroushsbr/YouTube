package espresso.youtube.DataBase.Utilities;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import espresso.youtube.Front.ChannelPage;
import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.account.Account;
import espresso.youtube.models.channel.Channel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel_DB {
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

    public static ServerResponse create_channel(UUID owner_id, String title, String description, int request_id) {
        System.out.println("[DATABASE] Creating channel ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        UUID id = UUID.randomUUID();
        String query = "INSERT INTO channels (id, title, owner_id, description, username) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setObject(3, owner_id);
            preparedStatement.setString(4, description);
            preparedStatement.setString(5, Account_DB.get_username_by_id(owner_id));
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

    public static ServerResponse subscribe_to_channel(UUID channel_id, UUID subscriber_id, int request_id) {
        System.out.println("[DATABASE] Subscribing user " + subscriber_id + "to channel "+channel_id + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "INSERT INTO channel_subscription (channel_id, subscriber_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, channel_id);
            preparedStatement.setObject(2, subscriber_id);
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

    public static void create_user_default_channel(UUID user_id, UUID id) {
        System.out.println("[DATABASE] Creating default channel of user " + user_id + " ...");
        String query = "INSERT INTO channels (id, title, username, owner_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, Account_DB.get_username_by_id(user_id));
            preparedStatement.setString(3, Account_DB.get_username_by_id(user_id));
            preparedStatement.setObject(4, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static ServerResponse check_if_user_subscribed(UUID channel_id, UUID user_id, int request_id) {
        System.out.println("[DATABASE] Checking if user " + user_id + " is subscribed to channel "+channel_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "SELECT EXISTS (SELECT 1 FROM channel_subscription WHERE channel_id = ? AND subscriber_id = ? )";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, channel_id);
            preparedStatement.setObject(2, user_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("[DATABASE] Done");
                    serverResponse.add_part("is_subscribed", resultSet.getBoolean(1));
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse unsubscribe_to_channel(UUID channel_id, UUID subscriber_id, int request_id) {
        System.out.println("[DATABASE] Unsubscribing user " + subscriber_id + "from channel "+channel_id + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "DELETE FROM channel_subscription WHERE channel_id = ? AND subscriber_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, channel_id);
            preparedStatement.setObject(2, subscriber_id);
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

    public static ServerResponse change_channel_title(UUID channel_id, String title, int request_id) {
        System.out.println("[DATABASE] Changing title of channel " + channel_id + "to "+title + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "UPDATE channels SET title = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, title);
            preparedStatement.setObject(2, channel_id);
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

    public static ServerResponse change_channel_description(UUID channel_id, String description, int request_id) {
        System.out.println("[DATABASE] Changing description of channel " + channel_id + "to "+description + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "UPDATE channels SET description = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, description);
            preparedStatement.setObject(2, channel_id);
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

    public static ServerResponse number_of_subscribers(UUID channel_id, int request_id) {
        String query = "SELECT COUNT(*) AS row_count FROM channel_subscription WHERE channel_id = ?";
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1,channel_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    serverResponse.add_part("number_of_subscribers", resultSet.getInt("row_count"));
                } else {
                    serverResponse.add_part("number_of_subscribers", resultSet.getInt(0));
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse number_of_posts(UUID channel_id, int request_id) {
        String query = "SELECT COUNT(*) AS row_count FROM posts WHERE channel_id = ?";
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1,channel_id);
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
        System.out.println("[DATABASE] Getting info of channel "+id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query ="SELECT * FROM channels WHERE id = ?";
        try(Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1 , id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    serverResponse.add_part("id" , resultSet.getString("id"));
                    serverResponse.add_part("title" , resultSet.getString("title"));
                    serverResponse.add_part("username" , resultSet.getString("username"));
                    serverResponse.add_part("owner_id" , resultSet.getString("owner_id"));
                    serverResponse.add_part("description" , resultSet.getString("description"));
                    serverResponse.setCreated_at(resultSet.getTimestamp("created_at"));
                }
            }
            System.out.println("[DATABASE] Done");
            return serverResponse;
        } catch (SQLException e){
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse delete_channel(UUID channel_id, int request_id) {
        System.out.println("[DATABASE] Deleting channel "+channel_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<String> queries = new ArrayList<>();
        queries.add("DELETE FROM post_likes WHERE post_id IN (SELECT id FROM posts WHERE channel_id = ?)");
        queries.add("DELETE FROM post_dislikes WHERE post_id IN (SELECT id FROM posts WHERE channel_id = ?)");
        queries.add("DELETE FROM comment_likes WHERE comment_id IN (SELECT id FROM comments WHERE post_id IN (SELECT id FROM posts WHERE channel_id = ?))");
        queries.add("DELETE FROM comment_dislikes WHERE comment_id IN (SELECT id FROM comments WHERE post_id IN (SELECT id FROM posts WHERE channel_id = ?))");
        queries.add("DELETE FROM comments WHERE post_id IN (SELECT id FROM posts WHERE channel_id = ?)");
        queries.add("DELETE FROM posts WHERE channel_id = ?");
        queries.add("DELETE FROM channel_subscription WHERE channel_id = ?");
        queries.add("DELETE FROM channels WHERE id = ?");
        try (Connection connection = create_connection()) {
            connection.setAutoCommit(false);
            for(String query : queries ){
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
                    preparedStatement.setObject(1, channel_id);
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
            printSQLException(e);
            serverResponse.add_part("isSuccessful", false);
        }
        return serverResponse;
    }

    public static ServerResponse get_channels_of_account(UUID user_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<Channel> channels = new ArrayList<>();
        String sql = "SELECT id, title, username, owner_id, description, created_at FROM channels WHERE owner_id = ?";

        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, user_id, Types.OTHER);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Channel channel = new Channel();
                    channel.setUsername(resultSet.getString("username"));
                    channel.setId(resultSet.getObject("id").toString());
                    channel.setName(resultSet.getString("title"));
                    channel.setOwner_id(resultSet.getObject("owner_id").toString());
                    channel.setDescription(resultSet.getString("description"));
                    channel.setCreated_at(resultSet.getTimestamp("created_at"));
                    channels.add(channel);
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        serverResponse.setChannels_list(channels);
        return serverResponse;
    }
  
    public static ServerResponse get_subscribers(UUID channel_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.id, a.username, a.gmail, a.password, a.dark_mode, a.is_premium, a.created_at FROM accounts a JOIN channel_subscription cs ON a.id = cs.subscriber_id WHERE cs.channel_id = ?";

        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, channel_id, Types.OTHER);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = new Account();
                    account.setId(resultSet.getObject("id").toString());
                    account.setUsername(resultSet.getString("username"));
                    account.setGmail(resultSet.getString("gmail"));
                    account.setPassword(resultSet.getString("password"));
                    account.setDark_mode(resultSet.getBoolean("dark_mode"));
                    account.setIs_premium(resultSet.getBoolean("is_premium"));
                    account.setCreated_at(resultSet.getTimestamp("created_at"));
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }

        serverResponse.setAccounts_list(accounts);
        return serverResponse;
    }

    public static ServerResponse change_channel_username(UUID channel_id, String username, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        System.out.println("[DATABASE] Changing username of channel " + channel_id + "to "+username + " ...");
        String query = "UPDATE channels SET username = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, username);
            preparedStatement.setObject(2, channel_id);
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
    /////+++

    public static void main(String[] args) {

    }


}



