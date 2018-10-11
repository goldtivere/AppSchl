/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.imgupload.UploadImagesX;
import com.schlmgt.logic.AESencrp;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.logic.LoadPPTfile;
import com.schlmgt.login.UserDetails;
import com.schlmgt.profile.SecondaryModel;
import com.schlmgt.school.SchoolGetterMethod;
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
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "freg")
@ViewScoped
public class FreshReg implements Serializable {

    private String fname;
    private String mname;
    private String lname;
    private Date dob;
    private String pnum;
    private String email;
    private String gfname;
    private String gmname;
    private String glname;
    private String grelation;
    private String gpnum;
    private String gemail;
    private String gcount;
    private String gstate;
    private String glga;
    private String gaddress;
    private String pschl;
    private String sclass;
    private String grade;
    private String disability;
    private String bgroup;
    private String disab;
    private String arm;
    private String sex;
    private String year;
    private String term;
    private boolean armStatus;
    private UploadedFile uploadImage;
    private List<RelationshipModel> relation;
    private RelationshipModel modes = new RelationshipModel();
    private List<CountryModel> country;
    private List<StateModel> state;
    private CountryModel counts = new CountryModel();
    private StateModel citys = new StateModel();
    private boolean relatio;
    private boolean dStatus;
    private boolean confirmPanel;
    private boolean studentPanel;
    private LgaModel lgas = new LgaModel();
    private List<LgaModel> lgamodel;
    private List<ClassModel> classmodel;
    private ClassModel modelclass = new ClassModel();
    private ClassModel classmode = new ClassModel();
    private List<GradeModel> grademodel;
    private List<GradeModel> grademodels;
    private GradeModel modelgrade = new GradeModel();
    private GradeModel gradeMode = new GradeModel();
    private BloodGroupModel grpmodel = new BloodGroupModel();
    private List<BloodGroupModel> modelgroup;
    private DisabilityModel modeldis = new DisabilityModel();
    private List<DisabilityModel> dismodel;
    private UploadedFile passport;
    private String messangerOfTruth;
    private String passport_url;
    private String ref_number;
    private String disoption;
    private String relationOption;
    private String displaydate;
    private String imageLocation;
    private List<GradeModel> modeGrade;
    private String sexs;
    private String school;
    private SchoolGetterMethod schlGetterMethod = new SchoolGetterMethod();

