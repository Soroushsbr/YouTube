package espresso.youtube.models.playlist;

import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.ServerResponse;

public class Server_playlist extends ClassInfo {
    @Override
    public ServerResponse handle_request() {
        if (request.equals("create_playlist")) {
            return create_playlist();
        } else if (request.equals("change_playlist_info")) {
            return change_playlist_info();
        } else if (request.equals("add_video")) {
            return add_video();
        } else if (request.equals("remove_video")) {
            return remove_video();
        }
        return null;
    }

    private ServerResponse create_playlist(){
        return null;
    }
    private ServerResponse change_playlist_info(){
        return null;
    }
    private ServerResponse add_video(){
        return null;
    }
    private ServerResponse remove_video(){
        return null;
    }

}
