package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.helper.Utils;
import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.viewHolders.FollowingActivityViewHolder;

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
            sb.append(" followed ");
        }else if(activity.getActivityType().equals("like")){
            sb.append(" liked ");
        }else if(activity.getActivityType().equals("comment")){
            sb.append(" commented ");
        }/*else if(activity.getActivityType().equals("post")){
            sb.append(" posted ");
        }*/
        sb.append(activity.getTargetUser().getUsername());
        if(activity.getEventId()!= null){
            sb.append("'s post. ");
        }
        Date date = new Date();
        //Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalString);
        Utils.dateFormat.format(date);
        long initial = Utils.getTime(Utils.dateFormat.format(date).toString());
        long finalTime = Utils.getTime(Utils.dateFormat.format(activity.getCreatedAt()).toString());

        sb.append(Utils.printElapsedTime(initial, finalTime));
        holder.text.setText(activity.getActivityId());

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public void updateData(List<Activity> activityList){
        this.activityList = activityList;
        this.notifyDataSetChanged();
    }
}
