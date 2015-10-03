package cliq.com.cliqgram.services;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.events.FollowersSuccessEvent;
import cliq.com.cliqgram.events.UserFailEvent;
import cliq.com.cliqgram.events.UserSuccessEvent;
import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.utils.Params;
import cliq.com.cliqgram.utils.ParseObject2ModelConverter;

/**
 * Created by litaoshen on 30/09/2015.
 */
public class UserService {

    private User user;
    private static UserService userService = new UserService();

    public static User getCurrentUser() {

        return userService.getUser();
    }

    public static void setCurrentUser() {

        ParseUser parseUser = ParseUser.getCurrentUser();
        userService.setUser(User.userFactory());

        userService.getUser().setUserId(parseUser.getObjectId());
        userService.getUser().setUsername(parseUser.getUsername());
        userService.getUser().setEmail(parseUser.getEmail());
        userService.getUser().setAvatarData((byte[]) parseUser.get(Params.FIELD_AVATAR));
        //get activities, followers and followings with another method, this is expensive
        //userService.getUser().setActivities((List<Activity>) parseUser.get("activities"));
        //userService.getUser().setFollowerList((List<User>) parseUser.get("followers"));
        //userService.getUser().setFollowingList((List<User>) parseUser.get("followings"));

    }

    public static void findParseUserByName(String userName){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(Params.FIELD_USER_NAME, userName);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && objects != null && objects.size() == 1) {
                    ParseUser parseUser = objects.get(0);
                    AppStarter.eventBus.post(new UserSuccessEvent(parseUser));
                } else {
                    AppStarter.eventBus.post(new UserFailEvent("User fail - " + e.getMessage()));
                }
            }
        });
    }

    public static void findParseUserById(String userId){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(userId, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if(e == null && object != null){
                    ParseUser parseUser = object;
                    AppStarter.eventBus.post(new UserSuccessEvent(parseUser));
                }else{
                    AppStarter.eventBus.post(new UserFailEvent("User fail - " + e.getMessage()));
                }
            }
        });
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
