package cliq.com.cliqgram.services;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.events.BaseEvent;
import cliq.com.cliqgram.events.GetPostEvent;
import cliq.com.cliqgram.events.PostFailEvent;
import cliq.com.cliqgram.events.PostSuccessEvent;
import cliq.com.cliqgram.events.PostsOfUserFailEvent;
import cliq.com.cliqgram.events.PostsOfUserSuccessEvent;
import cliq.com.cliqgram.events.UserFailEvent;
import cliq.com.cliqgram.events.UserSuccessEvent;
import cliq.com.cliqgram.helper.ProgressSpinner;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.utils.Params;
import cliq.com.cliqgram.utils.ParseObject2ModelConverter;
import de.greenrobot.event.Subscribe;

/**
 * Created by ilkan on 26/09/2015.
 */
public class PostService {

    public static void post(@NonNull Post post) {

        ParseObject postObject = new ParseObject(Params.TABLE_POST);
        String imgLabel = Params.IMAGE_PREFIX + post.getOwner().getUsername() + String.valueOf(post
                .getCreatedAt().getTime()) + Params.IMAGE_SUFFIX;
        ParseFile photo = new ParseFile(imgLabel, post.getPhotoData());
        photo.saveInBackground();

        postObject.put(Params.FIELD_PHOTO, photo);
        postObject.put(Params.FIELD_DESCRIPTION, post.getDescription());
        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(post.getLocation()
                .getLatitude(), post.getLocation().getLongitude());
        postObject.put(Params.FIELD_LOCATION, parseGeoPoint);
        // creates one-to-one relationship
        // associate to current user
        postObject.put(Params.FIELD_USER, ParseUser.getCurrentUser());
        postObject.saveInBackground();

        ArrayList<ParseObject> posts = (ArrayList)ParseUser.getCurrentUser().get(Params.FIELD_POSTS);
        if(posts == null){
            posts = new ArrayList<>();
        }
        posts.add(postObject);

        //post event should be pushed as an activity as well
        ParseObject activityObject = new ParseObject(Params.TABLE_ACTIVITY);
        activityObject.put(Params.FIELD_ACTIVITY_TYPE, Params.ACTIVITY_POST);
        activityObject.put(Params.FIELD_USER, ParseUser.getCurrentUser());
        activityObject.put(Params.FIELD_EVENT_ID, postObject.getObjectId());

        ArrayList<ParseObject> activities = (ArrayList)ParseUser.getCurrentUser().get(Params.FIELD_ACTIVITIES);
        if(activities == null){
            activities = new ArrayList<>();
        }
        activities.add(activityObject);

        ParseUser.getCurrentUser().put(Params.FIELD_POSTS, posts);
        ParseUser.getCurrentUser().put(Params.FIELD_ACTIVITIES, activities);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    AppStarter.eventBus.post(new PostSuccessEvent("post was successful!"));
                } else {
                    AppStarter.eventBus.post(new PostFailEvent("post failed -" +
                            " " + e.getMessage()));
                }
            }
        });

    }


    /**
     * @param id
     */
    public static void getPost(@NonNull final String postId) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Params.TABLE_POST);

        Log.d("POST SERVICE", Params.TABLE_POST);
        Log.d("POST SERVICE", postId);
        query.getInBackground(postId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    User user = (User) object.get(Params.FIELD_USER);
                    Post post = Post.createPost(object.getBytes(Params.FIELD_PHOTO),
                            user,
                            object.getString(Params.FIELD_TEXT));

                    post.setPostId( object.getObjectId() );

                    post.setCommentList(object.<Comment>getList(Params.FIELD_COMMENTS));

                    Log.d("POST SERVICE", post.getPostId());

                    AppStarter.eventBus.post(new GetPostEvent(post));
                } else {
                    Log.d("POST SERVICE", e.getMessage());
                    AppStarter.eventBus.post(new GetPostEvent(null, false));
                }
            }
        });
    }

    public static void getPostsOfUser(String userId){

        UserService.findParseUserById(userId);
    }

    @Subscribe
    public void onEvent(final BaseEvent baseEvent) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressSpinner.getInstance().dismissSpinner();
                if (baseEvent instanceof UserSuccessEvent) {
                    ParseUser parseUser = ((UserSuccessEvent) baseEvent).getParseUser();
                    ArrayList<ParseObject> objList = (ArrayList<ParseObject>)parseUser.get(Params.FIELD_POSTS);
                    List<Post> postList = new ArrayList<Post>();
                    for (ParseObject obj : objList) {
                       postList.add(ParseObject2ModelConverter.toPost(obj));
                    }
                    AppStarter.eventBus.post(new PostsOfUserSuccessEvent(postList));
                }else if (baseEvent instanceof UserFailEvent){
                    AppStarter.eventBus.post(new PostsOfUserFailEvent());
                }
            }
        }, 2000);
    }

}
