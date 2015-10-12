package cliq.com.cliqgram.events;

/**
 * Created by ilkan on 11/10/2015.
 */
public class ActivityFailEvent extends BaseEvent{

    public ActivityFailEvent(){
        super("Activity record operation failed!");
    }
    public ActivityFailEvent(String message){
        super(message);
    }
}
