package espresso.youtube.models.video;

import espresso.youtube.DataBase.Utilities.Post_DB;
import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.account.Account;
import espresso.youtube.models.notification.Notification;

import java.util.Optional;
import java.util.UUID;

public class Server_video extends Video {
    @Override
    public ServerResponse handle_request() {
        super.handle_request();

       if (request.equals("send_video_info")) {
            return insert_video_info();
       } else if(request.equals("send_thumbnail_info")) {
           return insert_thumbnail_info();
       } else if(request.equals("send_channel_profile_info")) {
           return insert_channel_profile_info();
       } else if(request.equals("send_profile_photo_info")) {
           return insert_profile_photo_info();
        } else if(request.equals("get_video_info")) {
            return send_video_info();
        } else if(request.equals("get_videos")) {
            return send_videos();
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
        } else if(request.equals("change_video_info")) {
            return change_video_info();
        } else if(request.equals("remove_user_like_from_post")) {
            return remove_user_like_from_post();
        } else if(request.equals("remove_user_dislike_from_post")) {
            return remove_user_dislike_from_post();
        }
        return null;
    }

    private ServerResponse get_all_viewers_of_a_post(){
        return Post_DB.get_all_viewers_of_a_post(UUID.fromString(super.getVideo_id()), super.getRequest_id());
    }
    private ServerResponse change_video_info(){
//        Post_DB.change_post_info(UUID.fromString(super.getVideo_id()), super.getTitle(), super.getDescription(), super.get)
        return null;
    }
    private ServerResponse get_all_posts_of_a_channel(){
        return Post_DB.get_all_posts_of_channel(UUID.fromString(super.getChannel().getId()), super.getRequest_id());
    }
    private ServerResponse get_all_posts_of_a_account(){
        return Post_DB.get_all_Posts_of_a_account(UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse get_all_posts(){
        return Post_DB.get_all_posts(super.getRequest_id() , UUID.fromString(super.getOwner_id())) ;
    }
    private ServerResponse delete_post(){
        return Post_DB.delete_post(UUID.fromString(super.getVideo_id()), super.getRequest_id());
    }
    private ServerResponse get_info(){
        return Post_DB.get_info(UUID.fromString(super.getVideo_id()), super.getRequest_id());
    }
    private ServerResponse number_of_comments(){
        return Post_DB.number_of_comments(UUID.fromString(super.getVideo_id()), super.getRequest_id());
    }
    private ServerResponse number_of_dislikes(){
        return Post_DB.number_of_dislikes(UUID.fromString(super.getVideo_id()), super.getRequest_id());
    }
    private ServerResponse number_of_likes(){
        return Post_DB.number_of_likes(UUID.fromString(super.getVideo_id()), super.getRequest_id());
    }
    private ServerResponse number_of_views(){
        return Post_DB.number_of_views(UUID.fromString(super.getVideo_id()), super.getRequest_id());
    }
    private ServerResponse check_if_user_viewed_post(){
        return Post_DB.check_if_user_viewed_post(UUID.fromString(super.getVideo_id()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse add_to_post_viewers(){
        return Post_DB.add_to_post_viewers(UUID.fromString(super.getVideo_id()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse check_user_dislikes_post(){
        return Post_DB.check_user_dislikes_post(UUID.fromString(super.getVideo_id()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse check_user_likes_post(){
        return Post_DB.check_user_likes_post(UUID.fromString(super.getVideo_id()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse dislike(){
        return Post_DB.dislike_post(UUID.fromString(super.getVideo_id()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse like(){
        return Post_DB.like_post(UUID.fromString(super.getVideo_id()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse remove_user_like_from_post(){
        return Post_DB.remove_user_like_from_post(UUID.fromString(super.getVideo_id()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse remove_user_dislike_from_post(){
        return Post_DB.remove_user_dislike_from_post(UUID.fromString(super.getVideo_id()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse search(){
//       return search(super.getText?? , super.getRequest_id())
        return null;
    }
    private ServerResponse insert_video_info(){
        Post_DB.add_post(UUID.fromString(super.getVideo_id()) , UUID.fromString(super.getOwner_id()), super.getTitle(),UUID.fromString(super.getChannel().getId()), super.getDescription(), true, false, super.getLength());
        return null;
    }
    private ServerResponse insert_thumbnail_info(){
        return null;
    }
    private ServerResponse insert_channel_profile_info(){
        return null;
    }
    private ServerResponse insert_profile_photo_info(){
        return null;
    }
    private ServerResponse send_video_info(){
        return Post_DB.get_info(UUID.fromString(super.getVideo_id()) , super.getRequest_id());
    }
    private ServerResponse send_videos(){
        return Post_DB.get_all_posts(super.getRequest_id(), UUID.fromString(super.getOwner_id()));
    }
}
