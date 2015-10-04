package cliq.com.cliqgram.events;


import cliq.com.cliqgram.model.Post;

/**
 * Created by ilkan on 29/09/2015.
 */
public class PostSuccessEvent extends BaseEvent {

    Post post;

    public PostSuccessEvent(Post post){
        super("Post was successful!");
        this.post = post;
    }
    public PostSuccessEvent(String message) {
        super(message);
    }

    public Post getPost() {
        return post;
    }
}
