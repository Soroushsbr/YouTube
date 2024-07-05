package espresso.youtube.models.comment;

import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.ServerResponse;

public class Server_comment extends ClassInfo {
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
        }
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
