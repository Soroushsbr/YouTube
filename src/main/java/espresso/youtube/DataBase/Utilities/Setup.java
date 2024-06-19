package espresso.youtube.DataBase.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Setup {
    public static void create_database() {
        String query = "CREATE DATABASE youtube;";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", "postgres", "123");Statement statement = connection.createStatement()){
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while creating database",e);
        }
    }
    public static void create_tables() {
        String[] queries = {
                "CREATE TABLE IF NOT EXISTS accounts (id UUID PRIMARY KEY, username TEXT, gmail TEXT, password TEXT, dark_mode BOOLEAN, is_premium BOOLEAN)",
                "CREATE TABLE IF NOT EXISTS channels (id UUID PRIMARY KEY, title TEXT, owner_id UUID, description TEXT)",
                "CREATE TABLE IF NOT EXISTS playlists (id UUID PRIMARY KEY, title TEXT, owner_id UUID, is_public BOOLEAN)",
                "CREATE TABLE IF NOT EXISTS posts (id UUID PRIMARY KEY, title TEXT, owner_id UUID, channel_id UUID, is_public BOOLEAN)",
                "CREATE TABLE IF NOT EXISTS playlist_posts (playlist_id UUID, post_id UUID)",
                "CREATE TABLE IF NOT EXISTS channel_subscription (channel_id uuid, subscriber_id uuid)",
                "CREATE TABLE IF NOT EXISTS playlist_subscription (playlist_id uuid, subscriber_id uuid)",
                "CREATE TABLE IF NOT EXISTS likes (post_id uuid, user_id uuid)",
                "CREATE TABLE IF NOT EXISTS dislikes (post_id uuid, user_id uuid)",
                "CREATE TABLE IF NOT EXISTS comments (id uuid PRIMARY KEY, owner_id uuid, content text, post_id uuid)",
                "CREATE TABLE IF NOT EXISTS replies (id uuid PRIMARY KEY, content text, owner_id uuid, super_comment_id uuid)",
                "CREATE TABLE IF NOT EXISTS categories (id uuid PRIMARY KEY, category_name text)",
                "CREATE TABLE IF NOT EXISTS notifications (user_id uuid , content text)"

        };
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/youtube", "postgres", "123");Statement statement = connection.createStatement()){
            connection.setAutoCommit(false);
            for (String query : queries){
                statement.executeUpdate(query);
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while creating database tables",e);
        }
    }

    public static void restart_tables() {
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
            for (String query : queries)
                statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while removing database tables",e);
        }
        create_tables();
    }

    public static void main(String[] args) {

    }
}
