package cliq.com.cliqgram.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

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

    public byte[] getAvatarData(){
        return this.getBytes("avatar");
    }

    public void setAvatarData(byte[] avatarData) {
//        String avatarLabel = "img_" + this.getUsername()+".jpg";
//        ParseFile avatar = new ParseFile(avatarLabel, avatarData);
//        avatar.saveInBackground();
        this.put("avatar", avatarData);
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
