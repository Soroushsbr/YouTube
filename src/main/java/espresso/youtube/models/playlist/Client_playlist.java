package espresso.youtube.models.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.models.video.Video;
import javafx.css.StyleableStringProperty;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Client_playlist {
    private Playlist playlist = new Playlist();
    private DataOutputStream out;
    private ObjectMapper mapper = new ObjectMapper();
    private String jsonString;

    public Client_playlist(DataOutputStream out){
        this.out = out;
    }

    public void create_playlist(String title, String description, boolean is_public, ArrayList<Video> videos, int request_id){
        playlist.setRequest("create_playlist");
        playlist.setRequest_id(request_id);
        playlist.setVideos(videos);
        playlist.setTitle(title);
        playlist.setDescription(description);
        playlist.setIs_public(is_public);
        send_request();
    }
    public void change_playlist_info(String playlist_id, String new_title, String new_description, boolean is_public, int request_id){
        playlist.setRequest("change_playlist_info");
        playlist.setRequest_id(request_id);
        playlist.setTitle(new_title);
        playlist.setDescription(new_description);
        playlist.setId(playlist_id);
        playlist.setIs_public(is_public);
        send_request();
    }
    public void add_video(String playlist_id, ArrayList<Video> videos, int request_id){
        playlist.setRequest("add_video");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        playlist.setVideos(videos);
        send_request();
    }
    public void remove_video(String playlist_id, ArrayList<Video> videos, int request_id){
        playlist.setRequest("remove_video");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        playlist.setVideos(videos);
        send_request();
    }
    public void make_playlist_public(String playlist_id, int request_id){
        playlist.setRequest("make_playlist_public");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        send_request();
    }
    public void make_playlist_private(String playlist_id, int request_id){
        playlist.setRequest("make_playlist_private");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        send_request();
    }
    public void number_of_posts(String playlist_id, int request_id){
        playlist.setRequest("number_of_posts");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        send_request();
    }
    public void get_info(String playlist_id, int request_id){
        playlist.setRequest("get_info");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        send_request();
    }
    public void delete_playlist(String playlist_id, String user_id, int request_id){
        playlist.setRequest("delete_playlist");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        playlist.setUser_id(user_id);
        send_request();
    }
    public void get_playlists_of_account(String user_id, int request_id){
        playlist.setRequest("get_playlists_of_account");
        playlist.setRequest_id(request_id);
        playlist.setUser_id(user_id);
        send_request();
    }
    public void get_all_posts_of_a_playlist(String playlist_id, int request_id){
        playlist.setRequest("get_all_posts_of_a_playlist");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        send_request();
    }
    public void save_playlist(String playlist_id, String user_id, int request_id){
        playlist.setRequest("save_playlist");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        playlist.setUser_id(user_id);
        send_request();
    }
    public void unSave_playlist(String playlist_id, String user_id, int request_id){
        playlist.setRequest("unSave_playlist");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        playlist.setUser_id(user_id);
        send_request();
    }
    public void check_if_user_saved_playlist(String playlist_id, String user_id, int request_id){
        playlist.setRequest("check_if_user_saved_playlist");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        playlist.setUser_id(user_id);
        send_request();
    }


    private void send_request(){
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(playlist);
            out.writeUTF(jsonString);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            playlist = new Playlist();
        }
    }

}