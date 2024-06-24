package espresso.youtube.models.account;

import espresso.youtube.DataBase.Utilities.Account_DB;
import espresso.youtube.models.ServerResponse;

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
        return Account_DB.login(super.getUsername() , super.getPassword() , super.getRequest_id());
    }

    private ServerResponse sign_up(){
        return Account_DB.sign_up(super.getUsername(), super.getPassword(), super.getGmail(), super.getRequest_id());
    }
}