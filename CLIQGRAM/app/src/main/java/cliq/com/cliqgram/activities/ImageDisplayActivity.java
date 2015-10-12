package cliq.com.cliqgram.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.services.UserService;
import cliq.com.cliqgram.utils.GPUImageFilterTools;
import cliq.com.cliqgram.utils.Util;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class ImageDisplayActivity extends AppCompatActivity {
    Bitmap originalBitmap;
    Bitmap editedBitmap;

    // For image processing
    GPUImage gpuImage;
    private GPUImageFilter mFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;

    @Bind(R.id.brightnessBar)
    SeekBar brightnessBar;

    @Bind(R.id.contrastBar)
    SeekBar contrastBar;

    @Bind(R.id.imageView)
    ImageView imageView;

    private final int SCALED_WIDTH = 600;
    private final int SCALED_HEIGHT = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        // bind this activity with ButterKnife
        ButterKnife.bind(this);

        // get image from intent when activity start and resize it
        originalBitmap = resizeBitmap(getImage());

        // initialise editedBitmap to originalBitmap
        editedBitmap = originalBitmap;

        // create the GPUImage
        gpuImage = new GPUImage(getBaseContext());

        // set the bitmap image for gpuImage
        gpuImage.setImage(this.originalBitmap);

        // show image to the view
        imageView.setImageBitmap(this.originalBitmap);

        // EDIT PHOTO

        // brightness
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editedBitmap = applyContrastAndBrightnessFilters();

                // show image
                imageView.setImageBitmap(editedBitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // contrast
        contrastBar.setOnSeekBarChangeListener((new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editedBitmap = applyContrastAndBrightnessFilters();

                // show image
                imageView.setImageBitmap(editedBitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        }));
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
            case (R.id.action_upload):
                //TODO complete options

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
                Post.createPost(owner,
                        Util.convertBitmapToByte(editedBitmap),
                        "This is a great photo now!");
                finish();
                break;
            case (R.id.action_crop):
                // TODO
                finish();
                break;
            case (R.id.action_apply_filter):
                // TODO

                // TODO: test GPUImage
                GPUImageFilterTools.showDialog(this, new GPUImageFilterTools.OnGpuImageFilterChosenListener() {
                    @Override
                    public void onGpuImageFilterChosenListener(GPUImageFilter filter) {
                        editedBitmap = switchFilterTo(filter);
                        imageView.setImageBitmap( editedBitmap );
                    }
                });

//                finish();
                break;
            case (R.id.action_brightness_and_contrast):
                finish();
                break;
            case (R.id.action_share):
                finish();
                break;
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

//        File imageFile = this.getFileStreamPath(imageName);
//        String value=null;
//        long fileSize=imageFile.length()/1024;//call function and convert
//        // bytes into Kb
//        if(fileSize>=1024)
//            value=fileSize/1024+" Mb";
//        else
//            value=fileSize+" Kb";
//
//        Log.e("ImageDisplay", value);


        return Util.decodeStream(this, imageName);
    }

    // scale any bitmap to correct size
    private Bitmap resizeBitmap(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, SCALED_WIDTH, SCALED_HEIGHT, false);
    }

    // convert brightnessBar value to brightness value
    // value between -1.0 < v < 1.0
    // seekbar is between 0 and 100
    private float calculateBrightnessValue(int b) {
        return (b / (float) 50) - 1.0f;
    }

    // convert contrastBar value to contrast value
    // value between 0.0 < v < 4.0 (1 is average, set progress to 25)
    // seekbar is between 0 and 100
    private float calculateContrastValue(int c) {
        return c / (float) 25;
    }

    // apply contrast and brightness filters to originalBitmap
    // returns new bitmap
    private Bitmap applyContrastAndBrightnessFilters() {
        // get the contrast and brightness values
        float contrast = calculateContrastValue(contrastBar.getProgress());
        float brightness = calculateBrightnessValue(brightnessBar.getProgress());

        // apply contrast
        gpuImage.setFilter(new GPUImageContrastFilter(contrast));
        Bitmap intermediateBitmap = gpuImage.getBitmapWithFilterApplied(originalBitmap);

        // apply brightness filter
        gpuImage.setFilter(new GPUImageBrightnessFilter(brightness));
        Bitmap editedBitmap = gpuImage.getBitmapWithFilterApplied(intermediateBitmap);

        // return new bitmap
        return editedBitmap;
    }

    private Bitmap switchFilterTo(final GPUImageFilter filter) {
        Bitmap editedBitMap = null;
        if (mFilter == null
                || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;
            gpuImage.setFilter(mFilter);
            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
            editedBitMap = gpuImage.getBitmapWithFilterApplied();

        }

        return editedBitMap;
    }
}
