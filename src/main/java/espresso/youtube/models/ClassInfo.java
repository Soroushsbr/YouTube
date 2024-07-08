package espresso.youtube.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import espresso.youtube.models.notification.Notification;
import espresso.youtube.models.notification.Server_notification;
import javafx.geometry.NodeOrientation;

public abstract class ClassInfo {
    protected String className;
    public String request;
    private int request_id;
    @JsonIgnore
    protected Server_notification notification;


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
        return null;
    }
}