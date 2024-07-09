package espresso.youtube.DataBase.Utilities;

import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.channel.Channel;
import espresso.youtube.models.playlist.Playlist;
import espresso.youtube.models.video.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.*;


public class Search {
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

    public static ArrayList<Channel> search_in_channels(String text) {
        ArrayList<Channel> channels = new ArrayList<>();
        String query = "SELECT id, title, username, owner_id, description FROM channels WHERE lower(title) LIKE ?";
        text = "%" + text.toLowerCase() + "%";

        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, text);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Channel channel = new Channel();
                    channel.setId(resultSet.getString("id"));
                    channel.setName(resultSet.getString("title"));
                    channel.setUsername(resultSet.getString("username"));
                    channel.setOwner_id(resultSet.getString("owner_id"));
                    channel.setDescription(resultSet.getString("description"));
                    channels.add(channel);
                }
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
        return channels;
    }
    public static ServerResponse search_titles(String text, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<String> titles = new ArrayList<>();
        String query = "SELECT title FROM channels WHERE title LIKE ? UNION SELECT title FROM posts WHERE title LIKE ? UNION SELECT title FROM playlists WHERE title LIKE ?";

        try (Connection connection = create_connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String searchPattern = "%" + text + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            preparedStatement.setString(3, searchPattern);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    titles.add(resultSet.getString("title"));
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        serverResponse.add_part("titles", titles);
        return serverResponse;
    }

    public static ArrayList<Playlist> search_in_playlists(String text) {
        ArrayList<Playlist> playlists = new ArrayList<>();
        String query = "SELECT id, title, owner_id, description, is_public FROM playlists WHERE lower(title) LIKE ?";
        text = "%" + text.toLowerCase() + "%";

        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, text);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Playlist playlist = new Playlist();
                    playlist.setId(resultSet.getString("id"));
                    playlist.setTitle(resultSet.getString("title"));
                    playlist.setUser_id(resultSet.getString("owner_id"));
                    playlist.setDescription(resultSet.getString("description"));
                    playlist.setIs_public(resultSet.getBoolean("is_public"));
                    playlists.add(playlist);
                }
            }

        } catch (SQLException e) {
            printSQLException(e);
        }

        return playlists;
    }

    public static ArrayList<Video> search_in_posts(String text) {
        ArrayList<Video> posts = new ArrayList<>();
        String query = "SELECT id, title, owner_id, channel_id, description, is_public, is_short, video_length FROM posts WHERE lower(title) LIKE ?";
        text = "%" + text.toLowerCase() + "%";

        try (Connection connection = create_connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, text);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Video post = new Video();
                    post.setVideo_id(resultSet.getString("id"));
                    post.setTitle(resultSet.getString("title"));
                    post.setOwner_id(resultSet.getString("owner_id"));
                    post.getChannel().setId(resultSet.getString("channel_id"));
                    post.setDescription(resultSet.getString("description"));
                    post.setIs_public(resultSet.getBoolean("is_public"));
                    post.setIs_short(resultSet.getBoolean("is_short"));
                    post.setLength(resultSet.getInt("video_length"));
                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }

        return posts;
    }
    //+++
    public static ServerResponse search(String text, int request_id){
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        serverResponse.setVideos_list(search_in_posts(text));
        serverResponse.setPlaylists_list(search_in_playlists(text));
        serverResponse.setChannels_list(search_in_channels(text));
        return serverResponse;
    }


}
