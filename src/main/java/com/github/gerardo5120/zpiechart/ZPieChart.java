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
    // com.github.gerardo5120.zpiechart

    private static float CIRCLE_DEGREES = 360f;

    /**
     * Default color pallete for slices
     */
    private int[] colorValues = { 0xFFBCAAA4, 0xFF8D6E63, 0xFFAFB42B,
            0xFFDCE775, 0xFF009688, 0xFF00897B,
            0xFF00796B };

    private float gradesToMove = -90f;

    private ScaleGestureDetector mScaleGestureDetector;

    private Paint paintCanvas;
    private Paint paintMark;
    private Paint paintMarkVal;
    private Paint paintSlices;

    private ViewPortInfo viewPortInfo;
    private HashSet<GradedDial> gradedDials;
    private HashSet<ValueDial> valueDials;

    private Paint paintGradedDial;
    private HashSet<Float> spansInDial;

    private OnDrawChartHandler mOnDrawChartHandler;

    private float gradesValueDial = 0f;



    private float txtL = 0f;
    private float txtT = 0f;

    private float txtR = 0f;
    private float txtB = 0f;


    /**
     * The list of values to represent.
     */
    private float[] values;

    /**
     * Initialize new Pie chart with specified values
     *
     * @param values
     */
    public ZPieChart(Context context, float[] values) {
        super(context);

        this.values = values;

        valueDials = new HashSet<>();
        gradedDials = new HashSet<>();

        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        viewPortInfo =  new ViewPortInfo();
        mOnDrawChartHandler = new OnDrawChartSimpleHandler();

        initPaints();
    }

    public void setOnDrawChartHandler(OnDrawChartHandler mOnDrawChartHandler) {
        this.mOnDrawChartHandler = mOnDrawChartHandler;
    }

    public HashSet<ValueDial> getValueDials() {
        return valueDials;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getGlobalVisibleRect(viewPortInfo.getRect());

        mOnDrawChartHandler.onDrawBegin(new OnDrawChartParams(canvas, paintCanvas));

        drawSlices(canvas);

        gradesValueDial = gradesToMove;

        for (ValueDial valueDial: valueDials) {
            drawValueDial(valueDial, canvas);
        }

        for (GradedDial gradedDial: gradedDials) {
            drawGradedDial(gradedDial, canvas);
        }
    }

    private void drawValueDial(ValueDial valueDial, Canvas canvas) {
        Paint paintValDial = new Paint();
        RectF rectDial = new RectF();


        float left = viewPortInfo.getLeft() - valueDial.getDistance();
        float top = viewPortInfo.getTop() - valueDial.getDistance();
        float right = viewPortInfo.getRight() + valueDial.getDistance();
        float bottom = viewPortInfo.getBottom() + valueDial.getDistance();

        rectDial.set(left, top, right, bottom);


        paintValDial.setColor(valueDial.getColor());
        paintValDial.setStrokeWidth(valueDial.getWidth());
        paintValDial.setAntiAlias(true);
        paintValDial.setStrokeCap(Paint.Cap.SQUARE);
        paintValDial.setStyle(Paint.Style.STROKE);

        float totalValues = getTotalValues();

        float valGrades = (valueDial.getValue() * CIRCLE_DEGREES) / totalValues;
        float startGrades = gradesToMove;

        if (valueDial.getStartAt() != 0)
            startGrades = ((valueDial.getStartAt() * CIRCLE_DEGREES) / totalValues) +
                    gradesToMove;

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
        float topBE = viewPortInfo.getCenterY() - (valueDial.getWidth() / 2);
        float rightBE = right + (valueDial.getBorderHeight() / 2);
        float bottomBE = viewPortInfo.getCenterY() + (valueDial.getWidth() / 2);

        Paint paintBE = new Paint();
        paintBE.setColor(valueDial.getColor());
        paintBE.setStyle(Paint.Style.FILL);
        paintBE.setAntiAlias(true);

        Rect rectBeginEnd = new Rect((int) leftBE, (int) topBE,
                (int) rightBE, (int) bottomBE);


        canvas.rotate(startGrades, viewPortInfo.getCenterX(), viewPortInfo.getCenterY());
        canvas.drawRect(rectBeginEnd, paintBE);

        canvas.rotate(valGrades, viewPortInfo.getCenterX(), viewPortInfo.getCenterY());
        canvas.drawRect(rectBeginEnd, paintBE);

        canvas.rotate(-valGrades, viewPortInfo.getCenterX(), viewPortInfo.getCenterY());
        canvas.rotate(-startGrades, viewPortInfo.getCenterX(), viewPortInfo.getCenterY());
    }

    private void drawSlices(Canvas canvas) {
        float total = getTotalValues();
        float startGrades = gradesToMove;

        float hypotenuse = (float) Math.sqrt(Math.pow(viewPortInfo.getRadius(), 2) +
                Math.pow(viewPortInfo.getRadius(), 2));

        System.out.println("Hypotenuse: " + hypotenuse);

        for (int i = 0; i < values.length; i++) {
            float val = values[i];
            float grades = (val * CIRCLE_DEGREES) / total;

            paintSlices.setColor(colorValues[i]);

            canvas.drawArc(viewPortInfo.getLeft(),
                    viewPortInfo.getTop(),
                    viewPortInfo.getRight(),
                    viewPortInfo.getBottom(),
                    startGrades,
                    grades,
                    true,
                    paintSlices);






            Paint paintText = new Paint();
            paintText.setColor(0xFF880E4F);
            paintText.setTextSize(24);
            paintText.setAntiAlias(true);

            float height = (grades * hypotenuse) / 90;

            String text = String.valueOf(val);

            OnDrawSliceParams params = new OnDrawSliceParams(canvas, paintText, text, val);


            System.out.println("Grades: " + startGrades);

            mOnDrawChartHandler.onDrawSlice(params);
            //if (!mOnDrawChartHandler.onDrawSlice(params)) {
            if (true) {
                float textLength = getTextWidth(params.getText(), paintText);


                float leftText = viewPortInfo.getCenterX() +
                        ((viewPortInfo.getRadius() / 2) - (textLength / 2));
                float topText = viewPortInfo.getCenterY() + 12;

                float pivotPointX = viewPortInfo.getCenterX() + (viewPortInfo.getRadius() / 2);
                float pivotPointY = viewPortInfo.getCenterY();

                /*if (textLength > height) {
                    leftText = viewPortInfo.getCenterX() + viewPortInfo.getRadius();
                    pivotPointX = viewPortInfo.getCenterX() + viewPortInfo.getRadius() +
                            (viewPortInfo.getRadius() / 2);
                }*/






                float outTextX = viewPortInfo.getCenterX() +
                        ((float) (Math.cos(Math.toRadians(startGrades + (grades / 2))) *
                                (viewPortInfo.getRadius() + 90)));

                float outTextY = viewPortInfo.getCenterY() +
                        (float) (Math.sin(Math.toRadians(startGrades + (grades / 2))) *
                                (viewPortInfo.getRadius() + 90));


                float initLineX = viewPortInfo.getCenterX() +
                        ((float) (Math.cos(Math.toRadians(startGrades + (grades / 2))) *
                                viewPortInfo.getRadius()));

                float initLineY = viewPortInfo.getCenterY() +
                        (float) (Math.sin(Math.toRadians(startGrades + (grades / 2))) *
                                viewPortInfo.getRadius());






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
                        " Center X: " + viewPortInfo.getCenterX() + " Y: " + viewPortInfo.getCenterY() +
                        " Cos: " + Math.cos(Math.toRadians(startGrades + (grades / 2))) +
                        " Radius: " + viewPortInfo.getRadius());
                */











                canvas.rotate(startGrades + (grades / 2),
                        viewPortInfo.getCenterX(), viewPortInfo.getCenterY());

                canvas.rotate(-(startGrades + (grades / 2)),
                        pivotPointX, pivotPointY);

                //canvas.drawText(params.getText(), leftText, topText, paintText);

                canvas.rotate(startGrades + (grades / 2),
                        pivotPointX, pivotPointY);

                canvas.rotate(-(startGrades + (grades / 2)),
                        viewPortInfo.getCenterX(), viewPortInfo.getCenterY());
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
                && txtR > viewPortInfo.getRect().right) {
            txtR = viewPortInfo.getRect().right;
            txtL = txtR - txtWidth;
        }
        else if (grades >= 90 && grades < 270
                && txtL < 0) {
            txtL += -(txtL);
            txtR = txtL + txtWidth;

            float distX = viewPortInfo.getCenterX() - txtR;
            float distY = (float) (Math.sqrt(Math.pow(viewPortInfo.getRadius(), 2) - distX));
            float cordY = 0;

            if (grades < 180) {
                cordY = viewPortInfo.getCenterY() + distY;
            }
            else if (grades >= 180) {
                cordY = viewPortInfo.getCenterY() - distY;
            }

            txtT = cordY;
            txtB = cordY + txtHeight;
        }
    }

    private void drawGradedDial(GradedDial dial, Canvas canvas) {
        paintGradedDial.setStrokeWidth(dial.getWidth());
        paintGradedDial.setColor(dial.getColor());

        if (dial.showLine()) {
            canvas.drawArc(viewPortInfo.getLeft() - dial.getDistance(),
                    viewPortInfo.getTop() - dial.getDistance(),
                    viewPortInfo.getRight() + dial.getDistance(),
                    viewPortInfo.getBottom() + dial.getDistance(),
                    0,
                    CIRCLE_DEGREES,
                    true,
                    paintGradedDial);
        }


        spansInDial.clear();

        for (Grad grad: dial.getGrads()) {
            if ((grad.getMaxScale() > viewPortInfo.getScaleX())
                    && (viewPortInfo.getScaleX() > grad.getMinScale())) {
                drawGrad(grad, dial, canvas);
                spansInDial.add(grad.getSpan());
            }
        }
    }

    private void drawGrad(Grad grad, Dial dial, Canvas canvas) {

        float markSpanSum = 0.0f;
        float startGrades = gradesToMove;
        float markLeft = viewPortInfo.getCenterX() +
                viewPortInfo.getRadius() + dial.getDistance();

        if (grad.getMarkPosition() == Grad.Position.INNER)
            markLeft -= grad.getMarkWidth();

        float markTop = viewPortInfo.getCenterY() - (grad.getMarkHeight() / 2);
        float markBottom = markTop + grad.getMarkHeight();
        float markRight = markLeft + grad.getMarkWidth();

        float degreesMarksSpan = (grad.getSpan() * CIRCLE_DEGREES) / getTotalValues();
        paintMark.setColor(grad.getColor());

        while (startGrades < (CIRCLE_DEGREES + gradesToMove)) {

            canvas.rotate(startGrades,
                    viewPortInfo.getCenterX(),
                    viewPortInfo.getCenterY());

            Rect rectMark = new Rect((int) markLeft,
                    (int) markTop,
                    (int) markRight,
                    (int) markBottom);

            OnDrawChartParams params = new OnDrawMarkParams(canvas, paintMark, startGrades
                    , rectMark, markSpanSum);

            canvas.drawRect(rectMark,
                    paintMark);

            canvas.rotate(-1 * (startGrades),
                    viewPortInfo.getCenterX(),
                    viewPortInfo.getCenterY());

            markSpanSum += grad.getSpan();
            startGrades += degreesMarksSpan;
        }





        if (!grad.showValues())
            return;


        startGrades = gradesToMove;
        markSpanSum = 0.0f;
        float valueLeft = markLeft + grad.getMarkWidth() + grad.getValMargin();
        float valueTop = markTop + (grad.getValSize() / 2);
        float pivotPointMarkX;
        float pivotPointMarkY;

        paintMarkVal.setColor(grad.getValColor());
        paintMarkVal.setTextSize(grad.getValSize());

        while (startGrades < (CIRCLE_DEGREES + gradesToMove)) {
            canvas.rotate(startGrades,
                    viewPortInfo.getCenterX(),
                    viewPortInfo.getCenterY());

            if (grad.getValPosition() == Grad.Position.INNER)
                valueLeft = markLeft - grad.getValMargin() - getTextWidth(markSpanSum, paintMarkVal);

            pivotPointMarkX = valueLeft + (getTextWidth(markSpanSum, paintMarkVal) / 2);
            pivotPointMarkY = valueTop;

            canvas.rotate(-1 * (startGrades),
                    pivotPointMarkX,
                    pivotPointMarkY);



            if (!valAlreadyInDial(markSpanSum)) {

                OnDrawChartParams params = new OnDrawMarkValueParams(canvas, paintMarkVal,
                        valueLeft, valueTop, startGrades, markSpanSum, grad.getSpan());

                    canvas.drawText(String.valueOf(markSpanSum),
                            valueLeft,
                            valueTop,
                            paintMarkVal);
            }

            canvas.rotate(startGrades,
                    pivotPointMarkX,
                    pivotPointMarkY);

            canvas.rotate(-1 * (startGrades),
                    viewPortInfo.getCenterX(),
                    viewPortInfo.getCenterY());

            markSpanSum += grad.getSpan();
            startGrades += degreesMarksSpan;
        }
    }

    private boolean valAlreadyInDial(float val) {
        for (Float span: spansInDial) {
            if ((val % span) == 0)
                return true;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retVal = mScaleGestureDetector.onTouchEvent(event);
        return retVal || super.onTouchEvent(event);
    }

    private float getTotalValues() {
        float total = 0;

        for (float val: values) {
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
        paintCanvas = new Paint();

        paintMarkVal = new Paint();
        paintMarkVal.setAntiAlias(true);

        paintMark = new Paint();
        paintMark.setAntiAlias(true);

        paintSlices = new Paint();
        paintSlices.setStyle(Paint.Style.FILL);
        paintSlices.setAntiAlias(true);

        paintGradedDial = new Paint();
        paintGradedDial.setStyle(Paint.Style.STROKE);
        paintGradedDial.setAntiAlias(true);
    }

    private final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener
            = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private float lastSpan;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            lastSpan = scaleGestureDetector.getCurrentSpan();

            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float span = scaleGestureDetector.getCurrentSpan();
            float newScale =  (span * viewPortInfo.getScaleX()) / lastSpan;

            float focusPosX = viewPortInfo.getPosX(scaleGestureDetector.getFocusX());
            float focusPosY = viewPortInfo.getPosY(scaleGestureDetector.getFocusY());

            viewPortInfo.setScale(newScale);

            float difFocusX = focusPosX -
                    viewPortInfo.getPosX(scaleGestureDetector.getFocusX());
            float difFocusY = focusPosY -
                    viewPortInfo.getPosY(scaleGestureDetector.getFocusY());


            if (difFocusX != 0 || difFocusY != 0) {
                float newTransX = viewPortInfo.getTransX() - difFocusX;
                float newTransY = viewPortInfo.getTransY() - difFocusY;

                viewPortInfo.setTranslate(newTransX, newTransY);
            }

            lastSpan = span;
            ViewCompat.postInvalidateOnAnimation(ZPieChart.this);

            return true;
        }
    };
}
