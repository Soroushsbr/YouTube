package espresso.youtube.Server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.DataBase.Utilities.Post_DB;
import espresso.youtube.models.ServerResponse;
import java.io.File;



import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

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
            String owner_id = rootNode.path("owner_id").asText();
            String media_id = rootNode.path("media_id").asText();
            String data_type = rootNode.path("data_type").asText();
            String type = rootNode.path("type").asText();
            int request_id = rootNode.path("request_id").asInt();
            System.out.println("[VIDEO SENDER] request "+request_id+" received");

            File mediaFile = new File("src/main/resources/espresso/youtube/Server/"+owner_id+"/"+ media_id+"."+data_type);
            if(mediaFile.exists()){
                out.writeUTF("1");
            } else {
                ServerResponse serverResponse = new ServerResponse();
                serverResponse.add_part("exist", false);
                client_handlers.get(client_handler_id).send_video_response(serverResponse);
                System.out.println("[VIDEO SENDER] media not found");
                out.writeUTF("0");
                return;
            }

            // video exists, setting server response
            ServerResponse serverResponse = Post_DB.get_post(UUID.fromString(media_id), request_id);// need to change
            serverResponse.add_part("exist", true);
            serverResponse.add_part("status", "sending");
            client_handlers.get(client_handler_id).send_video_response(serverResponse);

            // uploading media
            fin = new DataInputStream(new FileInputStream(mediaFile));
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            fin.close();
            System.out.println("[VIDEO SENDER] media sent");
            
            serverResponse.set_part("status", "complete");
            client_handlers.get(client_handler_id).send_video_response(serverResponse);
        } catch (IOException e){
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
