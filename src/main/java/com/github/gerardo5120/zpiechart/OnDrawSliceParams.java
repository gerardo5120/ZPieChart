package com.github.gerardo5120.zpiechart;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by cruzgerardoyanezteran on 31/5/16.
 */
public class OnDrawSliceParams extends OnDrawChartParams {
    private String text;
    private float value;

    public OnDrawSliceParams(Canvas canvas, Paint paint, String text, float value) {
        super(canvas, paint);
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public float getValue() {
        return value;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
