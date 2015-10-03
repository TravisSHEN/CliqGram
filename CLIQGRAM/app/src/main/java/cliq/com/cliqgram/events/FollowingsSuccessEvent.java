package cliq.com.cliqgram.events;

import com.parse.ParseUser;

import java.util.List;

/**
 * Created by ilkan on 3/10/2015.
 */
public class FollowingsSuccessEvent extends BaseEvent{

    private List<ParseUser> followings;

    public FollowingsSuccessEvent(List<ParseUser> followings){
        super("Following list success");
        this.followings = followings;
    }

    public List<ParseUser> getFollowings() {
        return followings;
    }

    public void setFollowings(List<ParseUser> followings) {
        this.followings = followings;
    }

}
