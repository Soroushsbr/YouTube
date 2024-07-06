package espresso.youtube.models.comment;

import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.ServerResponse;

public class Server_comment extends Comment {
    @Override
    public ServerResponse handle_request() {
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
        }
        return null;
    }
    private ServerResponse check_user_likes_comment(){
        return null;
    }
    private ServerResponse check_user_dislikes_comment(){
        return null;
    }
    private ServerResponse number_of_likes(){
        return null;
    }
    private ServerResponse number_of_dislikes(){
        return null;
    }
    private ServerResponse get_info(){
        return null;
    }
    private ServerResponse like_comment(){
        return null;
    }
    private ServerResponse dislike_comment(){
        return null;
    }
    private ServerResponse load_comments(){
        return null;
    }
    private ServerResponse put_comment(){
        return null;
    }
    private ServerResponse reply_comment(){
        return null;
    }
    private ServerResponse edit_comment(){
        return null;
    }
    private ServerResponse delete_comment(){
        return null;
    }

}
