package cliq.com.cliqgram.services;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cliq.com.cliqgram.events.RelationGetEvent;
import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.model.UserRelation;
import cliq.com.cliqgram.server.AppStarter;

/**
 * Created by ilkan on 2/10/2015.
 */
public class UserRelationsService {

    private static final String TABLE_NAME = "UserRelations";

    /**
     * @param userName
     */
    public static void follow(final User currentUser, final String userName) {

        followGenericAction(userName, "followers", currentUser, new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });

        UserService.getUserByUsername(userName, new GetCallback<User>() {
            @Override
            public void done(final User otherUser, ParseException e) {

                followGenericAction(currentUser.getUsername(), "followings",
                        otherUser, new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {

                                    //currentUser is currentUser,  otherUser is targetUser
                                    Activity.createActivity(currentUser,
                                            "follow", "", otherUser);
                                }
                            }
                        });

            }
        });
    }

    /**
     * @param userName
     * @param operationName
     * @param user
     */
    private static void followGenericAction(final String userName, final String operationName, final User user, final SaveCallback callback) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        query.whereEqualTo("username", userName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ParseObject userRelations;

                    if (objects == null || objects.size() == 0) {
                        userRelations = ParseObject.create(UserRelation.class);
                        // set acl
                        ParseACL acl = new ParseACL();
                        acl.setPublicReadAccess(true);
                        acl.setPublicWriteAccess(true);
                        userRelations.setACL(acl);

                        userRelations.put("username", userName);
                    } else {
                        userRelations = objects.get(0);
                    }

                    ArrayList<ParseUser> relation = (ArrayList<ParseUser>) userRelations.get(operationName);
                    if (relation == null) {
                        relation = new ArrayList<>();
                    }
                    if (isInList(relation, user)) {
                        return;
                    }
                    relation.add(user);


                    userRelations.put(operationName, relation);
                    userRelations.saveInBackground(callback);
                } else {
                    //TODO event
                }
            }
        });

    }

    /**
     * @param username
     */
    public static void getRelation(final String username) {
        ParseQuery<UserRelation> query = ParseQuery.getQuery(UserRelation.class);
        query.include("followings");
        query.include("followers");

        query.whereEqualTo("username", username);

        query.getFirstInBackground(new GetCallback<UserRelation>() {
            @Override
            public void done(UserRelation relation, ParseException e) {

                if (e == null) {
                    AppStarter.eventBus.post(new RelationGetEvent(relation));
                } else {
                    AppStarter.eventBus.post(new RelationGetEvent("Fail to " +
                            "get relation with" + username + " " + e.getMessage()));
                }

            }
        });
    }

    public static void getRelation(String username,
                                   GetCallback<UserRelation> callback) {
        ParseQuery<UserRelation> query =
                ParseQuery.getQuery(UserRelation.class);

        query.whereEqualTo("username", username);
        query.include("followings");
        query.include("followers");

        query.getFirstInBackground(callback);

    }

    public static void getParticularRelation(String username, String relation,
                                             GetCallback<UserRelation> callback) {
        ParseQuery<UserRelation> query =
                ParseQuery.getQuery(UserRelation.class);

        query.whereEqualTo("username", username);
        query.include(relation);

        query.getFirstInBackground(callback);

    }


    /**
     * @param userName
     * @param relation
     * @return
     */
//    public static List<User> getRelation(String userName, String relation) {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
//        query.whereEqualTo("username", userName);
//        query.include("posts");
//        List<User> userRelations = new ArrayList<>();
//        try {
//            List<ParseObject> parseObjects = query.find();
//            if (parseObjects != null && parseObjects.size() == 1) {
//                userRelations = parseObjects.get(0).getList(relation);
//            }
//        } catch (ParseException e) {
//            //TODO exception
//        } finally {
//            List<User> users = new ArrayList<>();
//            if (!userRelations.isEmpty()) {
//                for (Iterator iterator = userRelations.listIterator(); iterator
//                        .hasNext(); ) {
//                    User user = (User) iterator.next();
//                    users.add(user);
//                }
//            }
//
//            return users;
//        }
//
//    }

    public static boolean isInList(List<ParseUser> parseUsers, ParseUser
            parseUser) {

        boolean in = false;
        for (ParseUser user : parseUsers) {
            if (user.getObjectId().equals(parseUser.getObjectId())) {
                in = true;
            }
        }

        return in;
    }


}
