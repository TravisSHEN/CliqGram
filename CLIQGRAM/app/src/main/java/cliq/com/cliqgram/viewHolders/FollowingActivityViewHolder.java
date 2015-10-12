package cliq.com.cliqgram.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import cliq.com.cliqgram.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by litaoshen on 29/09/2015.
 */
public class FollowingActivityViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    @Bind(R.id.followingactivity_user_avatar)
    public CircleImageView user_avatar;
    @Bind(R.id.followingactivity_user_name)
    public TextView user_name;
    @Bind(R.id.followingactivity_text)
    public TextView text;
    @Bind(R.id.followingactivity_iamge)
    public ImageView image;


    public FollowingActivityViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
    }
}