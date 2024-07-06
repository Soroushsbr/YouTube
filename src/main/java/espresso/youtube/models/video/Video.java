package espresso.youtube.models.video;


import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.channel.Channel;

public class Video extends ClassInfo {
    private String title;
    private String description;
    private Channel channel = new Channel();
    private String video_id;
    private String owner_id;
    private String data_type;
    private int views, n_likes, length, n_comments;
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
}
