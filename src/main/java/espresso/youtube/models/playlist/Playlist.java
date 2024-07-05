package espresso.youtube.models.playlist;

import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.video.Video;

import java.util.ArrayList;

public class Playlist extends ClassInfo {
    private String title, description, channel_id, id;
    private ArrayList<String> video_ids = new ArrayList<>();
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
    public void setVideo_ids(ArrayList<String> video_ids) {
        this.video_ids = video_ids;
    }
    public void setId(String id) {
        this.id = id;
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
    public ArrayList<String> getVideo_ids() {
        return video_ids;
    }
    public String getId() {
        return id;
    }
}