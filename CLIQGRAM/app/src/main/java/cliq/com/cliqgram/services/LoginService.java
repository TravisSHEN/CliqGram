package cliq.com.cliqgram.services;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import cliq.com.cliqgram.events.LoginFailEvent;
import cliq.com.cliqgram.events.LoginSuccessEvent;
import cliq.com.cliqgram.app.AppStarter;

/**
 * Created by litaoshen on 2/09/2015.
 */
public class LoginService {


    public static void authenticate(String username, String password) {

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    AppStarter.eventBus.post(new LoginSuccessEvent());
                } else {
                    AppStarter.eventBus.post(new LoginFailEvent("Login failed - "
                            + e.getLocalizedMessage()));
                }
            }
        });
    }

}
