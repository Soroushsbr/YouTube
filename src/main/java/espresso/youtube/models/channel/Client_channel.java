package espresso.youtube.models.channel;

import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.DataBase.Utilities.Channel_DB;
import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.account.Account;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Client_channel {
    private Channel channel = new Channel();
    private DataOutputStream out;
    private ObjectMapper mapper = new ObjectMapper();
    private String jsonString;

    public Client_channel(DataOutputStream out){
        this.out = out;
    }

    public void create_channel(String owner_id, String name, String description, int request_id){
        channel.setRequest("create_channel");
        channel.setRequest_id(request_id);
        channel.setOwner_id(owner_id);
        channel.setName(name);
        channel.setDescription(description);
        send_request();
    }
    public void check_if_user_subscribed(String user_id, String channel_id, int request_id){
        channel.setRequest("check_if_user_subscribed");
        channel.setRequest_id(request_id);
        channel.setOwner_id(user_id);
        channel.setId(channel_id);
        send_request();
    }
    public void number_of_subscribers(String channel_id, int request_id){
        channel.setRequest("number_of_subscribers");
        channel.setRequest_id(request_id);
        channel.setId(channel_id);
        send_request();
    }
    private void number_of_posts(String channel_id, int request_id){
        channel.setRequest("number_of_posts");
        channel.setRequest_id(request_id);
        channel.setId(channel_id);
        send_request();
    }
    public void get_info(String channel_id, int request_id){
        channel.setRequest("get_info");
        channel.setRequest_id(request_id);
        channel.setId(channel_id);
        send_request();
    }
    public void delete_channel(String channel_id, int request_id){
        channel.setRequest("delete_channel");
        channel.setRequest_id(request_id);
        channel.setId(channel_id);
        send_request();
    }
    public void get_channels_of_account(String user_id, int request_id){
        channel.setRequest("get_channels_of_account");
        channel.setRequest_id(request_id);
        channel.setOwner_id(user_id);
        send_request();
    }

    public void subscribe(String channel_id, String user_id, int request_id){
        channel.setRequest("subscribe");
        channel.setRequest_id(request_id);
        channel.setId(channel_id);
        channel.getFollowers().add(new Account());
        channel.getFollowers().get(0).setId(user_id);
        send_request();
    }
    public void unsubscribe(String channel_id, String user_id, int request_id){
        channel.setRequest("unsubscribe");
        channel.setRequest_id(request_id);
        channel.setId(channel_id);
        channel.getFollowers().add(new Account());
        channel.getFollowers().get(0).setId(user_id);
        send_request();
    }
    public void get_subscribers(String channel_id, int request_id){
        channel.setRequest("get_subscribers");
        channel.setRequest_id(request_id);
        channel.setId(channel_id);
        send_request();
    }
    public void change_channel_name(String channel_id, String new_name, int request_id){
        channel.setRequest("change_channel_name");
        channel.setRequest_id(request_id);
        channel.setId(channel_id);
        channel.setName(new_name);
        send_request();
    }
    public void change_channel_username(String channel_id, String new_username, int request_id){
        channel.setRequest("change_channel_name");
        channel.setRequest_id(request_id);
        channel.setId(channel_id);
        channel.setUsername(new_username);
        send_request();
    }
    public void change_channel_description(String channel_id, String new_description, int request_id){
        channel.setRequest("change_channel_description");
        channel.setRequest_id(request_id);
        channel.setId(channel_id);
        channel.setDescription(new_description);
        send_request();
    }


    private void send_request(){
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(channel);
            out.writeUTF(jsonString);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            erase_info();
        }
    }
    private void erase_info(){
    }

}