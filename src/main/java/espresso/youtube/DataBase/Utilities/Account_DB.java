package espresso.youtube.DataBase.Utilities;

import espresso.youtube.models.ServerResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
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

    public static boolean check_gmail_exists(String gmail) {
        System.out.println("Checking if " + gmail + " exists...");
        String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE gmail = ?)";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, gmail);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Done");
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking gmail existence", e);
        }
        return false;
    }

    public static boolean check_gmail_validation(String gmail) {
        System.out.println("Checking if "+ gmail + " is a valid gmail...");
        String regex = "^[\\w.+-]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(gmail);
        System.out.println("Done");
        return matcher.matches();
    }

    public static String hash_password(String password) {
        System.out.println("Hashing password...");
        String salt = BCrypt.gensalt(11);
        String hashed_password;
        hashed_password = BCrypt.hashpw(password, salt);
        System.out.println("Done");
        return(hashed_password);
    }

    public static void save_account(String username, String password, String gmail) {
        System.out.println("Saving account of user "+username+" ...");
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
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while saving user account",e);
        }
    }

    public static boolean is_password_correct(String username, String password) {
        System.out.println("Checking if password is correct...");
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Done");
                    return BCrypt.checkpw(password, resultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking if the password is correct",e);
        }
        return false;
    }
    public static boolean check_username_exists(String username) {
        System.out.println("Checking if username of " + username + " exists...");
        String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE username = ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Done");
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking username existence", e);
        }
        return false;
    }

    public static void make_user_premium(UUID user_id) {
        System.out.println("Making user " + user_id + " premium...");
        String query = "UPDATE accounts SET is_premium = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while making user premium",e);
        }
    }

    public static void remove_premium_of_user(UUID user_id) {
        System.out.println("Removing premium of user " + user_id + " ...");
        String query = "UPDATE accounts SET is_premium = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, false);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while making user premium",e);
        }
    }

    public static void change_dark_mode(UUID user_id, boolean dark_mode) {
        System.out.println("Changing dark mode of user " + user_id + " ...");
        String query = "UPDATE accounts SET dark_mode = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, dark_mode);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing user dark mode",e);
        }
    }

    public static void change_password(UUID user_id, String password) {
        System.out.println("Changing password of user " + user_id + " ...");
        password = hash_password(password);
        String query = "UPDATE accounts SET password = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, password);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing password",e);
        }
    }

    public static void add_notification(UUID user_id, String content) {
        System.out.println("Sending notification for user " + user_id + " ...");
        String query = "INSERT INTO notifications (user_id, content) VALUES (?, ?)";
        try(Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setString(2, content);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("Done");
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while adding notification",e);
        }
    }

    public static ServerResponse sign_up(String username, String password, String gmail, int request_id) {
        System.out.println("Signing up for user " + username + " ...");
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
        System.out.println("Done");
        return serverResponse;
    }

    public static ServerResponse login(String username , String password, int request_id){
        System.out.println("User " + username + " is logging in ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        serverResponse.add_part("isSuccessful" , check_username_exists(username) && is_password_correct(username, password));
        if((boolean)serverResponse.get_part("isSuccessful")){
            serverResponse.add_part("userID", Objects.requireNonNull(get_id_by_username(username)).toString());
        }
        System.out.println("Done");
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
            throw new RuntimeException("Database error occurred while getting account ID by username", e);
        }
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
            throw new RuntimeException("Database error occurred while getting account username by ID", e);
        }
    }

    public static ServerResponse get_info(UUID id, int request_id){
        System.out.println("Getting info of account " + id + " ...");
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
            System.out.println("Done");
            return serverResponse;
        }catch (SQLException e){
            System.out.println("Database error occurred while getting account info");
        }
        return null;
    }
    /////+++
    public static void main(String[] args) {

    }
}


//correct posts with soroush
//check user is owner of a post, playlist, and channel, and comment???

//!!!!! delete channel---> posts and playlist posts--->> comments---->>> post likes/dislikes+ comment dislikes---->++channel subscription
//delete playlist
//delete post
//delete comment
//delete like/dislike
//delete account


//give data to load post with comments and views, channel, profile, playlist mobin array in server response?

//notification????


//array returns:
//return channels a user subscribed?
//subscribers of a channel/playlist/
//accounts that liked/disliked a post
//accounts that
//???

//change anything else?


//check tables with schema
//write list of methods to check
//write log with detail. add [DATABASE], complete logs. remove println to print in same line
//complete doc
//write comment about what method does

