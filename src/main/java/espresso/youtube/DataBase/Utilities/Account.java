package espresso.youtube.DataBase.Utilities;

import espresso.youtube.models.ServerResponse;
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
//        try {
//            Connection connection = create_connection();
//            String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE gmail = ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, gmail);
//            ResultSet resultSet = preparedStatement.executeQuery();
////            connection.close();
////            preparedStatement.close();
////            resultSet.close();
//            return resultSet.getBoolean(1);
//        } catch (SQLException e) {throw new RuntimeException(e);}
            String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE gmail = ?)";

            try (Connection connection = create_connection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, gmail);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBoolean(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Database error occurred", e);
            }

            return false; // Shouldn't reach here in typical usage
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

//    public static void save_account(String username, String password, String gmail) {
//        UUID uuid = UUID.randomUUID();
//        boolean darkmod = true;
//        boolean is_premium = false;
//        UUID profile_photo = null;
//
//        try {
//            Connection connection = create_connection();
//            String query = "INSERT INTO accounts (id, username, gmail, password, darkmod, profile_photo, is_premium) VALUES (?, ?, ?, ?, ?, ?, ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setObject(1, uuid);
//            preparedStatement.setString(2, username);
//            preparedStatement.setString(3, gmail);
//            preparedStatement.setString(4, password);
//            preparedStatement.setBoolean(5, darkmod);
//            preparedStatement.setObject(6, profile_photo);
//            preparedStatement.setObject(7, is_premium);
//
//            preparedStatement.executeUpdate();
//
//            connection.close();
//            preparedStatement.close();
//        } catch (SQLException e) { throw new RuntimeException(e); }
//    }
public static void save_account(String username, String password, String gmail) {
    UUID uuid = UUID.randomUUID();
    boolean darkmod = true;
    boolean isPremium = false;
    UUID profilePhoto = null;

    String query = "INSERT INTO accounts (id, username, gmail, password, darkmod, profile_photo, is_premium) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = create_connection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        preparedStatement.setObject(1, uuid);
        preparedStatement.setString(2, username);
        preparedStatement.setString(3, gmail);
        preparedStatement.setString(4, password);
        preparedStatement.setBoolean(5, darkmod);

        if (profilePhoto != null) {
            preparedStatement.setObject(6, profilePhoto);
        } else {
            preparedStatement.setNull(6, Types.OTHER);
        }

        preparedStatement.setBoolean(7, isPremium);

        preparedStatement.executeUpdate();

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

//    public static boolean is_password_correct(String username, String password) {
//        try {
//            Connection connection = create_connection();
//            String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE username = ? AND password = ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, username);
//            preparedStatement.setString(2, password);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            return resultSet.getBoolean(1);
//        } catch (SQLException e) { throw new RuntimeException(e); }
//    }

    public static boolean is_password_correct(String username, String password) {
        String query = "SELECT COUNT(1) FROM accounts WHERE username = ? AND password = ?";

        try (Connection connection = create_connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static boolean check_username_exists(String username) {
//        try {
//            Connection connection = create_connection();
//            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//            String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE username = ?)";
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setObject(1, username);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            return resultSet.getBoolean(1);
//        } catch (SQLException e) { throw new RuntimeException(e); }
        String query = "SELECT EXISTS (SELECT 1 FROM accounts WHERE username = ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error occurred", e);
        }

        return false; // Shouldn't reach here in typical usage
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

    public static ServerResponse sign_up(String username, String password, String gmail, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        serverResponse.add_part("isValidUsername" , !check_username_exists(username));
        serverResponse.add_part("isValidGmail" , !check_gmail_exists(gmail) && check_gmail_validation(gmail));

//        serverResponse.add_part("isValidGamil" , true);

        if ((boolean)serverResponse.get_part("isValidUsername") && (boolean)serverResponse.get_part("isValidGmail")){

            save_account(username, password,gmail);

            serverResponse.add_part("isSuccessful", true);
        } else {
            serverResponse.add_part("isSuccessful", false);
        }

//        serverResponse.add_part("isValidUsername" , true);
//        serverResponse.add_part("isValidGamil" , true);
//        serverResponse.add_part("isSuccessful", true);

        return serverResponse;
    }

    public static ServerResponse login(String username , String password, int request_id){
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        serverResponse.add_part("isSuccessful" , check_username_exists(username) && is_password_correct(username, password));
        return serverResponse;
    }

    public static void main(String[] args) {
        if(check_username_exists("ali")){
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }



}


//add comment like/dislike tables
//create a default channel with name of uesr
//watch later for each user
//

