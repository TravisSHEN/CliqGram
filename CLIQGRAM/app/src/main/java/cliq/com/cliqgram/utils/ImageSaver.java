package cliq.com.cliqgram.utils;

/**
 * Created by litaoshen on 28/11/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import cliq.com.cliqgram.callbacks.ImageSavedCallback;

/**
 * Saves a JPEG {@link Image} into the specified {@link File}.
 */
public class ImageSaver implements Runnable {

    private ImageSavedCallback mImageSavedCallback;

    /**
     * The JPEG image
     */
    private final Image mImage;

    private final Bitmap mBitmap;
    /**
     * The file we save the image into.
     */
    private final File mFile;

    /**
     * The context for running ImageSaver
     */
    private final Context mContext;

    public ImageSaver(Context context, Image image, File file,
                      ImageSavedCallback imageSavedCallback) {
        mContext = context;
        mImage = image;
        mBitmap = null;
        mFile = file;

        mImageSavedCallback = imageSavedCallback;
    }

    public ImageSaver(Context context, Bitmap bitmap, File file,
                      ImageSavedCallback imageSavedCallback) {
        mContext = context;
        mImage = null;
        mBitmap = bitmap;
        mFile = file;

        mImageSavedCallback = imageSavedCallback;
    }

    @Override
    public void run() {
        byte[] bytes = new byte[256];
        if (mImage != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
                bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                mImage.close();
            }

        } else if (mBitmap != null) {
            bytes = ImageUtil.convertBitmapToByte(mBitmap);
        }

        FileOutputStream output = null;
        try {
            output = mContext.openFileOutput(mFile.getName(), Context
                    .MODE_PRIVATE);
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // run callback
        if( mImageSavedCallback != null ){
            mImageSavedCallback.onImageSaved(mFile.getName());
        }
    }
}

