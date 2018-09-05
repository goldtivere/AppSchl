/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.report;

import java.io.Serializable;

/**
 *
 * @author Gold
 */
public class PositionModel implements Serializable {

    private int position;
    private double average;
    private double tSum;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public double gettSum() {
        return tSum;
    }

    public void settSum(double tSum) {
        this.tSum = tSum;
    }

}
