package espresso.youtube.models.video;

import espresso.youtube.DataBase.Utilities.Post_DB;
import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.account.Account;

import java.util.UUID;

public class Server_video extends Video {
    @Override
    public ServerResponse handle_request() {
        if (request.equals("send_video_info")) {
            return insert_video_info();
        } else if (request.equals("get_video")) {
            return send_video();
        }else if(request.equals("get_video_info")){
            return send_video_info();
        }else if(request.equals("get_videos_id")){
            return send_videos_id();
        }
        return null;
    }

    private ServerResponse send_video(){
        return null;
    }
    private ServerResponse insert_video_info(){
        System.out.println(super.getTitle());

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(super.getRequest_id());

        Post_DB.add_post(UUID.fromString(super.getVideo_id()) , UUID.fromString(super.getOwner_id()), super.getTitle(), super.getDescription(), UUID.randomUUID(), true);
        serverResponse.add_part("status", "received");
        return serverResponse;
    }
    private ServerResponse send_video_info(){
        return Post_DB.get_post(UUID.fromString(super.getVideo_id()) , super.getRequest_id());
    }
    private ServerResponse send_videos_id(){
        return Post_DB.get_all_posts(super.getRequest_id());
    }
}
