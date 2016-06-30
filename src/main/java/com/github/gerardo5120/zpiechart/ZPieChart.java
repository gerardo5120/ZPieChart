package com.github.gerardo5120.zpiechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.*;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.HashSet;

/**
 * Pie chart that allows make zoom and visualize data more accurately
 *
 * Created by cruzgerardoyanezteran on 5/9/16.
 *
 * @author Gerardo Yañez
 */
public class ZPieChart extends View {

    public enum PieChartComponent { GRADED_DIALS, VALUE_DIALS, LABELS };


    private static final float CIRCLE_DEGREES = 360f;
    private final float GRADES_ADDED = -90f;

    private float[] mValues;

    private boolean mShowGradedDials = true;
    private boolean mShowValueDials = true;
    private boolean mShowLabels = true;

    private boolean mScaling = false;

    private Zoomer mZoomer;

    private int[] mColorValues = { 0xFFBCAAA4, 0xFF8D6E63, 0xFFAFB42B,
            0xFFDCE775, 0xFF009688, 0xFF00897B,
            0xFF00796B };

    private ViewPortInfo mViewPortInfo;
    private HashSet<GradedDial> mGradedDials;
    private HashSet<ValueDial> mValueDials;
    private HashSet<Float> mSpansInDial;

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;

    private Paint mPaintCanvas;
    private Paint mPaintMark;
    private Paint mPaintMarkVal;
    private Paint mPaintSlices;
    private Paint mPaintGradedDial;

    private OnDrawChartHandler mOnDrawChartHandler;

    private float txtL = 0f;
    private float txtT = 0f;

    private float txtR = 0f;
    private float txtB = 0f;


    private float mMaxZoom = 5.00f;
    private float mMinZoom = 1.00f;

    private float mScaleFactor = 1.00f;



    public ZPieChart(Context context) {
        super(context);

        init(context);

        initPaints();
    }

