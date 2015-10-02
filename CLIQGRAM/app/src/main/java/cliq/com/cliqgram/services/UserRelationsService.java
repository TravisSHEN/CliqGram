package cliq.com.cliqgram.services;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilkan on 2/10/2015.
 */
public class UserRelationsService {

    private static final String TABLE_NAME = "UserRelations";

    /**
     *
     * @param userName
     */
    public static void follow(final String userName){

        final ParseUser currentUser = ParseUser.getCurrentUser();
        final ParseUser otherUser = UserService.findParseUserByName(userName);
        followGenericAction(userName, "followers", currentUser);
        followGenericAction(currentUser.getUsername(), "followings", otherUser);
    }

    private static void followGenericAction(final String userName, final String operationName, final ParseUser user){

        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        query.whereEqualTo("username", userName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ParseObject userRelations;
                    if (objects == null || objects.size() == 0) {
                        userRelations = new ParseObject(TABLE_NAME);
                        userRelations.put("username", userName);
                    } else {
                        userRelations = objects.get(0);
                    }

                    ArrayList<ParseUser> relation = (ArrayList<ParseUser>) userRelations.get(operationName);
                    if (relation == null) {
                        relation = new ArrayList<>();
                    }
                    if( isInList(relation, user )){
                        return;
                    }
                    relation.add(user);

                    userRelations.put(operationName, relation);
                    userRelations.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //TODO event
                            } else {
                                //TODO event
                            }
                        }
                    });
                } else {
                    //TODO event
                }
            }
        });

    }

    public static List<ParseUser> getRelation(String userName, String relation){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
        query.whereEqualTo("username", userName);
        List<ParseUser> userRelations = null;
        try {
            List<ParseObject> parseObjects = query.find();
            if(parseObjects != null && parseObjects.size() == 1){
                userRelations = parseObjects.get(0).getList(relation);
            }
        }catch(ParseException e){
            //TODO exception
        }finally {
            return userRelations;
        }

    }

    public static boolean isInList(List<ParseUser> parseUsers, ParseUser
            parseUser){

        boolean in = false;
        for (ParseUser user : parseUsers) {
           if( user.getObjectId().equals( parseUser.getObjectId() )) {
               in = true;
           }
        }

        return in;
    }


}
