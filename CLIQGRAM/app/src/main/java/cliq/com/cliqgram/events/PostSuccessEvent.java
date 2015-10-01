package cliq.com.cliqgram.events;


/**
 * Created by ilkan on 29/09/2015.
 */
public class PostSuccessEvent extends BaseEvent {

    public PostSuccessEvent(){
        super("Post was successful!");
    }
    public PostSuccessEvent(String message) {
        super(message);
    }
}
