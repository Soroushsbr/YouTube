package espresso.youtube.models.playlist;

import com.fasterxml.jackson.annotation.JsonFormat;
import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.video.Video;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Playlist extends ClassInfo {
    private String title, description, channel_id, channel_name, id, user_id;
    private String playlist_id;
    private boolean is_public;
    private int n_posts;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp created_at;
    private ArrayList<Video> videos = new ArrayList<>();
    public Playlist(){
        super.className = "playlist";
    }

    //------------------ setters -----------------------
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }
    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public void setN_posts(int n_posts) {
        this.n_posts = n_posts;
    }
    public void setIs_public(boolean is_public) {this.is_public = is_public;}
    public void setCreated_at(Timestamp created_at) {this.created_at = created_at;}

    public void setPlaylist_id(String playlist_id) {
        this.playlist_id = playlist_id;
    }

    //------------------ getters -----------------------
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getChannel_id() {
        return channel_id;
    }
    public String getId() {
        return id;
    }
    public String getChannel_name() {
        return channel_name;
    }
    public ArrayList<Video> getVideos() {
        return videos;
    }
    public String getUser_id() {
        return user_id;
    }
    public int getN_posts() {
        return n_posts;
    }
    public boolean getIs_public() {return is_public;}
    public Timestamp getCreated_at() {return created_at;}

    public String getPlaylist_id() {
        return playlist_id;
    }
}