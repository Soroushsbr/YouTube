package espresso.youtube.models;

public class ClassInfo {
    public String className;
    public String request;

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

    public void handle_request(){
    }
}
