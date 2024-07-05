package espresso.youtube.models.channel;

import espresso.youtube.models.ClassInfo;

import java.util.ArrayList;

public class Channel extends ClassInfo {
    private String id;
    private String name;
    private String owner_id;
    private ArrayList<String> subscriber_id = new ArrayList<>();
    private String description;
    public Channel(){
        super.className = "channel";
    }

    //------------------ setters -----------------------

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setSubscriber_id(ArrayList<String> subscriber_id) {
        this.subscriber_id = subscriber_id;
    }
    //------------------ getters -----------------------

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getOwner_id() {
        return owner_id;
    }
    public String getDescription() {
        return description;
    }
    public ArrayList<String> getSubscriber_id() {
        return subscriber_id;
    }
}