    public ZPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);

        initPaints();
    }

    public ZPieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);

        initPaints();
    }


    private void init(Context context) {
        this.mValues = new float[] { 50f, 20f, 80f };

        mValueDials = new HashSet<>();
        mGradedDials = new HashSet<>();

        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        mGestureDetector = new GestureDetector(context, mSimpleGestureListener);


        mViewPortInfo =  new ViewPortInfo();
        mOnDrawChartHandler = new OnDrawChartSimpleHandler();

        mSpansInDial = new HashSet<>();
        mZoomer = new Zoomer(context);
    }

    /**
     * Initialize new Pie chart with specified mValues
     *
     * @param mValues
     */
    public ZPieChart(Context context, float[] mValues) {
        super(context);

        this.mValues = mValues;

        init(context);

        initPaints();
    }

    public void setValues(float[] mValues) {
        this.mValues = mValues;
    }

    public void setOnDrawChartHandler(OnDrawChartHandler mOnDrawChartHandler) {
        this.mOnDrawChartHandler = mOnDrawChartHandler;
    }

    public HashSet<ValueDial> getValueDials() {
        return mValueDials;
    }

    public HashSet<GradedDial> getGradedDials() {
        return mGradedDials;
    }

    public void showOnly(PieChartComponent component) {
        mShowGradedDials = mShowValueDials = mShowLabels = false;

        if (component == PieChartComponent.GRADED_DIALS) {
            mShowGradedDials = true;
        }
        else if (component == PieChartComponent.VALUE_DIALS) {
            mShowValueDials = true;
        }
        else if (component == PieChartComponent.LABELS) {
            mShowLabels = true;
        }

        ViewCompat.postInvalidateOnAnimation(ZPieChart.this);
    }

    public void showAllComponents() {
        mShowGradedDials = mShowValueDials = mShowLabels = true;

        ViewCompat.postInvalidateOnAnimation(ZPieChart.this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getGlobalVisibleRect(mViewPortInfo.getRect());

        mOnDrawChartHandler.onDrawBegin(new OnDrawChartParams(canvas, mPaintCanvas));

        drawSlices(canvas);


        if (mShowValueDials) {
            for (ValueDial valueDial : mValueDials) {
                drawValueDial(valueDial, canvas);
            }
        }

        if (mShowGradedDials) {
            for (GradedDial gradedDial : mGradedDials) {
                drawGradedDial(gradedDial, canvas);
            }
        }
    }

    private void drawValueDial(ValueDial valueDial, Canvas canvas) {
        Paint paintValDial = new Paint();
        RectF rectDial = new RectF();


        float left = mViewPortInfo.getLeft() - valueDial.getDistance();
        float top = mViewPortInfo.getTop() - valueDial.getDistance();
        float right = mViewPortInfo.getRight() + valueDial.getDistance();
        float bottom = mViewPortInfo.getBottom() + valueDial.getDistance();

        rectDial.set(left, top, right, bottom);


        paintValDial.setColor(valueDial.getColor());
        paintValDial.setStrokeWidth(valueDial.getWidth());
        paintValDial.setAntiAlias(true);
        paintValDial.setStrokeCap(Paint.Cap.SQUARE);
        paintValDial.setStyle(Paint.Style.STROKE);

        float totalValues = getTotalValues();

        float valGrades = (valueDial.getValue() * CIRCLE_DEGREES) / totalValues;
        float startGrades = GRADES_ADDED;

        if (valueDial.getStartAt() != 0)
            startGrades = ((valueDial.getStartAt() * CIRCLE_DEGREES) / totalValues) +
                    GRADES_ADDED;

        canvas.drawArc(rectDial, startGrades, valGrades, false, paintValDial);







        Path pathText = new Path();
        pathText.addArc(rectDial, startGrades, valGrades);

        PathMeasure pathMeasure = new PathMeasure();
        pathMeasure.setPath(pathText, false);



        Paint paintText = new Paint();
        paintText.setColor(valueDial.getValColor());
        paintText.setStyle(Paint.Style.FILL_AND_STROKE);
        paintText.setTextSize(valueDial.getValSize());
        paintText.setAntiAlias(true);

        OnDrawValueDialParams params = new OnDrawValueDialParams(canvas, paintText,
                String.valueOf(valueDial.getValue()), valueDial.getValue());

        if (!mOnDrawChartHandler.onDrawValueDial(params)) {
            float textLength = getTextWidth(params.getText(), paintText);

            canvas.drawTextOnPath(params.getText(),
                    pathText,
                    (pathMeasure.getLength() / 2) - (textLength / 2),
                    -valueDial.getValMargin(),
                    paintText);
        }




        float leftBE = right - (valueDial.getBorderHeight() / 2);
        float topBE = mViewPortInfo.getPieCenterY() - (valueDial.getWidth() / 2);
        float rightBE = right + (valueDial.getBorderHeight() / 2);
        float bottomBE = mViewPortInfo.getPieCenterY() + (valueDial.getWidth() / 2);

        Paint paintBE = new Paint();
        paintBE.setColor(valueDial.getColor());
        paintBE.setStyle(Paint.Style.FILL);
        paintBE.setAntiAlias(true);

        Rect rectBeginEnd = new Rect((int) leftBE, (int) topBE,
                (int) rightBE, (int) bottomBE);


        canvas.rotate(startGrades, mViewPortInfo.getPieCenterX(), mViewPortInfo.getPieCenterY());
        canvas.drawRect(rectBeginEnd, paintBE);

        canvas.rotate(valGrades, mViewPortInfo.getPieCenterX(), mViewPortInfo.getPieCenterY());
        canvas.drawRect(rectBeginEnd, paintBE);

        canvas.rotate(-valGrades, mViewPortInfo.getPieCenterX(), mViewPortInfo.getPieCenterY());
        canvas.rotate(-startGrades, mViewPortInfo.getPieCenterX(), mViewPortInfo.getPieCenterY());
    }

    private void drawSlices(Canvas canvas) {
        float total = getTotalValues();
        float startGrades = GRADES_ADDED;

        float hypotenuse = (float) Math.sqrt(Math.pow(mViewPortInfo.getRadius(), 2) +
                Math.pow(mViewPortInfo.getRadius(), 2));

        for (int i = 0; i < mValues.length; i++) {
            float val = mValues[i];
            float grades = (val * CIRCLE_DEGREES) / total;

            mPaintSlices.setColor(mColorValues[i]);

            canvas.drawArc(mViewPortInfo.getLeft(),
                    mViewPortInfo.getTop(),
                    mViewPortInfo.getRight(),
                    mViewPortInfo.getBottom(),
                    startGrades,
                    grades,
                    true,
                    mPaintSlices);






            Paint paintText = new Paint();
            paintText.setColor(0xFF880E4F);
            paintText.setTextSize(24);
            paintText.setAntiAlias(true);

            float height = (grades * hypotenuse) / 90;

            String text = String.valueOf(val);

            OnDrawSliceParams params = new OnDrawSliceParams(canvas, paintText, text, val);


            //System.out.println("Grades: " + startGrades);

            mOnDrawChartHandler.onDrawSlice(params);


            //if (!mOnDrawChartHandler.onDrawSlice(params)) {

            if (mShowLabels) {

                if (true) {
                    float textLength = getTextWidth(params.getText(), paintText);


                    float leftText = mViewPortInfo.getPieCenterX() +
                            ((mViewPortInfo.getRadius() / 2) - (textLength / 2));
                    float topText = mViewPortInfo.getPieCenterY() + 12;

                    float pivotPointX = mViewPortInfo.getPieCenterX() + (mViewPortInfo.getRadius() / 2);
                    float pivotPointY = mViewPortInfo.getPieCenterY();

                /*if (textLength > height) {
                    leftText = mViewPortInfo.getPieCenterX() + mViewPortInfo.getRadius();
                    pivotPointX = mViewPortInfo.getPieCenterX() + mViewPortInfo.getRadius() +
                            (mViewPortInfo.getRadius() / 2);
                }*/


                    float outTextX = mViewPortInfo.getPieCenterX() +
                            ((float) (Math.cos(Math.toRadians(startGrades + (grades / 2))) *
                                    (mViewPortInfo.getRadius() + 90)));

                    float outTextY = mViewPortInfo.getPieCenterY() +
                            (float) (Math.sin(Math.toRadians(startGrades + (grades / 2))) *
                                    (mViewPortInfo.getRadius() + 90));


                    float initLineX = mViewPortInfo.getPieCenterX() +
                            ((float) (Math.cos(Math.toRadians(startGrades + (grades / 2))) *
                                    mViewPortInfo.getRadius()));

                    float initLineY = mViewPortInfo.getPieCenterY() +
                            (float) (Math.sin(Math.toRadians(startGrades + (grades / 2))) *
                                    mViewPortInfo.getRadius());


                    getTextCoords(textLength, 24, outTextX, outTextY, startGrades + (grades / 2));


                    canvas.drawText(params.getText(), txtL, txtT, paintText);


                    Paint paintLine = new Paint();
                    paintLine.setColor(Color.GRAY);
                    paintLine.setStrokeWidth(3f);
                    paintLine.setAntiAlias(true);
                    canvas.drawLine(outTextX, outTextY, initLineX, initLineY, paintLine);



                /*
                Rect r1 = new Rect((int) outTextX, (int) outTextY, (int) (outTextX + 5), (int) (outTextY + 5));
                Paint p1 = new Paint();
                p1.setColor(Color.BLACK);
                p1.setStyle(Paint.Style.FILL);
                canvas.drawRect(r1, p1);

                System.out.println("Grades: " + (startGrades + (grades / 2)) +
                        " Ex X: " + outTextX + " Y: " + outTextY +
                        " Center X: " + mViewPortInfo.getPieCenterX() + " Y: " + mViewPortInfo.getPieCenterY() +
                        " Cos: " + Math.cos(Math.toRadians(startGrades + (grades / 2))) +
                        " Radius: " + mViewPortInfo.getRadius());
                */


                    canvas.rotate(startGrades + (grades / 2),
                            mViewPortInfo.getPieCenterX(), mViewPortInfo.getPieCenterY());

                    canvas.rotate(-(startGrades + (grades / 2)),
                            pivotPointX, pivotPointY);

                    //canvas.drawText(params.getText(), leftText, topText, paintText);

                    canvas.rotate(startGrades + (grades / 2),
                            pivotPointX, pivotPointY);

                    canvas.rotate(-(startGrades + (grades / 2)),
                            mViewPortInfo.getPieCenterX(), mViewPortInfo.getPieCenterY());
                }
            }


            startGrades += grades;
        }
    }

    private void getTextCoords(float txtWidth, float txtHeight,
                                float outTextX, float outTextY, float grades) {

        if (grades >= -90 && grades < -45) {
            txtL = outTextX;
            txtT = outTextY;
        }
        else if (grades >= -45 && grades < 0) {
            txtL = outTextX;
            txtT = outTextY + (txtHeight / 2);
        }

        else if (grades >= 0 && grades < 45) {
            txtL = outTextX;
            txtT = outTextY + (txtHeight / 2);
        }
        else if (grades >= 45 && grades < 90) {
            txtL = outTextX;
            txtT = outTextY + txtHeight;
        }


        else if (grades >= 90 && grades < 135) {
            txtL = outTextX - txtWidth;
            txtT = outTextY + txtHeight;
        }
        else if (grades >= 135 && grades < 180) {
            txtL = outTextX - txtWidth;
            txtT = outTextY + (txtHeight / 2);
        }
        else if (grades >= 180 && grades < 225) {
            txtL = outTextX - txtWidth;
            txtT = outTextY + (txtHeight / 2);
        }
        else if (grades >= 225 && grades < 270) {
            txtL = outTextX - txtWidth;
            txtT = outTextY;
        }


        txtR = txtL + txtWidth;
        txtB = txtT + txtHeight;


        if (grades >= -90 && grades < 90
                && txtR > mViewPortInfo.getRect().right) {
            txtR = mViewPortInfo.getRect().right;
            txtL = txtR - txtWidth;
        }
        else if (grades >= 90 && grades < 270
                && txtL < 0) {
            txtL += -(txtL);
            txtR = txtL + txtWidth;

            float distX = mViewPortInfo.getPieCenterX() - txtR;
            float distY = (float) (Math.sqrt(Math.pow(mViewPortInfo.getRadius(), 2) - distX));
            float cordY = 0;

            if (grades < 180) {
                cordY = mViewPortInfo.getPieCenterY() + distY;
            }
            else if (grades >= 180) {
                cordY = mViewPortInfo.getPieCenterY() - distY;
            }

            txtT = cordY;
            txtB = cordY + txtHeight;
        }
    }

    private void drawGradedDial(GradedDial dial, Canvas canvas) {
        mPaintGradedDial.setStrokeWidth(dial.getWidth());
        mPaintGradedDial.setColor(dial.getColor());

        if (dial.showLine()) {
            canvas.drawArc(mViewPortInfo.getLeft() - dial.getDistance(),
                    mViewPortInfo.getTop() - dial.getDistance(),
                    mViewPortInfo.getRight() + dial.getDistance(),
                    mViewPortInfo.getBottom() + dial.getDistance(),
                    0,
                    CIRCLE_DEGREES,
                    true,
                    mPaintGradedDial);
        }


        mSpansInDial.clear();

        for (Grad grad: dial.getGrads()) {
            if ((grad.getMaxScale() > mViewPortInfo.getScaleX())
                    && (mViewPortInfo.getScaleX() > grad.getMinScale())) {
                drawGrad(grad, dial, canvas);
                mSpansInDial.add(grad.getSpan());
            }
        }

        for (SingleMark singleMark: dial.getSingleMarks()) {
            if ((singleMark.getMaxScale() > mViewPortInfo.getScaleX())
                    && (mViewPortInfo.getScaleX() > singleMark.getMinScale())) {
                drawSingleMark(singleMark, dial, canvas);
            }
        }
    }


    private void drawSingleMark(SingleMark mark, Dial dial, Canvas canvas) {
        float markLeft = mViewPortInfo.getPieCenterX() +
                mViewPortInfo.getRadius() + dial.getDistance();

        if (mark.getPosition() == Mark.Position.INNER)
            markLeft -= mark.getWidth();


        float markTop = mViewPortInfo.getPieCenterY() - (mark.getHeight() / 2);
        float markBottom = markTop + mark.getHeight();
        float markRight = markLeft + mark.getWidth();


        // TODO: Change to a most adecuate exception
        if (mark.getValue() > getTotalValues())
            return;

        float degreesMark = ((mark.getValue() * CIRCLE_DEGREES) / getTotalValues()) + GRADES_ADDED;

        mPaintMark.setColor(mark.getColor());

        canvas.rotate(degreesMark,
                mViewPortInfo.getPieCenterX(),
                mViewPortInfo.getPieCenterY());

        Rect rectMark = new Rect((int) markLeft,
                (int) markTop,
                (int) markRight,
                (int) markBottom);

        canvas.drawRect(rectMark,
                mPaintMark);

        canvas.rotate(-1 * degreesMark,
                mViewPortInfo.getPieCenterX(),
                mViewPortInfo.getPieCenterY());

        if (mark.showValue() == false)
            return;



        float markSpanSum = mark.getValue();
        float valueLeft = markLeft + mark.getWidth() + mark.getValMargin();
        float valueTop = markTop + (mark.getValSize() / 2);
        float pivotPointMarkX;
        float pivotPointMarkY;

        mPaintMarkVal.setColor(mark.getColor());
        mPaintMarkVal.setTextSize(mark.getValSize());

        canvas.rotate(degreesMark,
                mViewPortInfo.getPieCenterX(),
                mViewPortInfo.getPieCenterY());

        if (mark.getValPosition() == Mark.Position.INNER)
            valueLeft = markLeft - mark.getValMargin() - getTextWidth(markSpanSum, mPaintMarkVal);

        pivotPointMarkX = valueLeft + (getTextWidth(markSpanSum, mPaintMarkVal) / 2);
        pivotPointMarkY = valueTop;

        canvas.rotate(-1 * (degreesMark),
                pivotPointMarkX,
                pivotPointMarkY);


        canvas.drawText(String.valueOf(markSpanSum),
                valueLeft,
                valueTop,
                mPaintMarkVal);

        canvas.rotate(degreesMark,
                pivotPointMarkX,
                pivotPointMarkY);

        canvas.rotate(-1 * (degreesMark),
                mViewPortInfo.getPieCenterX(),
                mViewPortInfo.getPieCenterY());

    }

    private void drawGrad(Grad grad, Dial dial, Canvas canvas) {
        float markSpanSum = 0.0f;
        float startGrades = GRADES_ADDED;
        float markLeft = mViewPortInfo.getPieCenterX() +
                mViewPortInfo.getRadius() + dial.getDistance();

        if (grad.getMark().getPosition() == Mark.Position.INNER)
            markLeft -= grad.getMark().getWidth();

        float markTop = mViewPortInfo.getPieCenterY() - (grad.getMark().getHeight() / 2);
        float markBottom = markTop + grad.getMark().getHeight();
        float markRight = markLeft + grad.getMark().getWidth();

        float degreesMarksSpan = (grad.getSpan() * CIRCLE_DEGREES) / getTotalValues();
        mPaintMark.setColor(grad.getMark().getColor());

        while (startGrades < (CIRCLE_DEGREES + GRADES_ADDED)) {

            canvas.rotate(startGrades,
                    mViewPortInfo.getPieCenterX(),
                    mViewPortInfo.getPieCenterY());

            Rect rectMark = new Rect((int) markLeft,
                    (int) markTop,
                    (int) markRight,
                    (int) markBottom);

            OnDrawChartParams params = new OnDrawMarkParams(canvas, mPaintMark, startGrades
                    , rectMark, markSpanSum);

            canvas.drawRect(rectMark,
                    mPaintMark);

            canvas.rotate(-1 * (startGrades),
                    mViewPortInfo.getPieCenterX(),
                    mViewPortInfo.getPieCenterY());

            markSpanSum += grad.getSpan();
            startGrades += degreesMarksSpan;
        }





        if (grad.getMark().showValue() == false)
            return;


        startGrades = GRADES_ADDED;
        markSpanSum = 0.0f;
        float valueLeft = markLeft + grad.getMark().getWidth() + grad.getMark().getValMargin();
        float valueTop = markTop + (grad.getMark().getValSize() / 2);
        float pivotPointMarkX;
        float pivotPointMarkY;

        mPaintMarkVal.setColor(grad.getMark().getValColor());
        mPaintMarkVal.setTextSize(grad.getMark().getValSize());

        while (startGrades < (CIRCLE_DEGREES + GRADES_ADDED)) {
            canvas.rotate(startGrades,
                    mViewPortInfo.getPieCenterX(),
                    mViewPortInfo.getPieCenterY());

            if (grad.getMark().getValPosition() == Mark.Position.INNER)
                valueLeft = markLeft - grad.getMark().getValMargin() - getTextWidth(markSpanSum, mPaintMarkVal);

            pivotPointMarkX = valueLeft + (getTextWidth(markSpanSum, mPaintMarkVal) / 2);
            pivotPointMarkY = valueTop;

            canvas.rotate(-1 * (startGrades),
                    pivotPointMarkX,
                    pivotPointMarkY);



            if (!valAlreadyInDial(markSpanSum)) {

                OnDrawChartParams params = new OnDrawMarkValueParams(canvas, mPaintMarkVal,
                        valueLeft, valueTop, startGrades, markSpanSum, grad.getSpan());

                    canvas.drawText(String.valueOf(markSpanSum),
                            valueLeft,
                            valueTop,
                            mPaintMarkVal);
            }

            canvas.rotate(startGrades,
                    pivotPointMarkX,
                    pivotPointMarkY);

            canvas.rotate(-1 * (startGrades),
                    mViewPortInfo.getPieCenterX(),
                    mViewPortInfo.getPieCenterY());

            markSpanSum += grad.getSpan();
            startGrades += degreesMarksSpan;
        }
    }

    private boolean valAlreadyInDial(float val) {
        for (Float span: mSpansInDial) {
            if ((val % span) == 0)
                return true;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retValScale = mScaleGestureDetector.onTouchEvent(event);
        boolean retValGesture = mGestureDetector.onTouchEvent(event);


        return retValScale || retValGesture || super.onTouchEvent(event);
    }

    private float getTotalValues() {
        float total = 0;

        for (float val: mValues) {
            total += val;
        }

        return total;
    }

    private float getTextWidth(float val, Paint paint) {
        String text = String.valueOf(val);
        return getTextWidth(text, paint);
    }

    private float getTextWidth(String text, Paint paint) {
        float[] widths = new float[text.length()];

        paint.getTextWidths(text, widths);
        float totalWidth = 0f;
        for (float w: widths) totalWidth += w;

        return totalWidth;
    }

    private void initPaints() {
        mPaintCanvas = new Paint();

        mPaintMarkVal = new Paint();
        mPaintMarkVal.setAntiAlias(true);

        mPaintMark = new Paint();
        mPaintMark.setAntiAlias(true);

        mPaintSlices = new Paint();
        mPaintSlices.setStyle(Paint.Style.FILL);
        mPaintSlices.setAntiAlias(true);

        mPaintGradedDial = new Paint();
        mPaintGradedDial.setStyle(Paint.Style.STROKE);
        mPaintGradedDial.setAntiAlias(true);
    }



    private float lastFocusX;
    private float lastFocusY;

    private final GestureDetector.SimpleOnGestureListener mSimpleGestureListener =
            new GestureDetector.SimpleOnGestureListener() {

        private float lastTouchX;
        private float lastTouchY;

        @Override
        public boolean onDown(MotionEvent e) {
            lastTouchX = e.getX();
            lastTouchY = e.getY();

            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            System.out.println("Double Tap X: " + e.getX() + " Y: " + e.getY());

            mZoomer.forceFinished(true);

            lastFocusX = e.getX();
            lastFocusY = e.getY();

            mZoomer.startZoom(mViewPortInfo.getScaleX(),
                    mViewPortInfo.getScaleX() + mScaleFactor);

            ViewCompat.postInvalidateOnAnimation(ZPieChart.this);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float touchX = e2.getX();
            float diffX = (touchX - lastTouchX);
            float touchY = e2.getY();
            float diffY = (touchY - lastTouchY);


            if (!mScaling && mViewPortInfo.isInnerLimit(diffX, diffY)) {

                if ((Math.abs(diffX) < 150 && Math.abs(diffY) < 150)) {

                    float difX = mViewPortInfo.getPosX(touchX) -
                            mViewPortInfo.getPosX(lastTouchX);
                    float difY = mViewPortInfo.getPosY(touchY) -
                            mViewPortInfo.getPosY(lastTouchY);


                    mViewPortInfo.setTranslate(mViewPortInfo.getTransX() + difX,
                            mViewPortInfo.getTransY() + difY);


                    ViewCompat.postInvalidateOnAnimation(ZPieChart.this);
                }
            }


            lastTouchX = touchX;
            lastTouchY = touchY;

            return true;
        }
    };


    @Override
    public void computeScroll() {
        super.computeScroll();
        boolean needsInvalidate = false;

        if (mZoomer.computeZoom()) {
            needsInvalidate = true;

            ZPieChart.this.Scale(lastFocusX, lastFocusY,
                    mZoomer.getCurrZoom());

        }

        if (needsInvalidate)
            ViewCompat.postInvalidateOnAnimation(ZPieChart.this);
    }


    private final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener =
            new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private float lastSpan;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            lastSpan = scaleGestureDetector.getCurrentSpan();

            System.out.println("Scale Begin");
            mScaling = true;

            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float span = scaleGestureDetector.getCurrentSpan();
            float newScale =  (span * mViewPortInfo.getScaleX()) / lastSpan;


            float focusX = scaleGestureDetector.getFocusX();
            float focusY = scaleGestureDetector.getFocusY();

            lastSpan = span;

            return ZPieChart.this.Scale(focusX, focusY, newScale);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

            System.out.println("Scale End");

            super.onScaleEnd(detector);

            mScaling = false;
        }

    };


    private boolean Scale(float focusX, float focusY, float newScale) {

        if (newScale > mMaxZoom || newScale < mMinZoom)
            return false;

        boolean decrease = newScale < mViewPortInfo.getScaleX();

        if (decrease) {
            if (mViewPortInfo.getPieCenterX() != mViewPortInfo.getCenterX()) {
                float sepX = mViewPortInfo.getPieCenterX() - mViewPortInfo.getCenterX();

                focusX = (-1 * (sepX * 2)) + mViewPortInfo.getCenterX();
            }

            if (mViewPortInfo.getPieCenterY() != mViewPortInfo.getCenterY()) {
                float sepY = mViewPortInfo.getPieCenterY() - mViewPortInfo.getCenterY();

                focusY = (-1 * (sepY * 2)) + mViewPortInfo.getCenterY();
            }
        }



        float focusPosX = mViewPortInfo.getPosX(focusX);
        float focusPosY = mViewPortInfo.getPosY(focusY);

        mViewPortInfo.setScale(newScale);

        float newFocusX = mViewPortInfo.getPosX(focusX);
        float newFocusY = mViewPortInfo.getPosY(focusY);

        float difFocusX = focusPosX - newFocusX;
        float difFocusY = focusPosY - newFocusY;


        if (difFocusX != 0 || difFocusY != 0) {
            float newTransX = mViewPortInfo.getTransX() - difFocusX;
            float newTransY = mViewPortInfo.getTransY() - difFocusY;

            mViewPortInfo.setTranslate(newTransX, newTransY);
        }

        ViewCompat.postInvalidateOnAnimation(ZPieChart.this);

        return true;
    }
}
