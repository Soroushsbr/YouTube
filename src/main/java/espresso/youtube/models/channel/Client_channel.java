package espresso.youtube.models.channel;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.io.IOException;

public class Client_channel {
    private Channel channel = new Channel();
    private DataOutputStream out;
    private ObjectMapper mapper = new ObjectMapper();
    private String jsonString;

    public Client_channel(DataOutputStream out){
        this.out = out;
    }

    public void subscribe(String channel_id, String user_id, int request_id){
        channel.setRequest("subscribe");
        channel.setRequest_id(request_id);
        channel.setId(channel_id);
        channel.getSubscriber_id().add(user_id);
        send_request();
    }
    public void unsubscribe(String channel_id, String user_id, int request_id){
        channel.setRequest("unsubscribe");
        channel.setRequest_id(request_id);
        channel.setId(channel_id);
        channel.getSubscriber_id().add(user_id);
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