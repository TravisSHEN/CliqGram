package cliq.com.cliqgram.events;

import com.parse.ParseUser;

import cliq.com.cliqgram.model.User;

/**
 * Created by ilkan on 3/10/2015.
 */
public class UserSuccessEvent extends BaseEvent{


    ParseUser parseUser;

    public UserSuccessEvent(ParseUser parseUser){
        super("User success");
        this.parseUser = parseUser;
    }
    public UserSuccessEvent(String message){
        super(message);
    }
    public ParseUser getParseUser() {
        return parseUser;
    }

    public void setParseUser(ParseUser parseUser) {
        this.parseUser = parseUser;
    }


}
