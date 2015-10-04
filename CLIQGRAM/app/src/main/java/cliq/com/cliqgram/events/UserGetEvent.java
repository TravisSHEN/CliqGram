package cliq.com.cliqgram.events;

import cliq.com.cliqgram.model.User;

/**
 * Created by litaoshen on 4/10/2015.
 */
public class UserGetEvent extends BaseEvent{

    User user;

    public UserGetEvent(User user) {
        super("Get user success");
        this.user = user;
    }

    public UserGetEvent(String message) {
        super(message);
    }

    public User getUser() {
        return user;
    }
}
