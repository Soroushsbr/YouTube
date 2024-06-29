package espresso.youtube.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.models.account.Account;
import espresso.youtube.models.channel.Channel;
import espresso.youtube.models.comment.Comment;
import espresso.youtube.models.notification.Notification;
import espresso.youtube.models.playlist.Playlist;
import espresso.youtube.models.video.Video;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerResponse {
//------------------ jackson informations -------------------
    @JsonProperty("response_part")
    private HashMap<String, Object> response_parts = new HashMap<>();
    @JsonProperty("request_id")
    private int request_id = -1;
    @JsonProperty("accounts_list")
    private ArrayList<Account> accounts_list;
    @JsonProperty("channels_list")
    private ArrayList<Channel> channels_list;
    @JsonProperty("comments_list")
    private ArrayList<Comment> comments_list;
    @JsonProperty("notifications_list")
    private ArrayList<Notification> notifications_list;
    @JsonProperty("playlists_list")
    private ArrayList<Playlist> playlists_list;
    @JsonProperty("videos_list")
    private ArrayList<Video> videos_list;

    public void update_request(String jsonString){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readerForUpdating(this).readValue(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void add_part(String name, Object object){
        response_parts.put(name, object);
    }
    public Object get_part(String name){
        return response_parts.get(name);
    }
    public void set_part(String name, Object object){
        response_parts.replace(name, object);
    }
    public void delete_part(String name){
        response_parts.remove(name);
    }
    public void delete_all_parts(){
        response_parts.clear();
    }
//------------------------ getters -----------------------------

    public HashMap<String, Object> getResponse_parts() {
        return response_parts;
    }
    public int getRequest_id(){
        return request_id;
    }
    public ArrayList<Account> getAccounts_list() {
        return accounts_list;
    }
    public ArrayList<Channel> getChannels_list() {
        return channels_list;
    }
    public ArrayList<Comment> getComments_list() {
        return comments_list;
    }
    public ArrayList<Notification> getNotifications_list() {
        return notifications_list;
    }
    public ArrayList<Playlist> getPlaylists_list() {
        return playlists_list;
    }
    public ArrayList<Video> getVideos_list() {
        return videos_list;
    }

//------------------------ setters -----------------------------

    public void setResponse_parts(HashMap<String, Object> response_parts) {
        this.response_parts = response_parts;
    }
    public void setRequest_id(int request_id){
        this.request_id = request_id;
    }
    public void setAccounts_list(ArrayList<Account> accounts_list) {
        this.accounts_list = accounts_list;
    }
    public void setChannels_list(ArrayList<Channel> channels_list) {
        this.channels_list = channels_list;
    }
    public void setComments_list(ArrayList<Comment> comments_list) {
        this.comments_list = comments_list;
    }
    public void setNotifications_list(ArrayList<Notification> notifications_list) {
        this.notifications_list = notifications_list;
    }
    public void setPlaylists_list(ArrayList<Playlist> playlists_list) {
        this.playlists_list = playlists_list;
    }
    public void setVideos_list(ArrayList<Video> videos_list) {
        this.videos_list = videos_list;
    }
}