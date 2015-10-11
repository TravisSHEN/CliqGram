package cliq.com.cliqgram.services;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.events.ActivityFailEvent;
import cliq.com.cliqgram.events.ActivitySuccessEvent;
import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by ilkan on 11/10/2015.
 */
public class ActivityService {

    public static void createActivity(final Activity activity){

        User user = activity.getUser();
        List<Activity> relation = (ArrayList<Activity>)user.get("activities");
        if(relation == null){
            relation = new ArrayList<>();
        }
        relation.add(activity);
        user.put("activities", relation);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    AppStarter.eventBus.post(new
                            ActivitySuccessEvent(activity));
                }else{
                    AppStarter.eventBus.post(new
                            ActivityFailEvent("Activity record operation failed! - " + e.getMessage()));
                }
            }
        });

    }

}
