package cliq.com.cliqgram.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Benjamin on 15/10/3.
 */
public class MyGridView extends View {
    Paint paint;

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(255, 255, 255, 255));
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
}
