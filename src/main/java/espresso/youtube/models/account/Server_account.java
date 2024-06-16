package espresso.youtube.models.account;

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
        return espresso.youtube.DataBase.Utilities.Account.login(super.getUsername() , super.getPassword() , super.getRequest_id());
    }

    private ServerResponse sign_up(){
        return espresso.youtube.DataBase.Utilities.Account.sign_up(super.getUsername(), super.getPassword(), super.getGmail(), super.getRequest_id());
    }
}