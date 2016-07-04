package com.github.gerardo5120.zpiechart;

/**
 * Created by cruzgerardoyanezteran on 26/5/16.
 */
public interface OnDrawChartHandler {
    boolean onDrawBegin(OnDrawChartParams params);

    boolean onDrawValueDial(OnDrawChartParams params);

    boolean onDrawSlice(OnDrawChartParams params);

    boolean onDrawMarkValue(OnDrawChartParams params);
}
