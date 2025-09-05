package com.example.prime;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class GraphView extends View {

    private Paint paint;

    private float[] electricityData = {10, 20, 15, 25, 30, 22, 18};
    private float[] gasData = {15, 12, 18, 20, 15, 22, 25};
    private float[] waterData = {5, 10, 15, 12, 20, 18, 25};

    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true); // Enable anti-aliasing for smoother rendering
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calculate spacing and scaling
        float spacing = (float) getWidth() / (electricityData.length - 1);
        float scaleY = (float) getHeight() / (Math.max(
                Math.max(getMaxValue(electricityData), getMaxValue(gasData)),
                getMaxValue(waterData)) + 10);

        // Draw each data series
        drawSmoothPath(canvas, electricityData, Color.parseColor("#596EEF"), spacing, scaleY);
        drawSmoothPath(canvas, gasData, Color.parseColor("#F6869E"), spacing, scaleY);
        drawSmoothPath(canvas, waterData, Color.parseColor("#28D5B3"), spacing, scaleY);
    }

    private void drawSmoothPath(Canvas canvas, float[] data, int color, float spacing, float scaleY) {
        Path path = new Path();

        for (int i = 0; i < data.length - 1; i++) {
            float x1 = i * spacing;
            float y1 = getHeight() - data[i] * scaleY;
            float x2 = (i + 1) * spacing;
            float y2 = getHeight() - data[i + 1] * scaleY;

            if (i == 0) {
                path.moveTo(x1, y1);
            }

            // Calculate control points for a cubic Bézier curve
            float controlX1 = x1 + (x2 - x1) / 3f;
            float controlY1 = y1;
            float controlX2 = x1 + 2 * (x2 - x1) / 3f;
            float controlY2 = y2;

            path.cubicTo(controlX1, controlY1, controlX2, controlY2, x2, y2);
        }

        paint.setColor(color);
        canvas.drawPath(path, paint);
    }

    private float getMaxValue(float[] data) {
        float max = data[0];
        for (float value : data) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
