package espresso.youtube.Client;

import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.video.Client_video;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

// Client Class
public class Client {

    private static final String SERVER_IP = "127.0.0.1";
    private int req_id = 1;
    private String user_id;
    private static final int PORT = 8000;
    private Socket client;
    private DataOutputStream out;
    public HashMap<Integer, ServerResponse> requests = new HashMap<>();

    public Client() throws IOException {
        client = new Socket(SERVER_IP, PORT);
        out = new DataOutputStream(client.getOutputStream());
        System.out.println("[CLIENT] " + client.getInetAddress() + " connected to server.");
        Handle_Server_Response handleServerResponse = new Handle_Server_Response(client, requests);
        Thread listener = new Thread(handleServerResponse);
        listener.start();
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
        while (true){
            Thread.sleep(100);
            if(client1.requests.get(0) != null)
                break;
        }

        Client_video v = new Client_video(client1.getOut());
//        Client_video.get_media("","123","pdf","video",(int)client1.requests.get(0).get_part("client_handler_id"), 1);
//        while (true){
//            Thread.sleep(100);
//            if(client1.requests.get(1) != null){
//                System.out.println(client1.requests.get(1).get_part("status"));
//                break;
//            } else
//                System.out.println("still running");
//        }
        File file = new File("src/main/java/espresso/youtube/Client/video1.mp4");
        v.send_video_info("23","title","description","123",1);
        v.upload_media(file,"23","mp4","video",(int)client1.requests.get(0).get_part("client_handler_id"));
        while (true){
            Thread.sleep(100);
            if(client1.requests.get(1) != null){
                System.out.println(client1.requests.get(1).get_part("status"));
                break;
            } else
                System.out.println("still running");
        }

    }

    public int getReq_id() {
        return req_id;
    }

    public void setReq_id() {
        this.req_id++;
    }
}
