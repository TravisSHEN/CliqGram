package cliq.com.cliqgram.services;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import cliq.com.cliqgram.events.LoginFailEvent;
import cliq.com.cliqgram.events.LoginSuccessEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by litaoshen on 2/09/2015.
 */
public class LoginService {

    public static void authenticate(String username, String password) {

        final EventBus eventBus = EventBus.getDefault();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {

                    eventBus.post(new LoginSuccessEvent());

                } else {

                    eventBus.post(new LoginFailEvent("Login failed - "
                            + e.getLocalizedMessage()));

                }
            }
        });
    }
}
