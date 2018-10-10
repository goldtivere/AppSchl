/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.StudentLogin;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.logic.AESencrp;
import com.schlmgt.school.SchoolGetterMethod;
import com.schlmgt.school.SchoolManagementModel;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "Studentlogin")
@SessionScoped
public class Login implements Serializable {

    private String school;
    private String regNum;
    private String stuPin;
    private String stuSerialNum;
    private List<SchoolManagementModel> schlMgt;
    private String messangerOfTruth;
    private SchoolGetterMethod schlgetter = new SchoolGetterMethod();

    @PostConstruct
    public void init() {
        try {
            schlMgt = displaySchool();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SchoolManagementModel> displaySchool() throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT student.*, stu.dbname,stu.totalstudent,stu.totalmale,totalfemale FROM tbschlmgt student inner "
                    + "join tbschltablestructure stu on stu.schoolname=student.schlname where student.isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setBoolean(1, false);
            rs = pstmt.executeQuery();
            //
            List<SchoolManagementModel> lst = new ArrayList<>();
            while (rs.next()) {

                SchoolManagementModel coun = new SchoolManagementModel();
                coun.setId(rs.getInt("id"));
                coun.setSchoolName(rs.getString("schlname"));
                coun.setSchoolHeadName(rs.getString("schoolheadname"));
                coun.setPnum(rs.getString("phonenumber"));
                coun.setTableName(rs.getString("tablename"));
                coun.setEmailAdd(rs.getString("emailaddress"));
                coun.setDesignation(rs.getString("designation"));
                coun.setTotalstudent(rs.getInt("totalstudent"));
                coun.setTotalmale(rs.getInt("totalmale"));
                coun.setTotalfemale(rs.getInt("totalfemale"));

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

    public boolean confirmRegNum(String regNum) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnections.mySqlDBconnection();

            String queryProfile = "select * from " + getSchool() + "_tbstudentclass "
                    + " where studentid=? and isdeleted = false";

            pstmt = con.prepareStatement(queryProfile);
            pstmt.setString(1, regNum);

            //System.out.println(getUsername() + "," + doEncPwd  + "<>" + getPassword() );
            //pstmt.setString(2, "ok");
            rs = pstmt.executeQuery();

            if (rs.next()) {

                return true;
            }

        } catch (SQLException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage().toString(), "System unavailable111, please try again later."));
        } catch (Exception e) {

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage().toString(), "System unavailable, please try again later."));
            e.printStackTrace();
            return false;

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
        return false;
    }

    public boolean confirmPinSerial(String serialnumber, String studentpin) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            con = dbConnections.mySqlDBconnection();

            String queryProfile = "select * from tbserialpin "
                    + "where serialnumber=? and studentpin=? and (numberlogged <= 5 or numberlogged is null)";

            pstmt = con.prepareStatement(queryProfile);
            pstmt.setString(1, serialnumber);
            pstmt.setString(2, studentpin);

            //System.out.println(getUsername() + "," + doEncPwd  + "<>" + getPassword() );
            //pstmt.setString(2, "ok");
            rs = pstmt.executeQuery();

            if (rs.next()) {

                return true;
            }

        } catch (SQLException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage().toString(), "System unavailable111, please try again later."));
        } catch (Exception e) {

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage().toString(), "System unavailable, please try again later."));
            e.printStackTrace();
            return false;

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
        return false;
    }

    public void loginpage() throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println(confirmPinSerial(getStuSerialNum(), getStuPin()) + " and " + confirmRegNum(getRegNum()));
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please make sure you enter a valid Pin and serial Number", "Please make sure you enter a valid Pin and serial Number"));
    }

    public void noactivity(ActionEvent evt) {
        getLogout();
    }

    public boolean getLogout() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {

            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            session.invalidate();

            // pageMover.setValue01("page1.xhtml");
            //context.getExternalContext().getSessionMap().clear();
            NavigationHandler nav = context.getApplication().getNavigationHandler();

            nav.handleNavigation(context, null, "/student.xhtml?faces-redirect=true");
            //nav.handleNavigation(context, null, "/../../login.xhtml?faces-redirect=true");

            context.renderResponse();

            if (context.getExternalContext().getSessionMap().isEmpty()) {

                //System.out.println("Why:" + dto.getUsername());
                return true;

            } else {

                context.addMessage(null, new FacesMessage("App. cannot close at this time,try later."));
                //System.out.println("Why:" + dto.getUsername());
                return false;
            }

        } catch (Exception e) {

            context.addMessage(null, new FacesMessage("System Unavailable."));
            e.printStackTrace();
            return false;

        }

    }//end getLogout

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public String getStuPin() {
        return stuPin;
    }

    public void setStuPin(String stuPin) {
        this.stuPin = stuPin;
    }

    public String getStuSerialNum() {
        return stuSerialNum;
    }

    public void setStuSerialNum(String stuSerialNum) {
        this.stuSerialNum = stuSerialNum;
    }

    public List<SchoolManagementModel> getSchlMgt() {
        return schlMgt;
    }

    public void setSchlMgt(List<SchoolManagementModel> schlMgt) {
        this.schlMgt = schlMgt;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

}
