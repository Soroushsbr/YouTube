package espresso.youtube.models;

public class ClassInfo {
    protected String className;
    public String request;
    private int request_id;

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public int getRequest_id() {
        return request_id;
    }
    public void setClassName(String className){
        this.className = className;
    }
    public void setRequest(String request){
        this.request = request;
    }

    public String getClassName() {
        return className;
    }

    public String getRequest() {
        return request;
    }

    public ServerResponse handle_request(){
        return null;
    }
}