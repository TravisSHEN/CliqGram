package cliq.com.cliqgram.server;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import de.greenrobot.event.EventBus;
import android.content.Context;

/**
 * Created by ilkan on 25/09/2015.
 */
public class AppStarter extends Application {

    public static EventBus eventBus = EventBus.getDefault();

    @Override
    public void onCreate() {
        super.onCreate();

        ParseConnector.initialize(this);
    }
}