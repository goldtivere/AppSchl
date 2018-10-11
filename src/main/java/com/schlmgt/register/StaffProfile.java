/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.imgupload.UploadImagesX;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.logic.LoadPPTfile;
import com.schlmgt.login.UserDetails;
import com.schlmgt.profile.SecondaryModel;
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
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "staffProfile")
@ViewScoped
public class StaffProfile implements Serializable {
    
    private StaffModel staffModel = new StaffModel();
    private String fname;
    private String mname;
    private String lname;
    private String fullname;
    private String pnum;
    private int id;
    private int staffdd;
    private String image_name;
    private String email;
    private String staffClass;
    private String staffGrade;
    private String staffClass1;
    private String staffGrade2;
    private String year1;
    private Date doe;
    private Date dos;
    private String dateEmployed;
    private String dateStopped;
    private String highQua;
    private String address;
    private List<GradeModel> grademodels;
    private List<ClassModel> classmodel;
    private String messangerOfTruth;
    private String year;
    private List<String> term;
    private List<ModelStaff> sesTab1;
    private List<ModelStaff> sesTab;
    private ModelStaff tab = new ModelStaff();
    private String imagelink;
    private UploadedFile passport;
    private String passport_url;
    private String passportLocation;
    private String ref_number;
    private String imageLocation;
    private boolean roleAssigned;
    private boolean roleAssigned1;
    private int assignedRole;
    private String designation;
    // private String 

