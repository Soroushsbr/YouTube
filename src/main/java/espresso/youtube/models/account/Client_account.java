package espresso.youtube.models.account;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.io.IOException;

public class Client_account {
    private Account account = new Account();
    private DataOutputStream out;
    private ObjectMapper mapper = new ObjectMapper();
    private String jsonString;

    public Client_account(DataOutputStream out){
        this.out = out;
    }

    public void login(String username, String password, int request_id){
        account.setRequest("login");
        account.setRequest_id(request_id);
        account.setUsername(username);
        account.setPassword(password);
        send_request();
    }

    public void sign_up(String username, String password, String email, int request_id){
        account.setRequest("sign_up");
        account.setRequest_id(request_id);
        account.setUsername(username);
        account.setPassword(password);
        account.setGmail(email);
        send_request();
    }

    private void send_request(){
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(account);
            out.writeUTF(jsonString);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            erase_info();
        }
    }
    private void erase_info(){
        account.setRequest(null);
        account.setUsername(null);
        account.setPassword(null);
        account.setId("");
        account.setName(null);
    }

}