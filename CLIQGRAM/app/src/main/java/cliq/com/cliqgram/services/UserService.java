package cliq.com.cliqgram.services;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cliq.com.cliqgram.events.UserGetEvent;
import cliq.com.cliqgram.events.UserSuggestionRetrieved;
import cliq.com.cliqgram.helper.FollowerCounter;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.model.UserRelation;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.utils.ImageUtil;

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

    public static User findUserByName(String userName) {
        ParseQuery<ParseUser> query = User.getQuery();
        ParseUser parseUser = null;
        query.whereEqualTo("username", userName);
        query.include("posts");
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

    public static void getSuggestUsers() {
        User currentUser = UserService.getCurrentUser();

        UserRelationsService.getRelation(currentUser.getUsername(), new GetCallback<UserRelation>() {
            @Override
            public void done(UserRelation relation, ParseException e) {

                if (relation == null) {
                    return;
                }

                List<User> followings = relation.getFollowings();

                getSuggestUsers(followings);
            }
        });
    }

    private static void getSuggestUsers(List<User> userList) {

        List<String> usernameList = new ArrayList<>();

        if (userList == null || userList.isEmpty()) {
            return;
        }

        // add usernames to list for query purpose
        for (User user : userList) {
            usernameList.add(user.getUsername());
        }

        Location loc = AppStarter.gpsTracker.getLocation();

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

                long currentTime = ImageUtil.getCurrentDate().getTime();
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

    public static void getSuggestedUserList() {

        Location currentLocation = AppStarter.gpsTracker.getLocation();
        getPeopleNearBy(currentLocation, new FindCallback<User>() {
            @Override
            public void done(final List<User> sugList, ParseException e) {
                getMostPopularPeople(new FindCallback<UserRelation>() {
                    @Override
                    public void done(List<UserRelation> objects, ParseException e) {
                        if (e == null) {
                            List<FollowerCounter> fCounterList = new ArrayList<FollowerCounter>();
                            for (UserRelation relation : objects) {
                                int size = 0;
                                if (relation.getFollowers() != null) {
                                    size = relation.getFollowers().size();
                                }
                                FollowerCounter fc = new FollowerCounter(findUserByName(relation.getUsername()), size);
                                fCounterList.add(fc);
                            }

                            for (int i = 0; i <= 100 && i < fCounterList.size(); i++) {
                                if (fCounterList.get(i) != null) {
                                    FollowerCounter fc = fCounterList.get(i);
                                    User user = fc.getUser();
                                    sugList.add(user);
                                } else {
                                    break;
                                }

                            }

                            User currentUser = UserService.getCurrentUser();

                            if (currentUser == null) {
                                return;
                            }

                            UserRelationsService.getRelation(currentUser
                                    .getUsername(), new GetCallback<UserRelation>() {
                                @Override
                                public void done(UserRelation userRelation,
                                                 ParseException e) {

                                    List<User> followings = userRelation.getFollowings();
//                                    followings = getFollowingsFollowings(followings);
                                    getFollowingsFollowings(followings, new
                                            GetCallback<UserRelation>() {
                                                @Override
                                                public void done(UserRelation
                                                                         relation, ParseException e) {
                                                    if (e == null && relation
                                                            .getFollowings()
                                                            != null) {
                                                        List<User> sugUserList = new ArrayList<>();
                                                        List<User> followings2 = relation.getFollowings();
                                                        sugUserList.addAll(followings2);

                                                        sugList.addAll(sugUserList);
                                                        Set<User> hs = new HashSet<>();
                                                        hs.addAll(sugList);
                                                        sugList.clear();
                                                        sugList.addAll(hs);
                                                        sugList.removeAll(sugUserList);
                                                        AppStarter.eventBus.post(new UserSuggestionRetrieved(sugList));
                                                    } else {

                                                    }

                                                }
                                            });

                                }
                            });

                        }
                    }
                });
            }
        });


    }


    public static void getFollowingsFollowings(List<User> followings,
                                               GetCallback<UserRelation> callback) {
        if (followings == null) {
            return;
        }
        for (int i = 0; i < followings.size(); i++) {
            User following = followings.get(i);

            UserRelationsService.getRelation(following.getUsername(), callback);
        }
//
//            UserRelationsService.getRelation(following.getUsername(), new GetCallback<UserRelation>() {
//                @Override
//                public void done(UserRelation relation, ParseException e) {
//
//                    if (e == null) {
//
//                        List<User> sugUserList = new ArrayList<User>();
//                        List<User> followings2 = relation.getFollowings();
//                        sugUserList.addAll(followings2);
//
//                    } else {
//
//                    }
//
//                }
//            });
    }

    public static void getMostPopularPeople(FindCallback<UserRelation> callback) {

        ParseQuery<UserRelation> query = ParseQuery.getQuery(UserRelation.class);
        query.findInBackground(callback);
    }

    public static void getPeopleNearBy(Location currentLocation, FindCallback<User> callback) {
        ParseQuery<Post> innerQuery = ParseQuery.getQuery(Post.class);
        ParseGeoPoint geoPoint = new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
        innerQuery.whereWithinMiles("location", geoPoint, 100);
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereMatchesQuery("posts", innerQuery);
        query.include("posts");
        query.findInBackground(callback);

    }
}
