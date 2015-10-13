package cliq.com.cliqgram.events;

import cliq.com.cliqgram.model.Post;

/**
 * Created by litaoshen on 27/09/2015.
 */
public class OpenCommentEvent extends BaseEvent{

    Post post;

    public OpenCommentEvent(Post post){
        super("Open Comment Event");
        this.post = post;
    }
    public OpenCommentEvent(String message) {
        super(message);
    }

    public Post getPost() {
        return post;
    }
}
