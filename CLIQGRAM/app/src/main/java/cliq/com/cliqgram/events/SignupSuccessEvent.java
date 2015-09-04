package cliq.com.cliqgram.events;

/**
 * Created by litaoshen on 3/09/2015.
 */
public class SignupSuccessEvent extends BaseEvent {

    String username, password;

    public SignupSuccessEvent() {
        super("Sign up successfully !");
    }

    public SignupSuccessEvent(String message, String username, String password) {
        super(message);
        this.username = username;
        this.password = password;
    }

    public SignupSuccessEvent(String message) {
        super(message);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
