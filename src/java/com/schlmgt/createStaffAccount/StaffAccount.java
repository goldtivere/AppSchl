/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.createStaffAccount;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.logic.AESencrp;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.login.UserDetails;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "createStaffAcct")
@ViewScoped
public class StaffAccount implements Serializable {

    private String id;
    private String matno;
    private String errorLabel;
    private Boolean status;
    private Boolean Rstatus;
    private String password;
    private String messangerOfTruth;
    private String someParam;
    private String fullname;

    @PostConstruct
    public void init() {
        try {
            if (StudentNumber().equalsIgnoreCase("0")) {
                setMatno(null);
                setStatus(true);
                setRstatus(false);
                setErrorLabel(" LINK HAS EXPIRED OR DOES NOT EXIST. PLEASE CONTACT ADMIN!!!");

            } else {
                setStatus(false);
                setRstatus(true);
                setMatno(StudentNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String StudentNumber() throws SQLException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        Map<String, String> params = externalContext.getRequestParameterMap();
        someParam = params.get("id");

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testguid = "Select * from staffstatus where guid=?";
        pstmt = con.prepareStatement(testguid);
        pstmt.setString(1, someParam);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            fullname = rs.getString("fullname");
            return rs.getString("staffphone");

        }
        return "0";

    }

    public Boolean testReg() {
        try {
            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con = dbConnections.mySqlDBconnection();            
            String testflname = "Select * from Staffstatus where staffphone=? and guid=?";
            pstmt = con.prepareStatement(testflname);
            pstmt.setString(1, getMatno());
            pstmt.setString(2, someParam);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (NumberFormatException e) {
            setMessangerOfTruth("Link Expired or deleted!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void register() {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage msg;       
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            if (testReg()) {
                if (getPassword().length() < 6) {
                    setMessangerOfTruth("Password must be greater than 6 characters");
                    msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, msg);
                } else {
                    con = dbConnections.mySqlDBconnection();

                    String regStudent = "update user_details set Password=?, dateupdated=?,datetimeupdated=?,"
                            + "updatedby=? where username=?";

                    pstmt = con.prepareStatement(regStudent);
                    pstmt.setString(1, AESencrp.encrypt(getPassword()));
                    pstmt.setString(2, DateManipulation.dateAlone());
                    pstmt.setString(3, DateManipulation.dateAndTime());
                    pstmt.setString(4, fullname);
                    pstmt.setString(5, getMatno());                                        

                    pstmt.executeUpdate();

                    String deleteParam = "delete from staffstatus where guid=?";
                    pstmt = con.prepareStatement(deleteParam);
                    pstmt.setString(1, someParam);
                    System.out.println("Hi Param: "+ someParam);
                    pstmt.executeUpdate();

                    NavigationHandler nav = context.getApplication().getNavigationHandler();

                    String url_ = "/pages/success/success.xhtml?faces-redirect=true";
                    nav.handleNavigation(context, null, url_);
                    context.renderResponse();

                }
            } else {
                setMessangerOfTruth("Link have expired or User doesnt exist!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRstatus() {
        return Rstatus;
    }

    public void setRstatus(Boolean Rstatus) {
        this.Rstatus = Rstatus;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getErrorLabel() {
        return errorLabel;
    }

    public void setErrorLabel(String errorLabel) {
        this.errorLabel = errorLabel;
    }

    public String getMatno() {
        return matno;
    }

    public void setMatno(String matno) {
        this.matno = matno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
