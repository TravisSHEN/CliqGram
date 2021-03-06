package cliq.com.cliqgram.services;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cliq.com.cliqgram.app.AppStarter;
import cliq.com.cliqgram.events.UserGetEvent;
import cliq.com.cliqgram.events.UserSuggestionRetrieved;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.model.UserRelation;
import cliq.com.cliqgram.utils.GPSTracker;
import cliq.com.cliqgram.utils.Utils;

/**
 * Created by litaoshen on 30/09/2015.
 */
public class UserService {

    public static final long TIME_THRESHOLD = 4 * 60 * 60000;

    /**
     * @return
     */
    public static User getCurrentUser() {
        return (User) ParseUser.getCurrentUser();
    }

    /**
     *
     */
    public static void logOut() {
        ParseUser.logOut();
    }

    /**
     * @param callback
     */
    public static void getAllUsers(FindCallback<User>
                                           callback) {

        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.include("posts");

        query.findInBackground(callback);

    }

    /**
     * @param userId
     */
    public static void getUserById(String userId) {

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

    /**
     * @param userId
     * @param callback
     */
    public static void getUserById(String userId, GetCallback<ParseUser>
            callback) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.getInBackground(userId, callback);
    }

    /**
     * @param username
     * @param callback
     */
    public static void getUserByUsername(String username,
                                         GetCallback<User> callback
    ) {

        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo("username", username);

        query.getFirstInBackground(callback);
    }

    /**
     * @param userName
     * @return
     */
    public static User findParseUserByName(String userName) {
        ParseQuery<ParseUser> query = User.getQuery();
        ParseUser parseUser = null;
        query.whereEqualTo("username", userName);
        try {
            List<ParseUser> userList = query.find();
            if (userList != null && userList.size() == 1) {
                parseUser = userList.get(0);
            }

        } catch (ParseException e) {
            //TODO exception
        } finally {
            return (User) parseUser;
        }
    }

    public static void getSuggestUsers(final Context mContext) {

        User currentUser = UserService.getCurrentUser();

        UserRelationsService.getRelation(currentUser.getUsername(), new GetCallback<UserRelation>() {
            @Override
            public void done(UserRelation relation, ParseException e) {

                if (relation == null) {
                    return;
                }

                List<User> followings = relation.getFollowings();

                getSuggestUsers(mContext, followings);
            }
        });
    }

    private static void getSuggestUsers(Context mContext, List<User> userList) {

        List<String> usernameList = new ArrayList<>();

        if (userList == null || userList.isEmpty()) {
            return;
        }

        // add usernames to list for query purpose
        for (User user : userList) {
            usernameList.add(user.getUsername());
        }

        Location loc = GPSTracker.getInstance(mContext).getLocation();

        if( loc == null ){
            return;
        }

        final ParseGeoPoint currentLocation = new ParseGeoPoint(loc.getLatitude(),
                loc.getLongitude());

        ParseQuery<UserRelation> query =
                ParseQuery.getQuery(UserRelation.class);
        query.whereContainedIn("username", usernameList);

        // include Objects in following column
        query.include("followings");
//        query.include("followers");

        query.findInBackground(new FindCallback<UserRelation>() {
            @Override
            public void done(List<UserRelation> userRelations, ParseException e) {

                List<User> allFollowings = getFollowingUsers(userRelations);

                List<Post> allLatestPosts = new ArrayList<Post>();

                // test to see all followings to followings of current user
                for (User user : allFollowings) {
                    Log.e("All Followings", user.getUsername());
                    List<Post> postList = user.getPostList();
                    if (postList == null || postList.isEmpty()) {
                        break;
                    }
                    // get latest post
                    // get latest location of users according to their posts
                    Post post = postList.get(postList.size() - 1);
                    allLatestPosts.add(post);
                }

                long currentTime = Utils.getCurrentDate().getTime();
                // sort according to creation time
                Collections.sort(allLatestPosts);

                // TODO : uncomment it when there is enough data
                // trim all posts that are old more than TIME_THRESHOLD
//                int cut_index = 0;
//                for (int i = 0; i < allLatestPosts.size(); i++) {
//                    Post post = allLatestPosts.get(i);
//
//                    if (Math.abs(post.getCreatedAt().getTime() - currentTime)
//                            <= TIME_THRESHOLD) {
//                        cut_index = i;
//                    }
//                }
//
//                 get most recent posts
//                allLatestPosts = allLatestPosts.subList(0, cut_index);

                // sort these posts by location
                Collections.sort(allLatestPosts, new Comparator<Post>() {
                    @Override
                    public int compare(Post lhs, Post rhs) {
                        ParseGeoPoint lhs_loc = lhs.getLocation();
                        ParseGeoPoint rhs_loc = rhs.getLocation();

                        double dist_lhs_current = lhs_loc == null ?
                                Double.MAX_VALUE :
                                lhs_loc.distanceInKilometersTo(currentLocation);
                        double dist_rhs_current = rhs_loc == null ?
                                Double.MAX_VALUE :
                                rhs_loc.distanceInKilometersTo(currentLocation);
                        return (int) (dist_lhs_current - dist_rhs_current);
                    }
                });

                // return a list of user suggestions
                List<User> userSuggestions = new ArrayList<>();
                for (Post post : allLatestPosts) {
                    userSuggestions.add(post.getOwner());
                    Log.e("Suggest Users", post.getOwner().getUsername());
                }

                AppStarter.eventBus.post(new UserSuggestionRetrieved(
                        userSuggestions));

            }
        });
    }

    private static List<User> getFollowingUsers(List<UserRelation>
                                                        userRelationList) {

        List<User> userList = new ArrayList<>();

        if (userRelationList == null) {
            return userList;
        }

        for (UserRelation userRelation : userRelationList) {
            List<User> followings = userRelation.getFollowings();

            if (followings == null || followings.isEmpty()) {
                break;
            }

            for (User user : followings) {
                if (user != null && !userList.contains(user)) {
                    userList.add(user);
                }
            }
        }

        return userList;
    }


}
