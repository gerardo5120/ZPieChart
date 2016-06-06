package com.github.gerardo5120.zpiechart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by cruzgerardoyanezteran on 27/5/16.
 */
public class OnDrawMarkParams extends OnDrawChartParams {
    private float grades;
    private Rect rect;
    private float markValue;

    public OnDrawMarkParams(Canvas canvas, Paint paint, float grades, Rect rect, float markValue) {
        super(canvas, paint);

        this.grades = grades;
        this.rect = rect;
        this.markValue = markValue;
    }

    public float getGrades() {
        return grades;
    }

    public Rect getRect() {
        return rect;
    }

    public float getMarkValue() {
        return markValue;
    }

    public void setGrades(float grades) {
        this.grades = grades;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setMarkValue(float markValue) {
        this.markValue = markValue;
    }
}
