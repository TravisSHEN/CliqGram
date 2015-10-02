package cliq.com.cliqgram.services;

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

import cliq.com.cliqgram.events.GetPostEvent;
import cliq.com.cliqgram.events.PostFailEvent;
import cliq.com.cliqgram.events.PostSuccessEvent;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by ilkan on 26/09/2015.
 */
public class PostService {

    public static final String TABLENAME = "Post";

    public static void post(@NonNull Post post) {

        ParseUser currentUser = ParseUser.getCurrentUser();

        if( post.getOwner() != null && ! post.getOwner().getUsername().equals(
                currentUser.getUsername())){
            return;
        }

        ParseObject postObject = new ParseObject(TABLENAME);
        //ParseFile photo = new ParseFile("image.jpg", post.getPhotoData());
        String imgLabel = "img_" + post.getOwner().getUsername() + String.valueOf(post
                .getCreatedAt().getTime()) +
                ".jpg";
        ParseFile photo = new ParseFile(imgLabel, post.getPhotoData());
        photo.saveInBackground();

        postObject.put("photo", photo);
        postObject.put("description", post.getDescription());
        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(post.getLocation()
                .getLatitude(), post.getLocation().getLongitude());
        postObject.put("location", parseGeoPoint);
        // creates one-to-one relationship
        // associate to current user
        postObject.put("user", ParseUser.getCurrentUser());
        postObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null ){
                    Log.e("Post Service", "post successful");
                } else {

                    Log.e("Post Service", e.getMessage());
                }
            }
        });

        ParseUser.getCurrentUser().put("posts", postObject);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    AppStarter.eventBus.post(new PostSuccessEvent("post was " +
                            "successful!"));
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
    public static void getPost(@NonNull final String id) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLENAME);

        Log.d("POST SERVICE", TABLENAME);
        Log.d("POST SERVICE", id);
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    User user = (User) object.get("user");
                    Post post = Post.createPost(object.getBytes("photo"),
                            user,
                            object.getString("text"));

                    post.setPostId( object.getObjectId() );

                    post.setCommentList(object.<Comment>getList("comments"));

                    Log.d("POST SERVICE", post.getPostId());

                    AppStarter.eventBus.post(new GetPostEvent(post));
                } else {
                    Log.d("POST SERVICE", e.getMessage());
                    AppStarter.eventBus.post(new GetPostEvent(null, false));
                }
            }
        });
    }

}
