package cliq.com.cliqgram.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by litaoshen on 28/09/2015.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        @Bind(R.id.comment_user_avatar)
        public CircleImageView comment_user_avatar;
        @Bind(R.id.comment_user_name)
        public TextView comment_user_name;
        @Bind(R.id.comment_text)
        public TextView comment_text;

        public CommentViewHolder(Context context, View itemView) {
                super(itemView);

                this.context = context;
                // bind view with ButterKnife
                ButterKnife.bind(this, itemView);
        }
}
