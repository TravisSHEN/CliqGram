package cliq.com.cliqgram.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * Created by Benjamin on 15/9/11.
 * This is a texture view that changes its layout based on the ratio of the image.
 */
public class AutoFitTextureView extends TextureView {
    private int mWidthRatio = 0;
    private int mHeightRatio = 0;

    public AutoFitTextureView(Context context) {
        this(context, null);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Set up the ratio.
     *
     * @param width The width ratio.
     * @param height The height ratio.
     */
    public void setRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Parameters cannot be negative");
        }
        mWidthRatio = width;
        mHeightRatio = height;
        requestLayout();
    }

    /**
     * Change the layout of the texture view based on the ratio.
     *
     * @param widthMeasureSpec Parent width.
     * @param heightMeasureSpec Parent height.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mWidthRatio == 0 || mHeightRatio == 0) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mWidthRatio / mHeightRatio) {
                setMeasuredDimension(width, width * mHeightRatio / mWidthRatio);
            } else {
                setMeasuredDimension(height * mWidthRatio / mHeightRatio, height);
            }
        }
    }
}
