package cliq.com.cliqgram.events;

/**
 * Created by litaoshen on 3/09/2015.
 */
public class SignupFailEvent extends BaseEvent {

    public SignupFailEvent() {
        super("Sign up failed !");
    }

    public SignupFailEvent(String message) {
        super(message);
    }
}
