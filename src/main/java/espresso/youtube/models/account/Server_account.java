package espresso.youtube.models.account;

public class Server_account extends Account{
    @Override
    public void handle_request() {
        super.handle_request();
        if(request.equals("login")) {
            login();
        }
    }

    private void login(){
        //database.login(super.username, super.password);
        System.out.println("[SERVER] : user wants to login");
        System.out.println("username : " + super.getUsername());
        System.out.println("password : " + super.getPassword());

    }
}
