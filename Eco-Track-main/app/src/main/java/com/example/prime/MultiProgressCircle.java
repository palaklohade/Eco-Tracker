package com.example.prime;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MultiProgressCircle extends View {

    private final Paint paint;
    private final RectF rectF;

    private float progress1 = 25f;
    private float progress2 = 30f;
    private float progress3 = 45f;

    private final int[] colors = {
            0xFF02542D, // Dark green (#02542D)
            0xFF377D46, // Green (#377D46)
            0xFF84E6AE  // Light green (#84E6AE)
    };

    public MultiProgressCircle(Context context) {
        this(context, null);
    }

    public MultiProgressCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiProgressCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        rectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredSize = 250; // Default size
        int resolvedWidth = resolveSize(desiredSize, widthMeasureSpec);
        int resolvedHeight = resolveSize(desiredSize, heightMeasureSpec);

        setMeasuredDimension(resolvedWidth, resolvedHeight);
        paint.setStrokeWidth(Math.min(resolvedWidth, resolvedHeight) * 0.15f); // Dynamic stroke width
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float halfStrokeWidth = paint.getStrokeWidth() / 2;
        rectF.set(halfStrokeWidth, halfStrokeWidth, w - halfStrokeWidth, h - halfStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startAngle = -90f; // Start from the top
        float[] progresses = {progress1, progress2, progress3};

        for (int i = 0; i < progresses.length; i++) {
            paint.setColor(colors[i]);
            float sweepAngle = (progresses[i] / 100f) * 360f;
            canvas.drawArc(rectF, startAngle, sweepAngle, false, paint);
            startAngle += sweepAngle;
        }
    }

    public void setProgress(float p1, float p2, float p3) {
        this.progress1 = p1;
        this.progress2 = p2;
        this.progress3 = p3;
        invalidate();
    }
}