package cliq.com.cliqgram.services;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import cliq.com.cliqgram.activities.MainActivity;

/**
 * Created by litaoshen on 2/09/2015.
 */
public class LoginService {

    public static void authenticate(final Context ctx, String username, String password) {

        // open main activity
        Intent intent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(intent);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    Toast.makeText(ctx, "Login successfully - " + e
                            .getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    // open main activity
                    Intent intent = new Intent(ctx, MainActivity.class);
                    ctx.startActivity(intent);
                } else {

                    // display failure message
                    Toast.makeText(ctx, "Login failed - " + e
                            .getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
