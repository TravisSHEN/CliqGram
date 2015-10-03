package cliq.com.cliqgram.events;

import android.app.ListActivity;

import java.util.List;

import cliq.com.cliqgram.model.Activity;

/**
 * Created by ilkan on 3/10/2015.
 */
public class ActivitySuccessEvent extends BaseEvent{

    private List<Activity> activityList;

    public ActivitySuccessEvent(List<Activity> activityList){
        super("Activity success");
        this.activityList = activityList;
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }



}
