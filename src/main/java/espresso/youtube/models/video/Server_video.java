package espresso.youtube.models.video;

import espresso.youtube.DataBase.Utilities.Account_DB;
import espresso.youtube.DataBase.Utilities.Playlist_DB;
import espresso.youtube.DataBase.Utilities.Channel_DB;
import espresso.youtube.DataBase.Utilities.Post_DB;
import espresso.youtube.DataBase.Utilities.Search;
import espresso.youtube.models.ServerResponse;
import espresso.youtube.models.account.Account;
import espresso.youtube.models.channel.Channel;
import espresso.youtube.models.notification.Notification;

import java.util.ArrayList;
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
        } else if(request.equals("get_search_titles")){
           return send_search_titles();
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
       } else if(request.equals("get_recommended_posts")) {
           return get_recommended_posts();
       } else if(request.equals("get_watch_history")) {
           return get_watch_history();
       } else if(request.equals("get_categories")) {
           return get_categories();
       } else if(request.equals("set_category")) {
           return set_category();
       } else if(request.equals("get_posts_by_category")) {
           return get_posts_by_category();
       } else if(request.equals("get_posts_with_same_categories")) {
           return get_posts_with_same_categories();
        }
        return null;
    }

    private ServerResponse get_recommended_posts(){
        return Post_DB.get_recommended_posts(UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse get_watch_history(){
        return Post_DB.get_watch_history(UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse get_categories(){
        return Post_DB.get_categories(super.getRequest_id());
    }
    private ServerResponse set_category(){
        return Post_DB.set_category(UUID.fromString(super.getVideo_id()), super.getCategory_names(), super.getRequest_id());
    }
    private ServerResponse get_posts_by_category(){
        return Post_DB.get_posts_by_category(super.getCategory_names().get(0), super.getRequest_id());
    }
    private ServerResponse get_posts_with_same_categories(){
        return Post_DB.get_posts_with_same_categories(UUID.fromString(super.getVideo_id()), super.getRequest_id());
    }
    private ServerResponse get_all_viewers_of_a_post(){
        return Post_DB.get_all_viewers_of_a_post(UUID.fromString(super.getVideo_id()), super.getRequest_id());
    }
    private ServerResponse change_video_info(){
//        Post_DB.change_post_info(UUID.fromString(super.getVideo_id()), super.getTitle(), super.getDescription(), super.get)
        return null;
    }
    private ServerResponse get_all_posts_of_a_channel(){
        return Post_DB.get_all_posts_of_channel(UUID.fromString(super.getChannel().getId()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
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
//        ServerResponse sr = Post_DB.get_info(UUID.fromString(super.getVideo_id()), 0);
//        notification.like_post(super.getOwner_id(), super.getVideo_id(), (String) sr.get_part("owner_id"));
        ServerResponse sr = Post_DB.get_info(UUID.fromString(super.getVideo_id()), 0);
        notification.like_post(super.getOwner_id(), super.getVideo_id(), (String) sr.get_part("channel_id"));
        return Post_DB.like_post(UUID.fromString(super.getVideo_id()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse remove_user_like_from_post(){
        return Post_DB.remove_user_like_from_post(UUID.fromString(super.getVideo_id()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse remove_user_dislike_from_post(){
        return Post_DB.remove_user_dislike_from_post(UUID.fromString(super.getVideo_id()), UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
    private ServerResponse search(){
        return Search.search(super.getTitle() , super.getOwner_id() , super.getRequest_id());
    }
    public ServerResponse send_search_titles(){
        return Search.search_titles(super.getRequest_id());
    }
    private ServerResponse insert_video_info(){
//        ServerResponse sr = Channel_DB.get_subscribers(UUID.fromString(super.getChannel().getId()), super.getRequest_id());
//        ArrayList<String> ids = new ArrayList<>();
//        for(Channel channel : sr.getChannels_list())
//            ids.add(channel.getId());
//        notification.upload_post(ids, super.getVideo_id());

        Post_DB.add_post(UUID.fromString(super.getVideo_id()) , UUID.fromString(super.getOwner_id()), super.getTitle(), UUID.fromString(super.getChannel().getId()), super.getDescription(), true, false, super.getLength());
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
    private ServerResponse send_liked_videos(){
        return Account_DB.get_liked_posts(UUID.fromString(super.getOwner_id()), super.getRequest_id());
    }
}
