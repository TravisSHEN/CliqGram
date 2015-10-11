package cliq.com.cliqgram.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import cliq.com.cliqgram.R;

/**
 * Created by ilkan on 11/10/15.
 */
public class PhotoGridView extends View {

    Paint paint;

    public PhotoGridView(Context context) {
        super(context);
    }

    public PhotoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //  Find Screen size first
        int screenWidth = getMeasuredWidth();
        int screenHeight = getMeasuredHeight();

        canvas.drawLine((screenWidth / 3) * 2, 0, (screenWidth / 3) * 2, screenHeight, paint);
        canvas.drawLine((screenWidth / 3), 0, (screenWidth / 3), screenHeight, paint);
        canvas.drawLine(0, (screenHeight / 3) * 2, screenWidth, (screenHeight / 3) * 2, paint);
        canvas.drawLine(0, (screenHeight / 3), screenWidth, (screenHeight / 3), paint);
    }

    public static void setVisible(PhotoGridView view){
        view.setVisibility(View.VISIBLE);
    }

}
