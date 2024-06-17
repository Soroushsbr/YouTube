package espresso.youtube.models.video;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.io.IOException;

public class Client_video {
    private Video video = new Video();
    private DataOutputStream out;
    private ObjectMapper mapper = new ObjectMapper();
    private String jsonString;

    public Client_video(DataOutputStream out){
        this.out = out;
    }
    public void get_video(String owner_id, String video_id, int request_id){
        video.setRequest("get_video");
        video.setRequest_id(request_id);
        video.setOwner_id(owner_id);
        video.setVideo_id(video_id);
        send_request();
    }
    public void send_video_info(String owner_id, String title, String description, String channel_id, int request_id){
        video.setRequest("send_video_info");
        video.setRequest_id(request_id);
        video.setOwner_id(owner_id);
        video.setTitle(title);
        video.setDescription(description);
        video.setChannel_id(channel_id);
        send_request();
    }
    private void send_request(){
        try {
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(video);
            out.writeUTF(jsonString);
        } catch (IOException e) {
            System.out.println("error in Client_video");
        } finally {
            video = new Video();
        }
    }

}
