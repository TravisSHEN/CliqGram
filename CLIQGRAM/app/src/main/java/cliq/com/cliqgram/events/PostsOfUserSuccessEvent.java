package cliq.com.cliqgram.events;

import java.util.List;

import cliq.com.cliqgram.model.Post;

/**
 * Created by ilkan on 3/10/2015.
 */
public class PostsOfUserSuccessEvent extends BaseEvent{

    List<Post> postList;

    public PostsOfUserSuccessEvent(List<Post> postList){
        super("Posts of User Success");
        this.postList = postList;
    }

    public PostsOfUserSuccessEvent(String message){
        super(message);
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

}
