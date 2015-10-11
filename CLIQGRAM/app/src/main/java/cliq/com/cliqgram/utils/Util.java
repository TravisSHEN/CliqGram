package cliq.com.cliqgram.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by litaoshen on 28/09/2015.
 */
public class Util {

    /**
     * resize bitmap
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    /**
     *
     * @param context
     * @param bitmap
     * @param scaleFactor
     * @return
     */
    public static Bitmap resizeBitmap(Context context, Bitmap bitmap, float
            scaleFactor) {

        int sizeX = Math.round(bitmap.getWidth() * scaleFactor);
        int sizeY = Math.round(bitmap.getHeight() * scaleFactor);

        return Bitmap.createScaledBitmap(bitmap, sizeX, sizeY, true);
    }

    /**
     * resize Bitmap drawable
     *
     * @param context
     * @param bitmapDrawable
     * @param scaleFactor
     * @return
     */
    public static BitmapDrawable resizeBitmapDrawable(Context context,
                                                      BitmapDrawable bitmapDrawable,
                                                      float scaleFactor) {
        if (bitmapDrawable == null) {
            return null;
        }
        Bitmap bm = bitmapDrawable.getBitmap();
        int sizeX = Math.round(bm.getWidth() * scaleFactor);
        int sizeY = Math.round(bm.getHeight() * scaleFactor);

        Bitmap resized_bm = Util.resizeBitmap(bm, sizeX, sizeY);

        return new BitmapDrawable(context.getResources(), resized_bm);
    }

    /**
     * return a bitmap from drawable resource id
     *
     * @param context
     * @param res
     * @return
     */
    public static Bitmap decodeResource(Context context, @DrawableRes int res) {
        return BitmapFactory.decodeResource(context.getResources(),
                res);
    }

    public static Bitmap decodeStream(Context context, String imageName){
        Bitmap bitmap = null;
        try {
             bitmap = BitmapFactory.decodeStream(context.openFileInput
                    (imageName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * resize drawable resource
     *
     * @param context
     * @param res
     * @param scaleFactor
     * @return
     */
    public static BitmapDrawable resizeDrawable(Context context, @DrawableRes
    int res, float scaleFactor) {

        Bitmap bm = Util.decodeResource(context, res);
        int sizeX = Math.round(bm.getWidth() * scaleFactor);
        int sizeY = Math.round(bm.getHeight() * scaleFactor);

        Bitmap resized_bm = Util.resizeBitmap(bm, sizeX, sizeY);

        return new BitmapDrawable(context.getResources(), resized_bm);
    }

    /**
     * get byte[] from uri
     *
     * @param context
     * @param uri
     * @return
     * @throws IOException
     */
    /*public static byte[] getBytesFromUri(Context context, Uri uri) throws
            IOException {
        InputStream inputStream = context.getContentResolver().openInputStream
                (uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }
    */

    public static byte[] convertImageToByte(Uri uri, ContentResolver cr) {
        byte[] data = null;
        try {
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * conver bitmap to byte[]
     *
     * @param bitmap
     * @return
     */
    public static byte[] convertBitmapToByte(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
//        Log.e("Util-Bitmap Size", String.valueOf(bitmap.getByteCount()));
        byte[] bytes = stream.toByteArray();

        return bytes;
    }

    /**
     *  return bitmap
     * @param imgData
     * @return
     */
    public static Bitmap convertByteToBitmap(byte[] imgData){

        if (imgData == null || imgData.length == 0) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(imgData, 0,
                imgData.length);

        return bitmap;
    }
    /**
     * Return a bitmap drawable
     *
     * @param context
     * @return
     */
    public static BitmapDrawable convertByteToBitmapDrawable(Context context, byte[]
            imgData) {

        Bitmap bitmap = Util.convertByteToBitmap(imgData);
        BitmapDrawable bitmapDrawable = null;
        if( bitmap != null ) {

            bitmapDrawable = new BitmapDrawable(context
                    .getResources(), bitmap);
        }

        return bitmapDrawable;
    }

    public static Date getCurrentDate() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        return date;
    }

    public static Uri getImageUri(Context context, Bitmap inImage, String
            imageName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context
                .getContentResolver(), inImage, imageName, null);
        return Uri.parse(path);
    }

    /**
     *
     * @param context
     * @param resid
     * @param view
     * @param scaleRate
     */
    public static void loadResToView( Context context, @DrawableRes int resid,
                                      View view, float scaleRate){

        BitmapDrawable bitmapDrawable = Util.resizeDrawable(context, resid,
                scaleRate);

        view.setBackground( bitmapDrawable );

    }

}
