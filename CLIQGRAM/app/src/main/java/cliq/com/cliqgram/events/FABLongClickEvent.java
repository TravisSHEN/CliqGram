package cliq.com.cliqgram.events;

/**
 * Created by litaoshen on 24/09/2015.
 */
public class FABLongClickEvent extends BaseEvent{

    public FABLongClickEvent(){
        super("FAB button long clicked");

    }

    public FABLongClickEvent(String message) {
        super(message);
    }
}
