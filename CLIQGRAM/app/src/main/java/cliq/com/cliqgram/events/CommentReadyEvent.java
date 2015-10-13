package cliq.com.cliqgram.events;

import cliq.com.cliqgram.model.Comment;

/**
 * Created by litaoshen on 4/10/2015.
 */
public class CommentReadyEvent extends BaseEvent{
    Comment comment;
    public CommentReadyEvent(Comment comment){
        super("Comment ready");
        this.comment = comment;
    }

    public CommentReadyEvent(String message) {
        super(message);
    }

    public Comment getComment() {
        return comment;
    }
}
