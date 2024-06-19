package espresso.youtube.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Server class is where threads from Client_Handler runs
public class Server {

    public static void main(String[] args) {
        Client_part client_part = new Client_part();
        Sender_part sender_part = new Sender_part(client_part.getClient_handlers());
        Receiver_part receiver_part = new Receiver_part(client_part.getClient_handlers());

        Thread client_thread = new Thread(client_part);
        Thread sender_thread = new Thread(sender_part);
        Thread receiver_thread = new Thread(receiver_part);

        client_thread.start();
        sender_thread.start();
        receiver_thread.start();

        try {
            client_thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
class Client_part implements Runnable {
    private static final int PORT = 8000;
    private static ArrayList<Socket> clients = new ArrayList<>();
    private ArrayList<Client_Handler> client_handlers = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(4);
    @Override
    public void run() {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(PORT);
            System.out.println("[CLIENT PART] started. Waiting for client connections...");
            while (true) {
                Socket client = listener.accept();
                System.out.println("[CLIENT PART] Connected to client: " + client.getInetAddress());
                Client_Handler client_thread = new Client_Handler(client, client_handlers.size());
                client_handlers.add(client_thread);
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
    public ArrayList<Client_Handler> getClient_handlers() {
        return client_handlers;
    }
}
class Sender_part implements Runnable {
    private static final int PORT = 8001;
    private static ArrayList<Socket> clients = new ArrayList<>();
    private ArrayList<Client_Handler> client_handlers;
    private static ExecutorService pool = Executors.newFixedThreadPool(2);
    public Sender_part(ArrayList<Client_Handler> client_handlers){
        this.client_handlers = client_handlers;
    }
    @Override
    public void run() {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(PORT);
            System.out.println("[SENDER PART] started. Waiting for client connections...");
            while (true) {
                Socket client = listener.accept();
                System.out.println("[SENDER PART] Connected to client: " + client.getInetAddress());
                Video_sender sender_thread = new Video_sender(client, client_handlers);
                clients.add(client);
                pool.execute(sender_thread);
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
class Receiver_part implements Runnable {
    private static final int PORT = 8002;
    private static ArrayList<Socket> clients = new ArrayList<>();
    private ArrayList<Client_Handler> client_handlers;
    private static ExecutorService pool = Executors.newFixedThreadPool(2);
    public Receiver_part(ArrayList<Client_Handler> client_handlers){
        this.client_handlers = client_handlers;
    }
    @Override
    public void run() {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(PORT);
            System.out.println("[RECEIVER PART] started. Waiting for client connections...");
            while (true) {
                Socket client = listener.accept();
                System.out.println("[RECEIVER PART] Connected to client: " + client.getInetAddress());
                Video_receiver receiver_thread = new Video_receiver(client, client_handlers);
                clients.add(client);
                pool.execute(receiver_thread);
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
