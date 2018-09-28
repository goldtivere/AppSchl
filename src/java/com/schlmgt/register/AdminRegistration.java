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
import com.schlmgt.school.SchoolGetterMethod;
import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "adminReg")
@ViewScoped
public class AdminRegistration implements Serializable {

    private String fname;
    private String mname;
    private String lname;
    private String pnum;
    private String email;
    private String highestQua;
    private String address;
    private UploadedFile passport_file;
    private String messangerOfTruth;
    private String passport_url;
    private String ref_number;
    private String imageLocation;
    private Date dateEmployed;
    private String designation;
    private String schools;
    private int schoolId;
    private SchoolGetterMethod schlGetterMethod = new SchoolGetterMethod();

    @PostConstruct
    public void init() {
        try {

            FacesContext ctx = FacesContext.getCurrentInstance();
            FacesMessage msg;
            String stuValue = (String) ctx.getExternalContext().getApplicationMap().get("regDet");

            //test for null...
            if (stuValue != null) {

                setSchools(schlGetterMethod.tableNameDisplay(stuValue));
                setSchoolId(schlGetterMethod.tableNameDisplayValue(stuValue).getId());
            } else {
                setMessangerOfTruth("Please click on register and select name of school and registration type to proceed!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                ctx.addMessage(null, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AdminRegistration() {
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
            setPassport_file(null);
            passport_file = null;
            setPassport_url("");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleFileUpload(FileUploadEvent event) {

        setPassport_file(event.getFile());
        setPassport_url("");
        setImageLocation("");

        //byte fileNameByte[] = getFile().getContents();
        //System.out.println("fileNameByte:" + fileNameByte);
        FacesMessage message;
        UploadImagesX uploadImagesX = new UploadImagesX();

        try {

            if (!(uploadImagesX.uploadImg(getPassport_file(), "pix".concat(String.valueOf(getRef_number()))))) {

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

    public void refresh() {
        setFname("");
        setLname("");
        setPnum("");
        setMname("");
        setAddress("");
        setHighestQua("");
        setEmail("");
        setDesignation("");
        setDateEmployed(null);
        setSchools("");
        setPassport_url("");

    }

    public void register() {

        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;

        UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
        String on = String.valueOf(userObj);

        if (userObj == null) {
            setMessangerOfTruth("Expired Session, pleasere - login " + on);
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    getMessangerOfTruth(), getMessangerOfTruth()
            );
            context.addMessage(null, msg);
        }

        String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
        String createdId
                = String.valueOf(userObj.getId());
        String roleId
                = String.valueOf(userObj.getRole_id());

        con = dbConnections.mySqlDBconnection();
        UUID idOne = UUID.randomUUID();

        try {

            String testflname = "Select * from user_details where first_name=? and last_name=? and is_deleted=?";
            pstmt = con.prepareStatement(testflname);
            pstmt.setString(1, getFname());
            pstmt.setString(2, getLname());
            pstmt.setBoolean(3, false);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                setMessangerOfTruth("First Name and Last Name Aleady exists!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);

            } else {

                String testusername = "Select * from user_details where username=? and is_deleted=?";
                pstmt = con.prepareStatement(testusername);
                pstmt.setString(1, getPnum());
                pstmt.setBoolean(2, false);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    setMessangerOfTruth("Phone Number Aleady exists!!");
                    msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, msg);

                } else {

                    String testemail = "Select * from user_details where email_address=? and is_deleted=?";
                    pstmt = con.prepareStatement(testemail);
                    pstmt.setString(1, getEmail());
                    pstmt.setBoolean(2, false);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        setMessangerOfTruth("Email Aleady exists!!");
                        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, msg);

                    } else {

                        con.setAutoCommit(false);
                        //InputStream fin2 = file.getInputstream();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        String does = format.format(getDateEmployed());
                        String insert = "insert into user_details (first_name,middlename,last_name,username,image_name,img_location,email_address,role_id,"
                                + "date_created,date_time_created,created_by,is_deleted,designation,highestqua,address,dateemployed,suspendedstatus,Roleassigned,"
                                + "canupdateresult,canupdatesubject,canregisterstaff,canregisterstudent,cansendtext,canregisteradmin,school_name) "
                                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                        pstmt = con.prepareStatement(insert);

                        pstmt.setString(1, getFname());
                        pstmt.setString(2, getMname());
                        pstmt.setString(3, getLname());
                        pstmt.setString(4, getPnum());
                        pstmt.setString(5, getPassport_url());
                        pstmt.setString(6, getImageLocation());
                        pstmt.setString(7, getEmail());
                        pstmt.setString(8, createdId);
                        pstmt.setString(9, DateManipulation.dateAlone());
                        pstmt.setString(10, DateManipulation.dateAndTime());
                        pstmt.setString(11, createdby);
                        pstmt.setBoolean(12, false);
                        pstmt.setString(13, getDesignation());
                        pstmt.setString(14, getHighestQua());
                        pstmt.setString(15, getAddress());
                        pstmt.setString(16, does);
                        pstmt.setBoolean(17, false);
                        pstmt.setInt(18, 3);
                        pstmt.setBoolean(19, false);
                        pstmt.setBoolean(20, false);
                        pstmt.setBoolean(21, false);
                        pstmt.setBoolean(22, false);
                        pstmt.setBoolean(23, false);
                        pstmt.setBoolean(24, false);
                        pstmt.setInt(25, getSchoolId());

                        pstmt.executeUpdate();

                        String fullname = getLname() + " " + getFname();
                        String slink = "http://localhost:8084/AppSchl/faces/pages/createStaff/index.xhtml?id=";
                        String insertemail = "insert into staffstatus (guid,fullname,status,datelogged,staffemail,datetime,staffphone,link)"
                                + "values(?,?,?,?,?,?,?,?)";

                        pstmt = con.prepareStatement(insertemail);
                        pstmt.setString(1, idOne.toString());
                        pstmt.setString(2, fullname);
                        pstmt.setBoolean(3, false);
                        pstmt.setString(4, DateManipulation.dateAlone());
                        pstmt.setString(5, getEmail());
                        pstmt.setString(6, DateManipulation.dateAndTime());
                        pstmt.setString(7, getPnum());
                        pstmt.setString(8, slink + idOne.toString());

                        pstmt.executeUpdate();

                        /**
                         * MailSender send = new MailSender();
                         * send.sendMail(user, pass, getEmailadd(), sub,
                         * content);*
                         */
                        setMessangerOfTruth("User Created!!");
                        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, msg);

                        refresh();

                        con.commit();
                    }
                }
            }

        } catch (NullPointerException e) {
            setMessangerOfTruth("Please go to register and select a school to register an Admin under!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            
        } catch (Exception e) {
            e.printStackTrace();

        }

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

    public String getHighestQua() {
        return highestQua;
    }

    public void setHighestQua(String highestQua) {
        this.highestQua = highestQua;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UploadedFile getPassport_file() {
        return passport_file;
    }

    public void setPassport_file(UploadedFile passport_file) {
        this.passport_file = passport_file;
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

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public Date getDateEmployed() {
        return dateEmployed;
    }

    public void setDateEmployed(Date dateEmployed) {
        this.dateEmployed = dateEmployed;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSchools() {
        return schools;
    }

    public void setSchools(String schools) {
        this.schools = schools;
    }

    public SchoolGetterMethod getSchlGetterMethod() {
        return schlGetterMethod;
    }

    public void setSchlGetterMethod(SchoolGetterMethod schlGetterMethod) {
        this.schlGetterMethod = schlGetterMethod;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

}
