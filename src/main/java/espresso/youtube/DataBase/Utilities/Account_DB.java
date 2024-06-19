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
        } else {
            serverResponse.add_part("isSuccessful", false);
        }
        return serverResponse;
    }

    public static ServerResponse login(String username , String password, int request_id){
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        serverResponse.add_part("isSuccessful" , check_username_exists(username) && is_password_correct(username, password));
        return serverResponse;
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


//add comment like/dislike tables
//create a default channel with name of user
//watch later for each user
//

//post views
//delete post, account, channel, comment?
//understand code

//give data to load post with comments and views, channel, profile, playlist

//notif--> from channel and user, text, to a user?

//change channel and playlist title
//unsubscribe channel and playlist
//like/dislike comment and post. check if user has already liked or disliked. remove like or dislike.


//change everything?
//write log with detail.
//write comment with detail

//check tables with schema

//like and dislike for both post and comment

//view full table in sql shell