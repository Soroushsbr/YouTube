package espresso.youtube.DataBase.Utilities;

import espresso.youtube.models.ServerResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
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
        String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE gmail = ?)";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, gmail);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking gmail existence", e);
        }
        return false;
    }

    public static boolean check_gmail_validation(String gmail) {
        String regex = "^[\\w.+-]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(gmail);
        return matcher.matches();
    }

    public static String hash_password(String password) {
        String salt = BCrypt.gensalt(11);
        String hashed_password;
        hashed_password = BCrypt.hashpw(password, salt);
        return(hashed_password);
    }

    public static void save_account(String username, String password, String gmail) {
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
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while saving user account",e);
        }
    }

    public static boolean is_password_correct(String username, String password) {
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return BCrypt.checkpw(password, resultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking if the password is correct",e);
        }
        return false;
    }
    public static boolean check_username_exists(String username) {
        String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE username = ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking username existence", e);
        }
        return false;
    }

    public static void make_user_premium(UUID user_id) {
        String query = "UPDATE accounts SET is_premium = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while making user premium",e);
        }
    }

    public static void remove_premium_of_user(UUID user_id) {
        String query = "UPDATE accounts SET is_premium = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, false);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while making user premium",e);
        }
    }

    public static void change_dark_mode(UUID user_id, boolean dark_mode) {
        String query = "UPDATE accounts SET dark_mode = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, dark_mode);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing user dark mode",e);
        }
    }

    public static void change_password(UUID user_id, String password) {
        password = hash_password(password);
        String query = "UPDATE accounts SET password = ? WHERE id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setString(1, password);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while changing password",e);
        }
    }

    public static ServerResponse sign_up(String username, String password, String gmail, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        serverResponse.add_part("isValidUsername" , !check_username_exists(username));
        serverResponse.add_part("isValidGmail" , !check_gmail_exists(gmail) && check_gmail_validation(gmail));
        if ((boolean)serverResponse.get_part("isValidUsername") && (boolean)serverResponse.get_part("isValidGmail")){
            save_account(username, password,gmail);
            serverResponse.add_part("isSuccessful", true);
//            serverResponse.add_part("userID", (String)get_user_id);
        } else {
            serverResponse.add_part("isSuccessful", false);
        }
        return serverResponse;
    }

    public static ServerResponse login(String username , String password, int request_id){
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        serverResponse.add_part("isSuccessful" , check_username_exists(username) && is_password_correct(username, password));
//            serverResponse.add_part("userID", (String)get_user_id);
        return serverResponse;
    }




    //newww
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
    /////+++
    public static void main(String[] args) {

    }

//    public static void add_notification(UUID user_id, String content) {
//        try {
//            Connection connection = create_connection();
//            String query = "INSERT INTO notifications (user_id, content) VALUES (?, ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setObject(1, user_id);
//            preparedStatement.setString(2, content);
//            preparedStatement.executeUpdate();
//            connection.close();
//            preparedStatement.close();
//        } catch (SQLException e) {throw new RuntimeException(e);}
//    }



}

//return user id for login, sign up, ... ?soroush


//!!!!! delete chennel---> posts and playlist posts--->> comments---->>> likes/dislikes

//check user is owner of a post, playlist, and channel, and comment???

//give data to load post with comments and views, channel, profile, playlist by their ID with json

//notification????


//number of channel/playlist subscribers? number of post views?
// number of likes/dislikes of a post or comment.
// number of posts in channel or playlist

//change anything else?

//check tables with schema
//write list of methods to check
//write log with detail
//complete doc
//write comment about what method does

