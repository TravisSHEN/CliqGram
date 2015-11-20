package cliq.com.cliqgram.services;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.events.SignupFailEvent;
import cliq.com.cliqgram.events.SignupSuccessEvent;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.app.AppStarter;
import cliq.com.cliqgram.utils.ImageUtil;
import cliq.com.cliqgram.utils.Utils;

/**
 * Created by litaoshen on 2/09/2015.
 */
public class SignupService {

    public static void signup(Context context,
                              final String username,
                              final String password,
                              final String email) {

//        // set default avatar
        byte[] avatarData = ImageUtil.convertBitmapToByte(ImageUtil.resizeDrawable(context, R.drawable
                .icon_user, 0.7f).getBitmap());
//
        String avatarLabel = "img_" +
                String.valueOf(Utils.getCurrentDate().getTime()) + "" +
                ".jpg";
//
//        // after avatar file stored successfully, then signup new user.
        final ParseFile avatar = new ParseFile(avatarLabel, avatarData);
        avatar.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {

                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);

                    doSignup(user, avatar, username, password);
                }
            }
        });

    }

    private static void doSignup(final User user,
                                 ParseFile avatar,
                                 final String username,
                                 final String password) {

        user.setAvatarData(avatar);

        // Log out current user to prevent session invalid when
        // user being manually deleted on cloud.
        if (UserService.getCurrentUser() != null) {
            // do stuff with the user
            UserService.logOut();
        }

        user.signUpInBackground(new SignUpCallback() {

            public void done(ParseException e) {
                if (e == null)
                    AppStarter.eventBus.post(new SignupSuccessEvent("Sign up was successful!",
                            username, password));
                else {
                    AppStarter.eventBus.post(new SignupFailEvent("Sign up failed - " +
                            e.getMessage()));
                }
            }
        });
    }
}
