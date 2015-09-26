package cliq.com.cliqgram.model;

import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by litaoshen on 22/09/2015.
 */
public class Feed {

    int id;

    int likes_count;
    int photoId;

    List<String> who_likes;

    List<Comment> comments;

    User user;

    Date date;

    public static Feed feedFactory(int photoId, User user) {
        Feed feed = new Feed(photoId, user);

        return feed;
    }

    public Feed(int photoId, User user) {

        Calendar calendar = Calendar.getInstance();
        date = calendar.getTime();

        this.who_likes = new ArrayList<>();
        this.likes_count = this.who_likes.size();
        this.photoId = photoId;
        this.user = user;

        this.comments = new ArrayList<>();
    }

    /**
     * Increment likes count
     * @return true when successfully increment, false when fail increment
     */
    public boolean incrementLikes(){

        ParseUser parseUser = ParseUser.getCurrentUser();
        String username = parseUser.getUsername();

        if( this.who_likes.contains(username)){
            return false;
        }

        who_likes.add( username );
        this.updateLikes_count();

        return true;
    }

    public int getId() {
        return id;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int updateLikes_count(){
        this.likes_count = this.who_likes.size();

        return this.likes_count;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format( date );
    }

    public List<String> getWho_likes() {
        return who_likes;
    }

    public void setWho_likes(List<String> who_likes) {
        this.who_likes = who_likes;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", likes_count=" + likes_count +
                ", photoId=" + photoId +
                ", comment=" +
                (comments.isEmpty()?
                        "No comments" :
                        comments.get(0).getContent()) +
                ", date=" + date.toString() +
                '}';
    }
}
