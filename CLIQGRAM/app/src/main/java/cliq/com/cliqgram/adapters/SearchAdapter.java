package cliq.com.cliqgram.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.views.SquareImageView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by litaoshen on 5/10/2015.
 */
public class SearchAdapter extends CursorAdapter {

    List<User> userList;

    // text view for showing results
    @Bind(R.id.search_username)
    TextView text_username;
    @Bind(R.id.search_user_avatar)
    CircleImageView search_avatar;
    @Bind(R.id.search_post_image)
    SquareImageView post_image;


    public SearchAdapter(Context context, MatrixCursor cursor, List<User>
            userList) {
        super(context, cursor, false);

        if (userList == null) {
            this.userList = new ArrayList<>();
        } else {
            this.userList = userList;
        }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater
                .inflate(R.layout.search_result_item, parent, false);

        // bind view to ButterKnife
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        User user = userList.get(cursor.getPosition());
        if (user == null) {
            return;
        }

        text_username.setText(user.getUsername());
//        Bitmap avatar = user.getAvatarBitmap();
//        search_avatar.setImageBitmap(avatar);

//        List<Post> postList = user.getPostList();
//
//        if (postList == null || postList.isEmpty()) {
//            return;
//        }
//
//        Collections.sort(postList);
//        final Post recent_post = postList.get(0);
//        recent_post.getPhotoData(new GetDataCallback() {
//            @Override
//            public void done(byte[] data, ParseException e) {
//                if (e == null) {
//                    Bitmap bitmap = recent_post.getPhotoInBitmap(context, data);
//                    Bitmap resized_bitmap = Util.resizeBitmap(context,
//                            bitmap, 0.1f);
//                    post_image.setImageBitmap(resized_bitmap);
//                } else {
//                    Toast.makeText(context, e.getMessage(), Toast
//                            .LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}
