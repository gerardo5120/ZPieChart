package com.github.gerardo5120.zpiechart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by cruzgerardoyanezteran on 26/5/16.
 */
public class OnDrawChartSimpleHandler implements OnDrawChartHandler {
    @Override
    public boolean onDrawBegin(OnDrawChartParams params) {
        return false;
    }

    @Override
    public boolean onDrawValueDial(OnDrawChartParams params) {
        return false;
    }

    @Override
    public boolean onDrawSlice(OnDrawChartParams params) {
        return false;
    }
}
