/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.login;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Gold
 */
public class UserDetails implements Serializable {

    private int id;
    private String first_name;
    private String last_name;
    private String user_name;
    private String password;
    private String imageId;
    private String image;
    private String email;
    private Date date_created;
    private Boolean isdeleted;
    private String role_id;
    private String creatdBy;
    private int roleAssigned;
    private int roleid;
    private String staffClass;
    private String staffGrade;
    private boolean canUpdateResult;
    private boolean canUpdateSubject;
    private boolean canRegisterStudent;
    private boolean canRegisterStaff;
    private boolean canSendMessage;
    private boolean canManageSchool;
    private int schoolName;
    private boolean canViewProfile;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatdBy() {
        return creatdBy;
    }

    public void setCreatdBy(String creatdBy) {
        this.creatdBy = creatdBy;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
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

    public String getStaffClass() {
        return staffClass;
    }

    public void setStaffClass(String staffClass) {
        this.staffClass = staffClass;
    }

    public String getStaffGrade() {
        return staffGrade;
    }

    public void setStaffGrade(String staffGrade) {
        this.staffGrade = staffGrade;
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

    public int getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(int schoolName) {
        this.schoolName = schoolName;
    }

    public boolean isCanViewProfile() {
        return canViewProfile;
    }

    public void setCanViewProfile(boolean canViewProfile) {
        this.canViewProfile = canViewProfile;
    }

}
