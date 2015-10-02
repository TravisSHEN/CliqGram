package cliq.com.cliqgram.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.services.UserService;
import cliq.com.cliqgram.utils.Util;

/**
 * Created by ilkan on 27/09/2015.
 */
public class Post implements Comparable<Post>,Parcelable {

    private String postId;
    private User owner;
    private byte[] photoData;
    private String description;
    private Location location;
    private List<Comment> commentList;
    private Date createdAt;
    private List<Like> likeList;

    public static Post createPost(byte[] photoData, User owner, String
            description){

        Post post = new Post();
        //post.setPostId("");
        post.setOwner(owner);
        post.setPhotoData(photoData);
        post.setDescription(description);

        Location current_location = AppStarter.gpsTracker.getLocation();
        post.setLocation(current_location);

        post.setCommentList(new ArrayList<Comment>());
        post.setCreatedAt(Util.getCurrentDate());
        post.setLikeList(new ArrayList<Like>());

        return post;
    }

    public static Post createPost(BitmapDrawable bm, User owner, String
            description) {

        Post post = new Post();
        post.setPostId("");
        post.setOwner(owner);
        post.setPhotoData(Util.convertBitmapToByte(bm.getBitmap()));
        post.setDescription(description);

        Location current_location = AppStarter.gpsTracker.getLocation();
        post.setLocation(current_location);

        post.setCommentList(new ArrayList<Comment>());
        post.setCreatedAt(Util.getCurrentDate());
        post.setLikeList(new ArrayList<Like>());

        return post;

    }

    public Post() {

    }

    public Post(byte[] photoData, String description, User owner,
                List<Comment> commentList, Date createdAt,
                List<Like> likeList) {
        this.photoData = photoData;
        this.description = description;
        this.owner = owner;
        this.commentList = commentList;
        this.createdAt = createdAt;
        this.likeList = likeList;

        this.location = AppStarter.gpsTracker.getLocation();
    }


    /**
     * Increment likes count
     *
     * @return true when successfully increment, false when fail increment
     */
    public boolean incrementLikes() {

        ParseUser parseUser = ParseUser.getCurrentUser();
        String username = parseUser.getUsername();

        for (Like like : likeList) {
            if (like.getUser() != null && username.equals(like.getUser()
                    .getUsername())) {
                return false;
            }
        }

        Like like = new Like(this, UserService.getCurrentUser());

        likeList.add(like);

        return true;
    }

    /**
     * Return a bitmap drawable
     *
     * @param context
     * @return
     */
    public BitmapDrawable getPhotoInBitmapDrawable(Context context) {
        return Util.convertByteToBitmapDrawable(context, photoData);
    }

    public Bitmap getPhotoInBitmap( Context context ){
        return Util.convertByteToBitmapDrawable(context, photoData).getBitmap();
    }

    @Override
    public int compareTo(Post another) {
        return -1 * this.getCreatedAt().compareTo(another.getCreatedAt());
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
//                ", photoData=" + Arrays.toString(photoData) +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", commentList=" + commentList +
                ", createdAt=" + createdAt +
                ", likeList=" + likeList +
                ", owner=" + owner +
                '}';
    }



    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public byte[] getPhotoData() {
        return photoData;
    }

    public void setPhotoData(byte[] photo) {
        this.photoData = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDateString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(createdAt);
    }

    public List<Like> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<Like> likeList) {
        this.likeList = likeList;
    }

    public int getLikes_count() {
        return this.likeList.size();
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.postId);
        dest.writeParcelable(this.owner, flags);
        dest.writeByteArray(this.photoData);
        dest.writeString(this.description);
        dest.writeParcelable(this.location, 0);
        dest.writeList(this.commentList);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeList(this.likeList);
    }

    private Post(Parcel in) {
        this.postId = in.readString();
        this.owner = in.readParcelable(User.class.getClassLoader());
        this.photoData = in.createByteArray();
        this.description = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.commentList = new ArrayList<Comment>();
        in.readList(this.commentList, Comment.class.getClassLoader());
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        this.likeList = new ArrayList<Like>();
        in.readList(this.likeList, Like.class.getClassLoader());
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
