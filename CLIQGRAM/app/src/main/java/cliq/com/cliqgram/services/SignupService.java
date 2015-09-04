package cliq.com.cliqgram.services;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import cliq.com.cliqgram.events.SignupFailEvent;
import cliq.com.cliqgram.events.SignupSuccessEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by litaoshen on 2/09/2015.
 */
public class SignupService {

    public static void signup(final String username, final String
            password, String email) {

        final EventBus eventBus = EventBus.getDefault();

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // Log out current user to prevent session invalid when
        // user being manually deleted on cloud.
        if (ParseUser.getCurrentUser() != null) {
            // do stuff with the user
            ParseUser.logOut();
        }

        user.signUpInBackground(new SignUpCallback() {

            public void done(ParseException e) {
                if (e == null) {

                    eventBus.post(new SignupSuccessEvent("Sign up " +
                            "successfully", username, password));
                } else {

                    eventBus.post(new SignupFailEvent("Sign up failed - " +
                            e.getMessage()));
                }
            }
        });

    }
}
