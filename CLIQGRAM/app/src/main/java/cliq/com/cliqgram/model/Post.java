package cliq.com.cliqgram.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;

import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cliq.com.cliqgram.server.AppStarter;
import cliq.com.cliqgram.services.PostService;
import cliq.com.cliqgram.services.UserService;
import cliq.com.cliqgram.utils.Util;

/**
 * Created by ilkan on 27/09/2015.
 */
@ParseClassName("Post")
public class Post extends ParseObject implements Comparable<Post> {

    private byte[] photoData;

    public static Post createPost(byte[] photoData, User owner, String
            description) {

        Post post = new Post();
        post.setOwner(owner);
        post.setPhotoData(photoData);
        post.setDescription(description);

        Location current_location = AppStarter.gpsTracker.getLocation();
        if (current_location != null) {
            post.setLocation(new ParseGeoPoint(current_location.getLatitude(),
                    current_location.getLongitude()));
        }

        post.setCommentList(new ArrayList<Comment>());
        post.setLikeList(new ArrayList<Like>());

        post.saveInBackground();
        PostService.post(post);
        return post;
    }

    public static Post createPost(BitmapDrawable bm, User owner, String
            description) {

        Post post = new Post();
//        post.setPostId("");
        post.setOwner(owner);
        post.setPhotoData(Util.convertBitmapToByte(bm.getBitmap()));
        post.setDescription(description);

        Location current_location = AppStarter.gpsTracker.getLocation();
        if (current_location != null) {
            post.setLocation(new ParseGeoPoint(current_location.getLatitude(),
                    current_location.getLongitude()));
        }

        post.setCommentList(new ArrayList<Comment>());
        post.setLikeList(new ArrayList<Like>());

        post.saveInBackground();
        PostService.post(post);

        return post;

    }

    public Post() {
        super();
    }

//    @Subscribe
//    public void onReadySaveEvent(ModelReadyToSave event) {
//        this.saveInBackground();
//    }


    /**
     * Increment likes count
     *
     * @return true when successfully increment, false when fail increment
     */
    public boolean incrementLikes() {

        User user = UserService.getCurrentUser();
        String username = user.getUsername();

        for (Like like : this.getLikeList()) {
            if (like.getUser() != null && username.equals(like.getUser()
                    .getUsername())) {
                return false;
            }
        }

        Like like = Like.createLike(this, UserService.getCurrentUser());

        this.getLikeList().add(like);
        return true;

    }

    /**
     * Return a bitmap drawable
     *
     * @param context
     * @return
     */
    private BitmapDrawable getPhotoInBitmapDrawable(Context context, byte[] photoData) {
        return Util.convertByteToBitmapDrawable(context, photoData);
    }

    public Bitmap getPhotoInBitmap(Context context, byte[] photoData) {

        BitmapDrawable bm_drawable = this.getPhotoInBitmapDrawable(context,
                photoData);

        return bm_drawable == null ? null : bm_drawable.getBitmap();
    }

    @Override
    public int compareTo(Post another) {
        return -1 * this.getCreatedAt().compareTo(another.getCreatedAt());
    }

    public User getOwner() {

        ParseUser owner = this.getParseUser("user");
        return UserService.getUserFromParseUser(owner);
    }

    public void setOwner(User owner) {
        // creates one-to-one relationship
        // associate to current user
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.whereEqualTo("username", owner.getUsername());
//        query.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> objects, ParseException e) {
//                if (e == null && objects.size() > 0) {
//                    Post.this.put("user", objects.get(0));
//
//                    AppStarter.eventBus.post(new ModelReadyToSave());
//                } else {
//                    Log.e("Comment", "User not found");
//                }
//            }
//        });
//        this.put("user", UserService.findParseUserByName(owner.getUsername()));
//        this.owner = owner;
        this.put("user", ParseUser.getCurrentUser());
    }

    /*
     * get photoData from server
     */
    public byte[] getPhotoData(GetDataCallback callback) {
        ParseFile photo = this.getParseFile("photo");
        photo.getDataInBackground(callback);

        return this.photoData;
    }

    /*
     * return existing photoData.
     */
    public byte[] getPhotoData() {
        return this.photoData;
    }


    public void setPhotoData(byte[] photoData) {
        String photoLabel = "img_" + String.valueOf(Util.getCurrentDate()
                .getTime()) +
                ".jpg";
        ParseFile photo = new ParseFile(photoLabel, photoData);
        photo.saveInBackground();
        this.put("photo", photo);
        this.photoData = photoData;
    }

    public String getDescription() {
//        description = this.getString("description");
//        return description;

        return this.getString("description");
    }

    public void setDescription(String description) {
        this.put("description", description);
//        this.description = description;
    }

    public ParseGeoPoint getLocation() {
//        location = this.getParseGeoPoint("location");
//        return location;

        return this.getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint location) {
        this.put("location", location);
//        this.location = location;
    }

    public String getDateString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date createdAt = this.getCreatedAt();
        return sdf.format(createdAt);
    }

    public List<Comment> getCommentList() {
//        return commentList;
        return getList("comments");
    }

    public void setCommentList(List<Comment> commentList) {
        this.put("comments", commentList);
//        this.commentList = commentList;
    }

    public List<Like> getLikeList() {
//        this.likeList = this.getList("likes");
//        return likeList;
        return this.getList("likes");
    }

    public void setLikeList(List<Like> likeList) {
        this.put("likes", likeList);
//        this.likeList = likeList;
    }

    public int getLikes_count() {
        return this.getLikeList().size();
    }

}
