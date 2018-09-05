/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.login.UserDetails;
import com.schlmgt.profile.SecondaryModel;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Gold
 */
@ManagedBean
@ViewScoped
public class ClassAssigned implements Serializable {

    private String Staffgrade;
    private String staffClass;
    private String year;
    private List<StaffModel> listmodel;
    private StaffModel modelStaff = new StaffModel();

    @PostConstruct
    public void init() {
        try {
            staffDetails();
            listmodel = detailStaff();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void staffDetails() {
        try {
            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con = dbConnections.mySqlDBconnection();
            FacesContext context = FacesContext.getCurrentInstance();
            RequestContext cont = RequestContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            String studId;
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            FacesContext ctx = FacesContext.getCurrentInstance();
            StaffModel staff = (StaffModel) ctx.getExternalContext().getApplicationMap().get("staffRecord");
            //test for null...
            modelStaff = staff;
            int pnum = 0;
            if (modelStaff != null || modelStaff.equals(null)) {
                pnum = modelStaff.getId();
            }

            String testguid = "Select * from tbstaffclass where staffid=? and status=?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setInt(1, pnum);
            pstmt.setBoolean(2, true);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                setStaffClass(rs.getString("staffclass"));
                setStaffgrade(rs.getString("staffgrade"));
                setYear(rs.getString("year"));
            }

        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<StaffModel> detailStaff() throws SQLException {

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            RequestContext cont = RequestContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            String studId;
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            FacesContext ctx = FacesContext.getCurrentInstance();
            StaffModel staff = (StaffModel) ctx.getExternalContext().getApplicationMap().get("staffRecord");
            //test for null...

            modelStaff = staff;
            int pnum = 0;
            if (modelStaff != null) {
                pnum = modelStaff.getId();
            }

            String testguid = "Select * from tbstaffclass where staffid=? and status=?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setInt(1, pnum);
            pstmt.setBoolean(2, false);
            rs = pstmt.executeQuery();
            //
            List<StaffModel> lst = new ArrayList<>();
            while (rs.next()) {

                StaffModel coun = new StaffModel();
                coun.setId(rs.getInt("id"));
                coun.setStaffClass(Integer.parseInt(rs.getString("staffclass")));
                coun.setStaffGrade(Integer.parseInt(rs.getString("staffgrade")));
                coun.setYear(rs.getString("year"));

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

    public String getStaffgrade() {
        return Staffgrade;
    }

    public void setStaffgrade(String Staffgrade) {
        this.Staffgrade = Staffgrade;
    }

    public String getStaffClass() {
        return staffClass;
    }

    public void setStaffClass(String staffClass) {
        this.staffClass = staffClass;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<StaffModel> getListmodel() {
        return listmodel;
    }

    public void setListmodel(List<StaffModel> listmodel) {
        this.listmodel = listmodel;
    }

    public StaffModel getModelStaff() {
        return modelStaff;
    }

    public void setModelStaff(StaffModel modelStaff) {
        this.modelStaff = modelStaff;
    }

}
