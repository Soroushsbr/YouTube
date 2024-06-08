package espresso.youtube.Client;

import java.io.*;
import java.net.Socket;

public class Handle_Server_Response implements Runnable {
    private DataInputStream in;
    public Handle_Server_Response(Socket client) throws IOException {
        this.in = new DataInputStream(client.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                String s = this.in.readUTF();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
