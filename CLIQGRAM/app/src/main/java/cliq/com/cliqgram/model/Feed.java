package cliq.com.cliqgram.model;

/**
 * Created by litaoshen on 22/09/2015.
 */
public class Feed {

    int id;

    int likes_count;
    int photoId;

    Comment comment;

    public Feed(int likes_count, int photoId, Comment comment) {
        this.likes_count = likes_count;
        this.photoId = photoId;
        this.comment = comment;
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

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", likes_count=" + likes_count +
                ", photoId=" + photoId +
                ", comment=" + comment.getContent() +
                '}';
    }
}
