package cliq.com.cliqgram.services;

import android.content.Context;

import com.parse.ParseException;
import com.parse.SignUpCallback;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.events.SignupFailEvent;
import cliq.com.cliqgram.events.SignupSuccessEvent;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.utils.Util;

/**
 * Created by litaoshen on 2/09/2015.
 */
public class SignupService {

    public static void signup(Context context,
                              final String username,
                              final String password,
                              final String email) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // set default avatar
        byte[] avatarData = Util.convertBitmapToByte(Util.resizeDrawable(context, R.drawable
                .icon_user, 0.7f).getBitmap());
        user.setAvatarData(avatarData);

        // Log out current user to prevent session invalid when
        // user being manually deleted on cloud.
        if (UserService.getCurrentUser() != null) {
            // do stuff with the user
            UserService.logOut();
        }

        user.signUpInBackground(new SignUpCallback() {

            public void done(ParseException e) {
                if (e == null) {
                    AppStarter.eventBus.post(new SignupSuccessEvent("Sign up was successful!",
                            username, password));
                } else {
                    AppStarter.eventBus.post(new SignupFailEvent("Sign up failed - " +
                            e.getMessage()));
                }
            }
        });

    }
}
