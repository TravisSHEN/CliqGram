package cliq.com.cliqgram.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.events.GetPostEvent;
import cliq.com.cliqgram.events.PostFailEvent;
import cliq.com.cliqgram.events.PostSuccessEvent;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Like;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by ilkan on 26/09/2015.
 */
public class PostService {

    public static final String TABLE_NAME = "Post";

    public static void post(@NonNull Post post) {

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (post.getOwner() != null && !post.getOwner().getUsername().equals(
                currentUser.getUsername())) {
            return;
        }

        ParseObject postObject = new ParseObject(TABLE_NAME);
        //ParseFile photo = new ParseFile("image.jpg", post.getPhotoData());
        String imgLabel = "img_" + post.getOwner().getUsername() + String.valueOf(post
                .getCreatedAt().getTime()) + ".jpg";
        ParseFile photo = new ParseFile(imgLabel, post.getPhotoData());
        photo.saveInBackground();

        postObject.put("photo", photo);
        postObject.put("description", post.getDescription());
        if (post.getLocation() != null) {
            postObject.put("location", post.getLocation());
        }
        // creates one-to-one relationship
        // associate to current user
        postObject.put("user", currentUser);
        postObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e("Post Service", "post successful");
                } else {

                    Log.e("Post Service", e.getMessage());
                }
            }
        });

        ArrayList<ParseObject> relation = (ArrayList) currentUser.getList("posts");
        if (relation == null) {
            relation = new ArrayList<>();
        }
        relation.add(postObject);
        currentUser.put("posts", relation);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    AppStarter.eventBus.post(new PostSuccessEvent("post was successful!"));
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

        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);

        Log.d("POST SERVICE", TABLE_NAME);
        Log.d("POST SERVICE", id);
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    User user = (User) object.get("user");
                    Post post = Post.createPost(object.getBytes("photo"),
                            user,
                            object.getString("text"));

                    post.setPostId(object.getObjectId());

                    post.setCommentList(object.<Comment>getList("comments"));

                    Log.d("POST SERVICE", post.getPostId());

                    AppStarter.eventBus.post(new GetPostEvent(post));
                } else {
                    Log.d("POST SERVICE", e.getMessage());
                    AppStarter.eventBus.post(new GetPostEvent(null, false));
                }
            }
        });
    }

    public static void getPosts(@NonNull List<User> userList) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        List<String> userIdList = getUserIdList(userList);

        query.whereContainsAll("user", userIdList);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    List<Post> postList = new ArrayList<>();
                    postList.addAll(getPostsFromParseObjects(objects));

                    AppStarter.eventBus.post(new GetPostEvent(postList));
                } else {

                    AppStarter.eventBus.post(new GetPostEvent(null, false));
                }
            }
        });

    }


    public static List<String> getUserIdList(List<User> userList) {
        List<String> userIdList = new ArrayList<>();
        for (User user : userList) {
            userIdList.add(user.getUserId());
        }

        return userIdList;
    }

    public static List<Post> getPostsFromParseObjects(List<ParseObject>
                                                             parseObjectList) {

        List<Post> postList = new ArrayList<>();
        for (ParseObject parseObject : parseObjectList) {
            Post post = convertParseObjectToPost(parseObject);
            postList.add(post);
        }

        return postList;
    }

    public static Post convertParseObjectToPost(ParseObject parseObject) {

        Post post = Post.createPost();

        post.setPostId(parseObject.getObjectId());
        post.setOwner(UserService.getUserFromParseUser((ParseUser) parseObject.get
                ("user")));
        post.setPhotoData(parseObject.getBytes("photo"));
        post.setDescription(parseObject.getString("description"));
        post.setLocation(parseObject.getParseGeoPoint("location"));
        post.setCreatedAt(parseObject.getDate("createdAt"));

        // TODO: need to convert ParseObject to Comment & Like Object
        post.setCommentList(parseObject.<Comment>getList("comments"));
        post.setLikeList(parseObject.<Like>getList("likes"));


        return post;
    }

}
