/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package cliq.com.cliqgram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import de.greenrobot.event.EventBus;


public class StarterApplication extends Application {

    public static EventBus BUS = EventBus.getDefault();

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);


        // Add your initialization code here
        Parse.initialize(this, "chzaksNDCMU3xDsuty6ml11hlDkEyBqOTaSlWG0I", "9ZQ2NlS94zSHhmTYT5noOCUhXko3bFFD8h4P8bN4");

        ParseUser.enableRevocableSessionInBackground();

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
