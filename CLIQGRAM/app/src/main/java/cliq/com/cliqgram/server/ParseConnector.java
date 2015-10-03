package cliq.com.cliqgram.server;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;

/**
 * Created by ilkan on 26/09/2015.
 */
public class ParseConnector {

    public static void initialize(Context context) {
        Parse.enableLocalDatastore(context);

        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(Post.class);

        // Add your initialization code here
        Parse.initialize(context, Keys.applicationId, Keys.clientKey);
        ParseUser.enableRevocableSessionInBackground();
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

}
