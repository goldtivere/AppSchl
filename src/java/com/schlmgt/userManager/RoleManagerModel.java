/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.userManager;

import java.io.Serializable;

/**
 *
 * @author Gold
 */
public class RoleManagerModel implements Serializable{
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private boolean canUpdateResult;
    private boolean canUpdateSubject;
    private boolean canRegisterStudent;
    private boolean canRegisterStaff;
    private boolean canSendMessage;
    private boolean canManageSchool;
    private boolean canViewProfile;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isCanUpdateResult() {
        return canUpdateResult;
    }

    public void setCanUpdateResult(boolean canUpdateResult) {
        this.canUpdateResult = canUpdateResult;
    }

    public boolean isCanUpdateSubject() {
        return canUpdateSubject;
    }

    public void setCanUpdateSubject(boolean canUpdateSubject) {
        this.canUpdateSubject = canUpdateSubject;
    }

    public boolean isCanRegisterStudent() {
        return canRegisterStudent;
    }

    public void setCanRegisterStudent(boolean canRegisterStudent) {
        this.canRegisterStudent = canRegisterStudent;
    }

    public boolean isCanRegisterStaff() {
        return canRegisterStaff;
    }

    public void setCanRegisterStaff(boolean canRegisterStaff) {
        this.canRegisterStaff = canRegisterStaff;
    }

    public boolean isCanSendMessage() {
        return canSendMessage;
    }

    public void setCanSendMessage(boolean canSendMessage) {
        this.canSendMessage = canSendMessage;
    }

    public boolean isCanManageSchool() {
        return canManageSchool;
    }

    public void setCanManageSchool(boolean canManageSchool) {
        this.canManageSchool = canManageSchool;
    }

    public boolean isCanViewProfile() {
        return canViewProfile;
    }

    public void setCanViewProfile(boolean canViewProfile) {
        this.canViewProfile = canViewProfile;
    }
    
    
    
}
