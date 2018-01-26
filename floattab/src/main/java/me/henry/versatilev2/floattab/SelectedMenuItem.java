package me.henry.versatilev2.floattab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by henry on 2018/1/16.
 */

public class SelectedMenuItem extends ImageView {
    private Paint mCirclePaint;
    private float radius = 0f;


    public SelectedMenuItem(Context context, int color) {
        this(context, null, color);
    }

    public SelectedMenuItem(Context context, AttributeSet attrs, int color) {
        this(context, attrs, 0, color);
    }

    public SelectedMenuItem(Context context, AttributeSet attrs, int defStyleRes, int color) {
        super(context, attrs, defStyleRes);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(ResourcesCompat.getColor(getResources(), color, null));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isActivated()) {
            drawCircleIcon(canvas);
        }
    }


    private void drawCircleIcon(Canvas canvas) {
        canvas.drawCircle((float) canvas.getWidth() / 2.0f, (float) canvas.getHeight() - (float) getPaddingBottom() / 1.5f, radius, mCirclePaint);
        if (radius <= (float) canvas.getWidth() / 20.0f) {
            radius++;
            invalidate();
        }
    }
}
