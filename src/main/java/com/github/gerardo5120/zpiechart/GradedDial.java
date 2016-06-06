package com.github.gerardo5120.zpiechart;

import java.util.ArrayList;

/**
 * Created by cruzgerardoyanezteran on 30/5/16.
 */
public class GradedDial extends Dial {
    private ArrayList<Grad> grads;

    GradedDial() {
        super();
        grads = new ArrayList<>();
    }

    public ArrayList<Grad> getGrads() { return grads; }
}
