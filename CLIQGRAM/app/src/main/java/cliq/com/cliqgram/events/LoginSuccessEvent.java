package cliq.com.cliqgram.events;

/**
 * Created by litaoshen on 3/09/2015.
 */
public class LoginSuccessEvent extends BaseEvent {


    public LoginSuccessEvent() {
        super("Log in was successful !");
    }

    public LoginSuccessEvent(String message) {
        super(message);
    }
}
