/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.userManager;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.login.UserDetails;
import com.schlmgt.updateResult.ResultModel;
import com.schlmgt.updateSubject.SessionTable;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.PropertyNotFoundException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "roleManager")
@ViewScoped
public class RoleManager implements Serializable {

    private List<RoleManagerModel> roleManager;
    private List<RoleManagerModel> roleManager1;
    private RoleManagerModel roleManager2;
    private String messangerOfTruth;

    @PostConstruct
    public void init() {
        try {
            roleManager = displayRole();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<RoleManagerModel> displayRole() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM user_details where is_deleted=? and suspendedstatus=? and roleassigned=2";
            pstmt = con.prepareStatement(query);
            pstmt.setBoolean(1, false);
            pstmt.setBoolean(2, false);
            rs = pstmt.executeQuery();
            //
            List<RoleManagerModel> lst = new ArrayList<>();
            while (rs.next()) {

                RoleManagerModel coun = new RoleManagerModel();
                coun.setId(rs.getInt("id"));
                coun.setFirstName(rs.getString("first_name"));
                coun.setMiddleName(rs.getString("middlename"));
                coun.setLastName(rs.getString("last_name"));
                coun.setCanUpdateResult(rs.getBoolean("canupdateresult"));
                coun.setCanUpdateSubject(rs.getBoolean("canupdatesubject"));
                coun.setCanRegisterStudent(rs.getBoolean("canregisterstudent"));
                coun.setCanRegisterStaff(rs.getBoolean("canregisterstaff"));
                coun.setCanSendMessage(rs.getBoolean("cansendText"));
                coun.setCanManageSchool(rs.getBoolean("canmanageschool"));
                coun.setCanViewProfile(rs.getBoolean("canviewprofile"));

                lst.add(coun);
            }

            return lst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {

            if (!(con == null)) {
                con.close();
                con = null;
            }
            if (!(pstmt == null)) {
                pstmt.close();
                pstmt = null;
            }

        }
    }

    public void updateRole() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            con = dbConnections.mySqlDBconnection();
            if (roleManager2 == null) {
                setMessangerOfTruth("Item(s) not selected!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            } else {

                String updateSubject = "update user_details set canupdateresult=?,canupdatesubject=?,canregisterstaff=?, "
                        + " canregisterstudent=? , cansendtext=?,canManageSchool=?,canviewprofile=?,dateupdated=?,datetimeupdated=?,updatedby=? where id=?";

                pstmt = con.prepareStatement(updateSubject);
                RoleManagerModel ta = roleManager2;
                pstmt.setBoolean(1, ta.isCanUpdateResult());
                pstmt.setBoolean(2, ta.isCanUpdateSubject());
                pstmt.setBoolean(3, ta.isCanRegisterStaff());
                pstmt.setBoolean(4, ta.isCanRegisterStudent());
                pstmt.setBoolean(5, ta.isCanSendMessage());
                pstmt.setBoolean(6, ta.isCanManageSchool());
                pstmt.setBoolean(7, ta.isCanViewProfile());
                pstmt.setString(8, DateManipulation.dateAlone());
                pstmt.setString(9, DateManipulation.dateAndTime());
                pstmt.setString(10, createdby);
                pstmt.setInt(11, ta.getId());
                pstmt.executeUpdate();
                roleManager = displayRole();
                setMessangerOfTruth("Role Updated for Admin!!");
            }

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
        } catch (PropertyNotFoundException e) {

            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), e.getMessage());
            context.addMessage(null, msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<RoleManagerModel> getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(List<RoleManagerModel> roleManager) {
        this.roleManager = roleManager;
    }

    public List<RoleManagerModel> getRoleManager1() {
        return roleManager1;
    }

    public void setRoleManager1(List<RoleManagerModel> roleManager1) {
        this.roleManager1 = roleManager1;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public RoleManagerModel getRoleManager2() {
        return roleManager2;
    }

    public void setRoleManager2(RoleManagerModel roleManager2) {
        this.roleManager2 = roleManager2;
    }

}
