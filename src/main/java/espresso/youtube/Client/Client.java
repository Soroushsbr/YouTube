package espresso.youtube.Client;

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

    public static void main(String[] args) throws IOException {
    }
}