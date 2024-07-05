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
    public void change_name(String new_name, String user_id, int request_id){
        account.setRequest("change_name");
        account.setRequest_id(request_id);
        account.setName(new_name);
        account.setId(user_id);
        send_request();
    }
    public void change_email(String new_email, String user_id, int request_id){
        account.setRequest("change_email");
        account.setRequest_id(request_id);
        account.setGmail(new_email);
        account.setId(user_id);
        send_request();
    }
    public void change_password(String new_password, String user_id, int request_id){
        account.setRequest("change_password");
        account.setRequest_id(request_id);
        account.setPassword(new_password);
        account.setId(user_id);
        send_request();
    }
    public void change_username(String new_username, String user_id, int request_id){
        account.setRequest("change_username");
        account.setRequest_id(request_id);
        account.setUsername(new_username);
        account.setId(user_id);
        send_request();
    }

    private void send_request(){
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(account);
            out.writeUTF(jsonString);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            account = new Account();
        }
    }
}