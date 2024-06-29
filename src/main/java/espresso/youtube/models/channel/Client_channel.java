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