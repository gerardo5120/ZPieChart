package com.github.gerardo5120.zpiechart;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by cruzgerardoyanezteran on 27/5/16.
 */
public class OnDrawChartParams {
    private Canvas mCanvas;
    private Paint mPaint;

    public OnDrawChartParams(Canvas canvas, Paint paint) {
        mCanvas = canvas;
        mPaint = paint;
    }

    public Canvas getCanvas() {
        return mCanvas;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    public void setCanvas(Canvas mCanvas) {
        this.mCanvas = mCanvas;
    }
}
