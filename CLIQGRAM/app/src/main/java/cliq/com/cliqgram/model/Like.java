package cliq.com.cliqgram.model;

/**
 * Created by ilkan on 27/09/2015.
 */
public class Like {

    private String likeId;
    private Post post;
    private User user;


    public Like(Post post, User user){
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
    public User getUser(){
        return this.user;
    }
    public void setUser(User user){
        this.user = user;
    }
}
