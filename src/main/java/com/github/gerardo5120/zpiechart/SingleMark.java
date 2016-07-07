package com.github.gerardo5120.zpiechart;

/**
 * Created by cruzgerardoyanezteran on 28/6/16.
 */
public class SingleMark extends Mark implements DialElement {
    private float mValue;
    private float mMaxScale = Float.MAX_VALUE;
    private float mMinScale = Float.MIN_VALUE;
    private int mIcon;

    public SingleMark(float mValue) {
        this.mValue = mValue;
        this.setShowValue(false);
        //this.setValPosition(Position.INNER);
        //this.setMarkPosition(Position.OUTER);
    }

    public float getValue() {
        return mValue;
    }

    public int getIcon() { return mIcon; }

    public void setIcon(int mIcon) { this.mIcon = mIcon; }

    @Override
    public float getMinScale() {
        return mMinScale;
    }

    @Override
    public float getMaxScale() {
        return mMaxScale;
    }

    @Override
    public void setMaxScale(float maxScale) {
        this.mMaxScale = maxScale;
    }

    @Override
    public void setMinScale(float minScale) {
        this.mMinScale = minScale;
    }
}
