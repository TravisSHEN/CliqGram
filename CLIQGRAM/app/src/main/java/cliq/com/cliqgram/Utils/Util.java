package cliq.com.cliqgram.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.Log;

import java.io.ByteArrayOutputStream;
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
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    /**
     * return a bitmap from drawable resource id
     * @param context
     * @param res
     * @return
     */
    public static Bitmap decodeResource(Context context, @DrawableRes int res) {
        return BitmapFactory.decodeResource(context.getResources(),
                res);
    }

    /**
     * resize Bitmap drawable
     * @param context
     * @param bitmapDrawable
     * @param scaleFactor
     * @return
     */
    public static BitmapDrawable resizeBitmapDrawable(Context context,
                                                      BitmapDrawable bitmapDrawable,
                                                      float scaleFactor){
        Bitmap bm = bitmapDrawable.getBitmap();
        if( bm == null ){
            return null;
        }
        int sizeX = Math.round(bm.getWidth() * scaleFactor);
        int sizeY = Math.round(bm.getHeight() * scaleFactor);

        Bitmap resized_bm = Util.resizeBitmap(bm, sizeX, sizeY);

        return new BitmapDrawable(context.getResources(), resized_bm);
    }

    /**
     * resize drawable resource
     * @param context
     * @param res
     * @param scaleFactor
     * @return
     */
    public static BitmapDrawable resizeDrawable(Context context, @DrawableRes
    int res, float scaleFactor){

        Bitmap bm = Util.decodeResource(context, res);
        int sizeX = Math.round(bm.getWidth() * scaleFactor);
        int sizeY = Math.round(bm.getHeight() * scaleFactor);

        Bitmap resized_bm = Util.resizeBitmap(bm, sizeX, sizeY);

        return new BitmapDrawable(context.getResources(), resized_bm);
    }

    /**
     * get byte[] from uri
     * @param context
     * @param uri
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromUri(Context context, Uri uri) throws
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

    /**
     * conver bitmap to byte[]
     * @param bitmap
     * @return
     */
    public static byte[] convertBitmapToByte(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        Log.e("Util-Bitmap Size", String.valueOf(bitmap.getByteCount()));
        byte[] bytes = stream.toByteArray();

        return bytes;
    }

    /**
     * Return a bitmap drawable
     * @param context
     * @return
     */
    public static BitmapDrawable convertByteToBitmapDrawable(Context context, byte[]
            imgData){

        Bitmap bitmap = BitmapFactory.decodeByteArray(imgData, 0,
                imgData.length);

        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static Date getCurrentDate(){

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        return date;
    }

}
