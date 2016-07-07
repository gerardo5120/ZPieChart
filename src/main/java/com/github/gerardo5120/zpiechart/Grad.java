package com.github.gerardo5120.zpiechart;

/**
 * Created by cruzgerardoyanezteran on 5/19/16.
 */
public class Grad implements DialElement {
    private float mSpan;
    private Mark mMark;
    private float maxScale = Float.MAX_VALUE;
    private float minScale = Float.MIN_VALUE;
    private boolean startInZero = false;

    public Grad(float span, Mark mark) {
        this.mSpan = span;
        this.mMark = mark;
    }

    public float getSpan() {
        return mSpan;
    }

    public Mark getMark() { return mMark; }

    public boolean isStartInZero() { return startInZero; }

    @Override
    public float getMinScale() {
        return minScale;
    }

    @Override
    public float getMaxScale() {
        return maxScale;
    }

    @Override
    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
    }

    @Override
    public void setMinScale(float minScale) {
        this.minScale = minScale;
    }
}
