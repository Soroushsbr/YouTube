package espresso.youtube.Client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.models.ServerResponse;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class Handle_Server_Response implements Runnable {
    private DataInputStream in;
    public HashMap<Integer, ServerResponse> requests;
    public Handle_Server_Response(Socket client, HashMap<Integer, ServerResponse> requests) throws IOException {
        this.in = new DataInputStream(client.getInputStream());
        this.requests = requests;
    }
    @Override
    public void run() {
        try {
            String jsonString = "";

            while (true) {
                jsonString = this.in.readUTF();
                ServerResponse serverResponse = new ServerResponse();
                serverResponse.update_request(jsonString);
                requests.put(serverResponse.getRequest_id(), serverResponse);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
