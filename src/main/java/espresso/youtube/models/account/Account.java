package espresso.youtube.models.account;

import espresso.youtube.models.ClassInfo;

public class Account extends ClassInfo {
    private String username;
    private String gmail;
    private String password;
    private String name;
    private String id;
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
}