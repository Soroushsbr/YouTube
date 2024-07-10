package espresso.youtube.models.notification;

import espresso.youtube.Server.Client_Handler;
import espresso.youtube.models.ServerResponse;

import javax.accessibility.AccessibleComponent;
import java.util.ArrayList;
import java.util.HashMap;

public class Server_notification{
    private ServerResponse serverResponse = new ServerResponse();
    private Notification notification = new Notification();
    private HashMap<String, Client_Handler> online_clients = new HashMap<>();
    public Server_notification(HashMap<String, Client_Handler> online_clients){
        serverResponse.setResponse_type("notification");
        this.online_clients = online_clients;
    }
    public void upload_post(ArrayList<String> user_ids, String post_id){
        notification.setTitle("upload_post");
        notification.setPost_id(post_id);
        serverResponse.getNotifications_list().add(notification);

        for(String user_id : user_ids){
            if(online_clients.get(user_id) != null){
                online_clients.get(user_id).send_response(serverResponse);
            }
        }
    }
    public void like_post(String user_id, String post_id, String post_owner_id){
        notification.setTitle("like_post");
        notification.setPost_id(post_id);
        notification.setUser_id(user_id);
        serverResponse.getNotifications_list().add(notification);


        if(online_clients.get(post_owner_id) != null)
            online_clients.get(post_owner_id).send_response(serverResponse);
    }
    public void like_comment(String user_id, String comment_id, String comment_owner_id){
        notification.setTitle("like_comment");
        notification.setComment_id(comment_id);
        notification.setUser_id(user_id);
        serverResponse.getNotifications_list().add(notification);

        if(online_clients.get(comment_owner_id) != null)
            online_clients.get(comment_owner_id).send_response(serverResponse);
    }
    public void put_comment(String user_id, String comment_id, String post_owner_id){
        notification.setTitle("put_comment");
        notification.setComment_id(comment_id);
        notification.setUser_id(user_id);
        serverResponse.getNotifications_list().add(notification);

        if(online_clients.get(post_owner_id) != null)
            online_clients.get(post_owner_id).send_response(serverResponse);
    }
    public void reply_comment(String user_id, String comment_id, String comment_owner_id){
        notification.setTitle("reply_comment");
        notification.setComment_id(comment_id);
        notification.setUser_id(user_id);
        serverResponse.getNotifications_list().add(notification);

        if(online_clients.get(comment_owner_id) != null)
            online_clients.get(comment_owner_id).send_response(serverResponse);

    }
}