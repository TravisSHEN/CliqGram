package cliq.com.cliqgram.utils;

import android.location.Location;

import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.model.Comment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;

/**
 * Created by ilkan on 3/10/2015.
 */
public class ParseObject2ModelConverter {

    public static Activity toActivity(ParseObject object){
        Activity activity = new Activity();
        activity.setActivityId(object.getObjectId());
        activity.setActivityType((String) object.get(Params.FIELD_ACTIVITY_TYPE));
        activity.setCreatedAt((Date) object.get(Params.FIELD_CREATED_AT));
        //activity.setUser(); dangerous circle
        activity.setEventId((String)object.get(Params.FIELD_EVENT_ID));
        return activity;
    }


    public static User toUser(ParseUser parseUser){
        User user = new User();
        user.setUserId(parseUser.getObjectId());
        user.setAvatarData((byte[]) parseUser.get(Params.FIELD_AVATAR));
        user.setEmail((String) parseUser.getEmail());
        user.setUsername((String) parseUser.get(Params.FIELD_USER_NAME));
        //activities, followers and followings will take too long to take
        //they should be taken if they are necessary via get methods in the services
        //see UserRelationService class getRelation method
        //and see ActivityService class getActivitiesOfUser method

        return user;
    }


    public static Comment toComment(ParseObject object){
        Comment comment = new Comment();
        comment.setCommentId((String) object.get(Params.FIELD_OBJECT_ID));
        comment.setContent((String) object.get(Params.FIELD_CONTENT));
        comment.setCreatedAt((Date) object.get(Params.FIELD_CREATED_AT));
        comment.setOwner(toUser((ParseUser) object.get(Params.FIELD_OWNER)));
        return comment;
    }

    public static List<Comment> toCommentList(List<ParseObject> objList){
        List<Comment> comments = new ArrayList<Comment>();
        for (ParseObject obj : objList) {
            comments.add(toComment(obj));
        }
        return comments;
    }

    public static Post toPost(ParseObject object){
        Post post = new Post();
        post.setPostId(object.getObjectId());
        post.setOwner(toUser((ParseUser) object.get(Params.FIELD_OWNER)));
        post.setCreatedAt((Date) object.get(Params.FIELD_CREATED_AT));
        List<ParseObject> objList = (List<ParseObject>)object.get(Params.FIELD_COMMENTS);
        List<Comment> comments = toCommentList(objList);
        post.setCommentList(comments);
        post.setDescription((String) object.get(Params.FIELD_DESCRIPTION));
        ParseGeoPoint parseGeoPoint2 = new ParseGeoPoint(post.getLocation()
                .getLatitude(), post.getLocation().getLongitude());
        ParseGeoPoint parseGeoPoint = (ParseGeoPoint)object.get(Params.FIELD_LOCATION);
        Location location = new Location("New location");
        location.setAltitude(parseGeoPoint.getLongitude());
        location.setLatitude(parseGeoPoint.getLatitude());
        post.setLocation(location);
        post.setPhotoData((byte [])object.get(Params.FIELD_PHOTO));
        //post.setLikeList();//TODO likelist
        return post;
    }

}
