/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.mailsender;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.filter.ThreadRunner;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.login.UserDetails;
import com.schlmgt.profile.ClassModel;
import com.schlmgt.profile.NurseryModel;
import com.schlmgt.profile.PrimaryModel;
import com.schlmgt.profile.SecondaryModel;
import com.schlmgt.register.GradeModel;
import com.schlmgt.register.StaffModel;
import com.schlmgt.school.SchoolGetterMethod;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.el.PropertyNotFoundException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.swing.table.TableModel;
import org.mike.test.ThreadTest;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "sms")
@ViewScoped
public class SmsSend implements Serializable {

    private List<ClassModel> classmodel;
    private List<GradeModel> grademodel;
    private List<NurseryModel> nursModel;
    private List<PrimaryModel> priModel;
    private List<SecondaryModel> secModel;
    private List<SecondaryModel> secModel1;
    private List<SecondaryModel> secModel2;
    private List<StaffModel> staffModel;
    private List<StaffModel> staffModel1;
    private List<StaffModel> staffModel2;
    SecondaryModel tab = new SecondaryModel();
    StaffModel staffTab = new StaffModel();
    private ClassModel model = new ClassModel();
    private GradeModel gradModel = new GradeModel();
    private String student_class;
    private String student_grade;
    private String year;
    private String nfname;
    private String pfname;
    private String sfname;
    private String gohome;
    private String search_item;
    private String grade;
    private Boolean nursery, primary, secondary;
    private Boolean nbool, pbool, sbool, fbool, status, status1;
    private String messangerOfTruth;
    private ExecutorService service;
    private SchoolGetterMethod schlGetterMethod = new SchoolGetterMethod();
    private String school;
    private String schools;
    private boolean buttonActive;

