package com.github.gerardo5120.zpiechart;

import android.graphics.Color;

/**
 * Created by cruzgerardoyanezteran on 30/5/16.
 */
public class ValueDial extends Dial {
    private float valSize = 23f;
    private float valMargin = 10f;
    private int valColor = 0xFF00796B;
    private float value;
    private float startAt = 0f;
    private float borderHeight = 30f;

    public ValueDial(float value, float startAt) {
        super();

        this.value = value;
        this.startAt = startAt;
    }

    public float getBorderHeight() {
        return borderHeight;
    }

    public float getValSize() {
        return valSize;
    }

    public float getValMargin() {
        return valMargin;
    }

    public int getValColor() {
        return valColor;
    }

    public float getValue() {
        return value;
    }

    public float getStartAt() {
        return startAt;
    }

    public void setBorderHeight(float borderHeight) {
        this.borderHeight = borderHeight;
    }

    public void setValSize(float valSize) {
        this.valSize = valSize;
    }

    public void setValMargin(float valMargin) {
        this.valMargin = valMargin;
    }

    public void setValColor(int valColor) {
        this.valColor = valColor;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setStartAt(float startAt) {
        this.startAt = startAt;
    }
}
