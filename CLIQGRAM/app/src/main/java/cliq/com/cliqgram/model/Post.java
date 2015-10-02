package cliq.com.cliqgram.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;

import com.parse.ParseGeoPoint;

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
public class Post implements Comparable<Post>{

    private String postId;
    private User owner;
    private byte[] photoData;
    private String description;
    private Date createdAt;
    private ParseGeoPoint location;
    private List<Comment> commentList;
    private List<Like> likeList;

    public static Post createPost(){
        return new Post();
    }

    public static Post createPost(byte[] photoData, User owner, String
            description){

        Post post = new Post();
        //post.setPostId("");
        post.setOwner(owner);
        post.setPhotoData(photoData);
        post.setDescription(description);

        Location current_location = AppStarter.gpsTracker.getLocation();
        if( current_location != null ) {
            post.setLocation(new ParseGeoPoint(current_location.getLatitude(),
                    current_location.getLongitude()));
        }

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
        if( current_location != null ) {
            post.setLocation(new ParseGeoPoint(current_location.getLatitude(),
                    current_location.getLongitude()));
        }

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

        Location temp_location = AppStarter.gpsTracker.getLocation();
        this.location.setLongitude( temp_location.getLongitude() );
        this.location.setLatitude(temp_location.getLatitude());
    }


    /**
     * Increment likes count
     *
     * @return true when successfully increment, false when fail increment
     */
    public boolean incrementLikes() {

        User user = UserService.getCurrentUser();
        String username = user.getUsername();

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

    public ParseGeoPoint getLocation() {
        return location;
    }

    public void setLocation(ParseGeoPoint location) {
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

}
