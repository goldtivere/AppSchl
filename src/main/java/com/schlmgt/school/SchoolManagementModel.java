/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.school;

import java.io.Serializable;

/**
 *
 * @author Gold
 */
public class SchoolManagementModel  implements Serializable{
    private int id;
    private String schoolName;
    private String tableName;
    private String schoolHeadName;
    private String designation;
    private String emailAdd;
    private String pnum;
    private String lga;
    private int totalstudent;
    private int totalmale;
    private int totalfemale;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSchoolHeadName() {
        return schoolHeadName;
    }

    public void setSchoolHeadName(String schoolHeadName) {
        this.schoolHeadName = schoolHeadName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmailAdd() {
        return emailAdd;
    }

    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public int getTotalstudent() {
        return totalstudent;
    }

    public void setTotalstudent(int totalstudent) {
        this.totalstudent = totalstudent;
    }

    public int getTotalmale() {
        return totalmale;
    }

    public void setTotalmale(int totalmale) {
        this.totalmale = totalmale;
    }

    public int getTotalfemale() {
        return totalfemale;
    }

    public void setTotalfemale(int totalfemale) {
        this.totalfemale = totalfemale;
    }
    
    
    
}
