package cliq.com.cliqgram.services;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.events.ActivityFailEvent;
import cliq.com.cliqgram.events.ActivitySuccessEvent;
import cliq.com.cliqgram.events.CommentSuccessEvent;
import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.utils.Params;
import cliq.com.cliqgram.utils.ParseObject2ModelConverter;

/**
 * Created by ilkan on 3/10/2015.
 */
public class ActivityService {

    public void getActivitiesOfUser(String userId){

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(Params.FIELD_OBJECT_ID, userId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects.size() == 1){
                    ParseUser parseUser = objects.get(0);
                    List<ParseObject> actObjList = (List<ParseObject>)parseUser.get(Params.FIELD_ACTIVITIES);
                    List<Activity> activities = new ArrayList<Activity>();
                    for (ParseObject act : actObjList) {
                        activities.add(ParseObject2ModelConverter.toActivity(act));
                    }
                    AppStarter.eventBus.post(new ActivitySuccessEvent(activities));
                }else{
                    AppStarter.eventBus.post(new ActivityFailEvent("Activity failed - "+ e.getMessage()));
                }
            }
        });

    }

}
