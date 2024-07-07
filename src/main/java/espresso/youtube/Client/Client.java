package espresso.youtube.Client;

import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.video.Client_video;
import javafx.fxml.FXML;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

// Client Class
public class Client {

    private static final String SERVER_IP = "127.0.0.1";
    private int req_id = 1;
    private String user_id;
    private String channel_id;
    private static final int PORT = 8000;
    private Socket client;
    private DataOutputStream out;
    public HashMap<Integer, ServerResponse> requests = new HashMap<>();
    public Client() throws IOException {
        client = new Socket(SERVER_IP, PORT);
        out = new DataOutputStream(client.getOutputStream());
        Handle_Server_Response handleServerResponse = new Handle_Server_Response(client, requests);
        Thread listener = new Thread(handleServerResponse);
        listener.start();
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
    }

    public int getReq_id() {
        return req_id;
    }
    public void setReq_id() {
        this.req_id++;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }
    public String getChannel_id() {
        return channel_id;
    }
}
