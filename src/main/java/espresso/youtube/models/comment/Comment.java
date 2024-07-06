package espresso.youtube.models.comment;

import espresso.youtube.models.ClassInfo;

public class Comment extends ClassInfo {
    private String post_id, message, comment_id, user_id, username;
    private int n_likes;
    public Comment(){
        super.className = "comment";
    }
    //------------------ setters -----------------------
    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setN_likes(int n_likes) {
        this.n_likes = n_likes;
    }

    //------------------ getters -----------------------
    public String getPost_id() {
        return post_id;
    }
    public String getMessage() {
        return message;
    }
    public String getComment_id() {
        return comment_id;
    }
    public String getUser_id() {
        return user_id;
    }
    public String getUsername() {
        return username;
    }
    public int getN_likes() {
        return n_likes;
    }
}