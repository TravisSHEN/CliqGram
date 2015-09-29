package cliq.com.cliqgram.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.DrawableRes;

/**
 * Created by litaoshen on 28/09/2015.
 */
public class Util {

    public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    public static Bitmap decodeResource(Context context, @DrawableRes int res) {
        return BitmapFactory.decodeResource(context.getResources(),
                res);
    }

    public static BitmapDrawable resizeDrawable(Context context, @DrawableRes
    int res, float scaleFactor){

        Bitmap bm = Util.decodeResource(context, res);
        int sizeX = Math.round(bm.getWidth() * scaleFactor);
        int sizeY = Math.round(bm.getHeight() * scaleFactor);

        Bitmap resized_bm = Util.resizeBitmap(bm, sizeX, sizeY);

        return new BitmapDrawable(context.getResources(), resized_bm);
    }

}
