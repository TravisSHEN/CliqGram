package cliq.com.cliqgram.events;

/**
 * Created by litaoshen on 23/09/2015.
 */
public class FABClickEvent extends BaseEvent{

    public FABClickEvent(){
        super("FAB button clicked");

    }

    public FABClickEvent(String message) {
        super(message);
    }
}