    public StaffProfile() {
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

    @PostConstruct
    public void init() {
        try {
            staffDetails();
            //sesTab1 = displayStaff();
            term = yearDropdown();
            
            grademodels = gradeDropdowns();
            classmodel = classDropdown();
            if (getAssignedRole() == 1) {
                setRoleAssigned(false);
                setRoleAssigned1(true);
            } else if (getAssignedRole() == 2) {
                setRoleAssigned(true);
                setRoleAssigned1(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Boolean staffNameCheck(String fname, String lname, int id) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testflname = "Select count(*) studentCount,id from user_details where first_name=? and last_name=? and is_deleted=?";
        pstmt = con.prepareStatement(testflname);
        pstmt.setString(1, fname);
        pstmt.setString(2, lname);
        pstmt.setBoolean(3, false);
        rs = pstmt.executeQuery();
        
        rs.next();
        if (rs.getInt("studentCount") == 1 && rs.getInt("id") == id) {
            return true;
        } else if (rs.getInt("studentCount") < 1 && rs.getInt("id") != id) {
            return true;
        }
        return false;
        
    }
    
    public Boolean staffPhoneCheck(String username, int id) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testflname = "Select count(*) er,id from user_details where username=? and is_deleted=?";
        pstmt = con.prepareStatement(testflname);
        pstmt.setString(1, username);
        pstmt.setBoolean(2, false);
        rs = pstmt.executeQuery();
        
        rs.next();
        if (rs.getInt("er") == 1 && rs.getInt("id") == id) {
            return true;
        } else if (rs.getInt("er") < 1 && rs.getInt("id") != id) {
            return true;
        }
        return false;
        
    }
    
    public Boolean staffCheck(String email, int id) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testflname = "Select count(*) em,id from user_details where email_address=? and is_deleted=?";
        pstmt = con.prepareStatement(testflname);
        pstmt.setString(1, email);
        pstmt.setBoolean(2, false);
        rs = pstmt.executeQuery();
        
        rs.next();
        if (rs.getInt("em") == 1 && rs.getInt("id") == id) {
            return true;
        } else if (rs.getInt("em") < 1 && rs.getInt("id") != id) {
            return true;
        }
        return false;
        
    }
    
    public void staffUpload(ActionEvent event) {
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
            String dobs = format.format(getDoe());
            con = dbConnections.mySqlDBconnection();
            if (!staffPhoneCheck(getPnum(), getId())) {
                setMessangerOfTruth("Phone Number Already Exists !!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
                cont.addCallbackParam("loggedIn", loggedIn);
            } else if (!staffCheck(getEmail(), getId())) {
                setMessangerOfTruth("Email Already Exists !!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
                cont.addCallbackParam("loggedIn", loggedIn);
            } else if (!staffNameCheck(getFname(), getLname(), getId())) {
                setMessangerOfTruth("First Name and Lastname Already Exists !!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
                cont.addCallbackParam("loggedIn", loggedIn);
            } else if (staffNameCheck(getFname(), getLname(), getId()) && staffCheck(getEmail(), getId()) && staffPhoneCheck(getPnum(), getId())) {
                String personalDetails = "update user_details set first_name=? ,middlename=?,last_name=?, username=?, email_address=?,"
                        + "highestqua=?, designation=?,address=? ,dateupdated=?,datetimeupdated=?,updatedby=?,dateemployed=? where id=?";
                
                pstmt = con.prepareStatement(personalDetails);
                
                pstmt.setString(1, getFname());
                pstmt.setString(2, getMname());
                pstmt.setString(3, getLname());
                pstmt.setString(4, getPnum());
                pstmt.setString(5, getEmail());
                pstmt.setString(6, getHighQua());
                pstmt.setString(7, getDesignation());
                pstmt.setString(8, getAddress());
                pstmt.setString(9, DateManipulation.dateAlone());
                pstmt.setString(10, DateManipulation.dateAndTime());
                pstmt.setString(11, createdby);
                pstmt.setString(12, dobs);
                pstmt.setInt(13, getId());
                pstmt.executeUpdate();
                staffDetails();
                setMessangerOfTruth("Personal Details Updated!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
                cont.addCallbackParam("loggedIn", loggedIn);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void staffUpdate(ActionEvent event) {
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
            String dobs = format.format(getDoe());
            con = dbConnections.mySqlDBconnection();
            
            String personalDetails = "update tbstaffclass set staffclass=?,staffgrade=?,year=? where id=?";
            
            pstmt = con.prepareStatement(personalDetails);
            pstmt.setString(1, tab.getStaffClass());
            pstmt.setString(2, tab.getStaffGrade());
            pstmt.setString(3, tab.getYear());
            pstmt.setInt(4, tab.getId());
            pstmt.executeUpdate();
            
            sesTab1 = displayStaff();
            setMessangerOfTruth("Class Assigned updated!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            cont.addCallbackParam("loggedIn", loggedIn);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void UpdateStaffSuspend(ActionEvent event) {
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
            String dStopped = format.format(getDos());
            con = dbConnections.mySqlDBconnection();
            
            if (getDos().before(getDoe())) {
                setMessangerOfTruth("Date Suspended Cannot be before date employed!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            } else {
                
                String personalDetails = "update user_details set datestopped=?,suspendedstatus=?,dateupdated=?,datetimeupdated=?,updatedby=?  where id=?";
                
                pstmt = con.prepareStatement(personalDetails);
                pstmt.setString(1, dStopped);
                pstmt.setBoolean(2, true);
                pstmt.setString(3, DateManipulation.dateAlone());
                pstmt.setString(4, DateManipulation.dateAndTime());
                pstmt.setString(5, createdby);
                pstmt.setInt(6, getId());
                pstmt.executeUpdate();
                
                sesTab1 = displayStaff();
                setMessangerOfTruth("Staff Suspended!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
                cont.addCallbackParam("loggedIn", loggedIn);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void staffInsert(ActionEvent event) {
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
            String dobs = format.format(getDoe());
            con = dbConnections.mySqlDBconnection();
            
            String personalDetails = "update tbstaffclass set status=? where id=?";
            
            pstmt = con.prepareStatement(personalDetails);
            
            pstmt.setBoolean(1, false);
            pstmt.setInt(2, getStaffdd());
            pstmt.executeUpdate();
            
            String nurseryInsert = "insert into tbstaffclass (staffid,staffclass,staffgrade,year,datecreated,"
                    + "datetimecreated,createdby,status) values "
                    + "(?,?,?,?,?,?,?,?)";
            
            pstmt = con.prepareStatement(nurseryInsert);
            
            pstmt.setInt(1, getId());
            pstmt.setString(2, getStaffClass());
            pstmt.setString(3, getStaffGrade());
            pstmt.setString(4, getYear());
            pstmt.setString(5, DateManipulation.dateAlone());
            pstmt.setString(6, DateManipulation.dateAndTime());
            pstmt.setString(7, createdby);
            pstmt.setBoolean(8, true);
            pstmt.executeUpdate();
            sesTab1 = displayStaff();
            setMessangerOfTruth("Staff Assigned to class!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            cont.addCallbackParam("loggedIn", loggedIn);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void staffDetails() {
        try {
            DbConnectionX dbConnections = new DbConnectionX();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            con = dbConnections.mySqlDBconnection();
            FacesContext context = FacesContext.getCurrentInstance();
            RequestContext cont = RequestContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            String studId;
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            FacesContext ctx = FacesContext.getCurrentInstance();
            StaffModel staff = (StaffModel) ctx.getExternalContext().getApplicationMap().get("staffRecords");
            //test for null...
            staffModel = staff;
//            if (userObj.getRoleAssigned() == 1) {
//                setPnum(userObj.getUser_name());
//            } else if (userObj.getRoleAssigned() == 3 && staffModel != null) {
//                setPnum(staffModel.getPnum());
//            }
//            System.out.println(userObj.getUser_name() + " and " + staffModel.getPnum() + " AGAIN " + getPnum()+" why nw "+ userObj.getRoleAssigned()
//            );

            if (staffModel != null) {
                setPnum(staffModel.getPnum());
            }
            String testguid = "Select * from user_details where username=?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setString(1, getPnum());
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                setFname(rs.getString("first_name"));
                setMname(rs.getString("middlename"));
                setLname(rs.getString("last_name"));
                System.out.println(getFname() + " two " + getAddress());
                setImagelink(rs.getString("image_name"));
                setPassportLocation(rs.getString("img_location"));
                setEmail(rs.getString("email_address"));
                setStaffClass(rs.getString("staffClass"));
                setStaffGrade(rs.getString("staffgrade"));
                setYear(rs.getString("staffYear"));
                setId(rs.getInt("id"));
                setHighQua(rs.getString("highestqua"));
                setAddress(rs.getString("address"));
                setDateEmployed(rs.getString("dateemployed"));
                setDoe(rs.getDate("dateemployed"));
                setDos(rs.getDate("datestopped"));
                setDateStopped(rs.getString("datestopped"));
                setAssignedRole(rs.getInt("roleassigned"));
                setDesignation(rs.getString("designation"));
            }
            System.out.println(getFname() + " two " + getAddress());
        } catch (NullPointerException e) {
            e.printStackTrace();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public List<ModelStaff> displayStaff() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try {
            
            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbstaffclass where staffid=? and status=? order by id desc";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, getId());
            pstmt.setBoolean(2, true);
            rs = pstmt.executeQuery();
            //
            List<ModelStaff> lst = new ArrayList<>();
            while (rs.next()) {
                
                ModelStaff coun = new ModelStaff();
                coun.setId(rs.getInt("id"));
                setStaffdd(rs.getInt("id"));
                setStaffClass1(rs.getString("staffclass"));
                setStaffGrade2(rs.getString("staffgrade"));
                setYear1(rs.getString("year"));
                coun.setStaffid(rs.getInt("staffid"));
                coun.setStaffClass(rs.getString("staffclass"));
                coun.setStaffGrade(rs.getString("staffgrade"));
                coun.setYear(rs.getString("year"));

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
                
                String previous = "update user_details set image_name=?,img_location=?, dateupdated=?,datetimeupdated=?, updatedby=? where id=?";
                
                pstmt = con.prepareStatement(previous);
                
                pstmt.setString(1, getPassport_url());
                pstmt.setString(2, getImageLocation());
                pstmt.setString(3, DateManipulation.dateAlone());
                pstmt.setString(4, DateManipulation.dateAndTime());
                pstmt.setString(5, createdby);
                pstmt.setInt(6, getId());
                pstmt.executeUpdate();
                setPassport_url("");
                setMessangerOfTruth("Image Updated!!");
                
                if (getPassportLocation() == null || getPassportLocation().isEmpty() || getPassportLocation().equalsIgnoreCase(null)) {
                    
                } else {
                    File file = new File(getPassportLocation());
                    file.delete();
                }
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
                staffDetails();
            }
            
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
            setImageLocation(uploadImagesX.getPst_loc());
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        } catch (Exception ex) {
            
            ex.printStackTrace();
            message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        }
        
    }
    
    public List<String> yearDropdown() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try {
            
            con = dbConnections.mySqlDBconnection();
            String query = "SELECT distinct year FROM yearterm";
            pstmt = con.prepareStatement(query);
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
    
    public void ontermChanges() throws Exception {
        
        term = yearDropdown();
        
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
    
    public Date getDos() {
        return dos;
    }
    
    public void setDos(Date dos) {
        this.dos = dos;
    }
    
    public String getImageLocation() {
        return imageLocation;
    }
    
    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
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
    
    public String getPassport_url() {
        return passport_url;
    }
    
    public void setPassport_url(String passport_url) {
        this.passport_url = passport_url;
    }
    
    public String getPassportLocation() {
        return passportLocation;
    }
    
    public void setPassportLocation(String passportLocation) {
        this.passportLocation = passportLocation;
    }
    
    public String getImagelink() {
        return imagelink;
    }
    
    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }
    
    public String getYear1() {
        return year1;
    }
    
    public void setYear1(String year1) {
        this.year1 = year1;
    }
    
    public String getStaffClass1() {
        return staffClass1;
    }
    
    public void setStaffClass1(String staffClass1) {
        this.staffClass1 = staffClass1;
    }
    
    public String getStaffGrade2() {
        return staffGrade2;
    }
    
    public void setStaffGrade2(String staffGrade2) {
        this.staffGrade2 = staffGrade2;
    }
    
    public int getStaffdd() {
        return staffdd;
    }
    
    public void setStaffdd(int staffdd) {
        this.staffdd = staffdd;
    }
    
    public List<ModelStaff> getSesTab() {
        return sesTab;
    }
    
    public void setSesTab(List<ModelStaff> sesTab) {
        this.sesTab = sesTab;
    }
    
    public List<ModelStaff> getSesTab1() {
        return sesTab1;
    }
    
    public void setSesTab1(List<ModelStaff> sesTab1) {
        this.sesTab1 = sesTab1;
    }
    
    public ModelStaff getTab() {
        return tab;
    }
    
    public void setTab(ModelStaff tab) {
        this.tab = tab;
    }
    
    public List<String> getTerm() {
        return term;
    }
    
    public void setTerm(List<String> term) {
        this.term = term;
    }
    
    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }
    
    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }
    
    public String getYear() {
        return year;
    }
    
    public void setYear(String year) {
        this.year = year;
    }
    
    public List<ClassModel> getClassmodel() {
        return classmodel;
    }
    
    public void setClassmodel(List<ClassModel> classmodel) {
        this.classmodel = classmodel;
    }
    
    public Date getDoe() {
        return doe;
    }
    
    public void setDoe(Date doe) {
        this.doe = doe;
    }
    
    public void onClassChange() throws Exception {
        
        grademodels = gradeDropdowns();
        
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
            
            pstmt.setString(1, getStaffClass1());
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
    
    public List<GradeModel> getGrademodels() {
        return grademodels;
    }
    
    public void setGrademodels(List<GradeModel> grademodels) {
        this.grademodels = grademodels;
    }
    
    public String getFullname() {
        return fullname;
    }
    
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public String getHighQua() {
        return highQua;
    }
    
    public void setHighQua(String highQua) {
        this.highQua = highQua;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPnum() {
        return pnum;
    }
    
    public void setPnum(String pnum) {
        this.pnum = pnum;
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
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getImage_name() {
        return image_name;
    }
    
    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getStaffClass() {
        return staffClass;
    }
    
    public void setStaffClass(String staffClass) {
        this.staffClass = staffClass;
    }
    
    public String getStaffGrade() {
        return staffGrade;
    }
    
    public void setStaffGrade(String staffGrade) {
        this.staffGrade = staffGrade;
    }
    
    public String getDateEmployed() {
        return dateEmployed;
    }
    
    public void setDateEmployed(String dateEmployed) {
        this.dateEmployed = dateEmployed;
    }
    
    public String getDateStopped() {
        return dateStopped;
    }
    
    public void setDateStopped(String dateStopped) {
        this.dateStopped = dateStopped;
    }
    
    public StaffModel getStaffModel() {
        return staffModel;
    }
    
    public void setStaffModel(StaffModel staffModel) {
        this.staffModel = staffModel;
    }
    
    public boolean isRoleAssigned() {
        return roleAssigned;
    }
    
    public void setRoleAssigned(boolean roleAssigned) {
        this.roleAssigned = roleAssigned;
    }
    
    public int getAssignedRole() {
        return assignedRole;
    }
    
    public void setAssignedRole(int assignedRole) {
        this.assignedRole = assignedRole;
    }
    
    public String getDesignation() {
        return designation;
    }
    
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    
    public boolean isRoleAssigned1() {
        return roleAssigned1;
    }
    
    public void setRoleAssigned1(boolean roleAssigned1) {
        this.roleAssigned1 = roleAssigned1;
    }
    
}
