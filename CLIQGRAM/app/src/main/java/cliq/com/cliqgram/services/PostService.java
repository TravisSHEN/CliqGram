package cliq.com.cliqgram.services;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import cliq.com.cliqgram.events.PostFailEvent;
import cliq.com.cliqgram.events.PostSuccessEvent;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by ilkan on 26/09/2015.
 */
public class PostService {

    public static final String tableName = "Post";

    public static void post(final Post post) {


        final ParseObject postObject = new ParseObject(tableName);
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
        postObject.put("user", post.getOwner());//creates one-to-one relationship
        postObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    post.setPostId( postObject.getObjectId() );
                    AppStarter.eventBus.post(new PostSuccessEvent("post was " +
                            "successful!"));
                } else {
                    AppStarter.eventBus.post(new PostFailEvent("post failed -" +
                            " " +
                            e.getMessage()));
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
                            " " +
                            e.getMessage()));
                }
            }
        });

    }


    public static void getPost(String id){

        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e ==null){
                    User user = UserService.getUserFromParseUser((ParseUser)
                                            object.get("user"));
                    Post post = Post.createPost(object.getBytes("photo"),
                            user,
                            object.getString("text"));
                } else {

                }
            }
        });
    }

}
