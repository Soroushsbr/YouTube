package espresso.youtube.models.account;

import espresso.youtube.models.ServerResponse;
import java.io.IOException;

public class Server_account extends Account {
    @Override
    public ServerResponse handle_request() {
        if (request.equals("login")) {
            return login();
        } else if (request.equals("sign_up")) {
            return sign_up();
        }
        return null;
    }

    private ServerResponse login() {
        //return database.login(super.username, super.password);
        System.out.println("[SERVER] : user wants to login");
        System.out.println("username : " + super.getUsername());
        System.out.println("password : " + super.getPassword());

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(super.getRequest_id());
        serverResponse.add_part("message", "login successfully");

        return serverResponse;
    }

    private ServerResponse sign_up(){
        return null;
    }
}
