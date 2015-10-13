package cliq.com.cliqgram.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.*;
import android.hardware.camera2.*;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import cliq.com.cliqgram.R;
import cliq.com.cliqgram.callbacks.ImageSavedCallback;
import cliq.com.cliqgram.utils.Util;
import cliq.com.cliqgram.views.AutoFitTextureView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CameraActivity extends Activity implements OnClickListener {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final String TAG = "CameraActivity";

    private int PICK_IMAGE_REQUEST = 1;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    private final Semaphore mCameraOpenCloseLock = new Semaphore(1);
    @Bind(R.id.textureView_preview)
    AutoFitTextureView mTextureView;
    @Bind(R.id.view_grid)
    View myGridView;
    @Bind(R.id.button_capture)
    FloatingActionButton buttonCapture;
    //    Button buttonCapture;
    @Bind(R.id.button_flash)
    Button buttonFlash;
    @Bind(R.id.button_grid)
    Button buttonGrid;
    @Bind(R.id.button_gallery)
    Button buttonGallery;
    private boolean imageSaved = false;
    private boolean flashOn = true;
    private boolean gridOn  = false;
    private State mState = State.PREVIEW;
    private String mCameraId;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    byte[] photoBytes;
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            // TODO
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mImageSavedCallback));
//            mBackgroundHandler.post(new ImageInserter(reader.acquireNextImage()), mImageSavedCallback);


//            startDisplayActivity(reader.acquireNextImage());
        }

    };

//    private void startDisplayActivity(Image image) {
//        Intent intent = new Intent(this, ImageDisplayActivity.class);
//        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//        byte[] bytes = new byte[buffer.remaining()];
//        buffer.get(bytes);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        intent.putExtra("bitmap", bitmap);
//        startActivity(intent);
//    }

    private String imageFileName;
    private ImageSavedCallback mImageSavedCallback = new ImageSavedCallback() {
        @Override
        public void onImageSaved(String fileName) {
            imageSaved = true;
            imageFileName = fileName;
        }
    };
    private Size                   mPreviewSize;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest mPreviewRequest;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCaptureSession;
    private ImageReader mImageReader;
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
                                                                  = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            configTextureViewOutput(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };
    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            switch (mState) {
                case PREVIEW:
                    break;
                case WAITING_CLOCK:
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                        mState = State.PICTURE_TAKEN;
                    } else if (afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED || afState == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = State.PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                case WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE || aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = State.WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = State.PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }

            }
        }

        @Override
        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            process(result);
        }
    };
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraOpenCloseLock.release();
            mCameraDevice = camera;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            mCameraOpenCloseLock.release();
            mCameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            mCameraOpenCloseLock.release();
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;

            }
            finish();
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = State.WAITING_PRECAPTURE;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean isImageSaved() {
        return imageSaved;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void captureStillPicture() {
        try {
            if (null == mCameraDevice) {
                return;
            }
            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON);
            if (flashOn) {
                captureBuilder.set(CaptureRequest.FLASH_MODE, CaptureResult.FLASH_MODE_SINGLE);
            } else {
                captureBuilder.set(CaptureRequest.FLASH_MODE, CaptureResult.FLASH_MODE_OFF);
            }

            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            CameraCaptureSession.CaptureCallback captureCallback
                    = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
//                    if (filePath != null) {
//                        Log.e(TAG, filePath);
//                        showToast("Image imageSaved to " + filePath);
//                    }
                    unlockFocus();
                    while (true) {
                        if (isImageSaved()) {
                            imageSaved = false;
                            startActivity();
                            break;
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
//                    if (photoBytes != null) {
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
//                        startImageDisplayActivity(bitmap);
//                    }
                }
            };

            mCaptureSession.stopRepeating();
            mCaptureSession.capture(captureBuilder.build(), captureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void showToast(final String text) {
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            if (mPreviewRequestBuilder != null && mCaptureSession != null && mState != null) {
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                        CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON);
                System.out.println();
                mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                        mBackgroundHandler);
                // After this, the camera will go back to the normal state of preview.
                mState = State.PREVIEW;
                mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
                        mBackgroundHandler);
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreviewSession() {
        SurfaceTexture texture = mTextureView.getSurfaceTexture();
        texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

        Surface surface = new Surface(texture);

        try {
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), new CameraCaptureSession
                    .StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {

                    if (mCameraDevice == null) {
                        return;
                    }

                    mCaptureSession = session;
                    mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                    mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);

                    mPreviewRequest = mPreviewRequestBuilder.build();
                    try {
                        mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    showToast("Failed");
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ButterKnife.bind(this);

//        Log.d(TAG, (mTextureView == null ? "Null" : "Not Null"));

        // Full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        buttonCapture.setOnClickListener(this);
        buttonFlash.setOnClickListener(this);
        buttonGrid.setOnClickListener(this);
        buttonGallery.setOnClickListener(this);

        Util.loadResToView(this, R.drawable.icon_flash_on,
                buttonFlash, 0.3f );

        Util.loadResToView(this, R.drawable.icon_grid_off,
                buttonGrid, 0.3f );

        Util.loadResToView(this, R.drawable.icon_gallery,
                buttonGallery, 0.3f );

    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();

        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    protected void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (mCaptureSession != null) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (mImageReader != null) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mCameraOpenCloseLock.release();
        }

    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void openCamera(int width, int height) {
        setUpCameraOutput(width, height);
        configTextureViewOutput(width, height);

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting for Camera.");
            }
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (InterruptedException e) {
            throw new RuntimeException("Accessing camera interrupted.");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void configTextureViewOutput(int viewWidth, int viewHeight) {

        if (null == mTextureView || null == mPreviewSize) {
            return;
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);

    }

    private void setUpCameraOutput(int width, int height) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String id : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(id);

                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap configurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (configurationMap == null) {
                    continue;
                }

                Size largest = Collections.max(Arrays.asList(configurationMap.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());


                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, /*maxImages*/2);
                mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);

                mPreviewSize = chooseOptimalSize(configurationMap.getOutputSizes(SurfaceTexture.class), width, height, largest);

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mTextureView.setAspectRatio(
                            mPreviewSize.getWidth(), mPreviewSize.getHeight());
                } else {
                    mTextureView.setAspectRatio(
                            mPreviewSize.getHeight(), mPreviewSize.getWidth());
                }


                mCameraId = id;

                System.out.println();
            }
        } catch (CameraAccessException e) {
            finish();
        }

    }

    private Size chooseOptimalSize(Size[] outputSizes, int width, int height, Size aspectRatio) {
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : outputSizes) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "No matching resolution");
            return outputSizes[0];
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_capture:
                takePicture();
                break;
            case R.id.button_flash:
                if (flashOn) {
                    flashOn = false;
//                    buttonFlash.setText(R.string.button_flash_off);
                    Util.loadResToView(this, R.drawable.icon_flash_off,
                            buttonFlash, 0.7f );
                } else {
                    flashOn = true;
//                    buttonFlash.setText(R.string.button_flash_on);
                    Util.loadResToView(this, R.drawable.icon_flash_on,
                            buttonFlash, 0.7f );
                }
                break;
            case R.id.button_grid:
                // TODO
                if (gridOn) {
                    gridOn = false;
                    myGridView.setVisibility(View.INVISIBLE);
//                    buttonGrid.setText(R.string.button_grid_off);
                    Util.loadResToView(this, R.drawable.icon_grid_off,
                            buttonGrid, 0.7f );
                } else {
                    gridOn = true;
                    myGridView.setVisibility(View.VISIBLE);
//                    buttonGrid.setText(R.string.button_grid_on);
                    Util.loadResToView(this, R.drawable.icon_grid_on,
                            buttonGrid, 0.7f );
                }
                break;
            case R.id.button_gallery:

                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                break;
        }

    }

    private void takePicture() {
        lockFocus();
    }

    private void lockFocus() {
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
        mState = State.WAITING_CLOCK;

        try {
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private enum State {
        PREVIEW, WAITING_CLOCK, WAITING_PRECAPTURE, WAITING_NON_PRECAPTURE, PICTURE_TAKEN
    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getHeight() * (long) lhs.getWidth() - (long) rhs.getWidth() * (long) rhs.getHeight());
        }
    }

