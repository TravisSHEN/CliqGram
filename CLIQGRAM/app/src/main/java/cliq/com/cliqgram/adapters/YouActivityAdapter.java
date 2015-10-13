package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.utils.Utils;
import cliq.com.cliqgram.model.Activity;
import cliq.com.cliqgram.viewHolders.YouActivityViewHolder;

/**
 * Created by litaoshen on 29/09/2015.
 */
public class YouActivityAdapter extends RecyclerView.Adapter<YouActivityViewHolder>{
    Context context;
    private List<Activity> activityList = new ArrayList<>();

    public YouActivityAdapter(Context context) {
        this.context = context;
    }

    @Override
    public YouActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.youactivity_item,
                parent, false);
        YouActivityViewHolder holder = new YouActivityViewHolder(context, view);
        return holder;
    }

    @Override
    public void onBindViewHolder(YouActivityViewHolder holder, int position) {
        Activity activity = activityList.get(position);

        if(activity != null){
            activity.getUser().loadAvatarToView(context, holder.user_avatar);
            String textStr = Utils.activityStringBuilder(activity, context);
            holder.text.setText(textStr);
        }else{
            Toast.makeText(context, "No activity found", Toast.LENGTH_SHORT).show();
        }

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
