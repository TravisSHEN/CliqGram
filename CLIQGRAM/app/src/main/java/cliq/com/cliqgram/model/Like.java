package cliq.com.cliqgram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by ilkan on 27/09/2015.
 */
@ParseClassName("Like")
public class Like extends ParseObject{

    private String likeId;
    private Post post;
    private User user;


    public static Like createLike(Post post, User user){
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);

        return like;
    }

    public Like(){
       super();
    }


    public String getLikeId() {
        return likeId;
    }
    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }
    public Post getPost(){
        return this.post;
    }
    public void setPost(Post post){
        this.post = post;
    }
    public User getUser(){
        return this.user;
    }
    public void setUser(User user){
        this.user = user;
    }
}
