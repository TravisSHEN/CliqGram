package cliq.com.cliqgram.events;

/**
 * Created by litaoshen on 3/09/2015.
 */
public class LoginSuccessfulEvent extends BaseEvent {


    public LoginSuccessfulEvent() {
        super("Log in successfully !");
    }

    public LoginSuccessfulEvent(String message) {
        super(message);
    }
}
