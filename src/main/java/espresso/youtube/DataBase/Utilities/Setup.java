package espresso.youtube.DataBase.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Setup {

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

    public static void create_database() {
        System.out.println("[DATABASE] Creating youtube database...");
        String query = "CREATE DATABASE youtube;";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "postgres", "123");Statement statement = connection.createStatement()){
            statement.executeUpdate(query);
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void create_tables() {
        System.out.println("[DATABASE] Creating database tables...");
        String[] queries = {
                "CREATE TABLE IF NOT EXISTS accounts (id UUID PRIMARY KEY, username TEXT, gmail TEXT, password TEXT, dark_mode BOOLEAN, is_premium BOOLEAN, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS channels (id UUID PRIMARY KEY, title TEXT, username TEXT, owner_id UUID, description TEXT, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS playlists (id UUID PRIMARY KEY, title TEXT, owner_id UUID, description TEXT, is_public BOOLEAN, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS posts (id UUID PRIMARY KEY, title TEXT, owner_id UUID, channel_id UUID, description TEXT, is_public BOOLEAN, is_short BOOLEAN, video_length INTEGER, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS playlist_posts (playlist_id UUID, post_id UUID)",
                "CREATE TABLE IF NOT EXISTS channel_subscription (channel_id uuid, subscriber_id uuid)",
                "CREATE TABLE IF NOT EXISTS saved_playlists (playlist_id uuid, user_id uuid)",
                "CREATE TABLE IF NOT EXISTS post_likes (post_id uuid, user_id uuid)",
                "CREATE TABLE IF NOT EXISTS post_dislikes (post_id uuid, user_id uuid)",
                "CREATE TABLE IF NOT EXISTS comment_likes (comment_id uuid, user_id uuid)",
                "CREATE TABLE IF NOT EXISTS comment_dislikes (comment_id uuid, user_id uuid)",
                "CREATE TABLE IF NOT EXISTS comments (id uuid PRIMARY KEY, owner_id uuid, content text, post_id uuid, parent_comment_id uuid, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS categories ( post_id uuid, category_name text)",
                "CREATE TABLE IF NOT EXISTS notifications (user_id uuid , title text, comment_id uuid, post_id uuid, channel_id uuid, have_seen BOOLEAN, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
                "CREATE TABLE IF NOT EXISTS views (user_id uuid , post_id uuid)"
        };
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/youtube", "postgres", "123");Statement statement = connection.createStatement()){
            connection.setAutoCommit(false);

            for (String query : queries){
                statement.executeUpdate(query);
            }
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void restart_tables() {
        System.out.println("[DATABASE] Restarting database tables...");
        System.out.println("Deleting database tables...");
        String[] queries = {
                "DROP TABLE IF EXISTS accounts",
                "DROP TABLE IF EXISTS channels",
                "DROP TABLE IF EXISTS playlists",
                "DROP TABLE IF EXISTS posts",
                "DROP TABLE IF EXISTS playlist_posts",
                "DROP TABLE IF EXISTS channel_subscription",
                "DROP TABLE IF EXISTS playlist_subscription",
                "DROP TABLE IF EXISTS likes",
                "DROP TABLE IF EXISTS dislikes",
                "DROP TABLE IF EXISTS comments",
                "DROP TABLE IF EXISTS replies",
                "DROP TABLE IF EXISTS categories",
                "DROP TABLE IF EXISTS notifications",
        };
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/youtube", "postgres", "123"); Statement statement = connection.createStatement()){
            connection.setAutoCommit(false);

            for (String query : queries) {
                statement.executeUpdate(query);
            }
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
        create_tables();
    }

    public static void main(String[] args) {
        restart_tables();
    }
}
