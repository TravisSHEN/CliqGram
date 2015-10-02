package cliq.com.cliqgram.events;

import android.support.annotation.Nullable;

import cliq.com.cliqgram.model.Post;

/**
 * Created by litaoshen on 1/10/2015.
 */
public class GetPostEvent extends BaseEvent {

    Post post;
    boolean success;

    public GetPostEvent(Post post){
        super("Get post successfully");
        this.post = post;
        this.success = true;
    }

    public GetPostEvent(@Nullable Post post, boolean success) {
        super("");
        if(success){
            this.setMessage("Get post successfully");
            this.post = post;
            this.success = success;
        } else {

            this.setMessage("Get post failed");
            this.success = false;
        }
    }
    public GetPostEvent(String message) {
        super(message);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
