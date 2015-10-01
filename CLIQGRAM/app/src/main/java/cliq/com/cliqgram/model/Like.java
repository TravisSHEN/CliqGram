package cliq.com.cliqgram.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ilkan on 27/09/2015.
 */
public class Like implements Parcelable {

    private String likeId;
    private Post post;
    private User user;


    public Like(Post post, User user){
        this.post = post;
        this.user = user;
    }

    public String getLikeId() {
        return likeId;
    }
    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }
    public Post getPost(){
        return this.post;
    }
    public void setPost(Post post){
        this.post = post;
    }
    public User getUser(){
        return this.user;
    }
    public void setUser(User user){
        this.user = user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.likeId);
        dest.writeParcelable(this.post, 0);
        dest.writeParcelable(this.user, 0);
    }

    private Like(Parcel in) {
        this.likeId = in.readString();
        this.post = in.readParcelable(Post.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<Like> CREATOR = new Parcelable.Creator<Like>() {
        public Like createFromParcel(Parcel source) {
            return new Like(source);
        }

        public Like[] newArray(int size) {
            return new Like[size];
        }
    };
}
