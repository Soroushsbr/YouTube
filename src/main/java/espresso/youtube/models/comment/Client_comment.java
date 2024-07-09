package espresso.youtube.models.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.DataBase.Utilities.Comment_DB;
import espresso.youtube.models.ServerResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Client_comment {
    private Comment comment = new Comment();
    private DataOutputStream out;
    private ObjectMapper mapper = new ObjectMapper();
    private String jsonString;

    public Client_comment(DataOutputStream out){
        this.out = out;
    }

    public void put_comment(String message, String user_id, String post_id, int request_id){
        comment.setRequest("put_comment");
        comment.setRequest_id(request_id);
        comment.setMessage(message);
        comment.setUser_id(user_id);
        comment.setPost_id(post_id);
        send_request();
    }
    public void reply_comment(String message, String user_id, String post_id, String comment_id, int request_id){
        comment.setRequest("reply_comment");
        comment.setRequest_id(request_id);
        comment.setMessage(message);
        comment.setPost_id(post_id);
        comment.setUser_id(user_id);
        comment.setParent_comment_id(comment_id);
        send_request();
    }
    public void edit_comment(String new_message, String user_id, String comment_id, int request_id){
        comment.setRequest("edit_comment");
        comment.setRequest_id(request_id);
        comment.setComment_id(comment_id);
        comment.setUser_id(user_id);
        comment.setMessage(new_message);
        send_request();
    }
    public void delete_comment(String comment_id, String user_id, int request_id){
        comment.setRequest("delete_comment");
        comment.setRequest_id(request_id);
        comment.setComment_id(comment_id);
        comment.setUser_id(user_id);
        send_request();
    }
    public void load_comments(String post_id, int request_id){
        comment.setRequest("load_comments");
        comment.setRequest_id(request_id);
        comment.setPost_id(post_id);
        send_request();
    }
    public void like_comment(String comment_id, String user_id, int request_id){
        comment.setRequest("like_comment");
        comment.setRequest_id(request_id);
        comment.setComment_id(comment_id);
        comment.setUser_id(user_id);
        send_request();
    }
    public void dislike_comment(String comment_id, String user_id, int request_id){
        comment.setRequest("dislike_comment");
        comment.setRequest_id(request_id);
        comment.setComment_id(comment_id);
        comment.setUser_id(user_id);
        send_request();
    }
    public void remove_user_like_from_comment(String comment_id, String user_id, int request_id){
        comment.setRequest("remove_user_like_from_comment");
        comment.setRequest_id(request_id);
        comment.setComment_id(comment_id);
        comment.setUser_id(user_id);
        send_request();
    }
    public void remove_user_dislike_from_comment(String comment_id, String user_id, int request_id){
        comment.setRequest("remove_user_dislike_from_comment");
        comment.setRequest_id(request_id);
        comment.setComment_id(comment_id);
        comment.setUser_id(user_id);
        send_request();
    }
    public void check_user_likes_comment(String comment_id, String user_id, int request_id){
        comment.setRequest("check_user_likes_comment");
        comment.setRequest_id(request_id);
        comment.setComment_id(comment_id);
        comment.setUser_id(user_id);
        send_request();
    }
    public void check_user_dislikes_comment(String comment_id, String user_id, int request_id){
        comment.setRequest("check_user_dislikes_comment");
        comment.setRequest_id(request_id);
        comment.setComment_id(comment_id);
        comment.setUser_id(user_id);
        send_request();
    }
    public void number_of_likes(String comment_id, int request_id){
        comment.setRequest("number_of_likes");
        comment.setRequest_id(request_id);
        comment.setComment_id(comment_id);
        send_request();
    }
    public void number_of_dislikes(String comment_id, int request_id){
        comment.setRequest("number_of_dislikes");
        comment.setRequest_id(request_id);
        comment.setComment_id(comment_id);
        send_request();
    }
    public void get_info(String comment_id, int request_id){
        comment.setRequest("get_info");
        comment.setRequest_id(request_id);
        comment.setComment_id(comment_id);
        send_request();
    }


    private void send_request(){
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(comment);
            out.writeUTF(jsonString);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            comment = new Comment();
        }
    }

}