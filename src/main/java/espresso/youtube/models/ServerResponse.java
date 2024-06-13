package espresso.youtube.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class ServerResponse {
//------------------ jackson informations -------------------
    @JsonProperty("response_part")
    private HashMap<String, Object> response_parts = new HashMap<>();
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

    public void add_part(String name, Object object){
        response_parts.put(name, object);
    }
    public Object get_part(String name){
        return response_parts.get(name);
    }
    public void set_part(String name, Object object){
        response_parts.replace(name, object);
    }
    public void delete_part(String name){
        response_parts.remove(name);
    }
    public void delete_all_parts(){
        response_parts.clear();
    }
//------------------------ getters -----------------------------

    public HashMap<String, Object> getResponse_parts() {
        return response_parts;
    }
    public int getRequest_id(){
        return request_id;
    }
//------------------------ setters -----------------------------

    public void setResponse_parts(HashMap<String, Object> response_parts) {
        this.response_parts = response_parts;
    }
    public void setRequest_id(int request_id){
        this.request_id = request_id;
    }
}