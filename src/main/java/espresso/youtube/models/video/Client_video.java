package espresso.youtube.models.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import espresso.youtube.DataBase.Utilities.Post_DB;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class Client_video {
    private Video video = new Video();
    private DataOutputStream out;
    private ObjectMapper mapper = new ObjectMapper();
    private String jsonString;

    public Client_video(DataOutputStream out){
        this.out = out;
    }

    public void change_video_info(String video_id, String new_title, String new_description, int request_id){
        video.setRequest("change_video_info");
        video.setRequest_id(request_id);
        video.setTitle(new_title);
        video.setDescription(new_description);
        video.setVideo_id(video_id);
        send_request();
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
        json.put("request_id", video.getRequest_id());
        out.writeUTF(mapper.writeValueAsString(json));
        
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fin.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        fin.close();
        out.close();
        in.close();
        v.close();

        System.out.println("[CLIENT] media uploaded");
        video.setVideo_id(uuid.toString());
        send_request();
    }

    public static File get_media(String media_id, String owner_id, String data_type, String type, int client_handler_id, int request_id) throws IOException {
        Socket v = new Socket("127.0.0.1", 8001);
        DataOutputStream out = new DataOutputStream(v.getOutputStream());
        DataInputStream in = new DataInputStream(v.getInputStream());

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        json.put("owner_id", owner_id);
        json.put("media_id", media_id);
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
            System.out.println("[CLIENT] video not found");
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        out.close();
        in.close();
        v.close();

        System.out.println("[CLIENT] video received");

        // Create a temporary file and write the data to it
        File tempFile = File.createTempFile(media_id, "." + data_type);
        FileOutputStream fos = new FileOutputStream(tempFile);
        baos.writeTo(fos);
        fos.close();

        return tempFile;
    }
    public void send_video_info(String owner_id, String title, String description, String channel_id, String data_type, int request_id){// data type added
        video.setRequest("send_video_info");
        video.setRequest_id(request_id);
        video.setOwner_id(owner_id);
        video.setTitle(title);
        video.setDescription(description);
        video.setChannel_id(channel_id);
        video.setData_type(data_type);
    }
    public void change_channel_photo(String channel_id, String data_type, int request_id){
        video.setRequest("change_channel_photo");
        video.setRequest_id(request_id);
        video.setChannel_id(channel_id);
        video.setData_type(data_type);
    }
    public void change_profile_photo(String user_id, String data_type, int request_id){
        video.setRequest("change_profile_photo");
        video.setRequest_id(request_id);
        video.setOwner_id(user_id);
        video.setData_type(data_type);
    }
    public void change_thumbnail(String video_id, String data_type, int request_id){
        video.setRequest("change_thumbnail");
        video.setRequest_id(request_id);
        video.setVideo_id(video_id);
        video.setData_type(data_type);
    }
    public void get_video_info(String videoID , int request_id){
        video.setRequest("get_video_info");
        video.setRequest_id(request_id);
        video.setVideo_id(videoID);
        send_request();
    }

    public void get_videos_id(int request_id){
        video.setRequest_id(request_id);
        video.setRequest("get_videos_id");
        send_request();
    }
    private void send_request(){
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(video);
            out.writeUTF(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            video = new Video();
        }
    }

}
