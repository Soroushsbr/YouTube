package espresso.youtube.Server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.account.Server_account;
import espresso.youtube.models.video.Server_video;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client_Handler implements Runnable {
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private int client_handler_id;
    public Client_Handler(Socket client, int client_handler_id) throws IOException {
        this.client = client;
        this.client_handler_id = client_handler_id;
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
            serverResponse.add_part("client_handler_id" , client_handler_id);
            out.writeUTF(mapper.writeValueAsString(serverResponse));

            while (cond) {
                jsonString = this.in.readUTF();
                rootNode = mapper.readTree(jsonString);
                className = rootNode.path("className").textValue();

                if(className.equals("account")){
                    Server_account server_account = mapper.readValue(jsonString, Server_account.class);
                    serverResponse = server_account.handle_request();
                }else if(className.equals("video")){
                    Server_video server_video = mapper.readValue(jsonString, Server_video.class);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send_video_response(ServerResponse serverResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        out.writeUTF(mapper.writeValueAsString(serverResponse));
    }
}