package espresso.youtube.models.account;

import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.channel.Channel;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Account extends ClassInfo {
    private String username;
    private String gmail;
    private String password;
    private String name;
    private String id;
    private boolean dark_mode;
    private boolean is_premium;
    private Timestamp created_at;
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
    public void setDark_mode(boolean dark_mode) {this.dark_mode = dark_mode;}
    public void setIs_premium(boolean is_premium) {this.is_premium = is_premium;}
    public void setCreated_at(Timestamp created_at) {this.created_at = created_at;}
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
    public boolean getDark_mode() {return dark_mode;}
    public boolean getIs_premium() {return is_premium;}
    public Timestamp getCreated_at() {return created_at;}
}