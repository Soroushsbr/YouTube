package espresso.youtube.models.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import espresso.youtube.models.ClassInfo;

import java.time.LocalDateTime;

public class Notification {

    private String channel_id;
    private String post_id;
    private String comment_id;
    private String user_id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    private String title;
    private boolean have_seen;
    
    //------------------ setters -----------------------
    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public void setTitle(String message) {
        this.title = message;
    }
    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public void setHave_seen(boolean have_seen) {this.have_seen = have_seen;}

    //------------------ getters -----------------------
    public String getChannel_id() {
        return channel_id;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public String getTitle() {
        return title;
    }
    public String getPost_id() {
        return post_id;
    }
    public String getComment_id() {
        return comment_id;
    }
    public String getUser_id() {
        return user_id;
    }
    public boolean getHave_seen() {return have_seen;}


}