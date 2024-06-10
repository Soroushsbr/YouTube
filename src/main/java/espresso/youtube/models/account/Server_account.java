package espresso.youtube.models.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.models.ServerResponse;
import java.io.IOException;

public class Server_account extends Account {
    @Override
    public ServerResponse handle_request() {
        if (request.equals("login")) {
            return login();
        }
        return null;
    }

    private ServerResponse login() {
        //database.login(super.username, super.password);
        System.out.println("[SERVER] : user wants to login");
        System.out.println("username : " + super.getUsername());
        System.out.println("password : " + super.getPassword());

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setMessage("information is correct");
        serverResponse.setRequest_id(super.getRequest_id());

        return serverResponse;
    }
}
