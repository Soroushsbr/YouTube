package espresso.youtube.models.comment;

import espresso.youtube.DataBase.Utilities.Comment_DB;
import espresso.youtube.DataBase.Utilities.Post_DB;
import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.ServerResponse;

import java.util.UUID;

public class Server_comment extends Comment {
    @Override
    public ServerResponse handle_request() {
        super.handle_request();

        if (request.equals("put_comment")) {
            return  put_comment();
        } else if (request.equals("reply_comment")) {
            return reply_comment();
        } else if (request.equals("edit_comment")) {
            return edit_comment();
        } else if (request.equals("delete_comment")) {
            return delete_comment();
        } else if (request.equals("load_comments")) {
            return load_comments();
        } else if (request.equals("like_comment")) {
            return like_comment();
        } else if (request.equals("dislike_comment")) {
            return dislike_comment();
        } else if (request.equals("check_user_likes_comment")) {
            return check_user_likes_comment();
        } else if (request.equals("check_user_dislikes_comment")) {
            return check_user_dislikes_comment();
        } else if (request.equals("number_of_likes")) {
            return number_of_likes();
        } else if (request.equals("number_of_dislikes")) {
            return number_of_dislikes();
        } else if (request.equals("get_info")) {
            return get_info();
        } else if (request.equals("remove_user_like_from_comment")) {
            return remove_user_like_from_comment();
        } else if (request.equals("remove_user_dislike_from_comment")) {
            return remove_user_dislike_from_comment();
        }
        return null;
    }
    private ServerResponse check_user_likes_comment(){
        return Comment_DB.check_user_likes_comment(UUID.fromString(super.getComment_id()), UUID.fromString(super.getUser_id()), super.getRequest_id() );
    }
    private ServerResponse check_user_dislikes_comment(){
        return Comment_DB.check_user_dislikes_comment(UUID.fromString(super.getComment_id()), UUID.fromString(super.getUser_id()), super.getRequest_id() );
    }
    private ServerResponse number_of_likes(){
        return Comment_DB.number_of_likes(UUID.fromString(super.getComment_id()), super.getRequest_id());
    }
    private ServerResponse number_of_dislikes(){
        return Comment_DB.number_of_dislikes(UUID.fromString(super.getComment_id()), super.getRequest_id());
    }
    private ServerResponse get_info(){
        return Comment_DB.get_info(UUID.fromString(super.getComment_id()), super.getRequest_id());
    }
    private ServerResponse like_comment(){
        ServerResponse sr = Comment_DB.get_info(UUID.fromString(super.getComment_id()), 0);

        notification.like_comment(super.getUser_id(), super.getComment_id(), (String) sr.get_part("owner_id"));
        return Comment_DB.like_comment(UUID.fromString(super.getComment_id()), UUID.fromString(super.getUser_id()), super.getRequest_id());
    }
    private ServerResponse dislike_comment(){
        return Comment_DB.dislike_comment(UUID.fromString(super.getComment_id()), UUID.fromString(super.getUser_id()), super.getRequest_id());
    }
    private ServerResponse remove_user_like_from_comment(){
        return Comment_DB.remove_user_like_from_comment(UUID.fromString(super.getComment_id()), UUID.fromString(super.getUser_id()), super.getRequest_id());
    }
    private ServerResponse remove_user_dislike_from_comment(){
        return Comment_DB.remove_user_dislike_from_comment(UUID.fromString(super.getComment_id()), UUID.fromString(super.getUser_id()), super.getRequest_id());
    }
    private ServerResponse load_comments(){
        return Comment_DB.get_all_comments_of_a_post(UUID.fromString(super.getPost_id()), super.getRequest_id());
    }
    private ServerResponse put_comment(){
        ServerResponse sr = Comment_DB.add_comment(UUID.fromString(super.getUser_id()), UUID.fromString(super.getPost_id()), super.getMessage(), super.getRequest_id());
        ServerResponse sr2 = Post_DB.get_info(UUID.fromString(super.getPost_id()), 0);

        if((boolean) sr.get_part("isSuccessful"))
            super.notification.put_comment(super.getUser_id(), (String) sr.get_part("comment_id"), (String) sr2.get_part("owner_id"));
        return sr;
    }
    private ServerResponse reply_comment(){
        ServerResponse sr = Comment_DB.reply_to_comment(UUID.fromString(super.getUser_id()), UUID.fromString(super.getPost_id()), super.getMessage(), UUID.fromString(super.getParent_comment_id()), super.getRequest_id());

        if((boolean) sr.get_part("isSuccessful"))
            super.notification.reply_comment(super.getUser_id(), (String) sr.get_part("comment_id"), super.getParent_comment_id());
        return sr;
    }
    private ServerResponse edit_comment(){
        return Comment_DB.edit_comment(UUID.fromString(super.getComment_id()), super.getMessage(), super.getRequest_id());
    }
    private ServerResponse delete_comment(){
        return Comment_DB.delete_comment(UUID.fromString(super.getComment_id()), super.getRequest_id());
    }

}
