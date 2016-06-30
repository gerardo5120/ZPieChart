package com.github.gerardo5120.zpiechart;

import java.util.ArrayList;

/**
 * Created by cruzgerardoyanezteran on 30/5/16.
 */
public class GradedDial extends Dial {
    private ArrayList<Grad> mGrads;
    private ArrayList<SingleMark> mSingleMaks;

    public GradedDial() {
        super();
        mGrads = new ArrayList<>();
        mSingleMaks = new ArrayList<>();
    }

    public ArrayList<SingleMark> getSingleMarks() {
        return mSingleMaks;
    }

    public ArrayList<Grad> getGrads() {
        return mGrads;
    }
}
