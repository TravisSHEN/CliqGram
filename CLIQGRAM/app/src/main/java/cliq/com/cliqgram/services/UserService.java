package cliq.com.cliqgram.services;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.model.Post;
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
        userService.getUser().setAvatarData(parseUser.getBytes("avatar"));
//        userService.getUser().setActivities((List<Activity>) parseUser.get("activities"));
        //userService.getUser().setFollowerList((List<User>) parseUser.get("followers"));
        //userService.getUser().setFollowingList((List<User>) parseUser.get("followings"));
    }


    public static ParseUser findParseUserByName(String userName){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        ParseUser parseUser = null;
        query.whereEqualTo("username", userName);
        try {
            List<ParseUser> userList = query.find();
            if (userList != null && userList.size() == 1){
                parseUser = userList.get(0);
            }

        }catch (ParseException e){
            //TODO exception
        }
        finally {
            return parseUser;
        }
    }


    public static User getUserFromParseUser(ParseUser parseUser) {
        if(parseUser == null ){
            return null;
        }
        User user = User.userFactory();

        user.setUserId(parseUser.getObjectId());
        user.setUsername(parseUser.getUsername());
        user.setEmail(parseUser.getEmail());
        user.setAvatarData((byte[]) parseUser.get("avatar"));
        user.setActivities((List<Activity>) parseUser.get("activities"));
        user.setPostList((List<Post>) parseUser.get("posts"));
        user.setFollowerList((List<User>) parseUser.get("followers"));
        user.setFollowingList((List<User>) parseUser.get("followings"));

        return user;
    }


    public static ArrayList<String> getUserIdList(List<ParseUser> userList) {
        if( userList == null || userList.size() <= 0){
            return null;
        }
        ArrayList<String> userIdList = new ArrayList<>();
        for (ParseUser user : userList) {
            userIdList.add(user.getObjectId());
        }

        return userIdList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
