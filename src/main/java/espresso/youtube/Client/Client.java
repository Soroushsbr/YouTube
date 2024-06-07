package espresso.youtube.Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

// Client Class
public class Client {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 3000;

    public static void main(String[] args) throws IOException {
        Socket client = new Socket(SERVER_IP, PORT);
        System.out.println("[CLIENT] " + client.getInetAddress() + " connected to server.");
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        Handle_Server_Response handleServerResponse = new Handle_Server_Response(client);
        new Thread(handleServerResponse).start();

    }
}