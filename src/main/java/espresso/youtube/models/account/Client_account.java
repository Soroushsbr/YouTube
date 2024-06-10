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

    public void login(String username, String password, int request_id) throws IOException {
        account.setRequest("login");
        account.setUsername(username);
        account.setPassword(password);
        account.setRequest_id(request_id);
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
        account.setId(-1);
        account.setName(null);
    }

}
