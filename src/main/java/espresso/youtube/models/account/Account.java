package espresso.youtube.models.account;

import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.channel.Channel;

import java.util.ArrayList;

public class Account extends ClassInfo {
    private String username;
    private String gmail;
    private String password;
    private String name;
    private String id;
    private ArrayList<Channel> followed_channels = new ArrayList<>();
    public Account(){
        super.className = "account";
    }

    //------------------ setters -----------------------
    public void setId(String id) {
        this.id = id;
    }
    public void setGmail(String gmail) {
        this.gmail = gmail;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setFollowed_channels(ArrayList<Channel> followed_channels) {
        this.followed_channels = followed_channels;
    }

    //------------------ getters -----------------------
    public String getId() {
        return id;
    }
    public String getGmail() {
        return gmail;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public ArrayList<Channel> getFollowed_channels() {
        return followed_channels;
    }
}