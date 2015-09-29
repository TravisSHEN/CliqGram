package cliq.com.cliqgram.services;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

import cliq.com.cliqgram.events.LoginFailEvent;
import cliq.com.cliqgram.events.LoginSuccessEvent;
import cliq.com.cliqgram.events.PostFailEvent;
import cliq.com.cliqgram.events.PostSuccessEvent;
import cliq.com.cliqgram.events.SignupFailEvent;
import cliq.com.cliqgram.events.SignupSuccessEvent;
import cliq.com.cliqgram.helper.Utils;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Like;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by ilkan on 26/09/2015.
 */
public class PostService {

    public static void post(Post post){


        ParseObject postObject = new ParseObject("Post");
        //ParseFile photo = new ParseFile("image.jpg", post.getPhotoData());
        String imgLabel = "img_"+ post.getParseUser().getObjectId() + ".jpg";
        ParseFile photo = new ParseFile(imgLabel, post.getPhotoData());
        photo.saveInBackground();

        postObject.put("photo", photo);
        postObject.put("text", post.getText());
        postObject.put("location", post.getLocation());
        postObject.put("user", post.getParseUser());//creates one-to-one relationship
        postObject.saveInBackground();

        ParseUser.getCurrentUser().put("posts", postObject);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    AppStarter.eventBus.post(new PostSuccessEvent("Sign up was successful!"));
                } else {
                    AppStarter.eventBus.post(new PostFailEvent("Sign up failed - " +
                            e.getMessage()));
                }
            }
        });

    }


}
