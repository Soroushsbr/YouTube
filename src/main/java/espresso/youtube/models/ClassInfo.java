package espresso.youtube.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import espresso.youtube.Server.Client_Handler;
import espresso.youtube.models.notification.Notification;
import espresso.youtube.models.notification.Server_notification;
import javafx.geometry.NodeOrientation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ClassInfo {
    protected String className;
    public String request;
    private int request_id;
    @JsonIgnore
    protected Server_notification notification;

    @JsonIgnore
    private String id = null;
    public void log(String message) {
        String LOG_FILE = "src/main/resources/espresso/youtube/Server/" + id + "/server_log.log";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(LOG_FILE, true));
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.println(timeStamp + " - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


    //================ setter ===================
    public void setNotification(Server_notification notification){
        this.notification = notification;
    }
    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }
    public void setClassName(String className){
        this.className = className;
    }
    public void setRequest(String request){
        this.request = request;
    }
    public void setId(String id){
        this.id = id;
    }
    //================= getter ==================
    public int getRequest_id() {
        return request_id;
    }
    public String getClassName() {
        return className;
    }
    public String getRequest() {
        return request;
    }
    //==============================================
    public ServerResponse handle_request(){
        System.out.println("[SERVER] new request arrived : " + className + ", " + request + ", id:" + request_id);
        if(id != null)
            log("[SERVER] new request arrived : " + className + ", " + request + ", id:" + request_id);
        return null;
    }
}