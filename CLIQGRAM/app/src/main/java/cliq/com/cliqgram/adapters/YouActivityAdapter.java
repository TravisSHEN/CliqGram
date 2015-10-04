package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import cliq.com.cliqgram.viewHolders.YouActivityViewHolder;

/**
 * Created by litaoshen on 29/09/2015.
 */
public class YouActivityAdapter extends RecyclerView.Adapter<YouActivityViewHolder>{
    Context context;

    public YouActivityAdapter(Context context) {
        this.context = context;
    }

    @Override
    public YouActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(YouActivityViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
