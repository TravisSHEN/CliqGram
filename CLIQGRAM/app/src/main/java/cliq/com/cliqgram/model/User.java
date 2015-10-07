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

import cliq.com.cliqgram.utils.Util;

/**
 * Created by litaoshen on 2/09/2015.
 */
@ParseClassName("_User")
public class User extends ParseUser{

    public User() {
        super();
    }


    /**
     *
     * @param context
     * @return
     */
    public BitmapDrawable getAvatarInBitmapDrawable(Context context){
        return Util.convertByteToBitmapDrawable(context, this.getAvatarData());
    }

    public Bitmap getAvatarBitmap(){
        Bitmap bitmap = Util.convertByteToBitmap(this.getAvatarData());
        return bitmap;
    }


    public void getAvatarData(GetDataCallback callback) {
        ParseFile photo = this.getParseFile("photo");
        if(photo != null){
            photo.getDataInBackground(callback);
        }
    }

    public Uri getAvatarUri(){
        ParseFile photoFile = this.getParseFile("avatar");
        Uri imageUri = null;
        if( photoFile != null ) {
             imageUri = Uri.parse(photoFile.getUrl());
        }

        return imageUri;
    }

    public void loadAvatartoView( Context context, ImageView imageView){
        if(this.getAvatarUri() == null ){
            return;
        }
        Picasso.with(context)
                .load( this.getAvatarUri().toString() )
                .resize(200, 200)
                .centerCrop()
                .into(imageView);
    }

    public byte[] getAvatarData(){
        return this.getBytes("avatar");
    }

    public void setAvatarData(ParseFile avatar) {
        this.put("avatar", avatar);
//        this.put("avatar", avatarData);
    }

    public List<Post> getPostList() {
        return this.getList("posts");
    }

    public void setPostList(List<Post> postList) {
        List<Post> temp = new ArrayList<>();

        if(postList != null ){
            temp = postList;
        }
        this.put("posts", temp);
    }

}
