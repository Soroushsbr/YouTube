package espresso.youtube.models.video;


import espresso.youtube.models.ClassInfo;

public class Video extends ClassInfo {
    private String title;
    private String description;
    private String channel_id;
    private String video_id;
    private String owner_id;
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
    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }
    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
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
    public String getVideo_id() {
        return video_id;
    }
    public String getOwner_id() {
        return owner_id;
    }

}
