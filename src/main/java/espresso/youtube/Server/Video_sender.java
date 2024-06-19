package espresso.youtube.Server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.models.ServerResponse;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Video_sender implements Runnable{
    private Socket client;
    private ArrayList<Client_Handler> client_handlers;
    private DataInputStream in;
    private DataOutputStream out;
    private DataInputStream fin;
    public Video_sender(Socket client, ArrayList<Client_Handler> client_handlers) throws IOException {
        this.client = client;
        this.client_handlers = client_handlers;
        this.in = new DataInputStream(client.getInputStream());
        this.out = new DataOutputStream(client.getOutputStream());
    }
    @Override
    public void run() {
        try {
            String json = in.readUTF();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(json);
            int client_handler_id = rootNode.path("client_handler_id").asInt();

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.add_part("status", "sending...");
            serverResponse.setRequest_id(1);
            serverResponse.add_part("exist", true);

            File mediaFile = new File("src/main/java/espresso/youtube/models/video/jozv.pdf");
            if(mediaFile.exists()){
                serverResponse.set_part("exist", true);
                out.writeUTF("1");
            } else {
                serverResponse.set_part("exist", false);
                client_handlers.get(client_handler_id).send_video_response(serverResponse);
                out.writeUTF("0");
                return;
            }
            fin = new DataInputStream(new FileInputStream(mediaFile));
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                System.out.println("running");
            }
            fin.close();
            
            serverResponse.set_part("status", "complete");
            client_handlers.get(client_handler_id).send_video_response(serverResponse);
        } catch (IOException e){
            System.out.println("error");
        } finally {
            try {
                in.close();
                out.close();
                client.close();
                System.out.println("video sender closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String type;
    private String path;
    //------------------ setters --------------------
    public void setType(String type) {
        this.type = type;
    }
    public void setPath(String path){
        this.path = path;
    }
    //------------------ getters --------------------
    public String getType(){
        return type;
    }
    public String getPath(){
        return path;
    }
}
