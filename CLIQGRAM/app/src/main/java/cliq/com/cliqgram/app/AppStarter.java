package cliq.com.cliqgram.app;

import android.app.Application;

import de.greenrobot.event.EventBus;

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