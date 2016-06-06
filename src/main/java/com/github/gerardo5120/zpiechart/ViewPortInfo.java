package com.github.gerardo5120.zpiechart;

import android.graphics.Matrix;
import android.graphics.Rect;

/**
 * Created by cruzgerardoyanezteran on 5/17/16.
 */
public class ViewPortInfo {
    private Matrix mMatrix;
    private Rect mVisibleRect;
    public float mRadius = 250f;
    private float[] mMatrixValues = new float[9];

    ViewPortInfo() {
        mMatrix = new Matrix();
        mVisibleRect = new Rect();
    }

    protected Matrix getMatrix() {
        return mMatrix;
    }

    protected Rect getRect() {
        return mVisibleRect;
    }

    protected float getRadius() {
        mMatrix.getValues(mMatrixValues);

        return mRadius * mMatrixValues[Matrix.MSCALE_X];
    }

    protected float getCenterX() {
        mMatrix.getValues(mMatrixValues);

        return (mVisibleRect.width() / 2) +
                (((mRadius * mMatrixValues[Matrix.MSCALE_X]) * 2) * mMatrixValues[Matrix.MTRANS_X]);
    }

    protected float getCenterY() {
        mMatrix.getValues(mMatrixValues);

        return (mVisibleRect.height() / 2) +
                (((mRadius * mMatrixValues[Matrix.MSCALE_Y]) * 2) * mMatrixValues[Matrix.MTRANS_Y]);
    }

    protected float getLeft() {
        return getCenterX() -
                getRadius();
    }

    protected float getTop() {
        return getCenterY() -
                getRadius();
    }

    protected float getRight() {
        return getLeft() +
                (getRadius() * 2);
    }

    protected float getBottom() {
        return getTop() +
                (getRadius() * 2);
    }

    protected float getPosX(float xCoordinate) {
        return ((xCoordinate - (getCenterX() - getRadius())) * 0.5f) /
                getRadius();
    }

    protected float getPosY(float yCoordinate) {
        return ((yCoordinate - (getCenterY() - getRadius())) * 0.5f) /
                getRadius();
    }

    protected float getScaleX() {
        mMatrix.getValues(mMatrixValues);
        return mMatrixValues[Matrix.MSCALE_X];
    }

    protected  float getTransX() {
        mMatrix.getValues(mMatrixValues);
        return mMatrixValues[Matrix.MTRANS_X];
    }

    protected  float getTransY() {
        mMatrix.getValues(mMatrixValues);
        return mMatrixValues[Matrix.MTRANS_Y];
    }

    protected void setScale(float newScale) {
        mMatrix.getValues(mMatrixValues);
        mMatrix.reset();
        mMatrix.setScale(newScale, newScale);
        mMatrix.postTranslate(mMatrixValues[Matrix.MTRANS_X], mMatrixValues[Matrix.MTRANS_Y]);
    }

    protected void setTranslate(float transX, float transY) {
        mMatrix.getValues(mMatrixValues);
        mMatrix.reset();
        mMatrix.setScale(mMatrixValues[Matrix.MSCALE_X], mMatrixValues[Matrix.MSCALE_Y]);
        mMatrix.postTranslate(transX, transY);
    }
}
