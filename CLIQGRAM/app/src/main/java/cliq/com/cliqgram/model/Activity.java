package cliq.com.cliqgram.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by litaoshen on 30/09/2015.
 */
public class Activity implements Parcelable {

    private String activityId;
    private String activityType;
    private Date createdAt;
    private User user;
    private String eventId;

    public Activity(){

    }

    public Activity(String activityId, String activityType, Date createdAt, User user, String eventId){
        this.activityId = activityId;
        this.activityType = activityType;
        this.createdAt = createdAt;
        this.user = user;
        this.eventId = eventId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }



    //TODO from @ilkan to @litaos: "I don't know what the rest is doing?"

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    private Activity(Parcel in) {
    }

    public static final Parcelable.Creator<Activity> CREATOR = new Parcelable.Creator<Activity>() {
        public Activity createFromParcel(Parcel source) {
            return new Activity(source);
        }

        public Activity[] newArray(int size) {
            return new Activity[size];
        }
    };
}
