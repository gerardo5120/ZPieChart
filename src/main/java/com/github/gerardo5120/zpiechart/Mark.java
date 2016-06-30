package com.github.gerardo5120.zpiechart;

import android.graphics.Color;

/**
 * Created by cruzgerardoyanezteran on 27/6/16.
 */
public class Mark {
    public enum Position { INNER, OUTER }
    private Position markPosition = Position.INNER;
    private Position valPosition = Position.OUTER;
    private float valMargin = 10f;
    private float markMargin = 10f;
    private int color = 0xFF03A9F4;
    private int valColor = Color.BLACK;
    private float markWidth = 15f;
    private float markHeight = 3f;
    private float valSize = 20f;
    private boolean showValues = true;

    public Mark() {
        // NOPE
    }

    public void setValColor(int valColor) {
        this.valColor = valColor;
    }

    public void setValSize(float valSize) {
        this.valSize = valSize;
    }

    public void setWidth(float markWidth) {
        this.markWidth = markWidth;
    }

    public boolean showValue() { return showValues; }

    public void setShowValues(boolean showValues) { this.showValues = showValues; }

    public Position getPosition() { return markPosition; }

    public Position getValPosition() { return valPosition; }

    public int getValColor() {
        return valColor;
    }

    public float getValSize() { return valSize; }

    public float getHeight() {
        return markHeight;
    }

    public float getWidth() {
        return markWidth;
    }

    public int getColor() { return color; }

    public float getValMargin()  {
        return valMargin;
    }

}