    @PostConstruct
    public void init() {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage msg;
            confirmPanel = false;
            studentPanel = true;
            armStatus = true;
            grademodels = gradeDropdowns();
            dismodel = disabilityDropdown();
            modelgroup = bloodgroupDropdown();
            classmodel = classDropdown();
            relation = relationModel();
            country = countryModel();
            relatio = false;
            dStatus = false;
            String stuValue = null;

            FacesContext ctx = FacesContext.getCurrentInstance();
            stuValue = (String) ctx.getExternalContext().getApplicationMap().get("regDet");
            System.out.println(getSchool() + " hi");
            //test for null...
            if (stuValue != null) {
               
                setSchool(schlGetterMethod.tableNameDisplay(stuValue));
            } else {
                setMessangerOfTruth("Please click on register and select name of school and registration type to proceed!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onRelationshipChange() {
        if (modes.getRelation().equalsIgnoreCase("7")) {
            setRelatio(true);
        } else {
            setRelatio(false);
        }
    }

    public void onDisabilityChange() {
        if (modeldis.getDisability().equalsIgnoreCase("1")) {
            setdStatus(true);
        } else {
            setdStatus(false);
        }
    }

    public void onStateChange() {
        if (citys.getState() != null && !citys.getState().equalsIgnoreCase("")) {
            try {
                lgamodel = lgaModels();
            } catch (Exception ex) {
                Logger.getLogger(FreshReg.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void onClassChange() throws Exception {

        grademodel = gradeDropdown();

    }

    public void onClassChanges() throws Exception {

        grademodels = gradeDropdowns();

    }

    public void onGradChan() throws Exception {
        if (gradeMode.getGrade().isEmpty() || gradeMode.getGrade() == null) {
            setArmStatus(true);
        } else {
            setArmStatus(false);
        }

    }

    public void onCountryChange() throws Exception {

        if (counts.getCountry() == null || counts.getCountry().trim().equalsIgnoreCase("")) {
            state = nullmodel();
            FacesMessage msg;

            msg = new FacesMessage(counts.getCountry() + "d");

            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg;

            msg = new FacesMessage(counts.getCountry() + "s");

            FacesContext.getCurrentInstance().addMessage(null, msg);
            state = cityModel();

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

    public List<GradeModel> gradeDropdown() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbgrade where class=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, modelclass.getTbclass());
            rs = pstmt.executeQuery();
            //
            List<GradeModel> lst = new ArrayList<>();
            while (rs.next()) {

                GradeModel coun = new GradeModel();
                coun.setId(rs.getInt("id"));
                coun.setGrade(rs.getString("grade"));
                coun.setSclass(rs.getString("class"));

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
            pstmt.setString(1, classmode.getTbclass());
            rs = pstmt.executeQuery();
            //
            List<GradeModel> lst = new ArrayList<>();
            while (rs.next()) {

                GradeModel coun = new GradeModel();
                coun.setId(rs.getInt("id"));
                coun.setGrade(rs.getString("grade"));
                coun.setSclass(rs.getString("class"));

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
            pstmt.setString(1, citys.getState());
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
            setImageLocation(uploadImagesX.getPst_loc());
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (Exception ex) {

            ex.printStackTrace();
            message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }

    }

    public FreshReg() {
        ref_number = generateRefNo();
    }

    public boolean studentNameCheck(String fname, String lname) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage msg;
        try {
            con = dbConnections.mySqlDBconnection();
            String testflname = "Select * from " + getSchool() + "_student_details where first_name=? and last_name=? and is_deleted=?";
            pstmt = con.prepareStatement(testflname);
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt.setBoolean(3, false);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return true;
            }
            return false;
        } catch (MySQLSyntaxErrorException e) {
            setMessangerOfTruth("Please click on register and select name of school and registration type to proceed!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            return false;
        }
    }

    public int studentIdCheck() throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testflname = "Select * from " + getSchool() + "_student_details order by id DESC LIMIT 1";
        pstmt = con.prepareStatement(testflname);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("id");
        }
        return 0;
    }

    public boolean studentEmailCheck(String email, String gmail) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testemail = "Select * from " + getSchool() + "_student_details where student_email=? or guardian_email=? and is_deleted=?";
        pstmt = con.prepareStatement(testemail);
        pstmt.setString(1, email);
        pstmt.setString(2, gmail);
        pstmt.setBoolean(3, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;

        }
        return false;
    }

    public boolean studentEmailCheckName(String gmail, String fname, String mname, String lname) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testemail = "Select * from " + getSchool() + "_student_details where guardian_email=? and guardian_firstname=? and guardian_middlename=?"
                + " and guardian_lastname=? and is_deleted=?";
        pstmt = con.prepareStatement(testemail);
        pstmt.setString(1, gmail);
        pstmt.setString(2, fname);
        pstmt.setString(3, mname);
        pstmt.setString(4, lname);
        pstmt.setBoolean(5, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;

        }
        return false;
    }

    public boolean studentPhoneCheck(String pnum, String gpnum) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testemail = "Select * from " + getSchool() + "_student_details where student_phone=? or Guardian_phone=? and is_deleted=?";
        pstmt = con.prepareStatement(testemail);
        pstmt.setString(1, pnum);
        pstmt.setString(2, gpnum);
        pstmt.setBoolean(3, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;

        }
        return false;
    }

    public boolean studentPhoneCheckName(String gpnum, String fname, String mname, String lname) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testemail = "Select * from " + getSchool() + "_student_details where Guardian_phone=? and guardian_firstname=? and guardian_middlename=?"
                + " and guardian_lastname=? and is_deleted=?";
        pstmt = con.prepareStatement(testemail);
        pstmt.setString(1, gpnum);
        pstmt.setString(2, fname);
        pstmt.setString(3, mname);
        pstmt.setString(4, lname);
        pstmt.setBoolean(5, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;

        }
        return false;
    }

    public boolean guardianEmailCheck(String gmail, String email) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testemail = "Select * from " + getSchool() + "_student_details where guardian_email=? or student_email=? and is_deleted=?";
        pstmt = con.prepareStatement(testemail);
        pstmt.setString(1, getGemail());
        pstmt.setString(2, getEmail());
        pstmt.setBoolean(3, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;

        }
        return false;
    }

