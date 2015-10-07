package cliq.com.cliqgram.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by litaoshen on 29/09/2015.
 */
public class YouActivityViewHolder extends RecyclerView.ViewHolder {

    Context context;

    @Bind(R.id.youactivity_user_avatar)
    CircleImageView user_avatar;

    @Bind(R.id.youactivity_user_name)
    TextView user_name;

    @Bind(R.id.youactivity_text)
    TextView text;

    @Bind(R.id.youactivity_iamge)
    ImageView image;

    public YouActivityViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;

        ButterKnife.bind(this, itemView);
    }
}