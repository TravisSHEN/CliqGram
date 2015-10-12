package cliq.com.cliqgram.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.views.SquareImageView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by litaoshen on 7/10/2015.
 */
public class UserSuggestViewHolder extends RecyclerView.ViewHolder {

    Context context;

    @Bind(R.id.suggest_user)
    public LinearLayout suggest_user;
    @Bind(R.id.suggest_username)
    public TextView username;
    @Bind(R.id.suggest_user_avatar)
    public CircleImageView avatar;
    @Bind(R.id.suggest_image)
    public SquareImageView image;

    public UserSuggestViewHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;

        ButterKnife.bind(this, itemView);
    }
}
