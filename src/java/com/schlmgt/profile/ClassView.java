/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.register.TermModel;
import com.schlmgt.updateSubject.SessionTable;
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
@ManagedBean(name = "vaClass")
@ViewScoped
public class ClassView implements Serializable {

    private EditStudent edits = new EditStudent();
    private List<SubjectModel> sub;
    private String currentClass;
    private String year;
    private String term;
    private String classType;
    private List<TermModel> terms;
    private List<String> years;
    private String school;
    private String messangerOfTruth;
    private String studentid;
    private SecondaryModel secModel = new SecondaryModel();

    @PostConstruct
    public void init() {

        try {

            FacesContext ctx = FacesContext.getCurrentInstance();
            FacesMessage msg;

            String stuValue = null;
            stuValue = (String) ctx.getExternalContext().getApplicationMap().get("reDet");
            SecondaryModel secResult = (SecondaryModel) ctx.getExternalContext().getApplicationMap().get("SecData");
            //test for null...
            secModel = secResult;

            if (secModel != null) {
                setStudentid(secModel.getStudentid());
            }
            if (stuValue != null) {
                stuValue = stuValue.replaceAll("\\s", "_");
                setSchool(stuValue);
            } else {
                setMessangerOfTruth("Session Expired for** this Student. Please select student and try again!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                ctx.addMessage(null, msg);
            }
            sub = testMic();
            terms = termDropdown();
            years = yearDropdown(getTerm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onYearChange(String term, String year) throws Exception {

        sub = testMic(term, year);
    }

    public List<SubjectModel> testMic(String term, String year) throws SQLException {
        studentCurrentClass();
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs
                = null;
        con = dbConnections.mySqlDBconnection();
        String studId;

        try {
            setTerm(term);
            setYear(year);
            String testguid = "Select * from sessiontable where term=? and grade =? and year=?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setString(1, term);
            pstmt.setString(2, getCurrentClass());
            pstmt.setString(3, year);
            rs = pstmt.executeQuery();

            List<SubjectModel> lst = new ArrayList<>();
            while (rs.next()) {
                SubjectModel coun = new SubjectModel();
                coun.setId(rs.getInt("id"));
                coun.setSubject(rs.getString("subject"));

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

    public void ontermchange(String term) {
        try {
            years = yearDropdown(term);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> yearDropdown(String term) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT distinct year FROM yearterm where term=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, term);
            rs = pstmt.executeQuery();
            //
            List<String> lst = new ArrayList<>();
            while (rs.next()) {

                lst.add(rs.getString("year"));

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

    public List<TermModel> termDropdown() throws Exception {

        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbterm";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<TermModel> lst = new ArrayList<>();
            while (rs.next()) {

                TermModel coun = new TermModel();
                coun.setId(rs.getInt("id"));
                coun.setTerm(rs.getString("term"));

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

    public void studentCurrentClass() {
        try {

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con = dbConnections.mySqlDBconnection();

            String testguid = "Select * from " + getSchool() + "_tbstudentclass where studentid=? and currentclass =  ?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setString(1, getStudentid());
            pstmt.setBoolean(2, true);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                setCurrentClass(rs.getString("class"));
                setClassType(rs.getString("classtype"));
                setTerm(rs.getString("term"));
                setYear(rs.getString("year"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SubjectModel> testMic() throws SQLException {
        studentCurrentClass();
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs
                = null;
        con = dbConnections.mySqlDBconnection();
        String studId;

        try {
            String testguid = "Select * from sessiontable where term=? and grade =? and year=? and isdeleted=?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setString(1, getTerm());
            pstmt.setString(2, getCurrentClass());
            pstmt.setString(3, getYear());
            pstmt.setBoolean(4, false);
            rs = pstmt.executeQuery();

            List<SubjectModel> lst = new ArrayList<>();
            while (rs.next()) {
                SubjectModel coun = new SubjectModel();
                coun.setId(rs.getInt("id"));
                coun.setSubject(rs.getString("subject"));

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

    public List<TermModel> getTerms() {
        return terms;
    }

    public void setTerms(List<TermModel> terms) {
        this.terms = terms;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public List<SubjectModel> getSub() {
        return sub;
    }

    public void setSub(List<SubjectModel> sub) {
        this.sub = sub;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    public EditStudent getEdits() {
        return edits;
    }

    public void setEdits(EditStudent edits) {
        this.edits = edits;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public SecondaryModel getSecModel() {
        return secModel;
    }

    public void setSecModel(SecondaryModel secModel) {
        this.secModel = secModel;
    }

}
