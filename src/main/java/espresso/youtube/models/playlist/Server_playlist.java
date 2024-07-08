package espresso.youtube.models.playlist;

import espresso.youtube.DataBase.Utilities.Playlist_DB;
import espresso.youtube.DataBase.Utilities.Post_DB;
import espresso.youtube.models.ClassInfo;
import espresso.youtube.models.ServerResponse;

import java.util.UUID;

public class Server_playlist extends Playlist {
    @Override
    public ServerResponse handle_request() {
        super.handle_request();

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
        return Post_DB.get_all_posts_of_a_playlist(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse make_playlist_public(){
        return Playlist_DB.make_playlist_public(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse make_playlist_private(){
        return Playlist_DB.make_playlist_private(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse number_of_posts(){
        return Playlist_DB.number_of_posts(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse get_info(){
        return Playlist_DB.get_info(UUID.fromString(super.getId()), super.getRequest_id());

    }
    private ServerResponse delete_playlist(){
        return Playlist_DB.delete_playlist(UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse get_playlists_of_account(){
        return Playlist_DB.get_playlists_of_account(UUID.fromString(super.getUser_id()), super.getRequest_id());
    }
    private ServerResponse create_playlist(){
        return Playlist_DB.create_playlist(UUID.fromString(super.getUser_id()), super.getTitle(), super.getIs_public(), super.getDescription(), super.getRequest_id());
    }
    private ServerResponse change_playlist_info(){
        return Playlist_DB.change_playlist_info(UUID.fromString(super.getUser_id()), super.getDescription(), super.getTitle(), super.getRequest_id());
    }
    private ServerResponse add_video(){
        return Post_DB.add_post_to_playlist(super.getVideos(), UUID.fromString(super.getId()), super.getRequest_id());
    }
    private ServerResponse remove_video(){
        return Post_DB.delete_post_from_playlist(super.getVideos(), UUID.fromString(super.getId()), super.getRequest_id());
    }

}
