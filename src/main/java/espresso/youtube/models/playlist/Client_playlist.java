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

    public void create_playlist(String title, String description, ArrayList<String> video_ids, int request_id){
        playlist.setRequest("create_playlist");
        playlist.setRequest_id(request_id);
        playlist.setVideo_ids(video_ids);
        playlist.setTitle(title);
        playlist.setDescription(description);
        send_request();
    }
    public void change_playlist_info(String playlist_id, String new_title, String new_description, int request_id){
        playlist.setRequest("change_playlist_info");
        playlist.setRequest_id(request_id);
        playlist.setTitle(new_title);
        playlist.setDescription(new_description);
        playlist.setId(playlist_id);
        send_request();
    }
    public void add_video(String playlist_id, ArrayList<String> video_ids, int request_id){
        playlist.setRequest("add_video");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        playlist.setVideo_ids(video_ids);
        send_request();
    }
    public void remove_video(String playlist_id, ArrayList<String> video_ids, int request_id){
        playlist.setRequest("remove_video");
        playlist.setRequest_id(request_id);
        playlist.setId(playlist_id);
        playlist.setVideo_ids(video_ids);
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