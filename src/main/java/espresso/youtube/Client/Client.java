package espresso.youtube.Client;

import espresso.youtube.models.ServerResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

// Client Class
public class Client {

    private static final String SERVER_IP = "127.0.0.1";
    private int req_id = 1;
    private static final int PORT = 8000;
    private Socket client;
    private DataOutputStream out;
    public HashMap<Integer, ServerResponse> requests = new HashMap<>();

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
        Handle_Server_Response handleServerResponse = new Handle_Server_Response(client1.getClient(), client1.requests);
        Thread listener = new Thread(handleServerResponse);
        listener.start();

//        Client_account client_account = new Client_account(client1.getOut());
//        client_account.login("mobin", "1234", 100);
//        while (true){
//            Thread.sleep(50);
//            if(client1.requests.get(100) != null){
//                System.out.println(client1.requests.get(100).get_part("message"));
//                break;
//            }
//            System.out.println("still running");
//        }

    }

    public int getReq_id() {
        return req_id;
    }

    public void setReq_id() {
        this.req_id++;
    }
}
