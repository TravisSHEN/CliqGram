package cliq.com.cliqgram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by litaoshen on 4/10/2015.
 */
@ParseClassName("UserRelations")
public class UserRelation extends ParseObject{


    public UserRelation() {
        super();
    }

    public List<ParseUser> getFollowings() {
        return this.getList("followings");
    }

    public void setFollowings(List<ParseUser> followings) {
        this.put("followings", followings);
    }

    public List<ParseUser> getFollowers() {
        return this.getList("followers");
    }

    public void setFollowers(List<ParseUser> followers) {
        this.put("followers", followers);
    }

    public String getUsername() {
        return this.getString("username");
    }

    public void setUsername(String username) {
        this.put("username", username);
    }
}
