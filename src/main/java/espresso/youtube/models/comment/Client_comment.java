package espresso.youtube.models.comment;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.io.IOException;

public class Client_comment {
    private Comment comment = new Comment();
    private DataOutputStream out;
    private ObjectMapper mapper = new ObjectMapper();
    private String jsonString;

    public Client_comment(DataOutputStream out){
        this.out = out;
    }


    private void send_request(){
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(comment);
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