package cliq.com.cliqgram.events;

/**
 * Created by ilkan on 3/10/2015.
 */
public class UserFailEvent extends BaseEvent {

    public UserFailEvent(){
        super("User fail");
    }
    public UserFailEvent(String message){
        super(message);
    }
}
