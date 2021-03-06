package com.github.gerardo5120.zpiechart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by cruzgerardoyanezteran on 27/5/16.
 */
public class OnDrawMarkValueParams extends OnDrawChartParams {
    private float x;
    private float y;
    private float grades;
    private float markValue;
    private Bitmap icon;

    public OnDrawMarkValueParams(Canvas canvas, Paint paint, float x, float y,
                                 float grades, float markValue) {
        super(canvas, paint);

        this.x = x;
        this.y = y;
        this.grades = grades;
        this.markValue = markValue;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getGrades() {
        return grades;
    }

    public float getMarkValue() {
        return markValue;
    }

    public void setIcon(Bitmap icon) { this.icon = icon; }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setGrades(float grades) {
        this.grades = grades;
    }

    public void setMarkValue(float markValue) {
        this.markValue = markValue;
    }

    public Bitmap getIcon() {
        return icon;
    }
}
