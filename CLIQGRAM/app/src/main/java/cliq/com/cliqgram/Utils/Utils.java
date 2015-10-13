package cliq.com.cliqgram.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.services.UserService;

/**
 * Created by ilkan on 27/09/2015.
 */
public class Utils {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss.SSS'Z'");

    public static String printElapsedTime(long initial, long finalTime) {
        long lapse = finalTime - initial;
        long secs = (lapse/(1000))%60;
        long mins = lapse/(1000*60)%60;
        long hrs = lapse/(1000*60*60)%24;
        long days = lapse/(1000*60*60*24);
        long weeks = lapse/(1000*60*60*24*7);
        long months = lapse/(1000*60*60*24*30);

        StringBuilder lapseMsg = new StringBuilder();
        if(months >= 1){
            lapseMsg.append(Math.round(months)+"mth");
        }else if(weeks >= 1){
            lapseMsg.append(Math.round(weeks)+"w");
        }else if(days >= 1){
            lapseMsg.append(Math.round(days)+"d");
        }else if(mins >= 1){
            lapseMsg.append(Math.round(mins)+"m");
        }else if(secs >= 1){
            lapseMsg.append(Math.round(secs)+"s");
        }
        String msg = lapseMsg.toString();
        return msg;
    }

    public static long getTime(String date) {
        DateFormat format = Utils.dateFormat;
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            throw new RuntimeException();
        }
    }
    public static String activityStringBuilder(Activity activity, Context context){

        StringBuilder sb = new StringBuilder();
        sb.append(activity.getUser().getUsername());
        if(activity.getActivityType().equals("follow")){
            sb.append(" started following ");
        }else if(activity.getActivityType().equals("like")){
            sb.append(" liked ");
        }else if(activity.getActivityType().equals("comment")){
            sb.append(" commented ");
        }
        boolean isCurUser = activity.getTargetUser().equals(UserService.getCurrentUser());
        if(isCurUser){
            sb.append("you");
        }else {
            sb.append(activity.getTargetUser().getUsername());
        }
        if(activity.getEventId()!= null && !activity.getEventId().equals("") ){
            if(isCurUser){
                sb.append("r post. ");
            }else{
                sb.append("'s post. ");
            }

        }else{
            sb.append(". ");
        }
        long initial = ImageUtil.getCurrentDate().getTime();
        long finalTime = activity.getCreatedAt().getTime();
        String s = printElapsedTime(finalTime, initial);
        sb.append(s);
        return sb.toString();
    }
}
