package cliq.com.cliqgram.helper;

import java.util.Comparator;
import java.util.Date;

import cliq.com.cliqgram.model.Activity;

/**
 * Created by ilkan on 12/10/2015.
 */
public class ActivityDateComparer implements Comparator<Activity> {
    @Override
    public int compare(Activity lhs, Activity rhs) {

        int comparison = compare(lhs.getCreatedAt(), rhs.getCreatedAt());
        return comparison;
    }

    public static int compare(Date lDate, Date rDate){
        if(lDate.before(rDate)){
            return 0;
        }else{
            return 1;
        }
    }
}
