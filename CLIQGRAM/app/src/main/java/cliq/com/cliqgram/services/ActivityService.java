package cliq.com.cliqgram.services;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
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
//TODO set size for the activity list @ilkan
    public static void pullFollowingActivity(List<User> userList, FindCallback<Activity> callback){
        if(userList == null) {
            return;
        }
        List<Activity> wholeActivityList = new ArrayList<Activity>();
        ParseQuery<Activity> query = ParseQuery.getQuery(Activity.class);
        query.whereContainedIn("user", userList);
        query.findInBackground(callback);
        /*for (User user : userList) {

            List<Activity> activityList = (List<Activity>) user.get("activities");
            if(activityList != null && activityList.size() > 0){
                wholeActivityList.addAll(activityList);
            }
        }

        return wholeActivityList;*/
    }
    //TODO set size for the you activity list @ilkan
    public static void pullActivityRegardingToYou(User user, List<User> userList, FindCallback<Activity> callback){
        //List<Activity> wholeActivityList = new ArrayList<Activity>();
        ParseQuery<Activity> query = ParseQuery.getQuery(Activity.class);
        query.whereContainedIn("user", userList);
        query.whereEqualTo("targetUser", user);
        query.findInBackground(callback);
    }

}
