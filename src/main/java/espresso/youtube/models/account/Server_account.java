package espresso.youtube.models.account;

import espresso.youtube.DataBase.Utilities.Account_DB;
import espresso.youtube.models.ServerResponse;

import java.util.ArrayList;
import java.util.UUID;

public class Server_account extends Account {
    @Override
    public ServerResponse handle_request() {
        super.handle_request();

        if (request.equals("login")) {
            return login();
        } else if (request.equals("sign_up")) {
            return sign_up();
        } else if (request.equals("change_name")) {
            return sign_up();
        } else if (request.equals("change_email")) {
            return sign_up();
        } else if (request.equals("change_password")) {
            return sign_up();
        } else if (request.equals("make_user_premium")) {
            return sign_up();
        } else if (request.equals("remove_premium_of_user")) {
            return sign_up();
        } else if (request.equals("change_dark_mode")) {
            return sign_up();
        } else if (request.equals("get_username_by_id")) {
            return sign_up();
        } else if (request.equals("get_info")) {
            return sign_up();
        } else if (request.equals("get_subscribed_channels")) {
            return sign_up();
        } else if (request.equals("change_username")) {
            return sign_up();
        } else if (request.equals("get_liked_posts")) {
            return get_liked_posts();
        }
        return null;
    }
    private ServerResponse make_user_premium(){
        return Account_DB.make_user_premium(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse get_liked_posts(){
        return Account_DB.get_liked_posts(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse remove_premium_of_user(){
        return Account_DB.remove_premium_of_user(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse change_dark_mode(){
        return Account_DB.change_dark_mode(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse get_info(){
        return Account_DB.get_info(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse get_subscribed_channels(){
        return Account_DB.get_subscribed_channels(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse change_email(){
        return Account_DB.change_gmail(UUID.fromString(super.getId()),super.getGmail() ,super.getRequest_id());
    }
    private ServerResponse change_password(){
        return Account_DB.change_password(UUID.fromString(super.getId()), super.getPassword(), super.getRequest_id());
    }
    private ServerResponse change_username(){
        return Account_DB.change_username(UUID.fromString(super.getId()),super.getPassword() ,super.getRequest_id());
    }
    private ServerResponse login() {
        System.out.println("[SERVER] : user wants to login");
        System.out.println("username : " + super.getUsername());
        System.out.println("password : " + super.getPassword());
        return Account_DB.login(super.getUsername() , super.getPassword() , super.getRequest_id());
    }

    private ServerResponse sign_up(){
        return Account_DB.sign_up(super.getUsername(), super.getPassword(), super.getGmail(), super.getRequest_id());
    }
}