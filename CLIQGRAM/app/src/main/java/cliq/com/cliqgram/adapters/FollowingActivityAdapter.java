package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import cliq.com.cliqgram.viewHolders.FollowingActivityViewHolder;

/**
 * Created by litaoshen on 29/09/2015.
 */
public class FollowingActivityAdapter extends RecyclerView.Adapter<FollowingActivityViewHolder>{

    private Context context;

    public FollowingActivityAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FollowingActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FollowingActivityViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }
}
