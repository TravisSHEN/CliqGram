package cliq.com.cliqgram.model;

/**
 * Created by litaoshen on 2/09/2015.
 */
public class User {

    String username, email;
    int avatar_id;

    public static User userFactory(String username, String email, int avatar_id) {
        User user = new User(username, email, avatar_id);

        return user;
    }

    public User(String username,  String email, int avatar_id) {
        this.username = username;
        this.email = email;
        this.avatar_id = avatar_id;
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

    public int getAvatar_id() {
        return avatar_id;
    }

    public void setAvatar_id(int avatar_id) {
        this.avatar_id = avatar_id;
    }
}
