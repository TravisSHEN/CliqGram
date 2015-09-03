package cliq.com.cliqgram.events;

/**
 * Created by litaoshen on 3/09/2015.
 */
public class SignupSuccessEvent extends BaseEvent {

    public SignupSuccessEvent() {
        super("Sign up successfully !");
    }

    public SignupSuccessEvent(String message) {
        super(message);
    }
}
