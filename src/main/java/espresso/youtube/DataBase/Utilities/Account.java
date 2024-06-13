package espresso.youtube.DataBase.Utilities;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Account {
    private static final String URL = "jdbc:postgresql://localhost/youtube";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";
    private static Connection create_connection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean check_gmail_exists(String gmail) {
        try {
            Connection connection = create_connection();
            String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE gmail = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, gmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            connection.close();
            preparedStatement.close();
            resultSet.close();
            return resultSet.getBoolean(1);
        } catch (SQLException e) {throw new RuntimeException(e);}
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
        boolean darkmod = false;
        boolean is_premium = false;
        UUID profile_photo = null;

        try {
            Connection connection = create_connection();
            String query = "INSERT INTO accounts (id, username, gmail, password, darkmod, profile_photo, is_premium) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, uuid);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, gmail);
            preparedStatement.setString(4, password);
            preparedStatement.setBoolean(5, darkmod);
            preparedStatement.setObject(6, profile_photo);
            preparedStatement.setObject(7, is_premium);

            preparedStatement.executeUpdate();

            connection.close();
            preparedStatement.close();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean is_password_correct(String username, String password) {
        try {
            Connection connection = create_connection();
            String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE id = ? AND password = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getBoolean(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static boolean check_username_exists(String username) {
        try {
            Connection connection = create_connection();
            String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE username = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getBoolean(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void add_profile_photo(UUID uesr_id, UUID photo_id) {
        try {
            Connection connection = create_connection();
            String query = "UPDATE accounts SET profile_photo = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, photo_id);
            preparedStatement.setObject(2, uesr_id);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void make_user_premium(UUID user_id) {
        try {
            Connection connection = create_connection();
            String query = "UPDATE accounts SET is_premium = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void remove_premium_of_user(UUID user_id) {
        try {
            Connection connection = create_connection();
            String query = "UPDATE accounts SET is_premium = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, false);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void change_darkmod(UUID user_id, boolean darkmod) {
        try {
            Connection connection = create_connection();
            String query = "UPDATE accounts SET darkmod = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, darkmod);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void add_notification(UUID user_id, String content) {
        try {
            Connection connection = create_connection();
            String query = "INSERT INTO notifications (user_id, content) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setString(2, content);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {throw new RuntimeException(e);}
    }

    public static void change_password(UUID user_id, String password) {
        try {
            Connection connection = create_connection();
            password = hash_password(password);
            String query = "UPDATE accounts SET password = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, password);
            preparedStatement.setObject(2, user_id);
            preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {throw new RuntimeException(e);}
    }

}


//add comment like/dislike tables
//create a default channel with name of uesr
//watch later for each user
//

