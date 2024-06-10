package espresso.youtube.Server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.account.Server_account;

import java.io.*;
import java.net.Socket;

public class Client_Handler implements Runnable {
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;

    public Client_Handler(Socket client) throws IOException {
        this.client = client;
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

            while (cond) {
                jsonString = this.in.readUTF();
                rootNode = mapper.readTree(jsonString);
                className = rootNode.path("className").textValue();

                if(className.equals("account")){
                    Server_account Server_account = mapper.readValue(jsonString, Server_account.class);
                    serverResponse = Server_account.handle_request();
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
}
