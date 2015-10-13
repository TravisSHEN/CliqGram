package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.utils.Util;
import cliq.com.cliqgram.viewHolders.FollowingActivityViewHolder;

import static cliq.com.cliqgram.helper.Utils.printElapsedTime;

/**
 * Created by litaoshen on 29/09/2015.
 */
public class FollowingActivityAdapter extends RecyclerView.Adapter<FollowingActivityViewHolder>{

    private Context context;
    private List<Activity> activityList = new ArrayList<>();

    public FollowingActivityAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FollowingActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.followingactivity_item,
                parent, false);
        FollowingActivityViewHolder holder = new FollowingActivityViewHolder(context, view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FollowingActivityViewHolder holder, int position) {

        Activity activity = activityList.get(position);

        activity.getUser().loadAvatarToView(context, holder.user_avatar);
        StringBuilder sb = new StringBuilder();
        sb.append(activity.getUser().getUsername());
        if(activity.getActivityType().equals("follow")){
            sb.append(" started following ");
        }else if(activity.getActivityType().equals("like")){
            sb.append(" liked ");
        }else if(activity.getActivityType().equals("comment")){
            sb.append(" commented ");
        }/*else if(activity.getActivityType().equals("post")){
            sb.append(" posted ");
        }*/
        sb.append(activity.getTargetUser().getUsername());
        if(activity.getEventId()!= null && !activity.getEventId().equals("") ){
            sb.append("'s post. ");
        }else{
            sb.append(". ");
        }
        //Date date = Util.getCurrentDate().getTime();
        //Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalString);
        //Utils.dateFormat.format(date);
        long initial = Util.getCurrentDate().getTime();
        long finalTime = activity.getCreatedAt().getTime();
        String s = printElapsedTime(finalTime, initial);
        sb.append(s);
        holder.text.setText(sb.toString());

    }


    @Override
    public int getItemCount() {
        return this.activityList.size();
    }

    public void updateData(List<Activity> activityList){
        this.activityList = activityList;
        this.notifyDataSetChanged();
    }
}
