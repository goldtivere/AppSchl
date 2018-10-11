/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.updateResult;

import java.io.Serializable;

/**
 *
 * @author Gold
 */
public class ResultComputeModel implements Serializable{
    
    private String studentReg;
    private double termTotal;
    private String grade;
    private String term;
    private String year;
    private double sumTotal;

    public String getStudentReg() {
        return studentReg;
    }

    public void setStudentReg(String studentReg) {
        this.studentReg = studentReg;
    }

    public double getTermTotal() {
        return termTotal;
    }

    public void setTermTotal(double termTotal) {
        this.termTotal = termTotal;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(double sumTotal) {
        this.sumTotal = sumTotal;
    }
    
}
