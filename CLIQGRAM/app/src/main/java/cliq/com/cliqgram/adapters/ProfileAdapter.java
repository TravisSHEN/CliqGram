package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.viewHolders.ProfileViewHolder;

/**
 * Created by litaoshen on 4/10/2015.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder>{

    private Context context;
    private List<Post> postList;

    public ProfileAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout
                .profile_item, parent, false);

        ProfileViewHolder profileViewHolder = new ProfileViewHolder(view);
        return profileViewHolder;
    }

    @Override
    public void onBindViewHolder(final ProfileViewHolder holder, int position) {

        final Post post = postList.get(holder.getAdapterPosition());

        post.loadPhotoToView(context, holder.post_image);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if( this.postList != null ){
            size = postList.size();
        }
        return size;
    }

    public void updatePostList(List<Post> postList){
        if(postList == null ){
            return;
        }

        for(ListIterator<Post> iter = postList.listIterator(); iter.hasNext();){
            Post post = iter.next();
            if(post == null || post.getCreatedAt() == null ) {
                iter.remove();
           }
        }

        this.postList = postList;
        Collections.sort(postList);

        this.notifyDataSetChanged();
    }
}
