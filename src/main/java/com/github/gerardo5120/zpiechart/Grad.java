package com.github.gerardo5120.zpiechart;

import android.graphics.Color;

/**
 * Created by cruzgerardoyanezteran on 5/19/16.
 */
public class Grad {
    public enum Position { INNER, OUTER }
    private Position markPosition = Position.INNER;
    private Position valPosition = Position.OUTER;
    private float valMargin = 10f;
    private float markMargin = 10f;
    private float span;
    private int color = 0xFF03A9F4;
    private int valColor = Color.BLACK;
    private float markWidth = 15f;
    private float markHeight = 3f;
    private float valSize = 20f;
    private boolean showValues = true;

    private float maxScale = Float.MAX_VALUE;
    private float minScale = Float.MIN_VALUE;

    public Grad(float span) {
        this.span = span;
    }

    public void setValColor(int valColor) {
        this.valColor = valColor;
    }

    public void setValSize(float valSize) {
        this.valSize = valSize;
    }

    public Grad(float span, float minScale) {
        this.span = span;
        this.minScale = minScale;
    }

    public float getMaxScale() {
        return maxScale;
    }

    public float getMinScale() {
        return minScale;
    }

    public void setMarkWidth(float markWidth) {
        this.markWidth = markWidth;
    }

    public boolean showValues() { return showValues; }

    public void setShowValues(boolean showValues) { this.showValues = showValues; }

    public Position getMarkPosition() { return markPosition; }

    public Position getValPosition() { return valPosition; }

    public int getValColor() {
        return valColor;
    }

    public float getValSize() { return valSize; }

    public float getMarkHeight() {
        return markHeight;
    }

    public float getMarkWidth () {
        return markWidth;
    }

    public int getColor() { return color; }

    public float getSpan() {
        return span;
    }

    public float getValMargin()  {
        return valMargin;
    }
}
