/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.emailsave;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.logic.AESencrp;
import com.schlmgt.logic.DateManipulation;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "mail")
@SessionScoped
public class saveEmail implements Serializable {

    private String email;
    private String password;
    private String messangerOfTruth;
    private String status;
    private String rePassword;

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        try {
            con = dbConnections.mySqlDBconnection();
            String testemail = "Select * from senderdetails where activate=?";
            pstmt = con.prepareStatement(testemail);
            pstmt.setBoolean(1, true);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                setStatus("Email Set!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            } else {
                setStatus("Please Set email to send mail");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void refresh() {
        setEmail("");
        setPassword("");
        setRePassword("");

    }

    public void saveEmail() throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;

        /**
         * UserDetails userObj = (UserDetails)
         * context.getExternalContext().getSessionMap().get("sessn_nums");
         * String on = String.valueOf(userObj);
         *
         * if (userObj == null) { setMessangerOfTruth("Expired Session, please
         * re-login" + on); msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
         * getMessangerOfTruth(), getMessangerOfTruth());
         * context.addMessage(null, msg); }
         *
         * String createdby = String.valueOf(userObj.getFirst_name() + " " +
         * userObj.getLast_name()); String createdId =
         * String.valueOf(userObj.getId()); String roleId =
         * String.valueOf(userObj.getRole_id());
         *
         */
        try {
            con = dbConnections.mySqlDBconnection();
            String testemail = "Select * from senderdetails where username=? and isdeleted=?";
            pstmt = con.prepareStatement(testemail);
            pstmt.setString(1, getEmail());
            pstmt.setBoolean(2, false);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                setMessangerOfTruth("Email Address already exists!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);

            } else {
                String insert = "insert into senderdetails (username,password,date_created,date_time_created,createdby,createdid,isdeleted,activate) "
                        + "values(?,?,?,?,?,?,?,?)";
                pstmt = con.prepareStatement(insert);

                pstmt.setString(1, getEmail());
                pstmt.setString(2, AESencrp.encrypt(getPassword()));
                pstmt.setString(3, DateManipulation.dateAlone());
                pstmt.setString(4, DateManipulation.dateAndTime());
                pstmt.setString(5, "Osawota Gold");
                pstmt.setString(6, "1");
                pstmt.setBoolean(7, false);
                pstmt.setBoolean(8, true);

                pstmt.executeUpdate();

                setMessangerOfTruth("Email Saved!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
                refresh();
                setStatus("Email Set");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
