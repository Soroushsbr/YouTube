package espresso.youtube.models.video;

import espresso.youtube.DataBase.Utilities.Post_DB;
import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.account.Account;
import espresso.youtube.models.notification.Notification;

import java.util.UUID;

public class Server_video extends Video {
    @Override
    public ServerResponse handle_request() {
        if (request.equals("send_video_info")) {
            return insert_video_info();
        } else if(request.equals("get_video_info")) {
            return send_video_info();
        } else if(request.equals("get_videos")) {
            return send_videos();
        } else if(request.equals("change_channel_photo")) {
            return change_channel_photo();
        } else if(request.equals("change_profile_photo")) {
            return change_profile_photo();
        } else if(request.equals("change_thumbnail")) {
            return change_thumbnail();
        } else if(request.equals("change_playlist_photo")) {
            return change_playlist_photo();
        } else if(request.equals("search")) {
            return search();
        } else if(request.equals("like")) {
            return like();
        } else if(request.equals("dislike")) {
            return dislike();
        } else if(request.equals("check_user_likes_post")) {
            return check_user_likes_post();
        } else if(request.equals("check_user_dislikes_post")) {
            return check_user_dislikes_post();
        } else if(request.equals("add_to_post_viewers")) {
            return add_to_post_viewers();
        } else if(request.equals("check_if_user_viewed_post")) {
            return check_if_user_viewed_post();
        } else if(request.equals("number_of_views")) {
            return number_of_views();
        } else if(request.equals("number_of_likes")) {
            return number_of_likes();
        } else if(request.equals("number_of_dislikes")) {
            return number_of_dislikes();
        } else if(request.equals("number_of_comments")) {
            return number_of_comments();
        } else if(request.equals("get_info")) {
            return get_info();
        } else if(request.equals("delete_post")) {
            return delete_post();
        } else if(request.equals("get_all_posts")) {
            return get_all_posts();
        } else if(request.equals("get_all_posts_of_a_account")) {
            return get_all_posts_of_a_account();
        } else if(request.equals("get_all_posts_of_a_channel")) {
            return get_all_posts_of_a_channel();
        } else if(request.equals("get_all_viewers_of_a_post")) {
            return get_all_viewers_of_a_post();
        }
        return null;
    }

    private ServerResponse get_all_viewers_of_a_post(){
        return null;
    }
    private ServerResponse get_all_posts_of_a_channel(){
        return null;
    }
    private ServerResponse get_all_posts_of_a_account(){
        return null;
    }
    private ServerResponse get_all_posts(){
        return null;
    }
    private ServerResponse delete_post(){
        return null;
    }
    private ServerResponse get_info(){
        return null;
    }
    private ServerResponse number_of_comments(){
        return null;
    }
    private ServerResponse number_of_dislikes(){
        return null;
    }
    private ServerResponse number_of_likes(){
        return null;
    }
    private ServerResponse number_of_views(){
        return null;
    }
    private ServerResponse check_if_user_viewed_post(){
        return null;
    }
    private ServerResponse add_to_post_viewers(){
        return null;
    }
    private ServerResponse check_user_dislikes_post(){
        return null;
    }
    private ServerResponse check_user_likes_post(){
        return null;
    }
    private ServerResponse dislike(){
        return null;
    }
    private ServerResponse like(){
        return null;
    }
    private ServerResponse search(){
        return null;
    }
    private ServerResponse change_playlist_photo(){
        return null;
    }
    private ServerResponse change_thumbnail(){
        return null;
    }
    private ServerResponse change_profile_photo(){
        return null;
    }
    private ServerResponse change_channel_photo(){
        return null;
    }
    private ServerResponse insert_video_info(){
        System.out.println(super.getTitle());

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(super.getRequest_id());

        Post_DB.add_post(UUID.fromString(super.getVideo_id()) , UUID.fromString(super.getOwner_id()), super.getTitle(), UUID.fromString(super.getChannel().getId())  ,  super.getDescription(), true , false);
        serverResponse.add_part("status", "received");
        //notification.upload_post();
        return serverResponse;
    }
    private ServerResponse send_video_info(){
        return Post_DB.get_info(UUID.fromString(super.getVideo_id()) , super.getRequest_id());
    }
    private ServerResponse send_videos(){
        return Post_DB.get_all_posts(super.getRequest_id());
    }
}
