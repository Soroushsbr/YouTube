package espresso.youtube.models.playlist;

import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.ServerResponse;

public class Server_playlist extends Playlist {
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
        } else if (request.equals("make_playlist_public")) {
            return make_playlist_public();
        } else if (request.equals("make_playlist_private")) {
            return make_playlist_private();
        } else if (request.equals("create_watch_later")) {
            return create_watch_later();
        } else if (request.equals("number_of_posts")) {
            return number_of_posts();
        } else if (request.equals("get_info")) {
            return get_info();
        } else if (request.equals("delete_playlist")) {
            return delete_playlist();
        } else if (request.equals("get_playlists_of_account")) {
            return get_playlists_of_account();
        } else if (request.equals("get_all_posts_of_a_playlist")) {
            return get_all_posts_of_a_playlist();
        }
        return null;
    }

    private ServerResponse get_all_posts_of_a_playlist(){
        return null;
    }
    private ServerResponse make_playlist_public(){
        return null;
    }
    private ServerResponse make_playlist_private(){
        return null;
    }
    private ServerResponse create_watch_later(){
        return null;
    }
    private ServerResponse number_of_posts(){
        return null;
    }
    private ServerResponse get_info(){
        return null;
    }
    private ServerResponse delete_playlist(){
        return null;
    }
    private ServerResponse get_playlists_of_account(){
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
