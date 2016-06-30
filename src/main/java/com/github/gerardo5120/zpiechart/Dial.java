package com.github.gerardo5120.zpiechart;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by cruzgerardoyanezteran on 5/19/16.
 */
public class Dial {
    private boolean showLine = true;
    private float distance = 25f;
    private float width = 10f;
    private int color = 0xFF03A9F4;

    public void setShowLine(boolean showLine) { this.showLine = showLine; }

    public boolean showLine() {
        return showLine;
    }

    public float getDistance() {
        return distance;
    }

    public float getWidth() {
        return width;
    }

    public int getColor() {
        return color;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
