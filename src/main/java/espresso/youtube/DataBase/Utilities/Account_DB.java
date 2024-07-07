package espresso.youtube.DataBase.Utilities;

import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.channel.Channel;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Account_DB {
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

    public static boolean check_gmail_exists(String gmail) {
        System.out.println("[DATABASE] Checking if " + gmail + " exists... ");
        String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE gmail = ?)";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, gmail);
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

    public static boolean check_gmail_validation(String gmail) {
        System.out.println("[DATABASE] Checking if "+ gmail + " is a valid gmail... ");
        String regex = "^[\\w.+-]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(gmail);
        System.out.println("[DATABASE] Done");
        return matcher.matches();
    }

    public static String hash_password(String password) {
        System.out.println("[DATABASE] Hashing password... ");
        String salt = BCrypt.gensalt(11);
        String hashed_password;
        hashed_password = BCrypt.hashpw(password, salt);
        System.out.println("[DATABASE] Done");
        return(hashed_password);
    }

    public static void save_account(String username, String password, String gmail) {
        System.out.println("[DATABASE] Saving account of user "+username+" ...");
        Channel_DB.create_user_default_channel(get_id_by_username(username));
        Playlist_DB.create_watch_later(get_id_by_username(username));
        UUID uuid = UUID.randomUUID();
        boolean dark_mode = true;
        boolean isPremium = false;
        password = hash_password(password);

        String query = "INSERT INTO accounts (id, username, gmail, password, dark_mode, is_premium) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, uuid);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, gmail);
            preparedStatement.setString(4, password);
            preparedStatement.setBoolean(5, dark_mode);
            preparedStatement.setBoolean(6, isPremium);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static boolean is_password_correct(String username, String password) {
        System.out.println("[DATABASE] Checking if password is correct... ");
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("[DATABASE] Done");
                    return BCrypt.checkpw(password, resultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return false;
    }
    public static boolean check_username_exists(String username) {
        System.out.println("[DATABASE] Checking if " + username + " username exists...");
        String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE username = ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
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

    public static ServerResponse make_user_premium(UUID user_id, int request_id) {
        System.out.println("[DATABASE] Making " + user_id + " user premium...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "UPDATE accounts SET is_premium = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, true);
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

    public static ServerResponse remove_premium_of_user(UUID user_id, int request_id) {
        System.out.println("[DATABASE] Removing premium of user " + user_id + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "UPDATE accounts SET is_premium = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, false);
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

    public static ServerResponse change_dark_mode(UUID user_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String selectQuery= "SELECT dark_mode FROM accounts WHERE id = ?";
        String updateQuery = "UPDATE accounts SET dark_mode = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);PreparedStatement selectStatement = connection.prepareStatement(selectQuery);PreparedStatement updateStatement = connection.prepareStatement(updateQuery) ){
            connection.setAutoCommit(false);
            selectStatement.setObject(1, user_id);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                boolean dark_mode = resultSet.getBoolean("dark_mode");
                updateStatement.setBoolean(1, !dark_mode);
                updateStatement.setObject(2, user_id);
                updateStatement.executeUpdate();
                serverResponse.add_part("isSuccessful", true);
                System.out.println("Dark mode for account " + user_id + " has been set to: " + !dark_mode);
            } else {
                serverResponse.add_part("isSuccessful", false);
                System.out.println("Account with ID " + user_id + " not found.");
            }
            connection.commit();
        } catch (SQLException e) {
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }
        return serverResponse;
    }
    //??
    public static ServerResponse change_password(UUID user_id, String password, int request_id) {
        System.out.println("[DATABASE] Changing password of user " + user_id + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        password = hash_password(password);
        String query = "UPDATE accounts SET password = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, password);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
            serverResponse.add_part("isSuccessful", true);
            return serverResponse;
        } catch (SQLException e) {
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }
        return serverResponse;
    }

    public static void add_notification(UUID user_id, String title, UUID comment_id, UUID post_id, UUID channel_id ) {
        System.out.println("[DATABASE] Sending notification for user " + user_id + " ...");
        String query = "INSERT INTO notifications (user_id, title, comment_id, post_id, channel_id) VALUES (?, ?, ?, ?, ?)";
        try(Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setString(2, title);
            preparedStatement.setObject(3, comment_id);
            preparedStatement.setObject(4, post_id);
            preparedStatement.setObject(5, channel_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static ServerResponse sign_up(String username, String password, String gmail, int request_id) {
        System.out.println("[DATABASE] Signing up for user " + username + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        serverResponse.add_part("isValidUsername" , !check_username_exists(username));
        serverResponse.add_part("isValidGmail" , !check_gmail_exists(gmail) && check_gmail_validation(gmail));
        if ((boolean)serverResponse.get_part("isValidUsername") && (boolean)serverResponse.get_part("isValidGmail")){
            save_account(username, password,gmail);
            serverResponse.add_part("isSuccessful", true);
            serverResponse.add_part("userID", Objects.requireNonNull(get_id_by_username(username)).toString());
        } else {
            serverResponse.add_part("isSuccessful", false);
        }
        System.out.println("[DATABASE] Done");
        return serverResponse;
    }

    public static ServerResponse login(String username , String password, int request_id){
        System.out.println("[DATABASE] User " + username + " is logging in ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        serverResponse.add_part("isSuccessful" , check_username_exists(username) && is_password_correct(username, password));
        if((boolean)serverResponse.get_part("isSuccessful")){
            serverResponse.add_part("userID", Objects.requireNonNull(get_id_by_username(username)).toString());
        }
        System.out.println("[DATABASE] Done");
        return serverResponse;
    }

    public static UUID get_id_by_username(String username){
        String query = "SELECT id FROM accounts WHERE username = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return (UUID) resultSet.getObject("id");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return null;
    }

    public static String get_username_by_id(UUID id){
        String query = "SELECT username FROM accounts WHERE id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("username");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return null;
    }

    public static ServerResponse get_info(UUID id, int request_id){
        System.out.println("[DATABASE] Getting info of account " + id + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query ="SELECT * FROM accounts WHERE id = ?";
        try(Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1 , id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    serverResponse.add_part("id" , resultSet.getString("id"));
                    serverResponse.add_part("username" , resultSet.getString("username"));
                    serverResponse.add_part("gmail" , resultSet.getString("gmail"));
                    serverResponse.add_part("password" , resultSet.getString("password"));
                    serverResponse.add_part("dark_mode" , resultSet.getString("dark_mode"));
                    serverResponse.add_part("is_premium" , resultSet.getString("is_premium"));
                    serverResponse.add_part("created_at" , resultSet.getString("created_at"));
                }
            }
            serverResponse.add_part("isSuccessful", true);
            System.out.println("[DATABASE] Done");
            return serverResponse;
        }catch (SQLException e){
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }
        return serverResponse;
    }

    public static void delete_account(UUID account_id) {
        System.out.println("[DATABASE] Deleting account " + account_id + " ...");
        ArrayList<String> queries = new ArrayList<>();
        queries.add("DELETE FROM post_likes WHERE user_id = ?");
        queries.add("DELETE FROM post_dislikes WHERE user_id = ?");
        queries.add("DELETE FROM comment_likes WHERE user_id = ?");
        queries.add("DELETE FROM comment_dislikes WHERE user_id = ?");
        queries.add("DELETE FROM comments WHERE owner_id = ?");
        queries.add("DELETE FROM posts WHERE owner_id = ?");
        queries.add("DELETE FROM playlists WHERE owner_id = ?");
        queries.add("DELETE FROM channel_subscription WHERE subscriber_id = ?");
        queries.add("DELETE FROM playlist_subscription WHERE subscriber_id = ?");
        queries.add("DELETE FROM notifications WHERE user_id = ?");
        queries.add("DELETE FROM views WHERE user_id = ?");
        queries.add("DELETE FROM channels WHERE owner_id = ?");
        queries.add("DELETE FROM accounts WHERE id = ?");

        try (Connection connection = create_connection()) {
            connection.setAutoCommit(false);
            for(String query : queries ){
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
                    preparedStatement.setObject(1, account_id);
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

    public static ServerResponse get_subscribed_channels(UUID user_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<Channel> channels = new ArrayList<>();
        String sql = "SELECT c.id, c.title, c.username, c.owner_id, c.description, c.created_at FROM channels c JOIN channel_subscription cs ON c.id = cs.channel_id WHERE cs.subscriber_id = ?";

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

//    public static List<UUID> get_subscribed_playlists(UUID user_id) {
//        System.out.println("[DATABASE] Getting subscribed playlists of user " + user_id + " ...");
//        List<UUID> IDs = new ArrayList<>();
//        String sql = "SELECT playlist_id FROM playlist_subscription WHERE subscriber_id = ?";
//        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
//            preparedStatement.setObject(1, user_id);
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    UUID playlistId = (UUID) resultSet.getObject("playlist_id");
//                    IDs.add(playlistId);
//                }
//            }
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
//        System.out.println("[DATABASE] Done");
//        return IDs;
//    }
    /////+++
    public static ServerResponse change_username(UUID user_id, String username, int request_id) {
        System.out.println("[DATABASE] Changing username of user " + user_id + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "UPDATE accounts SET username = ? WHERE id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, username);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            serverResponse.add_part("isSuccessful", true);
        } catch (SQLException e) {
            printSQLException(e);
            serverResponse.add_part("isSuccessful", false);
        }
        System.out.println("[DATABASE] Done");
        return serverResponse;
    }

    public static ServerResponse change_gmail(UUID user_id, String gmail, int request_id) {
        System.out.println("[DATABASE] Changing username of user " + user_id + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "UPDATE accounts SET gmail = ? WHERE id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, gmail);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            serverResponse.add_part("isSuccessful", true);
        } catch (SQLException e) {
            printSQLException(e);
            serverResponse.add_part("isSuccessful", false);
        }
        System.out.println("[DATABASE] Done");
        return serverResponse;
    }



    public static void main(String[] args) {

    }
}

//methods that are gray?

//check user is owner of a post, playlist, and channel, and comment???



//check delete_account
//check get info for new columns


//give data to load post with comments and views, channel, profile, playlist mobin array in server response?

//notification????
//delete notifications??

//give ids in search and get_all_posts

//array returns:
//accounts that liked/disliked a post/comment????

//get all posts?

//watch history?

//change anything else?

//check tables with schema
//write list of methods to check
//write log with detail. add [DATABASE], complete logs. remove println to print in same line
//complete doc
//write comment about what method does

