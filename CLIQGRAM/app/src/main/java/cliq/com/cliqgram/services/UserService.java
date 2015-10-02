package cliq.com.cliqgram.services;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.model.User;

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
        userService.getUser().setAvatarData((byte[]) parseUser.get("avatar"));
        userService.getUser().setActivities((List<Activity>) parseUser.get("activities"));
        userService.getUser().setFollowerList((List<User>) parseUser.get("followers"));
        userService.getUser().setFollowingList((List<User>) parseUser.get("followings"));


    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    /**
     * follow other users
     *
     * @param username
     */
    public void follow(String username) {
        final ParseUser currentUser = ParseUser.getCurrentUser();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);


        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if (e == null) {
                    ParseUser user = objects.get(0);

                    // current user is one of followers of user
                    ArrayList<ParseUser> followers = (ArrayList<ParseUser>) user.get("followers");
                    if (followers == null) {
                        followers = new ArrayList<>();
                    }
                    followers.add(currentUser);
                    user.saveInBackground();


                    // current user start following user
                    ArrayList<ParseUser> following = (ArrayList<ParseUser>) currentUser.get("followings");
                    if (following == null) {
                        following = new ArrayList<>();
                    }
                    following.add(user);
                    currentUser.saveInBackground();
                } else {

                }
            }
        });
    }
}
