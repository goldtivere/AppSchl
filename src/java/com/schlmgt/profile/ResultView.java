/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.updateResult.ResultModel;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.SessionScoped;
import com.schlmgt.register.ClassModel;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "resultView")
@ViewScoped
public class ResultView implements Serializable {

    private EditStudent edits = new EditStudent();
    private String sclass;
    private String grade;
    private String arm;
    private String term;
    private String year;
    private List<ResultModel> resultmodel;
    private List<ResultModel> resultmodel1;
    private double stuAverage;
    private double stuTotal;
    private int position;
    private String fullname;
    private String school;
    private String messangerOfTruth;
    private List<ClassModel> classmodels;
    private String studentid;
    private SecondaryModel secModel = new SecondaryModel();
    private boolean visibleTab;

    @PostConstruct
    public void init() {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            FacesMessage msg;
            setVisibleTab(false);
            String stuValue = null;
            stuValue = (String) ctx.getExternalContext().getApplicationMap().get("reDet");

            SecondaryModel secResult = (SecondaryModel) ctx.getExternalContext().getApplicationMap().get("SecData");
            //test for null...
            secModel = secResult;

            if (secModel != null) {
                setStudentid(secModel.getStudentid());
            }

            if (stuValue != null) {
                classmodels = classDropdown();
                stuValue = stuValue.replaceAll("\\s", "_");
                setSchool(stuValue);
            } else {
                setMessangerOfTruth("Session Expired for** this Student. Please select student and try again!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                ctx.addMessage(null, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ClassModel> classDropdown() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbclass";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<ClassModel> lst = new ArrayList<>();
            while (rs.next()) {

                ClassModel coun = new ClassModel();
                coun.setId(rs.getInt("id"));
                coun.setTbclass(rs.getString("class"));
                System.out.println(rs.getString("class"));
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

    public SessionModel displayResultDetails(List<ResultModel> mode) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            SessionModel mm = new SessionModel();
            if (mode != null) {
                for (ResultModel m : mode) {

                    String nameSearch = "SELECT * FROM " + getSchool() + "_tbstudentclass where class=? and term=? and year=? and isdeleted=? and studentid=?";
                    pstmt = con.prepareStatement(nameSearch);
                    pstmt.setString(1, m.getGrade());
                    pstmt.setString(2, m.getTerm());
                    pstmt.setString(3, m.getYear());
                    pstmt.setBoolean(4, false);
                    pstmt.setString(5, m.getStudentId());
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        mm.setFullname(rs.getString("full_name"));
                    }

                    String query = "SELECT * FROM " + getSchool() + "_tbresultcompute where studentclass=? and term=? and year=? and isdeleted=? and studentreg=?";
                    pstmt = con.prepareStatement(query);
                    pstmt.setString(1, m.getGrade());
                    pstmt.setString(2, m.getTerm());
                    pstmt.setString(3, m.getYear());
                    pstmt.setBoolean(4, false);
                    pstmt.setString(5, m.getStudentId());
                    rs = pstmt.executeQuery();
                    List<SessionModel> lst = new ArrayList<>();
                    if (rs.next()) {
                        mm.setStudentClass(rs.getString("studentclass"));
                        mm.setStudentSession(rs.getString("year"));
                        mm.setStudentTerm(rs.getString("term"));
                        mm.setPosition(rs.getInt("postion"));
                        mm.setStudentAverage(Double.parseDouble(String.format("%.2f", rs.getDouble("average"))));
                        mm.setTermTotal(rs.getDouble("totalscore"));

                        //                   
                    }

                    break;

                }
            } else {
                System.out.println("damn");
            }
            return mm;
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

    public List<ResultModel> displayResult() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        edits.studDetails(getSchool());

        try {
            System.out.println(getGrade() + getArm() + getTerm() + getYear() + getStudentid() + getSchool());
            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM " + getSchool() + "_tbstudentresult where studentclass=? and arm=? and term=? and year=? and isdeleted=? and studentreg=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getGrade());
            pstmt.setString(2, getArm());
            pstmt.setString(3, getTerm());
            pstmt.setString(4, getYear());
            pstmt.setBoolean(5, false);
            pstmt.setString(6, getStudentid());
            rs = pstmt.executeQuery();
            //

            List<ResultModel> lst = new ArrayList<>();
            while (rs.next()) {

                ResultModel coun = new ResultModel();
                coun.setId(rs.getInt("id"));
                coun.setStudentId(rs.getString("studentreg"));
                coun.setSubject(rs.getString("subject"));
                coun.setFirstTest(rs.getDouble("firsttest"));
                coun.setSecondTest(rs.getDouble("secondtest"));
                coun.setExam(rs.getDouble("exam"));
                coun.setGrade(rs.getString("studentclass"));
                coun.setTerm(rs.getString("term"));
                coun.setYear(rs.getString("year"));
                coun.setMark(rs.getString("grade"));
                coun.setTotal(rs.getDouble("totalscore"));
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

    public String selectReco() throws Exception {

        return "reportCard.xhtml?faces-redirect=true";

    }

    public void viewResult() throws Exception {
        setVisibleTab(true);
        resultmodel = displayResult();
        displayResultDetails(displayResult());
        FacesContext ctx = FacesContext.getCurrentInstance();
        NavigationHandler nav = ctx.getApplication().getNavigationHandler();
        ctx.getExternalContext().getApplicationMap().put("examRecord", displayResult());
        ctx.getExternalContext().getApplicationMap().put("studentprofile", displayResultDetails(displayResult()));
        ctx.renderResponse();

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public double getStuAverage() {
        return stuAverage;
    }

    public void setStuAverage(double stuAverage) {
        this.stuAverage = stuAverage;
    }

    public double getStuTotal() {
        return stuTotal;
    }

    public void setStuTotal(double stuTotal) {
        this.stuTotal = stuTotal;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<ResultModel> getResultmodel1() {
        return resultmodel1;
    }

    public void setResultmodel1(List<ResultModel> resultmodel1) {
        this.resultmodel1 = resultmodel1;
    }

    public List<ResultModel> getResultmodel() {
        return resultmodel;
    }

    public void setResultmodel(List<ResultModel> resultmodel) {
        this.resultmodel = resultmodel;
    }

    public EditStudent getEdits() {
        return edits;
    }

    public void setEdits(EditStudent edits) {
        this.edits = edits;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
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

    public List<ClassModel> getClassmodels() {
        return classmodels;
    }

    public void setClassmodel(List<ClassModel> classmodels) {
        this.classmodels = classmodels;
    }

    public boolean isVisibleTab() {
        return visibleTab;
    }

    public void setVisibleTab(boolean visibleTab) {
        this.visibleTab = visibleTab;
    }

}
