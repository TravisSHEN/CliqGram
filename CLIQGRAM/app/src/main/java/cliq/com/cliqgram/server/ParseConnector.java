package cliq.com.cliqgram.server;

import android.content.Context;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;

import cliq.com.cliqgram.events.BaseEvent;
import cliq.com.cliqgram.events.LoginFailEvent;
import cliq.com.cliqgram.events.LoginSuccessEvent;

/**
 * Created by ilkan on 26/09/2015.
 */
public class ParseConnector {

    public static void initialize(Context context) {
        Parse.enableLocalDatastore(context);
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
