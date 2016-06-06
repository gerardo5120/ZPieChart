package com.github.gerardo5120.zpiechart;

/**
 * Created by cruzgerardoyanezteran on 26/5/16.
 */
public interface OnDrawChartHandler {
    public boolean onDrawBegin(OnDrawChartParams params);

    public boolean onDrawValueDial(OnDrawChartParams params);

    public boolean onDrawSlice(OnDrawChartParams params);
}
