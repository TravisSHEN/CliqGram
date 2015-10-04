package cliq.com.cliqgram.events;

import cliq.com.cliqgram.model.Comment;

/**
 * Created by ilkan on 29/09/2015.
 */
public class CommentSuccessEvent extends BaseEvent {

    Comment comment;

    public CommentSuccessEvent(Comment comment){
        super("Comment was successful!");
        this.comment = comment;
    }
    public CommentSuccessEvent(String message) {
        super(message);
    }

    public Comment getComment() {
        return comment;
    }
}
