package cliq.com.cliqgram.model;

import com.parse.ParseUser;

/**
 * Created by ilkan on 27/09/2015.
 */
public class Like {

    private String likeId;
    private Post post;
    private ParseUser user;

    public Like(Post post, ParseUser user){
        this.post = post;
        this.user = user;
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
    public ParseUser getUser(){
        return this.user;
    }
    public void setUser(ParseUser user){
        this.user = user;
    }

}
