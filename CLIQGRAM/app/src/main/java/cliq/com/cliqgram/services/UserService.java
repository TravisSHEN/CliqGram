package cliq.com.cliqgram.services;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.events.UserGetEvent;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by litaoshen on 30/09/2015.
 */
public class UserService {

    private User user = User.userFactory();
    private static UserService userService = new UserService();

    public static void getUser(String userId){

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.include("posts");
        query.getInBackground(userId, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if( e == null ){

                    List<Post> postList = object.getList("posts");
                    ParseFile avatar = object.getParseFile("avatar");
                    byte[] avatarData = new byte[0];
                    if (avatar != null ) {
                        try {
                            avatarData = avatar.getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }

                    User user = UserService.getUserFromParseUser(object);
                    user.setPostList(postList);
                    user.setAvatarData(avatarData);

                    AppStarter.eventBus.post(new UserGetEvent(user));
                } else {

                    AppStarter.eventBus.post(new UserGetEvent("Fail to get " +
                            "user - " + e.getMessage()));
                }
            }
        });
    }

    public static void getUser( String userId, GetCallback<ParseUser> callback ) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.getInBackground(userId, callback);
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

    public static User getCurrentUser() {

        ParseUser parseUser = ParseUser.getCurrentUser();

        userService.getUser().setUserId(parseUser.getObjectId());
        userService.getUser().setUsername(parseUser.getUsername());
        userService.getUser().setEmail(parseUser.getEmail());

        return userService.getUser();
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
