package cliq.com.cliqgram.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.fragments.CameraFragment;
import cliq.com.cliqgram.model.Post;
import cliq.com.cliqgram.services.UserService;
import cliq.com.cliqgram.utils.GPUImageFilterTools;
import cliq.com.cliqgram.utils.ImageUtil;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGlassSphereFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;

public class ImageDisplayActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final String FRAGMENT_DIALOG = "dialog";

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

    @Bind(R.id.postDescription)
    EditText postDescription;

    private final int SCALED_WIDTH = 600;
    private final int SCALED_HEIGHT = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        // bind this activity with ButterKnife
        ButterKnife.bind(this);

        // only check permission granted for sdk over M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestWriteExternalStoragePermisseon();

                finish();
            }
        }

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
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
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
                String description = postDescription.getText().toString();
                if (description == "" || description == null) {
                    description = "I love Cliqgram!";
                }

                Post.createPost(UserService.getCurrentUser(),
                        ImageUtil.convertBitmapToByte(editedBitmap),
                        description);
                finish();
                break;

            case (R.id.action_crop):
                String imageName = "image";

                // crop the image
                Uri original = ImageUtil.getImageUri(this.getBaseContext(), originalBitmap, imageName);
                beginCrop(original);
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
            croppedBitmap = ImageUtil.convertUriToBitmap(Crop.getOutput(result),
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

        return ImageUtil.decodeStream(this, imageName);
    }

    // scale any bitmap to correct size
    private Bitmap resizeBitmap(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, SCALED_WIDTH, SCALED_HEIGHT, false);
    }

    // convert brightnessBar value to brightness value
    // value between -1.0 < v < 1.0
    // seekbar is between 0 and 100
    private float calculateBrightnessValue(int b) {
        //return (b / (float) 50) - 1.0f;
        float divisor = 100.0f / 1.5f;
        return (b / divisor) - 0.75f;
    }

    // convert contrastBar value to contrast value
    // value between 0.0 < v < 4.0 (1 is average, set progress to 25)
    // seekbar is between 0 and 100
    private float calculateContrastValue(int c) {
        return (c / (float) 40) + 0.5f;
    }

    // apply all filters
    // brightness, contrast, and filter
    private Bitmap applyAllFilters() {
        // get the contrast, brightness, and filter values
        float contrast = calculateContrastValue(contrastBar.getProgress());
        float brightness = calculateBrightnessValue(brightnessBar.getProgress());
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
        switch (filterName) {
            case "No Filter":
                filter = null; // does nothing
                break;
            case "Emboss":
                filter = new GPUImageEmbossFilter();
                break;
            case "Gaussian Blur":
                filter = new GPUImageGaussianBlurFilter();
                break;
            case "Glass Sphere":
                filter = new GPUImageGlassSphereFilter(new PointF(0.5f, 0.5f), 0.5f, 0.71f);
                break;
            case "Grayscale":
                filter = new GPUImageGrayscaleFilter();
                break;
            case "Monochrome":
                filter = new GPUImageMonochromeFilter();
                break;
            case "Sepia":
                filter = new GPUImageSepiaFilter();
                break;
            case "Sketch":
                filter = new GPUImageSketchFilter();
                break;
            default: // apply no filter
                filter = null;
                break;
        }

        return filter;
    }

    // TODO: add permission confirmation response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestWriteExternalStoragePermisseon() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new CameraFragment.ConfirmationDialog().show
                    (this.getFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE_PERMISSION);
        }
    }
}