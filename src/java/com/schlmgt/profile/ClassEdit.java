/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.login.UserDetails;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "editClass")
@ViewScoped
public class ClassEdit implements Serializable {

    private EditStudent edits = new EditStudent();
    private List<SecondaryModel> subModel;
    private String grade;
    private String sclass;
    private String term;
    private String year;
    private String arm;
    private String messangerOfTruth;
    private String school;

    @PostConstruct
    public void init() {
        try {

            FacesContext ctx = FacesContext.getCurrentInstance();
            FacesMessage msg;

            String stuValue = null;
            stuValue = (String) ctx.getExternalContext().getApplicationMap().get("reDet");

            if (stuValue != null) {
                stuValue = stuValue.replaceAll("\\s", "_");
                setSchool(stuValue);
            } else {
                setMessangerOfTruth("Session Expired for this Student. Please select student and try again!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                ctx.addMessage(null, msg);
            }
            subModel = clasEdit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<SecondaryModel> clasEdit() throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        edits.studDetails();
        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM "+getSchool()+"_tbstudentclass where studentid=? and currentclass=? order by id desc";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, edits.getStudentid());
            System.out.println(edits.getStudentid());
            pstmt.setBoolean(2, false);
            rs = pstmt.executeQuery();
            //
            List<SecondaryModel> lst = new ArrayList<>();
            while (rs.next()) {

                SecondaryModel coun = new SecondaryModel();
                coun.setId(rs.getInt("id"));
                coun.setCurrentClass(rs.getString("class"));
                coun.setTerm(rs.getString("term"));
                coun.setYear(rs.getString("year"));
                coun.setNoOfTimesRepeated(rs.getString("NoOfTimesRepeated"));

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

    public void currentClass() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        FacesMessage msg;
        ResultSet rs = null;
        FacesContext context = FacesContext.getCurrentInstance();
        UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
        String on = String.valueOf(userObj);
        String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
        String createdId = String.valueOf(userObj.getId());
        String roleId = String.valueOf(userObj.getRole_id());

        try {

            con = dbConnections.mySqlDBconnection();

            String personalDetails = "update "+getSchool()+"_tbstudentclass set currentclass=?,"
                    + "updatedby=?,updaterid=?,dateupdated=? where studentid=? and id=?";

            pstmt = con.prepareStatement(personalDetails);

            pstmt.setBoolean(1, false);
            pstmt.setString(2, createdby);
            pstmt.setString(3, createdId);
            pstmt.setString(4, DateManipulation.dateAndTime());
            pstmt.setString(5, edits.getStudentid());
            pstmt.setInt(6, edits.getSid());

            pstmt.executeUpdate();

            String nurseryInsert = "insert into "+getSchool()+"_tbstudentclass (studentid,first_name,middle_name,last_name,full_name,class,"
                    + "classtype,isdeleted,datecreated,datetime_created,createdby,imagelink,Arm,currentclass,term,year) values "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pstmt = con.prepareStatement(nurseryInsert);

            pstmt.setInt(1, Integer.parseInt(edits.getStudentid()));
            pstmt.setString(2, edits.getFname());
            pstmt.setString(3, edits.getMname());
            pstmt.setString(4, edits.getLname());
            pstmt.setString(5, edits.getFullname());
            pstmt.setString(6, getGrade());
            pstmt.setString(7, getSclass());
            pstmt.setBoolean(8, false);
            pstmt.setString(9, DateManipulation.dateAlone());
            pstmt.setString(10, DateManipulation.dateAndTime());
            pstmt.setString(11, createdby);
            pstmt.setString(12, edits.getImagelink());
            pstmt.setString(13, getArm());
            pstmt.setBoolean(14, true);
            pstmt.setString(15, getTerm());
            pstmt.setString(16, getYear());
            pstmt.executeUpdate();
            edits.studDetails();
            setMessangerOfTruth("Current Class Updated!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            setYear("");
            setTerm("");
            setSclass("");
            setGrade("");
            setArm("");
            subModel = clasEdit();

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

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public EditStudent getEdits() {
        return edits;
    }

    public void setEdits(EditStudent edits) {
        this.edits = edits;
    }

    public List<SecondaryModel> getSubModel() {
        return subModel;
    }

    public void setSubModel(List<SecondaryModel> subModel) {
        this.subModel = subModel;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

}
