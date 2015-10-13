package cliq.com.cliqgram.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import cliq.com.cliqgram.services.LikeService;
import cliq.com.cliqgram.services.UserService;

/**
 * Created by ilkan on 27/09/2015.
 */
@ParseClassName("Like")
public class Like extends ParseObject{

    public static Like createLike(Post post, User user){
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);

        like.saveInBackground();
        LikeService.like(post, like);
        return like;
    }

    public Like(){
       super();
    }


    public Post getPost(){
        Post post = (Post) this.getParseObject("post");
        return post;
    }
    private void setPost(Post post){
        this.put("post", post);
    }
    public User getUser(){
        User owner = null;
        try {
            owner = (User) this.fetchIfNeeded().getParseUser("owner");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return owner;
    }
    private void setUser(User user){
        User currentUser = UserService.getCurrentUser();
        this.put("owner", currentUser);
    }
}
