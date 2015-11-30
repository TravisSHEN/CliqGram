package cliq.com.cliqgram.services;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.app.AppStarter;
import cliq.com.cliqgram.events.GetPostEvent;
import cliq.com.cliqgram.events.PostFailEvent;
import cliq.com.cliqgram.events.PostSuccessEvent;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;

/**
 * Created by ilkan on 26/09/2015.
 */
public class PostService {

    public static final String TABLE_NAME = "Post";

    public static void post(@NonNull final Post post) {

        User currentUser = UserService.getCurrentUser();

        if (post.getOwner() == null || !post.getOwner().getUsername().equals(
                currentUser.getUsername())) {
            return;
        }

        ArrayList<Post> relation = (ArrayList) currentUser.getList("posts");
        if (relation == null) {
            relation = new ArrayList<>();
        }
        relation.add(post);
        currentUser.put("posts", relation);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    AppStarter.eventBus.post(new PostSuccessEvent(post));
                } else {
                    AppStarter.eventBus.post(new PostFailEvent("post failed -" +
                            " " + e.getMessage()));
                }
            }
        });

    }


    /**
     * get post by id
     * @param id
     */
    public static void getPost(@NonNull final String id) {

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include("user");
        query.include("comments");
        query.include("likes");

        query.getInBackground(id, new GetCallback<Post>() {
            @Override
            public void done(Post post, ParseException e) {
                if (e == null) {

                    AppStarter.eventBus.post(new GetPostEvent(post));
                } else {
                    Log.d("POST SERVICE", e.getMessage());
                    AppStarter.eventBus.post(new GetPostEvent(null, false));
                }
            }
        });
    }

    /**
     * get all posts of users
     * @param userList
     */
    public static void getPosts(List<User> userList) {

        if (userList == null || userList.size() <= 0) {
            return;
        }

//        Location loc = GPSTracker.getInstance(mContext).getLocation();
        Location loc = null;

        ParseGeoPoint currentLocation;
        try{

            currentLocation = new ParseGeoPoint(loc.getLatitude(),
                    loc.getLongitude());
        }catch(Exception e){
            //TODO write the error in log
            currentLocation = null;
        }

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereContainedIn("user", userList);
        // order by createdAt
        query.orderByDescending("createdAt");
        if( currentLocation != null ) {
            // order by location
            query.whereNear("location", currentLocation);
        }
        query.include("user");
        query.include("comments");
        query.include("likes");

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    List<Post> postList = objects;

                    AppStarter.eventBus.post(new GetPostEvent(postList));
                } else {

                    AppStarter.eventBus.post(new GetPostEvent(null, false));
                }
            }
        });
    }

    public static void getPosts(User user){

        if( user == null){
            return;
        }

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("user", user);
        // order by createdAt
        query.orderByDescending("createdAt");

        query.include("user");
        query.include("comments");
        query.include("likes");

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> postList, ParseException e) {
                if (e == null) {

                    AppStarter.eventBus.post(new GetPostEvent(postList));
                } else {

                    AppStarter.eventBus.post(new GetPostEvent(null, false));
                }
            }
        });
    }
}
