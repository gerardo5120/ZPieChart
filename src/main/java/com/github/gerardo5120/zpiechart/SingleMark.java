package com.github.gerardo5120.zpiechart;

/**
 * Created by cruzgerardoyanezteran on 28/6/16.
 */
public class SingleMark extends Mark implements DialElement {
    private float mValue;
    private float maxScale = Float.MAX_VALUE;
    private float minScale = Float.MIN_VALUE;

    public SingleMark(float mValue) {
        this.mValue = mValue;
    }

    public float getValue() {
        return mValue;
    }






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
