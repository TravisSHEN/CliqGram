package cliq.com.cliqgram.events;

/**
 * Created by ilkan on 3/10/2015.
 */
public class ActivityFailEvent extends BaseEvent{

    public ActivityFailEvent(String message){
        super(message);
    }
    public ActivityFailEvent(){
        super("Activity failed");
    }
}
