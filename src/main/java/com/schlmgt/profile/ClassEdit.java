/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
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
    private SecondaryModel currentModel;
    private int id;
    private String gradeType;
    private String grade;
    private String sclass;
    private String term;
    private String year;
    private String arm;
    private String messangerOfTruth;
    private String school;
    private SecondaryModel secModel = new SecondaryModel();
    public String studentId;
    private String fname;
    private String mname;
    private String lname;
    private String imgLink;
    private String gradeType1;
    private String grade1;
    private String term1;
    private String year1;
    private String arm1;

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
                setMessangerOfTruth("Session Expired for** this Student. Please select student and try again!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                ctx.addMessage(null, msg);
            }
            classDetails();
            subModel = clasEdit(getSchool());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void classDetails() {
        try {
            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con = dbConnections.mySqlDBconnection();
            String studId;

            FacesContext ctx = FacesContext.getCurrentInstance();
            SecondaryModel secResult = (SecondaryModel) ctx.getExternalContext().getApplicationMap().get("SecData");
            //test for null...
            secModel = secResult;

            if (secModel != null) {
                setStudentId(secModel.getStudentid());
            }

            String testStud = "Select * from " + getSchool() + "_tbstudentclass where studentid=? and currentclass=?";
            pstmt = con.prepareStatement(testStud);
            pstmt.setString(1, getStudentId());
            pstmt.setBoolean(2, true);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                setId(rs.getInt("id"));
                setGradeType(rs.getString("class"));
                setGrade(rs.getString("classtype"));
                setArm(rs.getString("arm"));
                setTerm(rs.getString("term"));
                setYear(rs.getString("year"));
                setFname(rs.getString("first_name"));
                setMname(rs.getString("middle_name"));
                setLname(rs.getString("last_name"));
                setImgLink(rs.getString("imagelink"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SecondaryModel> clasEdit(String tbname) throws SQLException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg;
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM " + tbname + "_tbstudentclass where studentid=? and currentclass=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getStudentId());
            System.out.println(" how far " + tbname);
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
        } catch (MySQLSyntaxErrorException e) {
            setMessangerOfTruth("**Session Expired for this Student. Please select student and try again!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            ctx.addMessage(null, msg);
            e.printStackTrace();
            return null;
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

    public SecondaryModel clasCurrent() throws SQLException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        FacesMessage msg;
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            con = dbConnections.mySqlDBconnection();
            System.out.println(getSchool() + " hhh");
            String query = "SELECT * FROM " + getSchool() + "_tbstudentclass where studentid=? and currentclass=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getStudentId());
            System.out.println(edits.getStudentid() + " how far *** " + getSchool());
            pstmt.setBoolean(2, true);
            rs = pstmt.executeQuery();
            //
            SecondaryModel coun = new SecondaryModel();
            if (rs.next()) {

                coun.setId(rs.getInt("id"));
                coun.setCurrentClass(rs.getString("class"));
                coun.setTerm(rs.getString("term"));
                coun.setYear(rs.getString("year"));
                coun.setArm(rs.getString("arm"));
                coun.setNoOfTimesRepeated(rs.getString("NoOfTimesRepeated"));
            }
            System.out.println(coun.getCurrentClass() + " now i got you");
            return coun;
        } catch (MySQLSyntaxErrorException e) {
            setMessangerOfTruth("Session Expired for this Student. Please select studentzzzz and try again!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            ctx.addMessage(null, msg);
            e.printStackTrace();
            return null;
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

            String personalDetails = "update " + getSchool() + "_tbstudentclass set currentclass=?,"
                    + "updatedby=?,updaterid=?,dateupdated=? where studentid=? and id=?";

            pstmt = con.prepareStatement(personalDetails);
            System.out.println(getSchool() + "big head**" + edits.getSid());
            pstmt.setBoolean(1, false);
            pstmt.setString(2, createdby);
            pstmt.setString(3, createdId);
            pstmt.setString(4, DateManipulation.dateAndTime());
            pstmt.setString(5, getStudentId());
            pstmt.setInt(6, getId());

            pstmt.executeUpdate();
            String fullname = getLname() + " " + getMname() + " " + getFname();
            String nurseryInsert = "insert into " + getSchool() + "_tbstudentclass (studentid,first_name,middle_name,last_name,full_name,class,"
                    + "classtype,isdeleted,datecreated,datetime_created,createdby,imagelink,Arm,currentclass,term,year) values "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pstmt = con.prepareStatement(nurseryInsert);
            System.out.println(getSchool() + "big head " + edits.getStudentid());
            pstmt.setInt(1, Integer.parseInt(getStudentId()));
            pstmt.setString(2, getFname());
            pstmt.setString(3, getMname());
            pstmt.setString(4, getLname());
            pstmt.setString(5, fullname);
            pstmt.setString(6, getGradeType1());
            pstmt.setString(7, getGrade1());
            pstmt.setBoolean(8, false);
            pstmt.setString(9, DateManipulation.dateAlone());
            pstmt.setString(10, DateManipulation.dateAndTime());
            pstmt.setString(11, createdby);
            pstmt.setString(12, getImgLink());
            pstmt.setString(13, getArm1());
            pstmt.setBoolean(14, true);
            pstmt.setString(15, getTerm1());
            pstmt.setString(16, getYear1());
            pstmt.executeUpdate();
            setMessangerOfTruth("Current Class Updated!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            classDetails();
            subModel = clasEdit(getSchool());

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

    public SecondaryModel getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(SecondaryModel currentModel) {
        this.currentModel = currentModel;
    }

    public SecondaryModel getSecModel() {
        return secModel;
    }

    public void setSecModel(SecondaryModel secModel) {
        this.secModel = secModel;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getGradeType1() {
        return gradeType1;
    }

    public void setGradeType1(String gradeType1) {
        this.gradeType1 = gradeType1;
    }

    public String getGrade1() {
        return grade1;
    }

    public void setGrade1(String grade1) {
        this.grade1 = grade1;
    }

    public String getTerm1() {
        return term1;
    }

    public void setTerm1(String term1) {
        this.term1 = term1;
    }

    public String getYear1() {
        return year1;
    }

    public void setYear1(String year1) {
        this.year1 = year1;
    }

    public String getArm1() {
        return arm1;
    }

    public void setArm1(String arm1) {
        this.arm1 = arm1;
    }

}
