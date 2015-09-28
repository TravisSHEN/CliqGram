package cliq.com.cliqgram.events;

/**
 * Created by litaoshen on 27/09/2015.
 */
public class OpenCommentEvent extends BaseEvent{
    public OpenCommentEvent(){
        super("Open Comment Event");
    }
    public OpenCommentEvent(String message) {
        super(message);
    }
}
