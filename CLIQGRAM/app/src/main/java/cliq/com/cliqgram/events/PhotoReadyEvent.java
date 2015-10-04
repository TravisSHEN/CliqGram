package cliq.com.cliqgram.events;

import cliq.com.cliqgram.model.Post;

/**
 * Created by litaoshen on 4/10/2015.
 */
public class PhotoReadyEvent extends BaseEvent {
    Post post;

    public PhotoReadyEvent(Post post) {
        super("Photo ready");
        this.post = post;
    }

    public PhotoReadyEvent(String message) {
        super(message);
    }

    public Post getPost() {
        return post;
    }
}

