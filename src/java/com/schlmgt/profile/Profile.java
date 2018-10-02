/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.login.SchoolNameGet;
import com.schlmgt.login.UserDetails;
import com.schlmgt.register.GradeModel;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.swing.table.TableModel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import com.schlmgt.school.SchoolGetterMethod;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "prof")
@ViewScoped
public class Profile implements Serializable {

    private List<ClassModel> classmodel;
    private List<GradeModel> grademodel;
    private List<NurseryModel> nursModel;
    private List<PrimaryModel> priModel;
    private List<SecondaryModel> secModel;
    private List<SecondaryModel> secModel1;
    private ClassModel model = new ClassModel();
    private GradeModel gradModel = new GradeModel();
    private String student_class;
    private String nfname;
    private String pfname;
    private String sfname;
    private String gohome;
    private String search_item;
    private String grade;
    private Boolean nursery, primary, secondary;
    private Boolean nbool, pbool, sbool, fbool;
    private String schoolValue;
    private String school;
    private SchoolGetterMethod schlGetterMethod = new SchoolGetterMethod();
    private boolean schoolStatus;
    private SchoolNameGet schlname = new SchoolNameGet();

    @PostConstruct
    public void init() {
        try {

            nursery = false;
            primary = false;
            secondary = false;
            nbool = false;
            pbool = false;
            sbool = false;
            fbool = true;

            FacesMessage msg;
            FacesContext context = FacesContext.getCurrentInstance();
            RequestContext cont = RequestContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            if (userObj != null) {
                if (userObj.getRoleAssigned() == 3) {
                    setSchoolStatus(true);
                } else {
                    classmodel = classDropdown();
                    setSchoolStatus(false);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void selectReco(SecondaryModel secRecord) {

        try {
            String tablename = null;
            FacesMessage msg;
            FacesContext context = FacesContext.getCurrentInstance();
            RequestContext cont = RequestContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            if (userObj != null) {
                if (userObj.getRoleAssigned() == 3) {
                    tablename = getSchool();
                } else {
                    tablename = schlname.schoolName(userObj.getSchoolName());

                    setSecondary(true);
                }
            }
            FacesContext ctx = FacesContext.getCurrentInstance();
            NavigationHandler nav = ctx.getApplication().getNavigationHandler();
            ctx.getExternalContext().getApplicationMap().remove("SecData");
            ctx.getExternalContext().getApplicationMap().remove("reDet");
            ctx.getExternalContext().getApplicationMap().put("SecData", secRecord);
            ctx.getExternalContext().getApplicationMap().put("reDet", tablename);
            String url = "editprofile.xhtml?faces-redirect=true";
            nav.handleNavigation(ctx, null, url);
            ctx.renderResponse();

            System.out.println(getSchool() + " here it is");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void searchTab() {
        try {

            secModel = onSecondarySearch(getSfname());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public List<SecondaryModel> onSecondarySearch(String fullname) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            List<SecondaryModel> lst = new ArrayList<>();
            con = dbConnections.mySqlDBconnection();
            if (!fullname.isEmpty()) {

                String query = "SELECT * FROM " + getSchool() + "_tbstudentclass where full_name=? and classtype=? and currentclass=?";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, fullname);
                pstmt.setString(2, model.getTbclass());
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

                    //
                    lst.add(coun);
                }
            } else {
                String query = "SELECT * FROM " + getSchool() + "_tbstudentclass where classtype=?";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, model.getTbclass());
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

                    //
                    lst.add(coun);
                }
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

    public List<SecondaryModel> onSecondaryChange(String tbclas, String tbname) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM " + tbname + "_tbstudentclass where classtype=? and currentclass=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, tbclas);
            pstmt.setBoolean(2, true);
            rs = pstmt.executeQuery();
            //
            List<SecondaryModel> lst = new ArrayList<>();
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

    public String classGet(int tbclas) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbstaffclass where staffid=? and status=?";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, tbclas);
            pstmt.setBoolean(2, true);
            rs = pstmt.executeQuery();
            //

            if (rs.next()) {

                return rs.getString("staffClass");
            }

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

    public List<String> secondaryAuto() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM " + getSchool() + "_tbstudentclass where classtype=? and currentclass=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, model.getTbclass());
            pstmt.setBoolean(2, true);
            rs = pstmt.executeQuery();

            List<String> lst = new ArrayList<>();
            String fullname;
            while (rs.next()) {

                fullname = rs.getString("full_name");

                lst.add(fullname);
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

    public List<String> completSecondary(String val) {
        List<String> com = new ArrayList();
        try {
            for (String value : secondaryAuto()) {
                if (value.toUpperCase().contains(val.toUpperCase())) {
                    com.add(value);
                }

            }
            return com;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

    public List<String> completeSchool(String val) {
        List<String> com = new ArrayList<>();
        try {
            for (SchoolManagementModel value : displaySchool()) {
                if (value.getSchoolName().toUpperCase().contains(val.toUpperCase())) {
                    com.add(value.getSchoolName());
                }

            }
            return com;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

    public String tableNameDisplay(String tablename) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT student.*, stu.dbname,stu.totalstudent,stu.totalmale,totalfemale FROM tbschlmgt student inner "
                    + "join tbschltablestructure stu on stu.schoolname=student.schlname where student.schlname=? and student.isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, tablename);
            pstmt.setBoolean(2, false);
            rs = pstmt.executeQuery();
            //           
            if (rs.next()) {
                return rs.getString("tablename");

            }
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

    public void onItemSelect(SelectEvent event) {
        try {
            setSchool(tableNameDisplay(event.getObject().toString()));
            System.out.println(event.getObject().toString() + " table name: " + tableNameDisplay(event.getObject().toString()));
            classmodel = classDropdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClassChange() throws Exception {
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
        if (userObj != null) {
            if (userObj.getRoleAssigned() == 3) {
                secModel = onSecondaryChange(model.getTbclass(), getSchool());
                setSecondary(true);
            } else {
                System.out.println(schlname.schoolName(userObj.getSchoolName()) + " hi i am here" + userObj.getSchoolName());
                secModel = onSecondaryChange(model.getTbclass(), schlname.schoolName(userObj.getSchoolName()));

                setSecondary(true);
            }
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

    public List<SecondaryModel> getSecModel1() {
        return secModel1;
    }

    public void setSecModel1(List<SecondaryModel> secModel1) {
        this.secModel1 = secModel1;
    }

    public String getSchoolValue() {
        return schoolValue;
    }

    public void setSchoolValue(String schoolValue) {
        this.schoolValue = schoolValue;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public SchoolGetterMethod getSchlGetterMethod() {
        return schlGetterMethod;
    }

    public void setSchlGetterMethod(SchoolGetterMethod schlGetterMethod) {
        this.schlGetterMethod = schlGetterMethod;
    }

    public boolean isSchoolStatus() {
        return schoolStatus;
    }

    public void setSchoolStatus(boolean schoolStatus) {
        this.schoolStatus = schoolStatus;
    }

    public SchoolNameGet getSchlname() {
        return schlname;
    }

    public void setSchlname(SchoolNameGet schlname) {
        this.schlname = schlname;
    }

}
