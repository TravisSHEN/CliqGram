package cliq.com.cliqgram.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by litaoshen on 30/09/2015.
 */
public class Activity implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public Activity() {
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
