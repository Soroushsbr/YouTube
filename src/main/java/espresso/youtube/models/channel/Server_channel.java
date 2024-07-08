package espresso.youtube.models.channel;

import espresso.youtube.DataBase.Utilities.Account_DB;
import espresso.youtube.DataBase.Utilities.Channel_DB;
import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.ServerResponse;

import java.util.UUID;

public class Server_channel extends Channel {
    @Override
    public ServerResponse handle_request() {
        super.handle_request();

        if (request.equals("subscribe")) {
            return subscribe();
        } else if (request.equals("unsubscribe")) {
            return unsubscribe();
        } else if (request.equals("get_subscribers")) {
            return get_subscribers();
        } else if (request.equals("change_channel_description")) {
            return change_channel_description();
        } else if (request.equals("change_channel_name")) {
            return change_channel_name();
        } else if (request.equals("create_channel")) {
            return create_channel();
        } else if (request.equals("check_if_user_subscribed")) {
            return check_if_user_subscribed();
        } else if (request.equals("number_of_subscribers")) {
            return number_of_subscribers();
        } else if (request.equals("get_info")) {
            return get_info();
        } else if (request.equals("delete_channel")) {
            return delete_channel();
        } else if (request.equals("get_channels_of_account")) {
            return get_channels_of_account();
        } else if (request.equals("number_of_posts(")) {
            return number_of_posts();
        } else if (request.equals("see_notification")) {
            return see_notification();
        } else if (request.equals("stop_seeing_notification")) {
            return stop_seeing_notification();
        } else if (request.equals("delete_notification")) {
            return delete_notification();
        }
        return null;
    }

    private ServerResponse stop_seeing_notification(){return null;}
    private ServerResponse delete_notification(){return null;}
    private ServerResponse see_notification(){return null;}
    private ServerResponse create_channel(){
        return Channel_DB.create_channel(UUID.fromString(super.getOwner_id()), super.getDescription(), super.getName(), super.getRequest_id());
    }
    private ServerResponse check_if_user_subscribed(){
        return Channel_DB.check_if_user_subscribed(UUID.fromString(super.getId()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse number_of_subscribers(){
        return Channel_DB.number_of_subscribers(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse number_of_posts(){
        return Channel_DB.number_of_posts(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse get_info(){
        return Channel_DB.get_info(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse delete_channel(){
        return Channel_DB.delete_channel(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse get_channels_of_account(){
        return Channel_DB.get_channels_of_account(UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse change_channel_name(){
        return Channel_DB.change_channel_title(UUID.fromString(super.getId()), super.getName(), super.getRequest_id());
    }
    private ServerResponse change_channel_description(){
        return Channel_DB.change_channel_description(UUID.fromString(super.getId()), super.getDescription(), super.getRequest_id());
    }
    private ServerResponse change_channel_username(){
        return Channel_DB.change_channel_username(UUID.fromString(super.getId()), super.getUsername(), super.getRequest_id());
    }
    private ServerResponse get_subscribers(){
        return Channel_DB.get_subscribers(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse subscribe(){
        return Channel_DB.subscribe_to_channel(UUID.fromString(super.getId()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse unsubscribe(){
        return Channel_DB.unsubscribe_to_channel(UUID.fromString(super.getId()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }

}
