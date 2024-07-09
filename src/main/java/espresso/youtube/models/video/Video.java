package espresso.youtube.models.video;


import com.fasterxml.jackson.annotation.JsonFormat;
import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.channel.Channel;

import java.sql.Timestamp;

public class Video extends ClassInfo {
    private String title, media_type, thumbnail_id;
    private String description;
    private Channel channel = new Channel();
    private String video_id;
    private String owner_id;
    private String data_type;
    private boolean is_public;
    private boolean is_short;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp created_at;
    private int views, n_likes, length, n_comments;
    private boolean watched;
    public Video(){
        super.className = "video";
    }

    //------------------ setters -----------------------
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }
    public void setData_type(String data_type) {
        this.data_type = data_type;
    }
    public void setViews(int views) {
        this.views = views;
    }
    public void setN_likes(int n_likes) {
        this.n_likes = n_likes;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    public void setN_comments(int n_comments) {
        this.n_comments = n_comments;
    }
    public void setIs_public(boolean is_public) {this.is_public = is_public;}
    public void setIs_short(boolean is_short) {this.is_short = is_short;}
    public void setCreated_at(Timestamp created_at) {this.created_at = created_at;}
    public void setMedia_type(String media_type) {
        this.media_type = media_type;

    }
    public void setThumbnail_id(String thumbnail_id) {
        this.thumbnail_id = thumbnail_id;
    }
    public void setWatched(boolean watched){
        this.watched = watched;

    }
    public void setThumbnail_id(String thumbnail_id) {
        this.thumbnail_id = thumbnail_id;
    }
    public void setWatched(boolean watched){this.watched = watched;}

    //------------------ getters -----------------------
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getVideo_id() {
        return video_id;
    }
    public String getOwner_id() {
        return owner_id;
    }
    public String getData_type() {
        return data_type;
    }
    public int getViews() {
        return views;
    }
    public int getN_likes() {
        return n_likes;
    }
    public int getLength() {
        return length;
    }
    public Channel getChannel() {
        return channel;
    }
    public int getN_comments() {
        return n_comments;
    }
    public boolean getIs_public() {return is_public;}
    public boolean getIs_short() {return is_short;}
    public Timestamp getCreated_at() {return created_at;}
    public String getMedia_type() {
        return media_type;
    }
    public String getThumbnail_id() {
        return thumbnail_id;
    }
    public boolean getWatched(){
        return watched;
    }

}
