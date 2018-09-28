/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.imgupload.UploadImagesX;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.logic.LoadPPTfile;
import com.schlmgt.login.UserDetails;
import com.schlmgt.register.BloodGroupModel;
import com.schlmgt.register.ClassModel;
import com.schlmgt.register.CountryModel;
import com.schlmgt.register.DisabilityModel;
import com.schlmgt.register.FreshReg;
import com.schlmgt.register.GradeModel;
import com.schlmgt.register.LgaModel;
import com.schlmgt.register.RelationshipModel;
import com.schlmgt.register.StateModel;
import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "edit")
@ViewScoped
public class EditStudent implements Serializable {

    private String fullname;
    private String fname;
    private String lname;
    private String dob;
    private String gfname;
    private String gmname;
    private String glname;
    private String sphone;
    private String semail;
    private String sex;
    private String relationship;
    private String relationship_other;
    private String gphone;
    private String gmemail;
    private String gcountry;
    private String state;
    private String lga;
    private String address;
    private String previousSchl;
    private String previousClass;
    private String previousGrade;
    private String currentClass;
    private String currentGrade;
    private String arm;
    private String fullClass;
    private String disability;
    private String otherDis;
    private String bgroup;
    private String mname;
    private String sclass;
    private String url;
    private String messangerOfTruth;
    private Boolean shw;
    private String imagelink;
    private String someParam;
    private String scl;
    private String cras;
    private Date dateOfBirth;
    private String studentid;
    private String cla;
    private Boolean fpanel;
    private Boolean spanel;
    private Boolean relatio;
    private List<GradeModel> grademodels;
    private GradeModel mod = new GradeModel();
    private List<RelationshipModel> relation;
    private List<ClassModel> classmodel;
    private List<StateModel> states;
    private List<CountryModel> coun;
    private List<LgaModel> lgamodel;
    private String previousCla;
    private String privaClass;
    private List<DisabilityModel> dismodel;
    private Boolean status;
    private List<BloodGroupModel> modelgroup;
    private UploadedFile passport;
    private String passport_url;
    private String passportLocation;
    private String ref_number;
    private String from;
    private String imageLocation;
    private SecondaryModel secModel = new SecondaryModel();
    private String tbarm;
    private String term;
    private String year;
    private int sid;
    private String sexs;
    private String currentArm;
    private String GradeCurrent;
    private String ClassCurrent;
    private String yearCurrent;
    private String school;

    @PostConstruct
    public void init() {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            FacesMessage msg;

            String stuValue = null;
            stuValue = (String) ctx.getExternalContext().getApplicationMap().get("reDet");

            if (stuValue != null) {                
                setSchool(stuValue);
            } else {
                setMessangerOfTruth("Session Expired for this Student. Please select student and try again1!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                ctx.addMessage(null, msg);
            }

            setSpanel(false);
            setFpanel(true);
            relation = relationModel();
            studDetails(getSchool());
            studentClass();
            modelgroup = bloodgroupDropdown();
            dismodel = disabilityDropdown();
            grademodels = gradeDropdowns();
            coun = countryModel();
            classmodel = classDropdown();
            states = cityModel();
            lgamodel = lgaModels();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public EditStudent() {
        //
        ref_number = generateRefNo();

    }

    public String generateRefNo() {

        try {

            String timeStamp = new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime());

            int rnd = new Random().nextInt(99999753);
            String temp_val = String.valueOf(rnd).concat(timeStamp);
            return temp_val;

        } catch (Exception ex) {

            ex.printStackTrace();
            return null;

        }

    }//end generateRefNo(...)s

    public void valVal() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        FacesMessage msg;
        LoadPPTfile loadPPTfile = new LoadPPTfile();
        Map<String, String> params = externalContext.getRequestParameterMap();
        String sscl = params.get("from");

        System.out.println(sscl);

    }

