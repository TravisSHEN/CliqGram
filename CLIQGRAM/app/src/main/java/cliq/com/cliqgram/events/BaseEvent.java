package cliq.com.cliqgram.events;

/**
 * Created by litaoshen on 3/09/2015.
 */
public abstract class BaseEvent {

    private String message;

    public BaseEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
