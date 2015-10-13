package cliq.com.cliqgram.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cliq.com.cliqgram.R;
import cliq.com.cliqgram.utils.ImageUtil;

/**
 * Created by litaoshen on 2/09/2015.
 */
@ParseClassName("_User")
public class User extends ParseUser {

    public static final int AVATAR_WIDTH = 200;
    public static final int AVATAR_HEIGHT = 200;

    public User() {
        super();
    }


    /**
     * @param context
     * @return
     */
    public BitmapDrawable getAvatarInBitmapDrawable(Context context) {
        return ImageUtil.convertByteToBitmapDrawable(context, this.getAvatarData());
    }

    public Bitmap getAvatarBitmap() {
        Bitmap bitmap = ImageUtil.convertByteToBitmap(this.getAvatarData());
        return bitmap;
    }


    public void getAvatarData(GetDataCallback callback) {
        ParseFile photo = this.getParseFile("photo");
        if (photo != null) {
            photo.getDataInBackground(callback);
        }
    }

    public Uri getAvatarUri() {
        ParseFile photoFile = this.getParseFile("avatar");
        Uri imageUri = null;
        if (photoFile != null) {
            imageUri = Uri.parse(photoFile.getUrl());
        }

        return imageUri;
    }

    public void loadAvatarToView(Context context, ImageView imageView) {
        Uri avatarPath = this.getAvatarUri();

        if (avatarPath == null) {

            // show default avatar
            Picasso.with(context)
                    .load(R.drawable.icon_avatar)
                    .resize(AVATAR_WIDTH, AVATAR_HEIGHT)
                    .centerCrop()
                    .into(imageView);
        } else {

            Picasso.with(context)
                    .load(avatarPath)
                    .resize(AVATAR_WIDTH, AVATAR_HEIGHT)
                    .centerCrop()
                    .into(imageView);
        }

    }

    public byte[] getAvatarData() {
        return this.getBytes("avatar");
    }

    public void setAvatarData(ParseFile avatar) {
        this.put("avatar", avatar);
//        this.put("avatar", avatarData);
    }

    public void setAvatarFromPost(Post post) {
        this.put("avatar", post.getPhotoDataAsParseFile());
    }

    public List<Post> getPostList() {
        return this.getList("posts");
    }

    public void setPostList(List<Post> postList) {
        List<Post> temp = new ArrayList<>();

        if (postList != null) {
            temp = postList;
        }
        this.put("posts", temp);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Username: " + this.getUsername();
    }
}
