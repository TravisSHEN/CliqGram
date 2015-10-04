package cliq.com.cliqgram.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.events.GetPostEvent;
import cliq.com.cliqgram.events.PostFailEvent;
import cliq.com.cliqgram.events.PostSuccessEvent;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by ilkan on 26/09/2015.
 */
public class PostService {

    public static final String TABLE_NAME = "Post";

    public static void post(@NonNull final Post post) {

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (post.getOwner() != null && !post.getOwner().getUsername().equals(
                currentUser.getUsername())) {
            return;
        }

        ArrayList<Post> relation = (ArrayList) currentUser.getList("posts");
        if (relation == null) {
            relation = new ArrayList<>();
        }
        relation.add(post);
        currentUser.put("posts", relation);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    AppStarter.eventBus.post(new PostSuccessEvent(post));
                } else {
                    AppStarter.eventBus.post(new PostFailEvent("post failed -" +
                            " " + e.getMessage()));
                }
            }
        });

    }


    /**
     * @param id
     */
    public static void getPost(@NonNull final String id) {

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include("user");
        query.include("comments");
        query.include("likes");

        query.getInBackground(id, new GetCallback<Post>() {
            @Override
            public void done(Post post, ParseException e) {
                if (e == null) {

//                    Log.d("POST SERVICE", post.getPostId());

                    AppStarter.eventBus.post(new GetPostEvent(post));
                } else {
                    Log.d("POST SERVICE", e.getMessage());
                    AppStarter.eventBus.post(new GetPostEvent(null, false));
                }
            }
        });
    }

    public static void getPosts( List<ParseUser> userList) {

        if (userList == null || userList.size() <= 0) {
            return;
        }

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereContainedIn("user", userList);
        query.orderByDescending("createdAt");
        query.include("user");
        query.include("comments");
        query.include("likes");

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    List<Post> postList = objects;


                    AppStarter.eventBus.post(new GetPostEvent(postList));
                } else {

                    AppStarter.eventBus.post(new GetPostEvent(null, false));
                }
            }
        });

    }
}