    public boolean guardianphoneCheck(String gpnum, String pnum) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testemail = "Select * from " + getSchool() + "_student_details where Guardian_phone=? or Student_phone=? and is_deleted=?";
        pstmt = con.prepareStatement(testemail);
        pstmt.setString(1, getGpnum());
        pstmt.setString(2, getPnum());
        pstmt.setBoolean(3, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;

        }
        return false;
    }

    public void classUpload(int studentId, String imgLink, String createdby, String fullname, String currentclass) {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnections.mySqlDBconnection();

            String nurseryInsert = "insert into " + getSchool() + "_tbstudentclass (studentid,first_name,middle_name,last_name,full_name,class,"
                    + "classtype,isdeleted,datecreated,datetime_created,createdby,imagelink,Arm,currentclass,term,year) values "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pstmt = con.prepareStatement(nurseryInsert);

            pstmt.setInt(1, studentId);
            pstmt.setString(2, getFname());
            pstmt.setString(3, getMname());
            pstmt.setString(4, getLname());
            pstmt.setString(5, fullname);
            pstmt.setString(6, gradeMode.getGrade());
            pstmt.setString(7, currentclass);
            pstmt.setBoolean(8, false);
            pstmt.setString(9, DateManipulation.dateAlone());
            pstmt.setString(10, DateManipulation.dateAndTime());
            pstmt.setString(11, createdby);
            pstmt.setString(12, imgLink);
            pstmt.setString(13, getArm());
            pstmt.setBoolean(14, true);
            pstmt.setString(15, getTerm());
            pstmt.setString(16, getYear());
            pstmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void registerStudent() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage msg;
            int studentId;
            if (studentIdCheck() == 0) {
                studentId = 0 + 1;
            } else {
                studentId = studentIdCheck() + 1;
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String dob = format.format(getDob());

            //check if phone number and email of both parent an student is empty
            if (getPnum().isEmpty() || getPnum().equals("")) {
                setPnum(null);
            }
            if (getEmail().isEmpty() || getEmail().equals("")) {
                setEmail(null);
            }
            if (getGpnum().isEmpty() || getGpnum().equals("")) {
                setGpnum(null);
            }
            if (getGemail().isEmpty() || getGemail().equals("")) {
                setGemail(null);
            }

            //generate unique identifier
            String fullname = getLname() + " " + getMname() + " " + getFname();
            String gfullname = getGlname() + " " + getGmname() + " " + getGfname();
            if (studentEmailCheck(getEmail(), getGemail()) && !studentEmailCheckName(getGemail(), getGfname(), getMname(), getGlname())) {
                setMessangerOfTruth("Email Already Exist!! Please enter a different email");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            } else if (studentPhoneCheck(getPnum(), getGpnum()) && !studentPhoneCheckName(getGpnum(), getGfname(), getGmname(), getGlname())) {
                setMessangerOfTruth("Phone Number Already Exist!! Please enter a different Phone Number");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            } else if (!studentPhoneCheck(getPnum(), getGpnum()) && !studentPhoneCheckName(getGpnum(), getGfname(), getGmname(), getGlname())) {
                createStudent(studentId, dob, fullname, gfullname);
            } else if (!studentEmailCheck(getEmail(), getGemail()) && !studentPhoneCheck(getPnum(), getGpnum())) {
                createStudent(studentId, dob, fullname, gfullname);
            } else if ((studentEmailCheck(getEmail(), getGemail()) && studentEmailCheckName(getGemail(), getGfname(), getMname(), getGlname()))
                    || (studentPhoneCheck(getPnum(), getGpnum()) && studentPhoneCheckName(getPnum(), getGfname(), getGmname(), getGlname()))) {
                createStudent(studentId, dob, fullname, gfullname);
            } else {
                setMessangerOfTruth("User Created!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void createStudent(int studentId, String dob, String fullname, String gfullname) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();

            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            FacesMessage msg;
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            String createdId = String.valueOf(userObj.getId());
            String roleId = String.valueOf(userObj.getRole_id());
            con = dbConnections.mySqlDBconnection();
            UUID idOne = UUID.randomUUID();
            String insertStudentDetails = "insert into " + getSchool() + "_Student_details"
                    + "(first_name,middle_name,last_name,fullname,DOB,student_phone,student_email,sex,Guardian_firstname,"
                    + "Guardian_middlename,Guardian_lastname,Guardian_fullname,relationship,relationship_other,Guardian_phone,"
                    + "Guardian_email,guardian_country,guardian_state,guardian_lga,guardian_address,previous_school,"
                    + "previous_class,previous_grade,current_class,current_grade,Arm,disability,other_disability,bgroup,image,created_by,"
                    + "date_created,datetime_created,is_deleted,studentId,imglocation)"
                    + "values"
                    + "(?,?,?,?,?,"
                    + "?,?,?,?,?,"
                    + "?,?,?,?,?,?,"
                    + "?,?,?,?,?,"
                    + "?,?,?,?,?,"
                    + "?,?,?,?,?,?,"
                    + "?,?,?,?)";

            pstmt = con.prepareStatement(insertStudentDetails);
            pstmt.setString(1, getFname());
            pstmt.setString(2, getMname());
            pstmt.setString(3, getLname());
            pstmt.setString(4, fullname);
            pstmt.setString(5, dob);
            pstmt.setString(6, getPnum());
            pstmt.setString(7, getEmail());
            pstmt.setString(8, getSex());
            pstmt.setString(9, getGfname());
            pstmt.setString(10, getGmname());
            pstmt.setString(11, getGlname());
            pstmt.setString(12, gfullname);
            pstmt.setString(13, modes.getRelation());
            pstmt.setString(14, getRelationOption());
            pstmt.setString(15, getGpnum());
            pstmt.setString(16, getGemail());
            pstmt.setString(17, getGcount());
            pstmt.setString(18, getGstate());
            pstmt.setString(19, getGlga());
            pstmt.setString(20, getGaddress());
            pstmt.setString(21, getPschl());
            pstmt.setString(22, modelclass.getTbclass());
            pstmt.setString(23, modelgrade.getGrade());
            pstmt.setString(24, classmode.getTbclass());
            pstmt.setString(25, gradeMode.getGrade());
            pstmt.setString(26, getArm());
            pstmt.setString(27, modeldis.getDisability());
            pstmt.setString(28, getDisoption());
            pstmt.setString(29, grpmodel.getBloodgroup());
            pstmt.setString(30, getPassport_url());
            pstmt.setString(31, createdby);
            pstmt.setString(32, DateManipulation.dateAlone());
            pstmt.setString(33, DateManipulation.dateAndTime());
            pstmt.setBoolean(34, false);
            pstmt.setInt(35, studentId);
            pstmt.setString(36, getImageLocation());

            pstmt.executeUpdate();
            String slink = "http://localhost:8080/SchlMgt/faces/pages/create/index.xhtml?id=";
            String insertEmail = "insert into " + getSchool() + "_studentstatus (guid,full_name,status,datelogged,studentemail,date_time,studentId,link)"
                    + "values(?,?,?,?,?,?,?,?)";

            pstmt = con.prepareStatement(insertEmail);
            pstmt.setString(1, idOne.toString());
            pstmt.setString(2, fullname);
            pstmt.setBoolean(3, false);
            pstmt.setString(4, DateManipulation.dateAlone());
            pstmt.setString(5, getGemail());
            pstmt.setString(6, DateManipulation.dateAndTime());
            pstmt.setInt(7, studentId);
            pstmt.setString(8, slink + idOne.toString());

            pstmt.executeUpdate();

            classUpload(studentId, getPassport_url(), createdby, fullname, classmode.getTbclass());

            refresh();
            NavigationHandler nav = context.getApplication().getNavigationHandler();

            String url_ = "/pages/register/Success.xhtml?faces-redirect=true";
            nav.handleNavigation(context, null, url_);
            context.renderResponse();
            setMessangerOfTruth("User Created!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    }//end generateRefNo(...)

    public void clearPix() {

        LoadPPTfile loadPPTfile = new LoadPPTfile();

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

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void refresh() {

        setFname("");
        setMname("");
        setLname("");
        setDob(null);
        setSex("");

        setPnum("");
        setEmail("");

        setGfname("");
        setGmname("");
        setGlname("");
        setGrelation("");
        modes.setRelation("");
        setRelationOption("");
        counts.setCountry("");
        setGpnum("");
        setGemail("");
        citys.setState("");
        lgas.setLga("");
        setGaddress("");

        setPschl("");
        modelclass.setTbclass("");
        modelgrade.setGrade("");

        classmode.setTbclass("");
        gradeMode.setGrade("");
        setArm("");

        modeldis.setDisability("");
        setDisoption("");
        grpmodel.setBloodgroup("");

        setPassport_url("");

    }

    public void confirmDetails() throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        FacesMessage msg;
        if (studentNameCheck(getFname(), getLname())) {
            System.out.println(studentNameCheck(getFname(), getLname()));
            setMessangerOfTruth("Firstname and Lastname exists!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
        } else {

            setConfirmPanel(true);
            setStudentPanel(false);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            setFname(getFname());
            setMname(getMname());
            setLname(getLname());

            setPnum(getPnum());
            setEmail(getEmail());

            setGfname(getGfname());
            setGmname(getGmname());
            setGlname(getGlname());
            setGrelation(getGrelation());
            setRelationOption(getRelationOption());
            setGpnum(getGpnum());
            setGemail(getGemail());
            setGcount(counts.getCountry());
            setGstate(citys.getState());
            setGlga(lgas.getLga());
            setGaddress(getGaddress());

            setPschl(getPschl());
            modelclass.setTbclass(modelclass.getTbclass());
            modelgrade.setGrade(modelgrade.getGrade());

            classmode.setTbclass(classmode.getTbclass());
            gradeMode.setGrade(gradeMode.getGrade());
            setArm(getArm());
            setSex(getSex());
            if (getSex().equalsIgnoreCase("1")) {
                setSexs("Male");
            } else if (getSex().equalsIgnoreCase("2")) {
                setSexs("Female");
            }

            modeldis.setDisability(modeldis.getDisability());
            setDisoption(getDisoption());
            grpmodel.setBloodgroup(grpmodel.getBloodgroup());

            setPassport_url(getPassport_url());
        }

    }

    public void back() {
        setConfirmPanel(false);
        setStudentPanel(true);
    }

    public String getSexs() {
        return sexs;
    }

    public void setSexs(String sexs) {
        this.sexs = sexs;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<GradeModel> getModeGrade() {
        return modeGrade;
    }

    public void setModeGrade(List<GradeModel> modeGrade) {
        this.modeGrade = modeGrade;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    public String getDisplaydate() {
        return displaydate;
    }

    public void setDisplaydate(String displaydate) {
        this.displaydate = displaydate;
    }

    public String getRelationOption() {
        return relationOption;
    }

    public void setRelationOption(String relationOption) {
        this.relationOption = relationOption;
    }

    public String getDisoption() {
        return disoption;
    }

    public void setDisoption(String disoption) {
        this.disoption = disoption;
    }

    public UploadedFile getPassport() {
        return passport;
    }

    public List<GradeModel> getGrademodels() {
        return grademodels;
    }

    public void setGrademodels(List<GradeModel> grademodels) {
        this.grademodels = grademodels;
    }

    public void setPassport(UploadedFile passport) {
        this.passport = passport;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
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

    public DisabilityModel getModeldis() {
        return modeldis;
    }

    public void setModeldis(DisabilityModel modeldis) {
        this.modeldis = modeldis;
    }

    public List<DisabilityModel> getDismodel() {
        return dismodel;
    }

    public void setDismodel(List<DisabilityModel> dismodel) {
        this.dismodel = dismodel;
    }

    public BloodGroupModel getGrpmodel() {
        return grpmodel;
    }

    public void setGrpmodel(BloodGroupModel grpmodel) {
        this.grpmodel = grpmodel;
    }

    public List<BloodGroupModel> getModelgroup() {
        return modelgroup;
    }

    public void setModelgroup(List<BloodGroupModel> modelgroup) {
        this.modelgroup = modelgroup;
    }

    public GradeModel getGradeMode() {
        return gradeMode;
    }

    public void setGradeMode(GradeModel gradeMode) {
        this.gradeMode = gradeMode;
    }

    public ClassModel getClassmode() {
        return classmode;
    }

    public void setClassmode(ClassModel classmode) {
        this.classmode = classmode;
    }

    public List<ClassModel> getClassmodel() {
        return classmodel;
    }

    public void setClassmodel(List<ClassModel> classmodel) {
        this.classmodel = classmodel;
    }

    public ClassModel getModelclass() {
        return modelclass;
    }

    public void setModelclass(ClassModel modelclass) {
        this.modelclass = modelclass;
    }

    public List<GradeModel> getGrademodel() {
        return grademodel;
    }

    public void setGrademodel(List<GradeModel> grademodel) {
        this.grademodel = grademodel;
    }

    public GradeModel getModelgrade() {
        return modelgrade;
    }

    public void setModelgrade(GradeModel modelgrade) {
        this.modelgrade = modelgrade;
    }

    public LgaModel getLgas() {
        return lgas;
    }

    public void setLgas(LgaModel lgas) {
        this.lgas = lgas;
    }

    public List<LgaModel> getLgamodel() {
        return lgamodel;
    }

    public void setLgamodel(List<LgaModel> lgamodel) {
        this.lgamodel = lgamodel;
    }

    public List<StateModel> getState() {
        return state;
    }

    public void setState(List<StateModel> state) {
        this.state = state;
    }

    public StateModel getCitys() {
        return citys;
    }

    public void setCitys(StateModel citys) {
        this.citys = citys;
    }

    public List<CountryModel> getCountry() {
        return country;
    }

    public void setCountry(List<CountryModel> country) {
        this.country = country;
    }

    public CountryModel getCounts() {
        return counts;
    }

    public void setCounts(CountryModel counts) {
        this.counts = counts;
    }

    public List<RelationshipModel> getRelation() {
        return relation;
    }

    public void setRelation(List<RelationshipModel> relation) {
        this.relation = relation;
    }

    public RelationshipModel getModes() {
        return modes;
    }

    public void setModes(RelationshipModel modes) {
        this.modes = modes;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getGrelation() {
        return grelation;
    }

    public void setGrelation(String grelation) {
        this.grelation = grelation;
    }

    public String getGpnum() {
        return gpnum;
    }

    public void setGpnum(String gpnum) {
        this.gpnum = gpnum;
    }

    public String getGemail() {
        return gemail;
    }

    public void setGemail(String gemail) {
        this.gemail = gemail;
    }

    public String getGcount() {
        return gcount;
    }

    public void setGcount(String gcount) {
        this.gcount = gcount;
    }

    public String getGstate() {
        return gstate;
    }

    public void setGstate(String gstate) {
        this.gstate = gstate;
    }

    public String getGlga() {
        return glga;
    }

    public void setGlga(String glga) {
        this.glga = glga;
    }

    public String getGaddress() {
        return gaddress;
    }

    public void setGaddress(String gaddress) {
        this.gaddress = gaddress;
    }

    public String getPschl() {
        return pschl;
    }

    public void setPschl(String pschl) {
        this.pschl = pschl;
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

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getBgroup() {
        return bgroup;
    }

    public void setBgroup(String bgroup) {
        this.bgroup = bgroup;
    }

    public String getDisab() {
        return disab;
    }

    public void setDisab(String disab) {
        this.disab = disab;
    }

    public UploadedFile getUploadImage() {
        return uploadImage;
    }

    public void setUploadImage(UploadedFile uploadImage) {
        this.uploadImage = uploadImage;
    }

    public boolean isArmStatus() {
        return armStatus;
    }

    public void setArmStatus(boolean armStatus) {
        this.armStatus = armStatus;
    }

    public boolean isRelatio() {
        return relatio;
    }

    public void setRelatio(boolean relatio) {
        this.relatio = relatio;
    }

    public boolean isdStatus() {
        return dStatus;
    }

    public void setdStatus(boolean dStatus) {
        this.dStatus = dStatus;
    }

    public boolean isConfirmPanel() {
        return confirmPanel;
    }

    public void setConfirmPanel(boolean confirmPanel) {
        this.confirmPanel = confirmPanel;
    }

    public boolean isStudentPanel() {
        return studentPanel;
    }

    public void setStudentPanel(boolean studentPanel) {
        this.studentPanel = studentPanel;
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

}
