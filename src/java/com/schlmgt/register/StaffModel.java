/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Gold
 */
public class StaffModel implements Serializable {

    private int id;
    private String staffId;
    private String fname;
    private String mname;
    private String lname;
    private String email;
    private String pnum;
    private String highQua;
    private String address;
    private int staffClass;
    private int staffGrade;
    private Date doe;
    private String dateEmployed;
    private String dateStopped;
    private Date das;
    private String year;
    private int roleAssigned;
    private int roleid;
    private String designation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getDateEmployed() {
        return dateEmployed;
    }

    public void setDateEmployed(String dateEmployed) {
        this.dateEmployed = dateEmployed;
    }

    public String getDateStopped() {
        return dateStopped;
    }

    public void setDateStopped(String dateStopped) {
        this.dateStopped = dateStopped;
    }

    public Date getDas() {
        return das;
    }

    public void setDas(Date das) {
        this.das = das;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getHighQua() {
        return highQua;
    }

    public void setHighQua(String highQua) {
        this.highQua = highQua;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStaffClass() {
        return staffClass;
    }

    public void setStaffClass(int staffClass) {
        this.staffClass = staffClass;
    }

    public int getStaffGrade() {
        return staffGrade;
    }

    public void setStaffGrade(int staffGrade) {
        this.staffGrade = staffGrade;
    }

    public Date getDoe() {
        return doe;
    }

    public void setDoe(Date doe) {
        this.doe = doe;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getRoleAssigned() {
        return roleAssigned;
    }

    public void setRoleAssigned(int roleAssigned) {
        this.roleAssigned = roleAssigned;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

}
