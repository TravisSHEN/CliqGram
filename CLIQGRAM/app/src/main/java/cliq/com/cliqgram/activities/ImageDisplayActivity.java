package cliq.com.cliqgram.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.model.User;
import cliq.com.cliqgram.services.UserService;
import cliq.com.cliqgram.utils.GPUImageFilterTools;
import cliq.com.cliqgram.utils.Util;
import jp.co.cyberagent.android.gpuimage.*;

public class ImageDisplayActivity extends AppCompatActivity {
    Bitmap originalBitmap;
    Bitmap croppedBitmap;
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

    @Bind(R.id.filter_spinner)
    Spinner spinner;

    private final int SCALED_WIDTH  = 600;
    private final int SCALED_HEIGHT = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        // bind this activity with ButterKnife
        ButterKnife.bind(this);

        // get image from intent when activity start and resize it
        originalBitmap = resizeBitmap(getImage());

        // initialise cropped and edited Bitmaps to originalBitmap
        croppedBitmap = originalBitmap;
        editedBitmap = originalBitmap;

        // create the GPUImage
        gpuImage = new GPUImage(getBaseContext());

        // set the bitmap image for gpuImage
        gpuImage.setImage(this.originalBitmap);

        // show image to the view
        imageView.setImageBitmap(this.editedBitmap);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filters_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        // EDIT PHOTO

        // brightness
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //editedBitmap = applyContrastAndBrightnessFilters();
                editedBitmap = applyAllFilters();

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
        contrastBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //editedBitmap = applyContrastAndBrightnessFilters();
                editedBitmap = applyAllFilters();

                // show image
                imageView.setImageBitmap(editedBitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // image filtering
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // apply all filters
                editedBitmap = applyAllFilters();

                // show image
                imageView.setImageBitmap(editedBitmap);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                Post.createPost(UserService.getCurrentUser(),
                        Util.convertBitmapToByte(editedBitmap),
                        "This is a great photo now!");
                finish();
                break;

            case (R.id.action_crop):
                String imageName = "image";

                // crop the image
                Uri original = Util.getImageUri(this.getBaseContext(), originalBitmap, imageName);
                beginCrop(original);
                break;

            case (R.id.action_share):
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            handleCrop(resultCode, result);

            // re-apply filters
            editedBitmap = applyAllFilters();

            // show image
            imageView.setImageBitmap(editedBitmap);

        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            croppedBitmap = Util.convertUriToBitmap(Crop.getOutput(result),
                                                    this.getContentResolver());
        } else {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * get image from intent passed from CameraActivity
     *
     * @return image Bitmap (format)
     */
    private Bitmap getImage() {

        Intent intent = this.getIntent();
        String imageName = intent.getStringExtra("image");

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

    // apply all filters
    // brightness, contrast, and filter
    private Bitmap applyAllFilters() {
        // get the contrast, brightness, and filter values
        float contrast = calculateContrastValue(contrastBar.getProgress());
        float brightness           = calculateBrightnessValue(brightnessBar.getProgress());
        GPUImageFilter imageFilter = parseFilterFromString(spinner.getSelectedItem().toString());

        // apply contrast
        gpuImage.setFilter(new GPUImageContrastFilter(contrast));
        Bitmap afterContrastBitmap = gpuImage.getBitmapWithFilterApplied(croppedBitmap);

        // apply brightness filter
        gpuImage.setFilter(new GPUImageBrightnessFilter(brightness));
        Bitmap afterBrightnessBitmap = gpuImage.getBitmapWithFilterApplied(afterContrastBitmap);

        // apply other filter

        // if no filter selected, return here
        if (imageFilter == null) {
            return afterBrightnessBitmap;
        }

        // otherwise apply the filter
        gpuImage.setFilter(imageFilter);
        Bitmap finalBitmap = gpuImage.getBitmapWithFilterApplied(afterBrightnessBitmap);

        // return new bitmap
        return finalBitmap;
    }

    private GPUImageFilter parseFilterFromString(String filterName) {
        // GPUImage Filter
        GPUImageFilter filter;

        // determine filter
        switch(filterName){
            case "No Filter":
                filter = null; // does nothing
                break;
            case "Sepia":
                filter = new GPUImageSepiaFilter();
                break;
            case "Gaussian Blur":
                filter = new GPUImageGaussianBlurFilter();
                break;
            case "Grayscale":
                filter = new GPUImageGrayscaleFilter();
                break;
            case "Emboss":
                filter = new GPUImageEmbossFilter();
                break;
            case "Gamma":
                filter = new GPUImageGammaFilter();
                break;
            case "Glass Sphere":
                filter = new GPUImageGlassSphereFilter();
                break;
            default: // apply no filter
                filter = null;
                break;
        }

        return filter;
    }
}
