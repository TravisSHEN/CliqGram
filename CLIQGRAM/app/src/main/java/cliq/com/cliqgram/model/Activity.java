package cliq.com.cliqgram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

import cliq.com.cliqgram.services.ActivityService;

/**
 * Created by litaoshen on 30/09/2015.
 */
@ParseClassName("Activity")
public class Activity extends ParseObject implements Comparable<Activity>{

    /*private String activityId;
    private String activityType;
    private Date createdAt;
    private User user;
    private String eventId;
    */
    public Activity() {
        super();
    }

    public static Activity createActivity(User user, String activityType, String eventId, User targetUser){
        Activity activity = new Activity();
        activity.setUser(user);
        activity.setActivityType(activityType);
        activity.setEventId(eventId);
        activity.setTargetUser(targetUser);
        activity.saveInBackground();
        ActivityService.createActivity(activity);
        return activity;
    }
/*
    public static List<Activity> pullFollowersActivity(User user){

        List<User> followings = UserRelationsService.getRelation(user.getUsername(), "followings");
        List<Activity> wholeActivityList = (List<Activity>)ActivityService.pullActivity(followings);
        Comparator<Activity> comparator = new ActivityDateComparer();
        Collections.sort(wholeActivityList, comparator);
        return wholeActivityList;
    }

    public static List<Activity> pullFollowersActivityRegardingYou(User user){
        List<Activity> wholeActivityList = pullFollowersActivity(user);
        List<Activity> regardingUserActivityList = new ArrayList<Activity>();
        for (Activity activity : wholeActivityList ) {
            if(activity.getTargetUser().equals(user)){
                regardingUserActivityList.add(activity);
            }
        }
        return regardingUserActivityList;
    }
*/
    /*public Activity(String activityId, String activityType, Date createdAt, User user, String eventId){
        this.activityId = activityId;
        this.activityType = activityType;
        this.createdAt = createdAt;
        this.user = user;
        this.eventId = eventId;
    }*/

    public String getActivityId() {
        return (String)this.get("objectId");
    }

    //activityId is assigned by Parse
    /*
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }*/

    public String getActivityType() {
        return (String)this.get("activityType");
    }

    public void setActivityType(String activityType) {
        this.put("activityType", activityType);
    }

    public Date getCreatedAt() {
        return (Date)this.get("createdAt");
    }

    //createdAt is assigned by Parse
    /*public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }*/

    public User getUser() {
        return (User)this.getParseUser("user");
    }

    public void setUser(User user) {
        this.put("user", user);
    }

    public String getEventId() {
        return (String) this.get("eventId");
    }

    public void setEventId(String eventId) {
        this.put("eventId", eventId);
    }

    public User getTargetUser(){
        return (User)this.getParseUser("targetUser");
    }

    public void setTargetUser(User targetUser){
        this.put("targetUser", targetUser);
    }

    @Override
    public int compareTo(Activity another) {
        return -1 * this.getCreatedAt().compareTo(another.getCreatedAt());
    }

}
