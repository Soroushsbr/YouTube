package espresso.youtube.models;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerResponse {
//------------------ jackson informations -------------------
    @JsonProperty("permission1")
    private boolean permission1 = false;
    @JsonProperty("permission2")
    private boolean permission2 = false;
    @JsonProperty("message")
    private String message = "";
    @JsonProperty("request_id")
    private int request_id = -1;

    public void update_request(String jsonString){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readerForUpdating(this).readValue(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
//------------------------ getters -----------------------------
    public String getMessage(){
        return message;
    }
    public int getRequest_id(){
        return request_id;
    }
    public boolean getPermission1(){
        return permission1;
    }
    public boolean getPermission2(){
        return permission2;
    }
//------------------------ setters -----------------------------
    public void setMessage(String message){
        this.message = message;
    }
    public void setRequest_id(int request_id){
        this.request_id = request_id;
    }
    public void setPermission1(boolean permission1){
        this.permission1 = permission1;
    }
    public void setPermission2(boolean permission2){
        this.permission2 = permission2;
    }
}