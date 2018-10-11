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
public class PostionTermModel implements Serializable{
    private int id;
    private double firstTerm;
    private double secondTerm;
    private double thirdTerm;
    private double totalscore;
    private double average;
    private int position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getFirstTerm() {
        return firstTerm;
    }

    public void setFirstTerm(double firstTerm) {
        this.firstTerm = firstTerm;
    }

    public double getSecondTerm() {
        return secondTerm;
    }

    public void setSecondTerm(double secondTerm) {
        this.secondTerm = secondTerm;
    }

    public double getThirdTerm() {
        return thirdTerm;
    }

    public void setThirdTerm(double thirdTerm) {
        this.thirdTerm = thirdTerm;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(double totalscore) {
        this.totalscore = totalscore;
    }
    
    
}
