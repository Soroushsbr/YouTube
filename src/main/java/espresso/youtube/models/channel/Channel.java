package espresso.youtube.models.channel;

import espresso.youtube.models.ClassInfo;

public class Channel extends ClassInfo {
    private String id;
    private String name;
    private String owner_id;
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

}