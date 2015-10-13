package cliq.com.cliqgram.events;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.model.User;

/**
 * Created by litaoshen on 12/10/2015.
 */
public class UserSuggestionRetrieved extends BaseEvent{

    List<User> userSuggestionList = new ArrayList<>();

    public UserSuggestionRetrieved(List<User> userSuggestionList) {
        super("Get suggested users");
        this.userSuggestionList = userSuggestionList;
    }

    public UserSuggestionRetrieved(String message) {
        super(message);
    }

    public List<User> getUserSuggestionList() {
        return userSuggestionList;
    }

    public void setUserSuggestionList(List<User> userSuggestionList) {
        this.userSuggestionList = userSuggestionList;
    }
}
