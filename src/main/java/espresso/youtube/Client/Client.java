package espresso.youtube.Client;

import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.account.Client_account;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

// Client Class
public class Client {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 3000;
    private Socket client;
    private DataOutputStream out;


    public Client() throws IOException {
        client = new Socket(SERVER_IP, PORT);
        out = new DataOutputStream(client.getOutputStream());
        System.out.println("[CLIENT] " + client.getInetAddress() + " connected to server.");
    }

    public DataOutputStream getOut(){
        return out;
    }
    public Socket getClient(){
        return client;
    }
    public void close() throws IOException {
        out.close();
        client.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Client client1 = new Client();
        ServerResponse serverResponse = new ServerResponse();
        Handle_Server_Response handleServerResponse = new Handle_Server_Response(client1.getClient(), serverResponse);

        Thread listener = new Thread(handleServerResponse);
        listener.start();

        Client_account client_account = new Client_account(client1.getOut());
        client_account.login("mobin", "1234", 100);

        while (true){
            Thread.sleep(50);
            if(serverResponse.getRequest_id() == 100){
                System.out.println(serverResponse.get_part("message"));
                break;
            }
            System.out.println("still running");
        }

    }
}