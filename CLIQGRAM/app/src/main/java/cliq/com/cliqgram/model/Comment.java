package cliq.com.cliqgram.model;

/**
 * Created by litaoshen on 22/09/2015.
 */
public class Comment {

    private User user;
    private String content;

    public static Comment createComment(User user, String content){

        Comment comment = new Comment(user, content);

        return comment;
    }

    public Comment(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
