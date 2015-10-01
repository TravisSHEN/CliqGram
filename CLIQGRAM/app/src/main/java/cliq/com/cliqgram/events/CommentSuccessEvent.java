package cliq.com.cliqgram.events;

/**
 * Created by ilkan on 29/09/2015.
 */
public class CommentSuccessEvent extends BaseEvent {

    public CommentSuccessEvent(){
        super("Comment was successful!");
    }
    public CommentSuccessEvent(String message) {
        super(message);
    }
}
