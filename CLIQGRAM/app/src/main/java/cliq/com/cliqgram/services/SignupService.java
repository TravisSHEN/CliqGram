package cliq.com.cliqgram.services;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import cliq.com.cliqgram.activities.LoginActivity;

/**
 * Created by litaoshen on 2/09/2015.
 */
public class SignupService {

    public static void signup(final Context ctx, String username, String
            password, String email) {

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        user.signUpInBackground(new SignUpCallback() {

            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Intent intent = new Intent(ctx, LoginActivity.class);
                    ctx.startActivity(intent);
                } else {

                    // display failure message
                    Toast.makeText(ctx, "Sign up failed - " + e
                            .getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
