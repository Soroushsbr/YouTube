package espresso.youtube.models.account;

import espresso.youtube.models.ClassInfo;

public class Account extends ClassInfo {
    private String username;
    private String password;
    private String name;
    private int id;

    public Account(){
        super.className = "account";
    }

    public void setId(int id) {
        this.id = id;
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

    public int getId() {
        return id;
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