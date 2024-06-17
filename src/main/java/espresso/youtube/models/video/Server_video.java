package espresso.youtube.models.video;

import espresso.youtube.models.ServerResponse;

public class Server_video extends Video {
    @Override
    public ServerResponse handle_request() {
        if (request.equals("send_video_info")) {
            return insert_video_info();
        } else if (request.equals("get_video")) {
            return send_video();
        }
        return null;
    }

    private ServerResponse send_video(){
        return null;
    }
    private ServerResponse insert_video_info(){
        return null;
    }
}
