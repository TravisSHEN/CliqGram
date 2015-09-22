package cliq.com.cliqgram.services;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import cliq.com.cliqgram.StarterApplication;
import cliq.com.cliqgram.events.LoginFailEvent;
import cliq.com.cliqgram.events.LoginSuccessEvent;

/**
 * Created by litaoshen on 2/09/2015.
 */
public class LoginService {

    public static void authenticate(String username, String password) {

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {

                    StarterApplication.BUS.post(new LoginSuccessEvent());

                } else {

                    StarterApplication.BUS.post(new LoginFailEvent("Login failed - "
                            + e.getLocalizedMessage()));

                }
            }
        });
    }
}
