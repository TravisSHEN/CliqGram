package cliq.com.cliqgram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
    public void setPost(Post post){
        this.put("post", post);
    }
    public User getUser(){
        ParseUser parseUser = this.getParseUser("owner");
        User owner = UserService.getUserFromParseUser(parseUser);
        return owner;
    }
    public void setUser(User user){
        ParseUser currentUser = ParseUser.getCurrentUser();
        this.put("owner", currentUser);
    }
}
