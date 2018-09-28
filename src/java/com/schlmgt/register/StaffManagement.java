/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import com.schlmgt.dbconn.DbConnectionX;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "staff")
@ViewScoped
public class StaffManagement implements Serializable {
    
    private List<StaffModel> staff;
    private List<StaffModel> staff1;
    private List<StaffModel> staff2;
    
    @PostConstruct
    public void init() {
        try {
            staff = staffDetails();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<StaffModel> staffDetails() throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            List<StaffModel> lst = new ArrayList<>();
            con = dbConnections.mySqlDBconnection();
            
            String query = "SELECT * FROM user_details where is_deleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setBoolean(1, false);
            rs = pstmt.executeQuery();
            //

            while (rs.next()) {
                
                StaffModel coun = new StaffModel();
                coun.setId(rs.getInt("id"));
                coun.setFname(rs.getString("first_name"));
                coun.setMname(rs.getString("middlename"));
                coun.setLname(rs.getString("last_name"));
                coun.setPnum(rs.getString("username"));
                coun.setEmail(rs.getString("email_address"));
                coun.setDesignation(rs.getString("designation"));
//                coun.setStaffClass(Integer.parseInt(rs.getString("staffclass")));
//                coun.setStaffGrade(Integer.parseInt(rs.getString("staffgrade")));
                coun.setHighQua(rs.getString("HighestQua"));
                coun.setAddress(rs.getString("address"));
                coun.setDateEmployed(rs.getString("dateemployed"));
                coun.setDateStopped(rs.getString("datestopped"));
                coun.setRoleid(rs.getInt("roleid"));
                coun.setRoleAssigned(rs.getInt("roleassigned"));

                //
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
    
    public void selectRecos(StaffModel secRecords) {
        
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            NavigationHandler nav = ctx.getApplication().getNavigationHandler();
            ctx.getExternalContext().getApplicationMap().remove("staffRecords");
            ctx.getExternalContext().getApplicationMap().put("staffRecords", secRecords);
            String url = "staffProfile.xhtml?faces-redirect=true";
            nav.handleNavigation(ctx, null, url);
            ctx.renderResponse();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    public List<StaffModel> getStaff1() {
        return staff1;
    }
    
    public void setStaff1(List<StaffModel> staff1) {
        this.staff1 = staff1;
    }
    
    public List<StaffModel> getStaff2() {
        return staff2;
    }
    
    public void setStaff2(List<StaffModel> staff2) {
        this.staff2 = staff2;
    }
    
    public List<StaffModel> getStaff() {
        return staff;
    }
    
    public void setStaff(List<StaffModel> staff) {
        this.staff = staff;
    }
    
}
