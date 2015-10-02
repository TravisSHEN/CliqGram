package cliq.com.cliqgram.model;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.utils.Util;

/**
 * Created by litaoshen on 2/09/2015.
 */
public class User implements Parcelable {

    String userId, username, email;
    byte[] avatarData;

    List<Post> postList;
    List<Activity> activities;
    List<User> followingList;
    List<User> followerList;

    public static User userFactory() {
        User user = new User();

        return user;
    }

    public static User userFactory(String username, String email){
        byte[] avatarData = new byte[0];
        User user = new User(username, email, avatarData);

        return user;
    }

    public static User userFactory(String username, String email, byte[] avatarData) {
        User user = new User(username, email, avatarData);

        return user;
    }

    public User() {
        this.username = "";
        this.email = "";
        this.avatarData = new byte[0];

        this.activities = new ArrayList<>();
        this.followingList = new ArrayList<>();
        this.followerList = new ArrayList<>();
    }

    public User(String username, String email, byte[] avatarData) {
        this.username = username;
        this.email = email;
        this.avatarData = avatarData;
    }

    /**
     *
     * @param context
     * @return
     */
    public BitmapDrawable getAvatarInBitmapDrawable(Context context){
        return Util.convertByteToBitmapDrawable(context, avatarData);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getAvatarData() {
        return avatarData;
    }

    public void setAvatarData(byte[] avatarData) {
        this.avatarData = avatarData;
    }
    public List<Activity> getActivities() {
        return activities;
    }
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<User> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<User> followingList) {
        this.followingList = followingList;
    }

    public List<User> getFollowerList() {
        return followerList;
    }

    public void setFollowerList(List<User> followerList) {
        this.followerList = followerList;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeByteArray(this.avatarData);
        dest.writeList(this.activities);
        dest.writeList(this.postList);
        dest.writeList(this.followingList);
        dest.writeList(this.followerList);
    }

    private User(Parcel in) {
        this.username = in.readString();
        this.email = in.readString();
        this.avatarData = in.createByteArray();
        this.activities = new ArrayList<Activity>();
        in.readList(this.activities, Activity.class.getClassLoader());
        this.followingList = new ArrayList<User>();
        in.readList(this.postList, Post.class.getClassLoader());
        this.postList = new ArrayList<Post>();
        in.readList(this.followingList, User.class.getClassLoader());
        this.followerList = new ArrayList<User>();
        in.readList(this.followerList, User.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
