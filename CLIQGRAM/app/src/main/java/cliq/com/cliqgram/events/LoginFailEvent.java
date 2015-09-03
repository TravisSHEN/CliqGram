package cliq.com.cliqgram.events;

/**
 * Created by litaoshen on 3/09/2015.
 */
public class LoginFailEvent extends BaseEvent {


    public LoginFailEvent() {
        super("Log in failed !");
    }

    public LoginFailEvent(String message) {
        super(message);
    }
}
