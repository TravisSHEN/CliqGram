package cliq.com.cliqgram.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

}
