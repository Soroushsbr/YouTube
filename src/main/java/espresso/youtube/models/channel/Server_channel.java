package espresso.youtube.models.channel;

import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.ServerResponse;

public class Server_channel extends Channel {
    @Override
    public ServerResponse handle_request() {
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
        }
        return null;
    }

    private ServerResponse create_channel(){
        return null;
    }
    private ServerResponse check_if_user_subscribed(){
        return null;
    }
    private ServerResponse number_of_subscribers(){
        return null;
    }
    private ServerResponse get_info(){
        return null;
    }
    private ServerResponse delete_channel(){
        return null;
    }
    private ServerResponse get_channels_of_account(){
        return null;
    }
    private ServerResponse change_channel_name(){
        return null;
    }
    private ServerResponse change_channel_description(){
        return null;
    }
    private ServerResponse get_subscribers(){
        return null;
    }
    private ServerResponse subscribe(){
        return null;
    }
    private ServerResponse unsubscribe(){
        return null;
    }

}
