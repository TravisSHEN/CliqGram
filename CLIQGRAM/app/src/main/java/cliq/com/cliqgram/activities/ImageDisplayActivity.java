package cliq.com.cliqgram.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.services.UserService;
import cliq.com.cliqgram.utils.Util;

public class ImageDisplayActivity extends AppCompatActivity {


    Bitmap bitmap;

    @Bind(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        // bind this activity with ButterKnife
        ButterKnife.bind(this);

        // get image from intent when activity start
        this.bitmap = getImage();

        // show image to view
//        imageView.setImageBitmap(this.bitmap);

        // using this way to make a post after editing
        // PS: passing a byte[] into this function
        // PS: there is convert function in utils.Util
        // PS: Geo location is taken care in Post model when post create
        /**
         * @param imageDate byte[]
         * @param currentUser User
         * @param description String
         * @return post Post
         * Note: Any data in post object may not be able to
         * get before post.saveInBackground() in finished.
         * So, check the database (table "Post") on Parse to see if post is
         * created successfully.
         * If post is created successfully, it will be shown on home page.
         */
        User owner = UserService.getCurrentUser();
        Post.createPost(owner, Util.convertBitmapToByte(this.bitmap), "This is" +
                " a " +
                "good photo");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_upload:
                // TODO
                finish();
                break;
            case R.id.action_edit:
                // TODO
                finish();
                break;
            case R.id.action_share:
                // TODO
                finish();
                break;
            case R.id.action_open_camera:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * get image from intent passed from CameraActivity
     *
     * @return image Bitmap (format)
     */
    private Bitmap getImage() {

        Intent intent = this.getIntent();
        String imageName = intent.getStringExtra("image");

        File imageFile = this.getFileStreamPath(imageName);

        String value=null;
        long Filesize=imageFile.length()/1024;//call function and convert
        // bytes into Kb
        if(Filesize>=1024)
            value=Filesize/1024+" Mb";
        else
            value=Filesize+" Kb";

        Log.e("ImageDisplay", value);


        Picasso.with(this)
                .load(imageFile)
                .resize(600, 600)
                .centerCrop()
                .into(imageView);

        Bitmap bitmap = Util.decodeStream(this, imageName);

        return bitmap;
    }
}
