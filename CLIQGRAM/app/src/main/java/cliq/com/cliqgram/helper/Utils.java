package cliq.com.cliqgram.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

        //StringBuilder lapseMsg = new StringBuilder("Elapsed time since ").append(new Date(initial)).append(" to " + new Date(finalTime)).append(":\r\n");
        //lapseMsg.append(days).append(" Days, ").append(hrs).append(" Hours, ").append(mins).append(" Minutes, ").append(secs).append(" seconds");
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

    /*just used to get any date to test.*/
    public static long getTime(String date) {
        DateFormat format = DateFormat.getDateTimeInstance();
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            throw new RuntimeException();
        }
    }
}
