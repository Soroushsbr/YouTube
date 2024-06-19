package espresso.youtube.models.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import espresso.youtube.models.ServerResponse;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class Client_video {
    private Video video = new Video();
    private DataOutputStream out;
    private ObjectMapper mapper = new ObjectMapper();
    private String jsonString;

    public Client_video(DataOutputStream out){
        this.out = out;
    }
    public void upload_media(File mediaFile, String owner_id, String data_type, String type, int client_handler_id) throws IOException {
        Socket v = new Socket("127.0.0.1", 8002);
        DataOutputStream out = new DataOutputStream(v.getOutputStream());
        DataInputStream in = new DataInputStream(v.getInputStream());
        DataInputStream fin = new DataInputStream(new FileInputStream(mediaFile));
        UUID uuid = UUID.randomUUID();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("owner_id", owner_id);
        json.put("type", type);
        json.put("data_type", data_type);
        json.put("client_handler_id", client_handler_id);
        json.put("video_id", uuid.toString());
        out.writeUTF(mapper.writeValueAsString(json));
        
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fin.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            System.out.println("running");
        }
        fin.close();
        out.close();
        in.close();
        v.close();

        System.out.println("client video closed");
        video.setVideo_id(uuid.toString());
        System.out.println(video.getVideo_id());
        send_request();
    }

    public static void get_media(String owner_id, String media_id, String data_type, String type, int client_handler_id, int request_id) throws IOException {
        Socket v = new Socket("127.0.0.1", 8001);
        DataOutputStream out = new DataOutputStream(v.getOutputStream());
        DataInputStream in = new DataInputStream(v.getInputStream());

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("owner_id", owner_id);
        json.put("video_id", media_id);
        json.put("type", type);
        json.put("data_type", data_type);
        json.put("request_id", request_id);
        json.put("client_handler_id", client_handler_id);
        out.writeUTF(mapper.writeValueAsString(json));

        String s = in.readUTF();
        if(s.equals("0")){
            out.close();
            in.close();
            v.close();
            System.out.println("client video closed");
            return;
        }

        DataOutputStream dos = new DataOutputStream(new FileOutputStream("src/main/java/espresso/youtube/Client/" + media_id + "." + data_type));
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            dos.write(buffer, 0, bytesRead);
        }

        out.close();
        in.close();
        dos.close();
        v.close();

        System.out.println("client video closed");
    }
    public void send_video_info(String owner_id, String title, String description, String channel_id, int request_id){
        video.setRequest("send_video_info");
        video.setRequest_id(request_id);
        video.setOwner_id(owner_id);
        video.setTitle(title);
        video.setDescription(description);
        video.setChannel_id(channel_id);
    }
    private void send_request(){
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(video);
            out.writeUTF(jsonString);
        } catch (IOException e) {
            System.out.println("error in Client_video");
            e.printStackTrace();
        } finally {
            video = new Video();
        }
    }

}
