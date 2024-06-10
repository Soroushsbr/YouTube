package espresso.youtube.Client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import espresso.youtube.models.ServerResponse;

import java.io.*;
import java.net.Socket;

public class Handle_Server_Response implements Runnable {
    private DataInputStream in;
    public ServerResponse serverResponse;
    public Handle_Server_Response(Socket client, ServerResponse serverResponse) throws IOException {
        this.in = new DataInputStream(client.getInputStream());
        this.serverResponse = serverResponse;
    }
    @Override
    public void run() {
        try {
            String jsonString = "";

            while (true) {
                jsonString = this.in.readUTF();
                serverResponse.update_request(jsonString);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