    public void clearPix() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        FacesMessage msg;
        LoadPPTfile loadPPTfile = new LoadPPTfile();
        Map<String, String> params = externalContext.getRequestParameterMap();
        String sscl = params.get("from");
        try {

            String file_ = "pix".concat(String.valueOf(getRef_number())).concat(".jpg");

            if (!(loadPPTfile.isLoadPPtFile())) {
                setMessangerOfTruth("Cannot load configuration file...");
                setMessangerOfTruth("Operation failed");
                return;
            }
            //
            Properties ppt = loadPPTfile.getPptFile();
            String url = ppt.getProperty("pst_location");

            File file = new File(url + "".concat(file_));
            file.delete();
            //
            setPassport(null);
            passport = null;
            setPassport_url("");
            System.out.println(sscl);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleFileUpload(FileUploadEvent event) {

        setPassport(event.getFile());
        setPassport_url("");

        //byte fileNameByte[] = getFile().getContents();
        //System.out.println("fileNameByte:" + fileNameByte);
        FacesMessage message;
        UploadImagesX uploadImagesX = new UploadImagesX();

        try {

            if (!(uploadImagesX.uploadImg(getPassport(), "pix".concat(String.valueOf(getRef_number()))))) {

                message = new FacesMessage(FacesMessage.SEVERITY_FATAL, uploadImagesX.getMessangerOfTruth(), uploadImagesX.getMessangerOfTruth());
                FacesContext.getCurrentInstance().addMessage(null, message);

                //value.setPst_url(null);
                return;

            }

            message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            setPassport_url(uploadImagesX.getPst_url());
            setPassportLocation(uploadImagesX.getPst_loc());
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (Exception ex) {

            ex.printStackTrace();
            message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }

    }

    public void onDisabilityChange() {
        if (getDisability().equalsIgnoreCase("1")) {
            setStatus(true);

        } else {
            setStatus(false);
            setOtherDis("");
        }
    }

    public List<BloodGroupModel> bloodgroupDropdown() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbbloodgroup";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<BloodGroupModel> lst = new ArrayList<>();
            while (rs.next()) {

                BloodGroupModel coun = new BloodGroupModel();
                coun.setId(rs.getInt("id"));
                coun.setBloodgroup(rs.getString("bloodgroup"));

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

    public List<DisabilityModel> disabilityDropdown() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbdisability";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<DisabilityModel> lst = new ArrayList<>();
            while (rs.next()) {

                DisabilityModel coun = new DisabilityModel();
                coun.setId(rs.getInt("id"));
                coun.setDisability(rs.getString("disability"));

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

    public List<GradeModel> gradeDropdowns() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbgrade where class=?";
            pstmt = con.prepareStatement(query);
            System.out.println(getPreviousClass() + "a");
            System.out.println(getFullClass());
            System.out.println(getPreviousCla());
            pstmt.setString(1, getPreviousCla());
            rs = pstmt.executeQuery();
            //
            List<GradeModel> lst = new ArrayList<>();
            while (rs.next()) {

                GradeModel couns = new GradeModel();
                couns.setId(rs.getInt("id"));
                couns.setGrade(rs.getString("grade"));
                couns.setSclass(rs.getString("class"));

                //
                lst.add(couns);
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

                ClassModel couns = new ClassModel();
                couns.setId(rs.getInt("id"));
                couns.setTbclass(rs.getString("class"));

                //
                lst.add(couns);
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

    public void onClassChange() throws Exception {

        grademodels = gradeDropdowns();

    }

    public void onStateChange() {
        if (getState() != null && !getState().equalsIgnoreCase("")) {
            try {
                lgamodel = lgaModels();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<LgaModel> lgaModels() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbstatelga where state=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getState());
            rs = pstmt.executeQuery();
            //
            List<LgaModel> lst = new ArrayList<>();
            while (rs.next()) {

                LgaModel coun = new LgaModel();
                coun.setId(rs.getInt("Id"));
                coun.setState(rs.getString("state"));
                coun.setLga(rs.getString("lga"));
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

    public List<CountryModel> countryModel() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbcountry";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<CountryModel> lst = new ArrayList<>();
            while (rs.next()) {

                CountryModel coun = new CountryModel();
                coun.setId(rs.getInt("Id"));
                coun.setCountry(rs.getString("country"));
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

    public void onCountryChange() throws Exception {

        if (getGcountry() == null || getGcountry().trim().equalsIgnoreCase("")) {
            states = nullmodel();
            FacesMessage msg;

            msg = new FacesMessage(getGcountry() + "d");

            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg;

            msg = new FacesMessage(getGcountry() + "s");

            FacesContext.getCurrentInstance().addMessage(null, msg);
            states = cityModel();

        }
    }

    public List<StateModel> cityModel() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbstates";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<StateModel> lst = new ArrayList<>();
            while (rs.next()) {

                StateModel coun = new StateModel();
                coun.setId(rs.getInt("Id"));
                coun.setState(rs.getString("states"));
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

    public List<StateModel> nullmodel() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        return null;
    }

    public void onRelationshipChange() {
        if (getRelationship().equalsIgnoreCase("7")) {
            setRelatio(true);
        } else {
            setRelatio(false);
            setRelationship_other("");
        }
    }

    public List<RelationshipModel> relationModel() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM relationship";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<RelationshipModel> lst = new ArrayList<>();
            while (rs.next()) {

                RelationshipModel rela = new RelationshipModel();
                rela.setId(rs.getInt("Id"));
                rela.setRelation(rs.getString("relation"));
                //
                lst.add(rela);
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

    private String StudentId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        FacesMessage msg;

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<String, String> params = externalContext.getRequestParameterMap();
        scl = params.get("studentid");
        return scl;
    }

    private void studentClass() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        FacesMessage msg;

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

    }

    public void studDetails(String tbname) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage msg;
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
                setStudentid(secModel.getStudentid());
            }

            String testguid = "Select a.*,b.currentclass,b.classtype,b.class,b.Arm,b.year from " + tbname + "_student_details a inner join " + tbname + "_tbstudentclass b on "
                    + "b.studentid=a.id where b.currentclass=true and a.id=?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setString(1, getStudentid());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                setFname(rs.getString("first_name"));
                setMname(rs.getString("middle_name"));
                setLname(rs.getString("last_name"));
                setFullname(rs.getString("fullname"));
                setDob(rs.getString("DOB"));
                setDateOfBirth(rs.getDate("DOB"));
                setStudentid(rs.getString("studentid"));
                setSphone(rs.getString("student_phone"));
                setSemail(rs.getString("student_email"));
                setSex(rs.getString("sex"));
                setGfname(rs.getString("guardian_firstname"));
                setGlname(rs.getString("guardian_lastname"));
                setGmname(rs.getString("guardian_middlename"));
                setRelationship(rs.getString("relationship"));
                setRelationship_other(rs.getString("relationship_other"));
                setGphone(rs.getString("guardian_phone"));
                setGmemail(rs.getString("guardian_email"));
                setGcountry(rs.getString("guardian_country"));
                setState(rs.getString("guardian_state"));
                setLga(rs.getString("guardian_lga"));
                setAddress(rs.getString("guardian_address"));
                setPreviousSchl(rs.getString("previous_school"));
                setPreviousCla(rs.getString("previous_class"));
                setPreviousClass(rs.getString("previous_grade"));
                setArm(rs.getString("arm"));
                setDisability(rs.getString("disability"));
                setOtherDis(rs.getString("other_disability"));
                setBgroup(rs.getString("bgroup"));
                setImagelink(rs.getString("image"));
                setImageLocation(rs.getString("imgLocation"));
                setGradeCurrent(rs.getString("class"));
                setClassCurrent(rs.getString("classtype"));
                setCurrentArm(rs.getString("arm"));
                setYearCurrent(rs.getString("year"));
                if (getSex().equalsIgnoreCase("1")) {
                    setSexs("Male");
                } else if (getSex().equalsIgnoreCase("2")) {
                    setSexs("Female");
                }
            }

            //for studentclassupload
            String testStud = "Select * from " + tbname + "_tbstudentclass where studentid=? and currentclass=?";
            pstmt = con.prepareStatement(testStud);
            pstmt.setString(1, getStudentid());
            pstmt.setBoolean(2, true);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                setSid(rs.getInt("id"));
                setCurrentClass(rs.getString("class"));
                setCurrentGrade(rs.getString("classtype"));
                setTbarm(rs.getString("arm"));
                setTerm(rs.getString("term"));
                setYear(rs.getString("year"));

            }
            System.out.println(getImageLocation() + " l");
            System.out.println(getImagelink());
        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (MySQLSyntaxErrorException e) {
            setMessangerOfTruth("Sessionsss Expired for this Student. Please select student and try again2!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            e.printStackTrace();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int studentNameCheck(String fname, String mname, String lname) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testflname = "Select count(*) studentCount from " + getSchool() + "_student_details where first_name=? and middle_name=? and last_name=? and is_deleted=?";
        pstmt = con.prepareStatement(testflname);
        pstmt.setString(1, fname);
        pstmt.setString(2, mname);
        pstmt.setString(3, lname);
        pstmt.setBoolean(4, false);
        rs = pstmt.executeQuery();

        rs.next();
        return rs.getInt("studentCount");

    }

    public String studentCheck(String fname, String mname, String lname) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testflname = "Select studentid from " + getSchool() + "_student_details where first_name=? and middle_name=? and last_name=? and is_deleted=?";
        pstmt = con.prepareStatement(testflname);
        pstmt.setString(1, fname);
        pstmt.setString(2, mname);
        pstmt.setString(3, lname);
        pstmt.setBoolean(4, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getString("studentid");
        }
        return "";
    }

    public void updateClass() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String fullname = getLname() + " " + getMname() + " " + getFname();
        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String dobs = format.format(getDateOfBirth());
            con = dbConnections.mySqlDBconnection();

            String personalDetails = "update " + getSchool() + "_tbstudentclass set first_name=? ,middle_name=?, last_name=?, full_name=?,"
                    + "updatedby=?,updaterid=?,dateupdated=? where studentid=? and class=?";

            pstmt = con.prepareStatement(personalDetails);

            pstmt.setString(1, getFname());
            pstmt.setString(2, getMname());
            pstmt.setString(3, getLname());
            pstmt.setString(4, fullname);
            pstmt.setString(5, createdby);
            pstmt.setInt(6, createdId);
            pstmt.setString(7, DateManipulation.dateAndTime());
            pstmt.setString(8, getStudentid());
            pstmt.setString(9, secModel.getSclass());

            pstmt.executeUpdate();
            System.out.println("o" + secModel.getSclass());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateStudentDetails(ActionEvent event) {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String fullname = getLname() + " " + getMname() + " " + getFname();
        boolean loggedIn = true;

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String dobs = format.format(getDateOfBirth());
            con = dbConnections.mySqlDBconnection();
            if (studentNameCheck(getFname(), getMname(), getLname()) <= 1) {
                String personalDetails = "update " + getSchool() + "_student_details set first_name=? ,middle_name=?, last_name=?, fullname=?, dob=?,"
                        + "student_phone=? , student_email=?, sex=? ,updated_by=?,updated_id=?,date_updated=? where studentid=?";

                pstmt = con.prepareStatement(personalDetails);

                pstmt.setString(1, getFname());
                pstmt.setString(2, getMname());
                pstmt.setString(3, getLname());
                pstmt.setString(4, fullname);
                pstmt.setString(5, dobs);
                pstmt.setString(6, getSphone());
                pstmt.setString(7, getSemail());
                pstmt.setString(8, getSex());
                pstmt.setString(9, createdby);
                pstmt.setInt(10, createdId);
                pstmt.setString(11, DateManipulation.dateAndTime());
                pstmt.setString(12, getStudentid());
                System.out.println(getStudentid());
                pstmt.executeUpdate();
                updateClass();
                setMessangerOfTruth("Personal Details Updated!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
                studDetails(getSchool());
                cont.addCallbackParam("loggedIn", loggedIn);
            } else {
                setMessangerOfTruth("First Name and Lastname Already Exists !!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
                cont.addCallbackParam("loggedIn", loggedIn);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateParent() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String fullname = getLname() + " " + getMname() + " " + getFname();
        boolean loggedIn = true;

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            con = dbConnections.mySqlDBconnection();
            String nameFull = getGlname() + " " + getGmname() + " " + getGfname();
            String personalDetails = "update " + getSchool() + "_student_details set guardian_firstname=? ,guardian_middlename=?, guardian_lastname=?, relationship=?,"
                    + "relationship_other=?,"
                    + "guardian_phone=? , guardian_email=?, guardian_country=? ,guardian_state=?,"
                    + "guardian_lga=?,guardian_address=?,updated_by=?,updated_id=?,date_updated=?,guardian_fullname=? where studentid=?";

            pstmt = con.prepareStatement(personalDetails);

            pstmt.setString(1, getGfname());
            pstmt.setString(2, getGmname());
            pstmt.setString(3, getGlname());
            pstmt.setString(4, getRelationship());
            pstmt.setString(5, getRelationship_other());
            pstmt.setString(6, getGphone());
            pstmt.setString(7, getGmemail());
            pstmt.setString(8, getGcountry());
            pstmt.setString(9, getState());
            pstmt.setString(10, getLga());
            pstmt.setString(11, getAddress());
            pstmt.setString(12, createdby);
            pstmt.setInt(13, createdId);
            pstmt.setString(14, DateManipulation.dateAndTime());
            pstmt.setString(15, nameFull);
            pstmt.setString(16, getStudentid());
            System.out.println(getStudentid());
            pstmt.executeUpdate();
            updateClass();
            setMessangerOfTruth("Parent/Guardian Details Updated!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            studDetails(getSchool());
            cont.addCallbackParam("loggedIn", loggedIn);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updatePrevious() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String fullname = getLname() + " " + getMname() + " " + getFname();
        boolean loggedIn = true;

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            con = dbConnections.mySqlDBconnection();

            String previous = "update " + getSchool() + "_student_details set previous_school=? ,previous_class=?, previous_grade=?,"
                    + "updated_by=?,updated_id=?,date_updated=? where studentid=?";

            pstmt = con.prepareStatement(previous);

            pstmt.setString(1, getPreviousSchl());
            pstmt.setString(2, getPreviousCla());
            pstmt.setString(3, getPreviousClass());
            pstmt.setString(4, createdby);
            pstmt.setInt(5, createdId);
            pstmt.setString(6, DateManipulation.dateAndTime());
            pstmt.setString(7, getStudentid());
            System.out.println(getStudentid());
            pstmt.executeUpdate();
            updateClass();
            setMessangerOfTruth("Previous School Details Updated!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            studDetails(getSchool());
            cont.addCallbackParam("loggedIn", loggedIn);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateHealth() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String fullname = getLname() + " " + getMname() + " " + getFname();
        boolean loggedIn = true;

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            con = dbConnections.mySqlDBconnection();

            String previous = "update " + getSchool() + "_student_details set disability=? ,other_disability=?, bgroup=?,"
                    + "updated_by=?,updated_id=?,date_updated=? where studentid=?";

            pstmt = con.prepareStatement(previous);

            pstmt.setString(1, getDisability());
            pstmt.setString(2, getOtherDis());
            pstmt.setString(3, getBgroup());
            pstmt.setString(4, createdby);
            pstmt.setInt(5, createdId);
            pstmt.setString(6, DateManipulation.dateAndTime());
            pstmt.setString(7, getStudentid());
            System.out.println(getStudentid());
            pstmt.executeUpdate();
            updateClass();
            setMessangerOfTruth("Health Details Updated!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            studDetails(getSchool());
            cont.addCallbackParam("loggedIn", loggedIn);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void uploadPix() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String fullname = getLname() + " " + getMname() + " " + getFname();
        boolean loggedIn = true;

        try {
            if (!getPassport_url().isEmpty()) {
                UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
                String on = String.valueOf(userObj);
                String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
                int createdId = userObj.getId();
                con = dbConnections.mySqlDBconnection();

                String previous = "update " + getSchool() + "_student_details set image=?,imgLocation=?,"
                        + "updated_by=?,updated_id=?,date_updated=? where studentid=?";

                pstmt = con.prepareStatement(previous);

                pstmt.setString(1, getPassport_url());
                pstmt.setString(2, getPassportLocation());
                pstmt.setString(3, createdby);
                pstmt.setInt(4, createdId);
                pstmt.setString(5, DateManipulation.dateAndTime());
                pstmt.setString(6, getStudentid());
                System.out.println(getStudentid());
                pstmt.executeUpdate();
                setPassport_url("");
                updateImg();
                setMessangerOfTruth("Image Updated!!");

                if (getImageLocation() == null || getImageLocation().isEmpty() || getImageLocation().equalsIgnoreCase(null)) {

                } else {
                    File file = new File(getImageLocation());
                    file.delete();
                }
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
                studDetails(getSchool());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateImg() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String fullname = getLname() + " " + getMname() + " " + getFname();
        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String dobs = format.format(getDateOfBirth());
            con = dbConnections.mySqlDBconnection();

            String personalDetails = "update " + getSchool() + "_tbstudentclass set imagelink=?,"
                    + "updatedby=?,updaterid=?,dateupdated=? where studentid=? and class=? and currentclass=?";

            pstmt = con.prepareStatement(personalDetails);

            pstmt.setString(1, getPassport_url());
            pstmt.setString(2, createdby);
            pstmt.setInt(3, createdId);
            pstmt.setString(4, DateManipulation.dateAndTime());
            pstmt.setString(5, getStudentid());
            pstmt.setString(6, secModel.getSclass());
            pstmt.setBoolean(7, true);

            pstmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getSexs() {
        return sexs;
    }

    public void setSexs(String sexs) {
        this.sexs = sexs;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTbarm() {
        return tbarm;
    }

    public void setTbarm(String tbarm) {
        this.tbarm = tbarm;
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

    public String getPassportLocation() {
        return passportLocation;
    }

    public void setPassportLocation(String passportLocation) {
        this.passportLocation = passportLocation;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public List<BloodGroupModel> getModelgroup() {
        return modelgroup;
    }

    public SecondaryModel getSecModel() {
        return secModel;
    }

    public void setSecModel(SecondaryModel secModel) {
        this.secModel = secModel;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPassport_url() {
        return passport_url;
    }

    public void setPassport_url(String passport_url) {
        this.passport_url = passport_url;
    }

    public String getRef_number() {
        return ref_number;
    }

    public void setRef_number(String ref_number) {
        this.ref_number = ref_number;
    }

    public UploadedFile getPassport() {
        return passport;
    }

    public void setPassport(UploadedFile passport) {
        this.passport = passport;
    }

    public void setModelgroup(List<BloodGroupModel> modelgroup) {
        this.modelgroup = modelgroup;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getPreviousCla() {
        return previousCla;
    }

    public void setPreviousCla(String previousCla) {
        this.previousCla = previousCla;
    }

    public List<DisabilityModel> getDismodel() {
        return dismodel;
    }

    public void setDismodel(List<DisabilityModel> dismodel) {
        this.dismodel = dismodel;
    }

    public String getPrivaClass() {
        return privaClass;
    }

    public void setPrivaClass(String privaClass) {
        this.privaClass = privaClass;
    }

    public GradeModel getMod() {
        return mod;
    }

    public void setMod(GradeModel mod) {
        this.mod = mod;
    }

    public List<GradeModel> getGrademodels() {
        return grademodels;
    }

    public void setGrademodels(List<GradeModel> grademodels) {
        this.grademodels = grademodels;
    }

    public List<ClassModel> getClassmodel() {
        return classmodel;
    }

    public void setClassmodel(List<ClassModel> classmodel) {
        this.classmodel = classmodel;
    }

    public List<LgaModel> getLgamodel() {
        return lgamodel;
    }

    public void setLgamodel(List<LgaModel> lgamodel) {
        this.lgamodel = lgamodel;
    }

    public List<StateModel> getStates() {
        return states;
    }

    public void setStates(List<StateModel> states) {
        this.states = states;
    }

    public List<CountryModel> getCoun() {
        return coun;
    }

    public void setCoun(List<CountryModel> coun) {
        this.coun = coun;
    }

    public List<RelationshipModel> getRelation() {
        return relation;
    }

    public void setRelation(List<RelationshipModel> relation) {
        this.relation = relation;
    }

    public Boolean getRelatio() {
        return relatio;
    }

    public void setRelatio(Boolean relatio) {
        this.relatio = relatio;
    }

    public String getFullClass() {
        return fullClass;
    }

    public void setFullClass(String fullClass) {
        this.fullClass = fullClass;
    }

    public String getCras() {
        return cras;
    }

    public void setCras(String cras) {
        this.cras = cras;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getFpanel() {
        return fpanel;
    }

    public void setFpanel(Boolean fpanel) {
        this.fpanel = fpanel;
    }

    public Boolean getSpanel() {
        return spanel;
    }

    public void setSpanel(Boolean spanel) {
        this.spanel = spanel;
    }

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGfname() {
        return gfname;
    }

    public void setGfname(String gfname) {
        this.gfname = gfname;
    }

    public String getGmname() {
        return gmname;
    }

    public void setGmname(String gmname) {
        this.gmname = gmname;
    }

    public String getGlname() {
        return glname;
    }

    public void setGlname(String glname) {
        this.glname = glname;
    }

    public String getSphone() {
        return sphone;
    }

    public void setSphone(String sphone) {
        this.sphone = sphone;
    }

    public String getSemail() {
        return semail;
    }

    public void setSemail(String semail) {
        this.semail = semail;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRelationship_other() {
        return relationship_other;
    }

    public void setRelationship_other(String relationship_other) {
        this.relationship_other = relationship_other;
    }

    public String getGphone() {
        return gphone;
    }

    public void setGphone(String gphone) {
        this.gphone = gphone;
    }

    public String getGmemail() {
        return gmemail;
    }

    public void setGmemail(String gmemail) {
        this.gmemail = gmemail;
    }

    public String getGcountry() {
        return gcountry;
    }

    public void setGcountry(String gcountry) {
        this.gcountry = gcountry;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPreviousSchl() {
        return previousSchl;
    }

    public void setPreviousSchl(String previousSchl) {
        this.previousSchl = previousSchl;
    }

    public String getPreviousClass() {
        return previousClass;
    }

    public void setPreviousClass(String previousClass) {
        this.previousClass = previousClass;
    }

    public String getPreviousGrade() {
        return previousGrade;
    }

    public void setPreviousGrade(String previousGrade) {
        this.previousGrade = previousGrade;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    public String getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(String currentGrade) {
        this.currentGrade = currentGrade;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getOtherDis() {
        return otherDis;
    }

    public void setOtherDis(String otherDis) {
        this.otherDis = otherDis;
    }

    public String getBgroup() {
        return bgroup;
    }

    public void setBgroup(String bgroup) {
        this.bgroup = bgroup;
    }

    public Boolean getShw() {
        return shw;
    }

    public void setShw(Boolean shw) {
        this.shw = shw;
    }

    public String getScl() {
        return scl;
    }

    public void setScl(String scl) {
        this.scl = scl;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public String getSomeParam() {
        return someParam;
    }

    public void setSomeParam(String someParam) {
        this.someParam = someParam;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getCla() {
        return cla;
    }

    public void setCla(String cla) {
        this.cla = cla;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    public String getCurrentArm() {
        return currentArm;
    }

    public void setCurrentArm(String currentArm) {
        this.currentArm = currentArm;
    }

    public String getGradeCurrent() {
        return GradeCurrent;
    }

    public void setGradeCurrent(String GradeCurrent) {
        this.GradeCurrent = GradeCurrent;
    }

    public String getClassCurrent() {
        return ClassCurrent;
    }

    public void setClassCurrent(String ClassCurrent) {
        this.ClassCurrent = ClassCurrent;
    }

    public String getYearCurrent() {
        return yearCurrent;
    }

    public void setYearCurrent(String yearCurrent) {
        this.yearCurrent = yearCurrent;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

}
