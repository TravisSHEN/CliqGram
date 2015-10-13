package cliq.com.cliqgram.events;

/**
 * Created by ilkan on 29/09/2015.
 */
public class CommentFailEvent extends BaseEvent {

    public CommentFailEvent(){
        super("Comment failed!");
    }
    public CommentFailEvent(String message) {
        super(message);
    }
}
