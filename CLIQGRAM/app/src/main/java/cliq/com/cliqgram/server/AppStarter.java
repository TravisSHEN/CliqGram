package cliq.com.cliqgram.server;

import android.app.Application;

import cliq.com.cliqgram.utils.GPSTracker;
import de.greenrobot.event.EventBus;

/**
 * Created by ilkan on 25/09/2015.
 */
public class AppStarter extends Application {

    public static EventBus eventBus = EventBus.getDefault();
    public static GPSTracker gpsTracker;


    @Override
    public void onCreate() {
        super.onCreate();

        gpsTracker = new GPSTracker(this);
        ParseConnector.initialize(this);
    }
}