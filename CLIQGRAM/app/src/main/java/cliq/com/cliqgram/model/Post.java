package cliq.com.cliqgram.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cliq.com.cliqgram.app.AppStarter;
import cliq.com.cliqgram.services.LikeService;
import cliq.com.cliqgram.services.PostService;
import cliq.com.cliqgram.services.UserService;
import cliq.com.cliqgram.utils.ImageUtil;

/**
 * Created by ilkan on 27/09/2015.
 */
@ParseClassName("Post")
public class Post extends ParseObject implements Comparable<Post> {


    /**
     *
     * @param photoData
     * @param description
     * @return
     */
    public static Post createPost(User owner, byte[] photoData, String
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
        //owner is currentUser,  owner is targetUser
        //Activity.createActivity(owner, "post", post.getObjectId(), owner);
        return post;
    }


    public Post() {
        super();
    }

    /**
     * Increment likes count
     *
     * @return true when successfully increment, false when fail increment
     */
    public boolean incrementLikes() {

        User currentUser = UserService.getCurrentUser();

        if(LikeService.isAlreadyLiked( currentUser, this.getLikeList())){
            return false;
        }

        Like like = Like.createLike(this, currentUser);

        if(this.getLikeList() == null){
            this.setLikeList( new ArrayList<Like>());
            this.saveInBackground();
        }
        this.getLikeList().add(like);
        return true;

    }

    /**
     * unlike the post
     * @return
     */
    public boolean unlike(){
        User currentUser = UserService.getCurrentUser();

        for (int i = this.getLikeList().size() - 1; i >= 0; i--) {
           Like like = this.getLikeList().get(i);
            if(like.getUser().getObjectId().equals(currentUser.getObjectId())){
                this.getLikeList().remove(i);
                LikeService.unLike(like.getPost(), like);
                return true;
            }
        }
        return false;
    }

    /**
     * Return a bitmap drawable
     *
     * @param context
     * @return
     */
    private BitmapDrawable getPhotoInBitmapDrawable(Context context, byte[] photoData) {
        return ImageUtil.convertByteToBitmapDrawable(context, photoData);
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

        return (User) this.getParseUser("user");
    }

    /**
     * set current user as owner
     */
    public void setOwner(User owner) {
        // creates one-to-one relationship
        // associate to current user
        this.put("user", owner);
    }

    /**
     * get photo from server
     * @param callback
     */
    public void getPhotoData(GetDataCallback callback) {
        ParseFile photo = this.getParseFile("photo");
        photo.getDataInBackground(callback);
    }

    public ParseFile getPhotoDataAsParseFile() {
        return this.getParseFile("photo");
    }

    public Uri getPhotoUri(){
        ParseFile photoFile = this.getParseFile("photo");
        Uri imageUri = Uri.parse(photoFile.getUrl());

        return imageUri;
    }

    public void loadPhotoToView(Context context, ImageView imageView){
        Picasso.with(context)
                .load( this.getPhotoUri().toString() )
                .resize(400, 400)
                .centerCrop()
                .into(imageView);
    }

    public void setPhotoData(byte[] photoData) {
        String photoLabel = "img_" + String.valueOf(ImageUtil.getCurrentDate()
                .getTime()) +
                ".jpg";
        ParseFile photo = new ParseFile(photoLabel, photoData);
        photo.saveInBackground();
        this.put("photo", photo);
    }

    public String getDescription() {

        return this.getString("description");
    }

    public void setDescription(String description) {
        this.put("description", description);
    }

    public ParseGeoPoint getLocation() {

        return this.getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint location) {
        this.put("location", location);
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
    }

    public List<Like> getLikeList() {
        return this.getList("likes");
    }

    public void setLikeList(List<Like> likeList) {
        this.put("likes", likeList);
    }

    public int getLikes_count() {
        int count = 0;
        if( this.getLikeList() != null ){
           count = this.getLikeList().size();
        }
        return count;
    }

}
