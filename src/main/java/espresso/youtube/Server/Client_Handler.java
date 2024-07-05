package espresso.youtube.Server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.account.Server_account;
import espresso.youtube.models.notification.Notification;
import espresso.youtube.models.notification.Server_notification;
import espresso.youtube.models.video.Server_video;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Client_Handler implements Runnable {
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private int client_handler_id;
    private HashMap<String, Client_Handler> online_clients = new HashMap<>();
    private String random_key;
    public Client_Handler(Socket client, int client_handler_id, HashMap<String, Client_Handler> online_clients, String random_key) throws IOException {
        this.client = client;
        this.client_handler_id = client_handler_id;
        this.online_clients = online_clients;
        this.random_key = random_key;
        this.in = new DataInputStream(client.getInputStream());
        this.out = new DataOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
        try {
            String jsonString = "";
            String response = "";
            ServerResponse serverResponse = null;
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode;
            String className;
            boolean cond = true;

            serverResponse = new ServerResponse();
            serverResponse.setRequest_id(0);
            serverResponse.setResponse_type("serverInfo");
            serverResponse.add_part("client_handler_id" , client_handler_id);
            out.writeUTF(mapper.writeValueAsString(serverResponse));

            while (cond) {
                jsonString = this.in.readUTF();
                rootNode = mapper.readTree(jsonString);
                className = rootNode.path("className").textValue();

                Server_notification notification = new Server_notification(online_clients);

                if(className.equals("account")){
                    Server_account server_account = mapper.readValue(jsonString, Server_account.class);
                    server_account.setNotification(notification);
                    serverResponse = server_account.handle_request();
                    if((server_account.getRequest().equals("login") || server_account.getRequest().equals("sign_up")) && (boolean)serverResponse.get_part("isSuccessful") == true){
                        Client_Handler clientHandler = online_clients.get(random_key);
                        online_clients.remove(random_key);
                        online_clients.put((String) serverResponse.get_part("UserID"), clientHandler);
                        random_key = (String) serverResponse.get_part("UserID");
                    }
                }else if(className.equals("video")){
                    Server_video server_video = mapper.readValue(jsonString, Server_video.class);
                    server_video.setNotification(notification);
                    serverResponse = server_video.handle_request();
                }


                try {
                    response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(serverResponse);
                    System.out.println(response);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                out.writeUTF(response);
            }
        } catch (IOException e) {
            System.err.println("IO Exception in client handler!!!!!!");
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                client.close();
                online_clients.remove(random_key);
                System.out.println("[SERVER] client closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send_response(ServerResponse serverResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            out.writeUTF(mapper.writeValueAsString(serverResponse));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}