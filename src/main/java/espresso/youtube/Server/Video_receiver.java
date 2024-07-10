package espresso.youtube.Server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.models.ServerResponse;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Video_receiver implements Runnable {
    private Socket client;
    private ArrayList<Client_Handler> client_handlers;
    private DataInputStream in;
    private DataOutputStream out;
    private DataInputStream fin;
    public Video_receiver(Socket client, ArrayList<Client_Handler> client_handlers) throws IOException {
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
            String data_type = rootNode.path("data_type").asText();
            String type = rootNode.path("type").asText();
            String video_id = rootNode.path("video_id").asText();
            int request_id = rootNode.path("request_id").asInt();
            System.out.println("[VIDEO RECEIVER] downloading video for request " + request_id);
//            log("[VIDEO RECEIVER] downloading video for request" + request_id, owner_id);

            File folder = new File("src/main/resources/espresso/youtube/Server/" + owner_id);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File("src/main/resources/espresso/youtube/Server/"+owner_id+"/"+video_id);
            if (file.exists()) {
                file.delete();
                System.out.println("[VIDEO RECEIVER] "+video_id+" existed from before and deleted");
//                log("[VIDEO RECEIVER] "+video_id+" existed from before and deleted", owner_id);
            }

            DataOutputStream dos = new DataOutputStream(new FileOutputStream("src/main/resources/espresso/youtube/Server/"+owner_id+"/"+video_id+"."+data_type));
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
            dos.close();



            System.out.println("[VIDEO RECEIVER] video downloaded successfully");
//            log("[VIDEO RECEIVER] video downloaded successfully", owner_id);
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
//    public void log(String message, String id) {
//        String LOG_FILE = "src/main/resources/espresso/youtube/Server/" + id + "/server_log.log";
//        PrintWriter writer = null;
//        try {
//            writer = new PrintWriter(new FileWriter(LOG_FILE, true));
//            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//            writer.println(timeStamp + " - " + message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (writer != null) {
//                writer.close();
//            }
//        }
//    }
}
