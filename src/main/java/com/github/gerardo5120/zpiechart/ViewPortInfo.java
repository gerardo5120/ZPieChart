package com.github.gerardo5120.zpiechart;

import android.graphics.Matrix;
import android.graphics.Rect;

/**
 * Created by cruzgerardoyanezteran on 5/17/16.
 */
public class ViewPortInfo {
    private Matrix mMatrix;
    private Rect mVisibleRect;
    public float mRadius = 170f;
    private float[] mMatrixValues = new float[9];

    ViewPortInfo() {
        mMatrix = new Matrix();
        mVisibleRect = new Rect();
    }

    public boolean isInnerLimit(float difX, float difY) {
        float limitX = (mVisibleRect.right / 2) / (mRadius * 2);
        float newPosCenterX = getPieCenterX() + difX;
        float centerDistLeft = getPosX(0f, newPosCenterX) - 0.5f;
        float centerDistRight = getPosX(mVisibleRect.right, newPosCenterX) - 0.5f;


        float limitY = (mVisibleRect.height() / 2) / (mRadius * 2);
        float newPosCenterY = getPieCenterY() + difY;
        float centerDistTop = getPosY(0f, newPosCenterY) - 0.5f;
        float centerDistBottom = getPosY(mVisibleRect.height(), newPosCenterY) -0.5f;



        boolean innerLimitX = (Math.abs(centerDistLeft) <= limitX &&
                Math.abs(centerDistRight) <= limitX);
        boolean innerLimitY = (Math.abs(centerDistTop) <= limitY &&
                Math.abs(centerDistBottom) <= limitY);


        /*System.out.println("Port Limit Left: " +
                limitX +
                " "
                + centerDistLeft +
                " " +
                getPosX(0f) +
                " " +
                centerDistRight);*/

        System.out.println("Port Limit Top: " +
                limitY +
                " "
                + centerDistTop +
                " " +
                getPosY(0f) +
                " " +
                centerDistBottom);

        //System.out.println("Return: " +
        //        (Math.abs(centerDistLeft) <= limitX && Math.abs(centerDistRight) <= limitX));

        System.out.println("Return: " +
                (Math.abs(centerDistTop) <= limitY && Math.abs(centerDistBottom) <= limitY));



        return innerLimitX && innerLimitY;
        //return true;
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
        return mVisibleRect.width() / 2;
    }

    protected float getCenterY() {
        return mVisibleRect.height() / 2;
    }

    protected float getPieCenterX() {
        mMatrix.getValues(mMatrixValues);

        return (mVisibleRect.width() / 2) +
                (((mRadius * mMatrixValues[Matrix.MSCALE_X]) * 2) * mMatrixValues[Matrix.MTRANS_X]);
    }

    protected float getPieCenterY() {
        mMatrix.getValues(mMatrixValues);

        return (mVisibleRect.height() / 2) +
                (((mRadius * mMatrixValues[Matrix.MSCALE_Y]) * 2) * mMatrixValues[Matrix.MTRANS_Y]);
    }

    protected float getLeft() {
        return getPieCenterX() -
                getRadius();
    }

    protected float getTop() {
        return getPieCenterY() -
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

    protected float getPosX(float xCoordinate, float pieCenterX) {
        return ((xCoordinate - (pieCenterX - getRadius())) * 0.5f) /
                getRadius();
    }

    protected float getPosX(float xCoordinate) {
        return ((xCoordinate - (getPieCenterX() - getRadius())) * 0.5f) /
                getRadius();
    }

    protected float getPosY(float yCoordinate, float pieCenterY) {
        return ((yCoordinate - (pieCenterY - getRadius())) * 0.5f) /
                getRadius();
    }

    protected float getPosY(float yCoordinate) {
        return ((yCoordinate - (getPieCenterY() - getRadius())) * 0.5f) /
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
