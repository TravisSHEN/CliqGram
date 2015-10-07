package cliq.com.cliqgram.services;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import cliq.com.cliqgram.events.UserGetEvent;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by litaoshen on 30/09/2015.
 */
public class UserService {

    public static User getCurrentUser() {
        return (User) ParseUser.getCurrentUser();
    }

    public static void logOut(){
        ParseUser.logOut();
    }

    public static void getAllUsers(FindCallback<User>
            callback){

        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.include("posts");

        query.findInBackground(callback);

    }

    public static void getUserById(String userId){

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.include("posts");
        query.getInBackground(userId, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    AppStarter.eventBus.post(new UserGetEvent((User)
                            parseUser));
                } else {

                    AppStarter.eventBus.post(new UserGetEvent("Fail to get " +
                            "user - " + e.getMessage()));
                }
            }
        });
    }

    public static void getUserById(String userId, GetCallback<ParseUser>
            callback) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.getInBackground(userId, callback);
    }

    public static void getUserByUsername( String username,
                                         GetCallback<ParseUser> callback
    ) {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);

        query.getFirstInBackground(callback);
    }

    public static User findParseUserByName(String userName){
        ParseQuery<ParseUser> query = User.getQuery();
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
            return (User) parseUser;
        }
    }

//    public static User getCurrentUser() {
//
//        ParseUser parseUser = ParseUser.getCurrentUser();
//
//        userService.getUser().setUserId(parseUser.getObjectId());
//        userService.getUser().setUsername(parseUser.getUsername());
//        userService.getUser().setEmail(parseUser.getEmail());
//
//        return userService.getUser();
//    }

//    public static User getUserFromParseUser(ParseUser parseUser) {
//        if(parseUser == null ){
//            return null;
//        }
//        User user = User.userFactory();
//
//        user.setUserId(parseUser.getObjectId());
//        user.setUsername(parseUser.getUsername());
//        user.setEmail(parseUser.getEmail());
//        user.setAvatarData((byte[]) parseUser.get("avatar"));
//
//        return user;
//    }

//    public static ArrayList<String> getUserIdList(List<ParseUser> userList) {
//        if( userList == null || userList.size() <= 0){
//            return null;
//        }
//        ArrayList<String> userIdList = new ArrayList<>();
//        for (ParseUser user : userList) {
//            userIdList.add(user.getObjectId());
//        }
//
//        return userIdList;
//    }

//    public User getUser() {
//        return user;
//    }

//    public void setUser(User user) {
//        this.user = user;
//    }
}
