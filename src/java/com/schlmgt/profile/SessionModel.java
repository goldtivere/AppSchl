/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import java.io.Serializable;

/**
 *
 * @author Gold
 */
public class SessionModel implements Serializable{
    private double termTotal;
    private String studentSession;
    private String studentTerm;
    private int position;
    private double studentAverage;
    private String studentClass;
    private String studentReg;
    private String fullname;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    

    public double getTermTotal() {
        return termTotal;
    }

    public void setTermTotal(double termTotal) {
        this.termTotal = termTotal;
    }

    public String getStudentSession() {
        return studentSession;
    }

    public void setStudentSession(String studentSession) {
        this.studentSession = studentSession;
    }

    public String getStudentTerm() {
        return studentTerm;
    }

    public void setStudentTerm(String studentTerm) {
        this.studentTerm = studentTerm;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getStudentAverage() {
        return studentAverage;
    }

    public void setStudentAverage(double studentAverage) {
        this.studentAverage = studentAverage;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getStudentReg() {
        return studentReg;
    }

    public void setStudentReg(String studentReg) {
        this.studentReg = studentReg;
    }
    
}
