package espresso.youtube.DataBase.Utilities;

import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.account.Account;
import espresso.youtube.models.channel.Channel;

import espresso.youtube.models.video.Video;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Post_DB {
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

    public static void add_post(UUID id, UUID owner_id, String title, UUID channel_id, String description, Boolean is_public, Boolean is_short, int video_length) {
        System.out.println("[DATABASE] User "+owner_id+" adding post to channel "+channel_id+" ...");
        String query = "INSERT INTO posts (id, name, owner_id, channel_id, description, is_public, is_short, video_length) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setObject(3, owner_id);
            preparedStatement.setObject(4, channel_id);
            preparedStatement.setString(5, description);
            preparedStatement.setBoolean(6, is_public);
            preparedStatement.setBoolean(7, is_short);
            preparedStatement.setInt(8, video_length);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static ServerResponse delete_post_from_playlist(ArrayList<Video> videos, UUID playlist_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "DELETE FROM playlist_posts WHERE post_id = ? AND playlist_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false); // Start transaction

            for (Video video : videos) {
                System.out.println("[DATABASE] Deleting post " + video.getVideo_id() + " from playlist " + playlist_id + "...");
                try {
                    preparedStatement.setObject(1, video.getVideo_id());
                    preparedStatement.setObject(2, playlist_id);
                    preparedStatement.executeUpdate();
                    System.out.println("[DATABASE] Done");
                } catch (SQLException e) {
                    serverResponse.add_part("isSuccessful", false); // Set failure if any deletion fails
                    printSQLException(e);
                }
            }

            connection.commit(); // Commit transaction after all deletions
            serverResponse.add_part("isSuccessful", true); // If no exceptions, consider operation successful

        } catch (SQLException e) {
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }

        return serverResponse;
    }

    public static ServerResponse add_post_to_playlist(ArrayList<Video> videos, UUID playlist_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "INSERT INTO playlist_posts (playlist_id, post_id) VALUES (?, ?)";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            for (Video video : videos) {
                System.out.println("[DATABASE] Adding post " + video.getVideo_id() + " to playlist " + playlist_id + "...");
                try {
                    preparedStatement.setObject(1, playlist_id);
                    preparedStatement.setObject(2, video.getVideo_id());
                    preparedStatement.executeUpdate();
                    System.out.println("[DATABASE] Done");
                } catch (SQLException e) {
                    serverResponse.add_part("isSuccessful", false); // Set failure if any insertion fails
                    printSQLException(e);
                }
            }

            connection.commit();
            serverResponse.add_part("isSuccessful", true); // If no exceptions, consider operation successful

        } catch (SQLException e) {
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse like_post(UUID post_id, UUID user_id, int request_id) {
        System.out.println("[DATABASE] User "+user_id+" liking post "+post_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "INSERT INTO post_likes (post_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
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

    public static ServerResponse dislike_post(UUID post_id, UUID user_id, int request_id) {
        System.out.println("[DATABASE] User "+user_id+" disliking post "+post_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "INSERT INTO post_dislikes (post_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
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

    public static ServerResponse check_user_likes_post(UUID post_id, UUID user_id, int request_id) {
        System.out.println("[DATABASE] Checking if user "+user_id+" has liked post "+post_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "SELECT EXISTS (SELECT 1 FROM post_likes WHERE user_id = ? AND post_id = ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    serverResponse.add_part("user_likes_post", resultSet.getBoolean(1));
                    System.out.println("[DATABASE] Done");
                } else {
                    return serverResponse;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse check_user_dislikes_post(UUID post_id, UUID user_id, int request_id) {
        System.out.println("[DATABASE] Checking if user "+user_id+" has disliked post "+post_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "SELECT EXISTS (SELECT 1 FROM post_likes WHERE user_id = ? AND post_id = ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    serverResponse.add_part("user_dislikes_post", resultSet.getBoolean(1));
                    System.out.println("[DATABASE] Done");
                } else {
                    return serverResponse;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse remove_user_like_from_post(UUID post_id, UUID user_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        System.out.println("[DATABASE] Removing like of user "+user_id+" from post "+post_id+" ...");
        String query = "DELETE FROM post_likes WHERE user_id = ? AND post_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
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

    public static ServerResponse remove_user_dislike_from_post(UUID post_id, UUID user_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        System.out.println("[DATABASE] Removing dislike of user "+user_id+" from post "+post_id+" ...");
        String query = "DELETE FROM post_dislikes WHERE user_id = ? AND post_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, user_id);
            preparedStatement.setObject(2, post_id);
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

    public static ServerResponse add_to_post_viewers(UUID post_id, UUID user_id, int request_id) {
        System.out.println("[DATABASE] Adding a viewer to post "+post_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "INSERT INTO views (post_id, user_id) VALUES (?, ?)";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
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

    public static ServerResponse check_if_user_viewed_post(UUID post_id, UUID user_id, int request_id) {
        System.out.println("[DATABASE] Checking if user "+user_id+" has viewed the post "+post_id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query = "SELECT EXISTS (SELECT 1 FROM views WHERE post_id = ? AND user_id = ? )";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, post_id);
            preparedStatement.setObject(2, user_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    serverResponse.add_part("user_viewed_post", resultSet.getBoolean(1));
                    System.out.println("[DATABASE] Done");
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static void restart_post_views(UUID post_id) {
        System.out.println("[DATABASE] Restarting views of post "+post_id+" ...");
        String query = "DELETE FROM views WHERE post_id = ?";
        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, post_id);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

//    public static void change_post_title(UUID post_id, String title) {
//        //check if user is owner of post??
//        System.out.println("[DATABASE] Changing title of post "+post_id+" to "+title+" ...");
//        String query = "UPDATE posts SET title = ? WHERE id = ?";
//        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
//            connection.setAutoCommit(false);
//            preparedStatement.setString(1, title);
//            preparedStatement.setObject(2, post_id);
//            preparedStatement.executeUpdate();
//            connection.commit();
//            System.out.println("[DATABASE] Done");
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
//    }
//
//    public static void change_post_description(UUID post_id, String description) {
//        //check if user is owner of post??
//        System.out.println("[DATABASE] Changing description of post "+post_id+" to "+description+" ...");
//        String query = "UPDATE posts SET description = ? WHERE id = ?";
//        try (Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
//            connection.setAutoCommit(false);
//            preparedStatement.setString(1, description);
//            preparedStatement.setObject(2, post_id);
//            preparedStatement.executeUpdate();
//            connection.commit();
//            System.out.println("[DATABASE] Done");
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
//    }

    public static ServerResponse number_of_views(UUID post_id, int request_id) {
        String query = "SELECT COUNT(*) AS row_count FROM views WHERE post_id = ?";
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, post_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    serverResponse.add_part("number_of_views", resultSet.getInt("row_count"));
                } else {
                    return serverResponse;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse number_of_likes(UUID post_id, int request_id) {
        String query = "SELECT COUNT(*) AS row_count FROM post_likes WHERE post_id = ?";
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, post_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    serverResponse.add_part("number_of_likes", resultSet.getInt("row_count"));
                } else {
                    return serverResponse;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse number_of_dislikes(UUID post_id, int request_id) {
        String query = "SELECT COUNT(*) AS row_count FROM post_dislikes WHERE post_id = ?";
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, post_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    serverResponse.add_part("number_of_dislikes", resultSet.getInt("row_count"));
                } else {
                    return serverResponse;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse number_of_comments(UUID post_id, int request_id) {
        String query = "SELECT COUNT(*) AS row_count FROM comments WHERE post_id = ?";
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, post_id);
            try(ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    serverResponse.add_part("number_of_comments", resultSet.getInt("row_count"));
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
        System.out.println("[DATABASE] Getting info of post "+id+" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        String query ="SELECT * FROM posts WHERE id = ?";
        try(Connection connection = create_connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1 , id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    serverResponse.add_part("id" , resultSet.getString("id"));
                    serverResponse.add_part("title" , resultSet.getString("title"));
                    serverResponse.add_part("owner_id" , resultSet.getString("owner_id"));
                    serverResponse.add_part("channel_id" , resultSet.getString("channel_id"));
                    serverResponse.add_part("description" , resultSet.getString("description"));
                    serverResponse.add_part("is_public" , resultSet.getString("is_public"));
                    serverResponse.add_part("is_short" , resultSet.getString("is_short"));
                    serverResponse.add_part("created_at" , resultSet.getString("created_at"));
                    serverResponse.add_part("video_length" , resultSet.getString("video_length"));
                }
            }
            System.out.println("[DATABASE] Done");
        }catch (SQLException e){
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse delete_post(UUID post_id, int request_id) {
        System.out.println("[DATABASE] Deleting post "+ post_id +" ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<String> queries = new ArrayList<>();
        queries.add("DELETE FROM post_likes WHERE post_id = ?");
        queries.add("DELETE FROM post_dislikes WHERE post_id = ?");
        queries.add("DELETE FROM comment_likes WHERE comment_id IN (SELECT id FROM comments WHERE post_id = ?)");
        queries.add("DELETE FROM comment_dislikes WHERE comment_id IN (SELECT id FROM comments WHERE post_id = ?)");
        queries.add("DELETE FROM comments WHERE post_id = ?");
        queries.add("DELETE FROM playlist_posts WHERE post_id = ?");
        queries.add("DELETE FROM posts WHERE id = ?");

        try (Connection connection = create_connection()) {
            connection.setAutoCommit(false);
            for (String query : queries) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setObject(1, post_id);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    serverResponse.add_part("isSuccessful", false);
                    connection.rollback();
                    printSQLException(e);
                }
            }
            serverResponse.add_part("isSuccessful", true);
            connection.commit();
            System.out.println("[DATABASE] Done");
        } catch (SQLException e) {
            serverResponse.add_part("isSuccessful", false);
            printSQLException(e);
        }
        return serverResponse;
    }

    public static ServerResponse get_all_posts(int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<Video> posts = new ArrayList<>();
        String query = "SELECT id, title, owner_id, channel_id, description, is_public, is_short, video_length, created_at FROM posts";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
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
                post.setCreated_at(resultSet.getTimestamp("created_at"));

                posts.add(post);
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
        serverResponse.setVideos_list(posts);
        return serverResponse;
    }
public static ServerResponse get_all_Posts_of_a_account(UUID account_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<Video> posts = new ArrayList<>();
        String query = "SELECT id, title, owner_id, channel_id, description, is_public, is_short, video_length, created_at FROM posts WHERE owner_id = ?";

        try (Connection connection = create_connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, account_id);

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
                    post.setCreated_at(resultSet.getTimestamp("created_at"));

                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        serverResponse.setVideos_list(posts);
        return serverResponse;
    }

    public static ServerResponse get_all_posts_of_channel(UUID channel_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<Video> posts = new ArrayList<>();
        String query = "SELECT p.id, p.title, p.owner_id, p.channel_id, p.description, p.is_public, p.is_short, p.video_length, p.created_at FROM posts p JOIN channels c ON p.channel_id = c.id WHERE p.channel_id = ?";

        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, channel_id);
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
                    post.setCreated_at(resultSet.getTimestamp("created_at"));
                    posts.add(post);
                }
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
        serverResponse.setVideos_list(posts);
        return serverResponse;
    }

    public static ServerResponse get_all_posts_of_a_playlist(UUID playlist_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<Video> posts = new ArrayList<>();

        String query = "SELECT p.id, p.title, p.owner_id, p.channel_id, p.description, p.is_public, p.is_short, p.video_length, p.created_at FROM posts p JOIN playlist_posts pp ON p.id = pp.post_id WHERE pp.playlist_id = ?";
        try (Connection connection = create_connection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, playlist_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Video post = new Video();

                    post.setVideo_id(resultSet.getString("id"));
                    post.setOwner_id(resultSet.getString("channel_id"));
                    post.getChannel().setId(resultSet.getString("channel_id"));
                    post.setTitle(resultSet.getString("title"));
                    post.setDescription(resultSet.getString("description"));
                    post.setIs_public(resultSet.getBoolean("is_public"));
                    post.setIs_short(resultSet.getBoolean("is_short"));
                    post.setLength(resultSet.getInt("video_length"));
                    post.setCreated_at(resultSet.getTimestamp("created_at"));

                    posts.add(post);
                }
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
        serverResponse.setVideos_list(posts);
        return serverResponse;
    }

    public static ServerResponse get_all_viewers_of_a_post(UUID post_id, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        ArrayList<Account> viewers = new ArrayList<>();

        String query = "SELECT a.id, a.username, a.gmail, a.dark_mode, a.is_premium, a.created_at FROM accounts a JOIN views v ON a.id = v.user_id WHERE v.post_id = ?";
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setObject(1, post_id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = new Account();
                    account.setId(resultSet.getString("id"));
                    account.setUsername(resultSet.getString("username"));
                    account.setGmail(resultSet.getString("gmail"));
                    account.setDark_mode(resultSet.getBoolean("dark_mode"));
                    account.setIs_premium(resultSet.getBoolean("is_premium"));
                    account.setCreated_at(resultSet.getTimestamp("created_at"));
                    viewers.add(account);
                }
            }

        } catch (SQLException e) {
            printSQLException(e);
        }
        serverResponse.setAccounts_list(viewers);
        return serverResponse;
    }
    ///+++
    public static ServerResponse change_post_info(UUID post_id, String title, String description, Boolean is_public, Boolean is_short, int video_length, int request_id) {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(request_id);
        StringBuilder queryBuilder = new StringBuilder("UPDATE posts SET ");

        boolean isFirst = true; // Used to manage commas in the query
        if (title != null) {
            queryBuilder.append(isFirst ? "" : ", ").append("title = ?");
            isFirst = false;
        }
        if (description != null) {
            queryBuilder.append(isFirst ? "" : ", ").append("description = ?");
            isFirst = false;
        }
        if (is_public != null) {
            queryBuilder.append(isFirst ? "" : ", ").append("is_public = ?");
            isFirst = false;
        }
        if (is_short != null) {
            queryBuilder.append(isFirst ? "" : ", ").append("is_short = ?");
            isFirst = false;
        }

        queryBuilder.append(isFirst ? "" : ", ").append("video_length = ?");
        queryBuilder.append(" WHERE id = ?");
        String query = queryBuilder.toString();
        try (Connection connection = create_connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int parameterIndex = 1;
            if (title != null) {
                preparedStatement.setString(parameterIndex++, title);
            }
            if (description != null) {
                preparedStatement.setString(parameterIndex++, description);
            }
            if (is_public != null) {
                preparedStatement.setBoolean(parameterIndex++, is_public);
            }
            if (is_short != null) {
                preparedStatement.setBoolean(parameterIndex++, is_short);
            }
            preparedStatement.setInt(parameterIndex++, video_length);
            preparedStatement.setObject(parameterIndex, post_id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Post with ID " + post_id + " updated successfully.");
                serverResponse.add_part("isSuccessful", true);
            } else {
                System.out.println("No post found with ID " + post_id);
                serverResponse.add_part("isSuccessful", false);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return serverResponse;
    }

    public static void main(String[] args) {

    }
}





