package cliq.com.cliqgram.activities;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.helper.CameraManager;
import cliq.com.cliqgram.views.PhotoGridView;

import static cliq.com.cliqgram.R.*;

/**
 * Created by ilkan on 11/10/2015.
 */
public class CameraActivity2 extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Button captureButton;
    private boolean gridOn;
    //@Bind(R.id.view_grid)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(CameraManager.checkCameraHardware(this)){
            setContentView(layout.activity_camera2);

            // Create an instance of Camera
            mCamera = CameraManager.getCameraInstance();

            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(id.camera_preview);
            preview.addView(mPreview);
            captureButton = (Button) findViewById(id.button_capture);
            captureButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // get an image from the camera
                            mCamera.takePicture(null, null, mPicture);
                            PhotoGridView gridView = (PhotoGridView) findViewById(R.id.view_grid);
                            PhotoGridView.setVisible(gridView);
                            CameraManager.releaseCamera(mCamera);
                        }
                    }
            );
        }
    }



    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = CameraManager.getOutputMediaFile(CameraManager.MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                //Log.d(TAG, "Error creating media file, check storage permissions: " +
                 //       e.getMessage());
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                //Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                //Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };






}
