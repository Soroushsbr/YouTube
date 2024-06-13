package espresso.youtube.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Server class is where threads from Client_Handler runs
public class Server {

    private static final int PORT = 8000;

    private static ArrayList<Socket> clients = new ArrayList<>();

    private static ExecutorService pool = Executors.newFixedThreadPool(4);
    public static List<String> messages = new ArrayList<>();
    public static void main(String[] args) {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(PORT);
            System.out.println("[SERVER] Server started. Waiting for client connections...");
            while (true) {
                Socket client = listener.accept();
                System.out.println("[SERVER] Connected to client: " + client.getInetAddress());
                Client_Handler client_thread = new Client_Handler(client);
                clients.add(client);
                pool.execute(client_thread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (listener != null) {
                try {
                    listener.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            pool.shutdown();
        }
    }
}
