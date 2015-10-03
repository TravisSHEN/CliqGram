package cliq.com.cliqgram.events;

import com.parse.Parse;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by ilkan on 3/10/2015.
 */
public class FollowersSuccessEvent extends BaseEvent {

    private List<ParseUser> followers;

    public FollowersSuccessEvent(List<ParseUser> followers){
        super("Followers success");
        this.followers = followers;
    }

    public List<ParseUser> getFollowers() {
        return followers;
    }

    public void setFollowers(List<ParseUser> followers) {
        this.followers = followers;
    }
}