//    private class ImageInserter implements Runnable {
//
//        private Image mImage;
//        private String fileName;
//
//        public ImageInserter(Image mImage) {
//            this.fileName = "image_" + Long.toString(mImage.getTimestamp()) + ".jpg";
//            this.mImage = mImage;
//        }
//
//        @Override
//        public void run() {
//            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
//            byte[] bytes = new byte[buffer.remaining()];
//            buffer.get(bytes);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            MediaStore.Images.Media.insertImage(CameraActivity.this.getContentResolver(), bitmap, fileName,
//                    null);
//
//            // start ImageDisplayActivity
//            startImageDisplayActivity(bitmap);
//        }
//    }

    private class ImageSaver implements Runnable {

        private final Image mImage;
        private ImageSavedCallback mImageSavedCallback;

        public ImageSaver(Image image, ImageSavedCallback imageSavedCallback) {
            mImage = image;
            mImageSavedCallback = imageSavedCallback;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] photoBytes = new byte[buffer.remaining()];
            buffer.get(photoBytes);
            String fileName = String.valueOf(Util.getCurrentDate().getTime());
            Bitmap bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
            byte[] imageData = Util.convertBitmapToByte(bitmap);

            try {

                FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
                fo.write(imageData);
                fo.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                fileName = null;
            }

            mImageSavedCallback.onImageSaved(fileName);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

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
             */    Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap
                        (getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                startImageDisplayActivity(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
    }

    /**
     * open ImageDisplayActivity and pass recent taken/selected image to it
     *
     * @param bitmap
     */
    private void startImageDisplayActivity(Bitmap bitmap) {

        String fileName = savePhoto(bitmap);

        if (fileName == null) {
            Toast.makeText(this, "Photo is not taken successfully.", Toast
                    .LENGTH_SHORT).show();
            return;
        }

        imageFileName = fileName;
        startActivity();

    }

    private void startActivity() {
        Intent intent = new Intent(CameraActivity.this, ImageDisplayActivity.class);
        intent.putExtra("image", imageFileName);
        startActivity(intent);
    }

    private String savePhoto(Bitmap bitmap) {
        String fileName = String.valueOf(Util.getCurrentDate().getTime());
        byte[] imageData = Util.convertBitmapToByte(bitmap);

        try {

            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(imageData);
            fo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            fileName = null;
        }

        return fileName;
    }
}