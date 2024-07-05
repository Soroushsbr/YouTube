package espresso.youtube.models.comment;

import espresso.youtube.models.ClassInfo;

public class Comment extends ClassInfo {
    private String post_id, message, comment_id;

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

}