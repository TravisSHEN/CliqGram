package cliq.com.cliqgram.events;

/**
 * Created by ilkan on 29/09/2015.
 */
public class PostFailEvent extends BaseEvent {

    public PostFailEvent(){
        super("Post failed!");
    }
    public PostFailEvent(String message) {
        super(message);
    }
}
