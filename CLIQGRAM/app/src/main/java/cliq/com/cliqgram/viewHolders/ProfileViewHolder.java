package cliq.com.cliqgram.viewHolders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;

/**
 * Created by litaoshen on 4/10/2015.
 */
public class ProfileViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.profile_item_view)
    public CardView profile_item_view;
    @Bind(R.id.profile_post_image)
    public ImageView post_image;

    public ProfileViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }
}