    @PostConstruct
    public void init() {
        try {
            setStatus(false);
            setStatus1(false);
            setButtonActive(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ongradeChanges(String studentClass, String studentGrade) throws Exception {
        String tablename = schlGetterMethod.tableNameDisplay(getSchools());
        setSchool(tablename);
        System.out.println(tablename + " " + getSchools());
        secModel = onSecondarySearch(studentClass, studentGrade, tablename);
        setStatus(true);
        setStatus1(false);

    }

    public void onStaffChange() throws Exception {
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            if (getSchools().isEmpty() || getSchools() == null || getSchools().equals("")) {
                setMessangerOfTruth("Please select a school");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);

            } else {
                setStatus(false);
                setStatus1(true);
                staffModel = onStaffSearch();
            }
        } catch (NullPointerException e) {
            setMessangerOfTruth("Please select a school");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SecondaryModel> onSecondarySearch(String studentClass, String studentGrade, String tablename) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            List<SecondaryModel> lst = new ArrayList<>();
            con = dbConnections.mySqlDBconnection();

            String query = "SELECT student.*,stu.guardian_phone FROM " + tablename + "_tbstudentclass student inner join " + tablename + "_student_details stu on "
                    + "stu.id=student.studentid where student.classtype=? and student.class=? and student.currentclass=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, studentClass);
            pstmt.setString(2, studentGrade);
            pstmt.setBoolean(3, true);
            rs = pstmt.executeQuery();
            //

            while (rs.next()) {

                SecondaryModel coun = new SecondaryModel();
                coun.setId(rs.getInt("id"));
                coun.setStudentid(rs.getString("studentid"));
                coun.setFirst_name(rs.getString("first_name"));
                coun.setMiddle_name(rs.getString("middle_name"));
                coun.setLast_name(rs.getString("last_name"));
                coun.setFull_name(rs.getString("full_name"));
                coun.setSclass(rs.getString("class"));
                coun.setClasstype(rs.getString("classtype"));
                coun.setPromoted(rs.getBoolean("promoted"));
                coun.setImageLink(rs.getString("imagelink"));
                coun.setArm(rs.getString("arm"));
                coun.setPnum(rs.getString("guardian_phone"));
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

    public List<StaffModel> onStaffSearch() throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            List<StaffModel> lst = new ArrayList<>();
            con = dbConnections.mySqlDBconnection();

            String query = "select * from user_details staffuser "
                    + "where is_deleted=? and suspendedstatus=?";
            pstmt = con.prepareStatement(query);
            pstmt.setBoolean(1, false);
            pstmt.setBoolean(2, false);
            rs = pstmt.executeQuery();
            //

            while (rs.next()) {

                StaffModel coun = new StaffModel();
                coun.setStaffId(rs.getString("id"));
                coun.setFname(rs.getString("first_name"));
                coun.setMname(rs.getString("middlename"));
                coun.setLname(rs.getString("last_name"));
                coun.setPnum(rs.getString("username"));
//                coun.setStaffClass(rs.getString("staffclass"));
//                coun.setStaffGrade(rs.getString("staffgrade"));
                // coun.setYear(rs.getString("year"));
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

    public void sendStudent() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        boolean loggedIn = true;
        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            con = dbConnections.mySqlDBconnection();
            if (secModel1 == null || secModel1.isEmpty()) {
                setMessangerOfTruth("Item(s) not selected!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            } else {

                String updateStaff = "insert into " + getSchool() + "_smstable (body,phonenumbers,status,datesent,datetimesent,sentby) values"
                        + "(?,?,?,?,?,?)";
                System.out.println(getSchool() + " hiGold");
                pstmt = con.prepareStatement(updateStaff);
                for (SecondaryModel ta : secModel1) {
                    pstmt.setString(1, getSearch_item());
                    pstmt.setString(2, ta.getPnum());
                    pstmt.setBoolean(3, false);
                    pstmt.setString(4, DateManipulation.dateAlone());
                    pstmt.setString(5, DateManipulation.dateAndTime());
                    pstmt.setString(6, createdby);
                    pstmt.executeUpdate();

                }

                setMessangerOfTruth("Message Sent!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
                cont.addCallbackParam("loggedIn", loggedIn);
                setGohome("");
            }
        } catch (PropertyNotFoundException e) {

            setMessangerOfTruth("Item(s) not selected!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public void sendStaff() {

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        boolean loggedIn = true;
        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            con = dbConnections.mySqlDBconnection();
            if (staffModel1 == null || staffModel1.isEmpty()) {
                setMessangerOfTruth("Item(s) not selected!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            } else {

                String updateStaff = "insert into " + getSchool() + "_smstable (body,phonenumbers,status,datesent,datetimesent,sentby) values"
                        + "(?,?,?,?,?,?)";

                pstmt = con.prepareStatement(updateStaff);
                for (StaffModel ta : staffModel1) {
                    pstmt.setString(1, getGohome());
                    pstmt.setString(2, ta.getPnum());
                    pstmt.setBoolean(3, false);
                    pstmt.setString(4, DateManipulation.dateAlone());
                    pstmt.setString(5, DateManipulation.dateAndTime());
                    pstmt.setString(6, createdby);
                    pstmt.executeUpdate();

                }

                setMessangerOfTruth("Message Sent!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
                cont.addCallbackParam("loggedIn", loggedIn);
                setGohome("");
            }
        } catch (PropertyNotFoundException e) {

            setMessangerOfTruth("Item(s) not selected!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void onItemSelect(SelectEvent event) {
        try {
            setSchool(schlGetterMethod.tableNameDisplay(event.getObject().toString()).replaceAll("\\s", "_"));
            classmodel = classDropdown();
            setSchool(schlGetterMethod.tableNameDisplay(event.getObject().toString()).replaceAll("\\s", "_"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public StaffModel getStaffTab() {
        return staffTab;
    }

    public void setStaffTab(StaffModel staffTab) {
        this.staffTab = staffTab;
    }

    public List<StaffModel> getStaffModel() {
        return staffModel;
    }

    public void setStaffModel(List<StaffModel> staffModel) {
        this.staffModel = staffModel;
    }

    public List<StaffModel> getStaffModel1() {
        return staffModel1;
    }

    public void setStaffModel1(List<StaffModel> staffModel1) {
        this.staffModel1 = staffModel1;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Boolean getStatus1() {
        return status1;
    }

    public void setStatus1(Boolean status1) {
        this.status1 = status1;
    }

    public List<SecondaryModel> getSecModel2() {
        return secModel2;
    }

    public void setSecModel2(List<SecondaryModel> secModel2) {
        this.secModel2 = secModel2;
    }

    public List<SecondaryModel> getSecModel1() {
        return secModel1;
    }

    public void setSecModel1(List<SecondaryModel> secModel1) {
        this.secModel1 = secModel1;
    }

    public SecondaryModel getTab() {
        return tab;
    }

    public void setTab(SecondaryModel tab) {
        this.tab = tab;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getStudent_grade() {
        return student_grade;
    }

    public void setStudent_grade(String student_grade) {
        this.student_grade = student_grade;
    }

    public String getNfname() {
        return nfname;
    }

    public void setNfname(String nfname) {
        this.nfname = nfname;
    }

    public String getPfname() {
        return pfname;
    }

    public void setPfname(String pfname) {
        this.pfname = pfname;
    }

    public String getSfname() {
        return sfname;
    }

    public void setSfname(String sfname) {
        this.sfname = sfname;
    }

    public String getGohome() {
        return gohome;
    }

    public void setGohome(String gohome) {
        this.gohome = gohome;
    }

    public Boolean getNbool() {
        return nbool;
    }

    public void setNbool(Boolean nbool) {
        this.nbool = nbool;
    }

    public Boolean getPbool() {
        return pbool;
    }

    public void setPbool(Boolean pbool) {
        this.pbool = pbool;
    }

    public Boolean getSbool() {
        return sbool;
    }

    public void setSbool(Boolean sbool) {
        this.sbool = sbool;
    }

    public Boolean getFbool() {
        return fbool;
    }

    public void setFbool(Boolean fbool) {
        this.fbool = fbool;
    }

    public List<ClassModel> getClassmodel() {
        return classmodel;
    }

    public void setClassmodel(List<ClassModel> classmodel) {
        this.classmodel = classmodel;
    }

    public List<NurseryModel> getNursModel() {
        return nursModel;
    }

    public void setNursModel(List<NurseryModel> nursModel) {
        this.nursModel = nursModel;
    }

    public List<PrimaryModel> getPriModel() {
        return priModel;
    }

    public void setPriModel(List<PrimaryModel> priModel) {
        this.priModel = priModel;
    }

    public List<SecondaryModel> getSecModel() {
        return secModel;
    }

    public void setSecModel(List<SecondaryModel> secModel) {
        this.secModel = secModel;
    }

    public Boolean getNursery() {
        return nursery;
    }

    public void setNursery(Boolean nursery) {
        this.nursery = nursery;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public Boolean getSecondary() {
        return secondary;
    }

    public void setSecondary(Boolean secondary) {
        this.secondary = secondary;
    }

    public List<GradeModel> getGrademodel() {
        return grademodel;
    }

    public void setGrademodel(List<GradeModel> grademodel) {
        this.grademodel = grademodel;
    }

    public ClassModel getModel() {
        return model;
    }

    public void setModel(ClassModel model) {
        this.model = model;
    }

    public GradeModel getGradModel() {
        return gradModel;
    }

    public void setGradModel(GradeModel gradModel) {
        this.gradModel = gradModel;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getSearch_item() {
        return search_item;
    }

    public void setSearch_item(String search_item) {
        this.search_item = search_item;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<StaffModel> getStaffModel2() {
        return staffModel2;
    }

    public void setStaffModel2(List<StaffModel> staffModel2) {
        this.staffModel2 = staffModel2;
    }

    public SchoolGetterMethod getSchlGetterMethod() {
        return schlGetterMethod;
    }

    public void setSchlGetterMethod(SchoolGetterMethod schlGetterMethod) {
        this.schlGetterMethod = schlGetterMethod;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchools() {
        return schools;
    }

    public void setSchools(String schools) {
        this.schools = schools;
    }

    public boolean isButtonActive() {
        return buttonActive;
    }

    public void setButtonActive(boolean buttonActive) {
        this.buttonActive = buttonActive;
    }

}
