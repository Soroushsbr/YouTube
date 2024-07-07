package espresso.youtube.models.channel;

import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.account.Account;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Channel extends ClassInfo {
    private String id;
    private String name;
    private String owner_id;
    private ArrayList<Account> followers = new ArrayList<>();
    private String description;
    private int n_posts;
    private String username;
    private Timestamp created_at;
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
    public void setFollowers(ArrayList<Account> followers) {
        this.followers = followers;
    }
    public void setN_posts(int n_posts) {
        this.n_posts = n_posts;
    }
    public void setUsername(String username) {this.username = username;}
    public Timestamp getCreated_at() {return created_at;}
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
    public ArrayList<Account> getFollowers() {
        return followers;
    }
    public int getN_posts() {
        return n_posts;
    }
    public String getUsername() {return username;}
    public void setCreated_at(Timestamp created_at) {this.created_at = created_at;}
}