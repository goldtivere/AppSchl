/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.register;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.filter.ThreadRunnerEmail;
import com.schlmgt.imgupload.UploadImagesX;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.login.UserDetails;
import com.schlmgt.school.SchoolManagementModel;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gold
 */
@ManagedBean
@ViewScoped
public class Registration implements Serializable {

    private String registration;
    private String regType;
    private List<String> typeValue = new ArrayList<>();
    private boolean studentStatus;
    private boolean staffStatus;
    private boolean but;
    private boolean put;
    private Boolean loi;
    private UploadedFile csv;
    private String messangerOfTruth;
    private boolean lut;
    private boolean staff;
    private boolean student;
    private boolean admin;
    private boolean staffStudent;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    DbConnectionX dbConnections = new DbConnectionX();
    private Map<String, String> roleGetter;
    private String school;

    @PostConstruct
    public void init() {
        studentStatus = false;
        staffStatus = false;
        but = false;
        lut = false;
        put = false;

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
            System.out.println(getSchool() + " hi");
            FacesContext context = FacesContext.getCurrentInstance();
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");

            if (userObj.getRoleAssigned() == 3) {
                setAdmin(true);

            }
            if (userObj.getRoleAssigned() == 2 && userObj.isCanRegisterStaff() && !userObj.isCanRegisterStudent()) {

                setStaff(true);

            }
            if (userObj.getRoleAssigned() == 2 && userObj.isCanRegisterStudent() && !userObj.isCanRegisterStaff()) {

                setStudent(true);
            }
            if (userObj.getRoleAssigned() == 2 && userObj.isCanRegisterStudent() && userObj.isCanRegisterStaff()) {

                setStaffStudent(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void regStudent() throws Exception {
        FacesContext ctx = FacesContext.getCurrentInstance();
        NavigationHandler nav = ctx.getApplication().getNavigationHandler();
        ctx.getExternalContext().getApplicationMap().remove("regDet");
        ctx.getExternalContext().getApplicationMap().put("regDet", getSchool());
        String url = "registerStudent.xhtml?faces-redirect=true";
        nav.handleNavigation(ctx, null, url);
        ctx.renderResponse();

    }

    public void regAdmin() throws Exception {
        FacesContext ctx = FacesContext.getCurrentInstance();
        NavigationHandler nav = ctx.getApplication().getNavigationHandler();
        ctx.getExternalContext().getApplicationMap().remove("regDet");
        ctx.getExternalContext().getApplicationMap().put("regDet", getSchool());
        String url = "registerAdmin.xhtml?faces-redirect=true";
        nav.handleNavigation(ctx, null, url);
        ctx.renderResponse();

    }

    public String regStaff() throws Exception {

        return "register.xhtml?faces-redirect=true";

    }

    public void regTypeChanges() {
        if ("1".equalsIgnoreCase(getRegistration()) && "Data upload".equalsIgnoreCase(getRegType())) {
            setStaffStatus(true);
            setBut(false);
            setPut(false);
            setLut(false);
        } else if ("2".equalsIgnoreCase(getRegistration()) && "Data upload".equalsIgnoreCase(getRegType())) {
            setStaffStatus(true);
            setBut(false);
            setPut(false);
            setLut(false);
        } else if ("1".equalsIgnoreCase(getRegistration()) && "Data Entry".equalsIgnoreCase(getRegType())) {
            setStaffStatus(false);
            setBut(true);
            setPut(false);
            setLut(false);
        } else if ("2".equalsIgnoreCase(getRegistration()) && "Data entry".equalsIgnoreCase(getRegType())) {
            setStaffStatus(false);
            setBut(false);
            setPut(true);
            setLut(false);
        } else if ("3".equalsIgnoreCase(getRegistration()) && "Data entry".equalsIgnoreCase(getRegType())) {
            setLut(true);
            setStaffStatus(false);
            setBut(false);
            setPut(false);

        }
    }

    public void staffUpload(FileUploadEvent event) throws SQLException {
        FacesMessage msg;
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        UploadImagesX uploadImagesX = new UploadImagesX();
        try {
            con = dbConnections.mySqlDBconnection();
            FreshReg reg = new FreshReg();
            StaffModel mode = new StaffModel();
            Register rg = new Register();
            List<String> studentDetails = new ArrayList<>();
            studentDetails.add("FirstName");
            studentDetails.add("MiddleName");
            studentDetails.add("LastName");
            studentDetails.add("EmailAddress");
            studentDetails.add("PhoneNumber");
            studentDetails.add("HighestQualification");
            studentDetails.add("Address");
            studentDetails.add("StaffClass");
            studentDetails.add("StaffGrade");
            studentDetails.add("DateEmployed");
            studentDetails.add("StaffYear");

            InputStream mn = event.getFile().getInputstream();
            XSSFWorkbook wb = new XSSFWorkbook(mn);
            XSSFSheet ws = wb.getSheetAt(0);
            Row row;
            row = (Row) ws.getRow(0);
            int rowNum = ws.getLastRowNum() + 1;
            int val = 0;
            int studentId;

            String fullname = null;
            String gfullname = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String does = null;
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            String createdId
                    = String.valueOf(userObj.getId());
            DataFormatter df = new DataFormatter();
            int success = 0;
            int fail = 0;
            if (studentDetails.size() == row.getPhysicalNumberOfCells()) {
                for (int i = 0; i < studentDetails.size(); i++) {
                    if (row.getCell(i).toString().equalsIgnoreCase(studentDetails.get(i))) {
                        val++;
                    } else {
                        setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, message);
                        break;
                    }
                }

                if (val == row.getPhysicalNumberOfCells()) {

//                    if (getPnum().isEmpty() || getPnum().equals("")) {
//                        setPnum(null);
//                    }
//                    if (getEmail().isEmpty() || getEmail().equals("")) {
//                        setEmail(null);
//                    }
//                    if (getGpnum().isEmpty() || getGpnum().equals("")) {
//                        setGpnum(null);
//                    }
//                    if (getGemail().isEmpty() || getGemail().equals("")) {
//                        setGemail(null);
//                    }
                    Row ro = null;
                    for (int i = 1; i < rowNum; i++) {
                        ro = (Row) ws.getRow(i);
                        if (ro.getCell(0) != null) {
                            mode.setFname(ro.getCell(0).getStringCellValue());
                        } else {
                            setMessangerOfTruth("First Name is required: Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }

                        if (ro.getCell(1) != null) {
                            mode.setMname(ro.getCell(1).getStringCellValue());
                        } else {
                            mode.setMname(null);
                        }

                        if (ro.getCell(2) != null) {
                            mode.setLname(ro.getCell(2).getStringCellValue());
                        } else {
                            setMessangerOfTruth("Last Name is required: Row " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (ro.getCell(3) != null) {
                            mode.setEmail(ro.getCell(3).getStringCellValue());
                        } else {
                            setMessangerOfTruth("Email Address is required: Row " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (ro.getCell(4) != null) {
                            mode.setPnum(df.formatCellValue(ro.getCell(4)));
                        } else {
                            setMessangerOfTruth("Phone Number is required: Row " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (ro.getCell(5) != null) {
                            mode.setHighQua(ro.getCell(5).getStringCellValue());
                        } else {
                            mode.setHighQua(null);
                        }
                        if (ro.getCell(6) != null) {
                            mode.setAddress(ro.getCell(6).getStringCellValue());
                        } else {
                            mode.setAddress(null);
                        }
                        if (ro.getCell(7) != null) {
                            mode.setStaffClass((int) ro.getCell(7).getNumericCellValue());
                        } else {
                            setMessangerOfTruth("Staff Class is required: Row " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (ro.getCell(8) != null) {
                            mode.setStaffGrade((int) ro.getCell(8).getNumericCellValue());
                        } else {
                            setMessangerOfTruth("Staff Grade is required: Row " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (ro.getCell(9) != null) {
                            mode.setDoe(ro.getCell(9).getDateCellValue());
                            does = format.format(mode.getDoe());
                        } else {
                            setMessangerOfTruth("Date employed is required: Row " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (ro.getCell(10) != null) {
                            mode.setYear(df.formatCellValue(ro.getCell(10)));
                        } else {
                            setMessangerOfTruth("Staff Year is required. Row: " + i);
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        if (mode.getStaffClass() != 1 && mode.getStaffClass() != 2 && mode.getStaffClass() != 3) {
                            setMessangerOfTruth("Staff Class field should be either ; 1(Nursery), 2(Primary) or 3(Secondary): Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if (mode.getStaffClass() == 1 && (mode.getStaffGrade() != 1 && mode.getStaffGrade() != 2 && mode.getStaffGrade() != 3)) {
                            setMessangerOfTruth("Staff Class 1(Nursery) must have current grade 1(Nursery 1),2(Nursery 2) or 3(Nursery 3): Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if (mode.getStaffClass() == 2 && (mode.getStaffGrade() != 4 && mode.getStaffGrade() != 5 && mode.getStaffGrade() != 6 && mode.getStaffGrade() != 7 && mode.getStaffGrade() != 8)) {
                            setMessangerOfTruth("Staff Class 2(Primary) must have current grade 4(Primary 1),5(Primary 2), 6(Primary 3), 7(Primary 4) or 8(Primary 5): Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else if (mode.getStaffClass() == 3 && (mode.getStaffGrade() != 9 && mode.getStaffGrade() != 10 && mode.getStaffGrade() != 11 && mode.getStaffGrade() != 12 && mode.getStaffGrade() != 13 && mode.getStaffGrade() != 14)) {
                            setMessangerOfTruth("Staff Class Secondary must have previous grade 9(Jss 1),10(Jss 2),11(Jss 3), 12(SS 1),13(SS 2) or 14 (SS 3): Row " + (i + 1));
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        }
                        String testflname = "Select * from user_details where first_name=? and last_name=? and is_deleted=?";
                        pstmt = con.prepareStatement(testflname);
                        pstmt.setString(1, mode.getFname());
                        pstmt.setString(2, mode.getLname());
                        pstmt.setBoolean(3, false);
                        rs = pstmt.executeQuery();
                        if (rs.next()) {
                            setMessangerOfTruth("First Name and Last Name Aleady exists!!");
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            break;
                        } else {

                            String testusername = "Select * from user_details where username=? and is_deleted=?";
                            pstmt = con.prepareStatement(testusername);
                            pstmt.setString(1, mode.getPnum());
                            pstmt.setBoolean(2, false);
                            rs = pstmt.executeQuery();
                            if (rs.next()) {
                                setMessangerOfTruth("Phone Number Aleady exists!!");
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else {

                                String testemail = "Select * from user_details where email_address=? and is_deleted=?";
                                pstmt = con.prepareStatement(testemail);
                                pstmt.setString(1, mode.getEmail());
                                pstmt.setBoolean(2, false);
                                rs = pstmt.executeQuery();
                                if (rs.next()) {
                                    setMessangerOfTruth("Email Aleady exists!!");
                                    msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                    context.addMessage(null, msg);
                                    break;

                                } else {
                                    UUID idOne = UUID.randomUUID();
                                    //InputStream fin2 = file.getInputstream();                                    
                                    String insert = "insert into user_details (first_name,middlename,last_name,username,email_address,role_id,"
                                            + "date_created,date_time_created,created_by,is_deleted,staffclass,staffgrade,staffyear,highestqua,address,dateemployed,suspendedstatus,roleassigned,"
                                            + "canupdateresult,canupdatesubject,canregisterstudent,canregisterstaff,cansenttext,canregisteradmin) "
                                            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                                    pstmt = con.prepareStatement(insert);

                                    pstmt.setString(1, mode.getFname());
                                    pstmt.setString(2, mode.getMname());
                                    pstmt.setString(3, mode.getLname());
                                    pstmt.setString(4, mode.getPnum());
                                    pstmt.setString(5, mode.getEmail());
                                    pstmt.setString(6, createdId);
                                    pstmt.setString(7, DateManipulation.dateAlone());
                                    pstmt.setString(8, DateManipulation.dateAndTime());
                                    pstmt.setString(9, createdby);
                                    pstmt.setBoolean(10, false);
                                    pstmt.setString(11, String.valueOf(mode.getStaffClass()));
                                    pstmt.setString(12, String.valueOf(mode.getStaffGrade()));
                                    pstmt.setString(13, mode.getYear());
                                    pstmt.setString(14, mode.getHighQua());
                                    pstmt.setString(15, mode.getAddress());
                                    pstmt.setString(16, does);
                                    pstmt.setBoolean(17, false);
                                    pstmt.setInt(18, 1);
                                    pstmt.setBoolean(19, false);
                                    pstmt.setBoolean(20, false);
                                    pstmt.setBoolean(21, false);
                                    pstmt.setBoolean(22, false);
                                    pstmt.setBoolean(23, false);
                                    pstmt.setBoolean(24, false);

                                    pstmt.executeUpdate();

                                    String fullnames = mode.getLname() + " " + mode.getFname();
                                    String slink = "http://localhost:8080/SchlMgt/faces/pages/createStaff/index.xhtml?id=";
                                    String insertemail = "insert into " + getSchool() + "_staffstatus (guid,fullname,status,datelogged,staffemail,datetime,staffphone,link)"
                                            + "values(?,?,?,?,?,?,?,?)";

                                    pstmt = con.prepareStatement(insertemail);
                                    pstmt.setString(1, idOne.toString());
                                    pstmt.setString(2, fullname);
                                    pstmt.setBoolean(3, false);
                                    pstmt.setString(4, DateManipulation.dateAlone());
                                    pstmt.setString(5, mode.getEmail());
                                    pstmt.setString(6, DateManipulation.dateAndTime());
                                    pstmt.setString(7, mode.getPnum());
                                    pstmt.setString(8, slink + idOne.toString());

                                    pstmt.executeUpdate();
                                    rg.classUpload(rg.staffIdCheck(), String.valueOf(mode.getStaffClass()), String.valueOf(mode.getStaffGrade()), mode.getYear(), createdby);
                                    success++;
                                    /**
                                     * MailSender send = new MailSender();
                                     * send.sendMail(user, pass, getEmailadd(),
                                     * sub, content);*
                                     */
                                }
                            }
                        }

                    }
                    setMessangerOfTruth(success + " Staff Data Upload Successful");
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                } else {
                    setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                }
            } else {
                setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, message);
            }
        } catch (IllegalStateException e) {
            setMessangerOfTruth("Please check that phone number and sex is in the correct format");
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, message);
        } catch (Exception exx) {
            exx.printStackTrace();
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

    public int studentIdCheck(Connection con) throws SQLException {

        String testflname = "Select * from " + getSchool() + "_student_details order by id DESC LIMIT 1";
        pstmt = con.prepareStatement(testflname);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("id");
        }
        return 0;
    }

    public boolean studentEmailCheck(String email, String gmail, Connection con) throws SQLException {
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

    public boolean studentEmailCheckName(String gmail, String fname, String mname, String lname, Connection con) throws SQLException {
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

    public boolean studentPhoneCheck(String pnum, String gpnum, Connection con) throws SQLException {
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

    public boolean studentPhoneCheckName(String gpnum, String fname, String mname, String lname, Connection con) throws SQLException {
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

    public boolean studentNameCheck(String fname, String lname, Connection con) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void studentUpload(FileUploadEvent event) throws SQLException {
        FacesMessage msg;
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        UploadImagesX uploadImagesX = new UploadImagesX();
        try {
            con = dbConnections.mySqlDBconnection();
            StudentModel mode = new StudentModel();
            List<String> studentDetails = new ArrayList<>();
            studentDetails.add("SFirstName");
            studentDetails.add("SMiddleName");
            studentDetails.add("SLastName");
            studentDetails.add("SDateOfBirth");
            studentDetails.add("SPhoneNumber");
            studentDetails.add("SEmailAddress");
            studentDetails.add("Sex");
            studentDetails.add("PFirstName");
            studentDetails.add("PMiddleName");
            studentDetails.add("PLastName");
            studentDetails.add("PhoneNumber");
            studentDetails.add("EmailAddress");
            studentDetails.add("Address");
            studentDetails.add("PreviousEdu");
            studentDetails.add("PrevClass");
            studentDetails.add("PrevGrade");
            studentDetails.add("CurrentClass");
            studentDetails.add("CurrentGrade");
            studentDetails.add("Arm");
            studentDetails.add("Term");
            studentDetails.add("Year");

            InputStream mn = event.getFile().getInputstream();
            XSSFWorkbook wb = new XSSFWorkbook(mn);
            XSSFSheet ws = wb.getSheetAt(0);
            Row row;
            row = (Row) ws.getRow(0);
            int rowNum = ws.getLastRowNum() + 1;
            int val = 0;
            int studentId;

            String fullname = null;
            String gfullname = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String dob = null;
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            DataFormatter df = new DataFormatter();
            int success = 0;
            int fail = 0;
            if (studentDetails.size() == row.getPhysicalNumberOfCells()) {
                for (int i = 0; i < studentDetails.size(); i++) {
                    if (row.getCell(i).toString().equalsIgnoreCase(studentDetails.get(i))) {
                        val++;
                    } else {
                        setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, message);
                        break;
                    }
                }

                if (val == row.getPhysicalNumberOfCells()) {

                    if (studentIdCheck(con) == 0) {
                        studentId = 0 + 1;
                    } else {
                        studentId = studentIdCheck(con) + 1;
                    }

//                    if (getPnum().isEmpty() || getPnum().equals("")) {
//                        setPnum(null);
//                    }
//                    if (getEmail().isEmpty() || getEmail().equals("")) {
//                        setEmail(null);
//                    }
//                    if (getGpnum().isEmpty() || getGpnum().equals("")) {
//                        setGpnum(null);
//                    }
//                    if (getGemail().isEmpty() || getGemail().equals("")) {
//                        setGemail(null);
//                    }
                    Row ro = null;
                    for (int i = 1; i < rowNum; i++) {
                        try {
                            ro = (Row) ws.getRow(i);
                            if (ro.getCell(0) != null) {
                                mode.setFname(ro.getCell(0).getStringCellValue());
                            } else {
                                setMessangerOfTruth("Student First Name is required Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            }

                            if (ro.getCell(1) != null) {
                                mode.setMname(ro.getCell(1).getStringCellValue());
                            } else {
                                mode.setMname(null);
                            }

                            if (ro.getCell(2) != null) {
                                mode.setLname(ro.getCell(2).getStringCellValue());
                            } else {
                                setMessangerOfTruth("Student Last Name is required Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            }

                            if (ro.getCell(3) != null) {
                                mode.setDob(ro.getCell(3).getDateCellValue());
                                dob = format.format(mode.getDob());
                            } else {
                                mode.setDob(null);
                            }

                            if (ro.getCell(4) != null) {
                                mode.setPnum(df.formatCellValue(ro.getCell(4)));
                            } else {
                                mode.setPnum(null);
                            }

                            if (ro.getCell(5) != null) {
                                mode.setEmail(ro.getCell(5).getStringCellValue());
                            } else {
                                mode.setEmail(null);
                            }

                            if (ro.getCell(6) != null) {
                                mode.setSex((int) ro.getCell(6).getNumericCellValue());
                            } else {
                                setMessangerOfTruth("Sex should be 1(male) or 2(female): Row " + (i + 1));
                                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, message);
                                break;
                            }

                            if (ro.getCell(7) != null) {
                                mode.setPfname(ro.getCell(7).getStringCellValue());
                            } else {
                                setMessangerOfTruth("Parent First Name is required Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            }
                            if (ro.getCell(8) != null) {
                                mode.setPmname(ro.getCell(8).getStringCellValue());

                            } else {
                                mode.setPmname(null);
                            }

                            if (ro.getCell(9) != null) {
                                mode.setPlname(ro.getCell(9).getStringCellValue());
                            } else {
                                setMessangerOfTruth("Parent Last Name is required Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                            }

                            if (ro.getCell(10) != null) {
                                mode.setPpnum(df.formatCellValue(ro.getCell(10)));
                            } else {
                                setMessangerOfTruth("Parent Phone Number is required. Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                            }

                            if (ro.getCell(11) != null) {
                                mode.setPemail(ro.getCell(11).getStringCellValue());
                            } else {
                                setMessangerOfTruth("Parent Email Number is required. Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                            }

                            if (ro.getCell(12) != null) {
                                mode.setAddress(ro.getCell(12).getStringCellValue());
                            } else {
                                mode.setAddress(null);
                            }

                            if (ro.getCell(13) != null) {
                                mode.setPreviousEdu(ro.getCell(13).getStringCellValue());
                            } else {
                                mode.setPreviousEdu(null);
                            }

                            if (ro.getCell(14) != null) {
                                mode.setPreviousClass((int) ro.getCell(14).getNumericCellValue());
                            } else {
                                setMessangerOfTruth("Previous class field is required: Row " + (i + 1));
                                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, message);
                                break;
                            }

                            if (ro.getCell(15) != null) {
                                mode.setPreviousGrade((int) ro.getCell(15).getNumericCellValue());
                            } else {
                                setMessangerOfTruth("Previous Grade field is required: Row " + (i + 1));
                                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, message);
                                break;
                            }

                            if (ro.getCell(16) != null) {
                                mode.setCurrentClass((int) ro.getCell(16).getNumericCellValue());
                            } else {
                                setMessangerOfTruth("Current class field is required: " + studentDetails.toString());
                                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, message);
                                break;
                            }

                            if (ro.getCell(17) != null) {
                                mode.setCurrentGrade((int) ro.getCell(17).getNumericCellValue());
                            } else {
                                setMessangerOfTruth("Cuurent Grade field is required: Row " + (i + 1));
                                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, message);
                                break;
                            }

                            if (ro.getCell(18) != null) {
                                mode.setArm(ro.getCell(18).getStringCellValue());
                            } else {
                                setMessangerOfTruth("Arm is required:Row " + (i + 1));
                                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, message);
                                break;
                            }

                            if (ro.getCell(19) != null) {
                                mode.setTerm((int) ro.getCell(19).getNumericCellValue());
                            } else {
                                setMessangerOfTruth("Term field is required: Row " + (i + 1));
                                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, message);
                                break;
                            }

                            if (ro.getCell(20) != null) {
                                mode.setYear(df.formatCellValue(ro.getCell(20)));
                            } else {
                                setMessangerOfTruth("Year field is required: Row " + (i + 1));
                                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, message);
                                break;
                            }
                            fullname = mode.getLname() + " " + mode.getMname() + " " + mode.getFname();
                            gfullname = mode.getPlname() + " " + mode.getPmname() + " " + mode.getPfname();
                            if (studentNameCheck(mode.getFname(), mode.getLname(), con)) {

                                setMessangerOfTruth("Firstname: " + mode.getFname() + " and Lastname: " + mode.getLname() + " exists in row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (mode.getSex() != 1 && mode.getSex() != 2) {

                                setMessangerOfTruth("Sex is either 1(male) or 2(female): Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if ((mode.getFname().isEmpty() || mode.getFname() == null) || (mode.getLname().isEmpty() || mode.getLname() == null)) {
                                setMessangerOfTruth("Student FirstName and LastName is required: Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if ((mode.getPfname().isEmpty() || mode.getPfname() == null) || (mode.getPlname().isEmpty() || mode.getPlname() == null)) {
                                setMessangerOfTruth("Parent FirstName and LastName is required: Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (mode.getPpnum().isEmpty() || mode.getPpnum() == null) {
                                setMessangerOfTruth("Parent Phone Number is required: Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (mode.getPemail().isEmpty() || mode.getPemail() == null) {
                                setMessangerOfTruth("Parent Email Address is required: Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (mode.getPreviousClass() != 1 && mode.getPreviousClass() != 2 && mode.getPreviousClass() != 3) {
                                setMessangerOfTruth("Previous Class field should be either ; 1(Nursery), 2(Primary) or 3(Secondary): Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (mode.getCurrentClass() != 1 && mode.getCurrentClass() != 2 && mode.getCurrentClass() != 3) {
                                setMessangerOfTruth("Current Class field should be either ; 1(Nursery), 2(Primary) or 3(Secondary): Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (mode.getCurrentClass() == 1 && (mode.getCurrentGrade() != 1 && mode.getCurrentGrade() != 2 && mode.getCurrentGrade() != 3)) {
                                setMessangerOfTruth("Current Class 1(Nursery) must have current grade 1(Nursery 1),2(Nursery 2) or 3(Nursery 3): Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (mode.getCurrentClass() == 2 && (mode.getCurrentGrade() != 4 && mode.getCurrentGrade() != 5 && mode.getCurrentGrade() != 6 && mode.getCurrentGrade() != 7 && mode.getCurrentGrade() != 8)) {
                                setMessangerOfTruth("Current Class 2(Primary) must have current grade 4(Primary 1),5(Primary 2), 6(Primary 3), 7(Primary 4) or 8(Primary 5): Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (mode.getPreviousClass() == 1 && (mode.getPreviousGrade() != 1 && mode.getPreviousGrade() != 2 && mode.getPreviousGrade() != 3)) {
                                setMessangerOfTruth("Previous Class 1(Nursery) must have current grade 1(Nursery 1), 2(Nursery 2) or 3(Nursery 3): Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (mode.getPreviousClass() == 2 && (mode.getPreviousGrade() != 4 && mode.getPreviousGrade() != 5 && mode.getPreviousGrade() != 6 && mode.getPreviousGrade() != 7 && mode.getPreviousGrade() != 8)) {
                                setMessangerOfTruth("Previous Class 2(Primary) must have current grade 4(Primary 1),5(Primary 2), 6(Primary 3), 7(Primary 4) or 8(Primary 5): Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (mode.getPreviousClass() == 3 && (mode.getPreviousGrade() != 9 && mode.getPreviousGrade() != 10 && mode.getPreviousGrade() != 11 && mode.getPreviousGrade() != 12 && mode.getPreviousGrade() != 13 && mode.getPreviousGrade() != 14)) {
                                setMessangerOfTruth("Previous Class Secondary must have previous grade 9(Jss 1),10(Jss 2),11(Jss 3), 12(SS 1),13(SS 2) or 14 (SS 3): Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (mode.getCurrentClass() == 3 && (mode.getCurrentGrade() != 9 && mode.getCurrentGrade() != 10 && mode.getCurrentGrade() != 11 && mode.getCurrentGrade() != 12 && mode.getCurrentGrade() != 13 && mode.getCurrentGrade() != 14)) {
                                setMessangerOfTruth("Current Class Secondary must have current grade 9(Jss 1),10(Jss 2),11(Jss 3), 12(SS 1),13(SS 2) or 14 (SS 3): Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (mode.getTerm() != 1 && mode.getTerm() != 2 && mode.getTerm() != 3) {
                                setMessangerOfTruth("Term must be either; 1(First Term),2(Second Term),3(Third Term). Row: " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (studentEmailCheck(mode.getEmail(), mode.getPemail(), con) && !studentEmailCheckName(mode.getPemail(), mode.getPfname(), mode.getPmname(), mode.getPlname(), con)) {
                                setMessangerOfTruth("Email Already Exist!! Please enter a different email. Row: " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (studentPhoneCheck(mode.getPnum(), mode.getPpnum(), con) && !studentPhoneCheckName(mode.getPpnum(), mode.getPfname(), mode.getPmname(), mode.getPlname(), con)) {
                                setMessangerOfTruth("Phone Number Already Exist!! Please enter a different Phone Number. Row: " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else {
                                createStudent(mode, fullname, dob, gfullname, createdby, studentId);
                                studentId++;
                                success++;
                            }
                        } catch (IllegalStateException e) {
                            setMessangerOfTruth("Please check that phone number and sex and Date of Birth is in the correct format. Row " + (i + 1));
                            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, message);
                            break;
                        }
                    }
                    setMessangerOfTruth(success + " Student(s) Data Upload Successful");
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                } else {
                    setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                }
            } else {
                setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + studentDetails.toString());
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, message);
            }
        } catch (IllegalStateException e) {

            e.printStackTrace();
        } catch (Exception exx) {
            exx.printStackTrace();
        }
    }

    public void createStudent(StudentModel mode, String fullname, String dob, String gfullname, String createdby, int studentId) {

        try {
            con = dbConnections.mySqlDBconnection();
            UUID idOne = UUID.randomUUID();
            String insertStudentDetails = "insert into " + getSchool() + "_student_details"
                    + "(first_name,middle_name,last_name,fullname,DOB,student_phone,student_email,sex,Guardian_firstname,"
                    + "Guardian_middlename,Guardian_lastname,Guardian_fullname,Guardian_phone,"
                    + "Guardian_email,guardian_address,previous_school,"
                    + "previous_class,previous_grade,current_class,current_grade,Arm,created_by,"
                    + "date_created,datetime_created,is_deleted,studentId)"
                    + "values"
                    + "(?,?,?,?,?,"
                    + "?,?,?,?,?,"
                    + "?,?,?,?,?,?,"
                    + "?,?,?,?,?,"
                    + "?,?,?,?,?)";
            pstmt = con.prepareStatement(insertStudentDetails);
            pstmt.setString(1, mode.getFname());
            pstmt.setString(2, mode.getMname());
            pstmt.setString(3, mode.getLname());
            pstmt.setString(4, fullname);
            pstmt.setString(5, dob);
            pstmt.setString(6, mode.getPnum());
            pstmt.setString(7, mode.getEmail());
            pstmt.setString(8, String.valueOf(mode.getSex()));
            pstmt.setString(9, mode.getPfname());
            pstmt.setString(10, mode.getPmname());
            pstmt.setString(11, mode.getPlname());
            pstmt.setString(12, gfullname);
            pstmt.setString(13, mode.getPpnum());
            pstmt.setString(14, mode.getPemail());
            pstmt.setString(15, mode.getAddress());
            pstmt.setString(16, mode.getPreviousEdu());
            pstmt.setString(17, String.valueOf(mode.getPreviousClass()));
            pstmt.setString(18, String.valueOf(mode.getPreviousGrade()));
            pstmt.setString(19, String.valueOf(mode.getCurrentClass()));
            pstmt.setString(20, String.valueOf(mode.getCurrentGrade()));
            pstmt.setString(21, mode.getArm());
            pstmt.setString(22, createdby);
            pstmt.setString(23, DateManipulation.dateAlone());
            pstmt.setString(24, DateManipulation.dateAndTime());
            pstmt.setBoolean(25, false);
            pstmt.setInt(26, studentId);

            pstmt.executeUpdate();

            String slink = "http://localhost:8080/SchlMgt/faces/pages/create/index.xhtml?id=";
            String insertEmail = "insert into " + getSchool() + "_studentstatus (guid,full_name,status,datelogged,studentemail,date_time,studentId,link)"
                    + "values(?,?,?,?,?,?,?,?)";

            pstmt = con.prepareStatement(insertEmail);
            pstmt.setString(1, idOne.toString());
            pstmt.setString(2, fullname);
            pstmt.setBoolean(3, false);
            pstmt.setString(4, DateManipulation.dateAlone());
            pstmt.setString(5, mode.getPemail());
            pstmt.setString(6, DateManipulation.dateAndTime());
            pstmt.setInt(7, studentId);
            pstmt.setString(8, slink + idOne.toString());

            pstmt.executeUpdate();
            classUpload(studentId, mode.getFname(), mode.getMname(), mode.getLname(), String.valueOf(mode.getCurrentClass()), mode.getArm(), String.valueOf(mode.getTerm()), mode.getYear(), createdby, fullname, String.valueOf(mode.getCurrentGrade()), con);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void classUpload(int studentId, String fname, String mname, String lname, String grade, String arm, String term, String year, String createdby, String fullname, String currentclass, Connection con) {

        try {
            String nurseryInsert = "insert into " + getSchool() + "_tbstudentclass (studentid,first_name,middle_name,last_name,full_name,class,"
                    + "classtype,isdeleted,datecreated,datetime_created,createdby,Arm,currentclass,term,year) values "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(nurseryInsert);

            pstmt.setInt(1, studentId);
            pstmt.setString(2, fname);
            pstmt.setString(3, mname);
            pstmt.setString(4, lname);
            pstmt.setString(5, fullname);
            pstmt.setString(6, currentclass);
            pstmt.setString(7, grade);
            pstmt.setBoolean(8, false);
            pstmt.setString(9, DateManipulation.dateAlone());
            pstmt.setString(10, DateManipulation.dateAndTime());
            pstmt.setString(11, createdby);
            pstmt.setString(12, arm);
            pstmt.setBoolean(13, true);
            pstmt.setString(14, term);
            pstmt.setString(15, year);
            pstmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws SQLException {

        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();

        try {

            if ("1".equalsIgnoreCase(getRegistration())) {
                studentUpload(event);
                setCsv(null);
            } else if ("2".equalsIgnoreCase(getRegistration())) {
                staffUpload(event);
                setCsv(null);
            }
        } catch (Exception ex) {
            setMessangerOfTruth(ex.getMessage());
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, message);

        }
    }

    public void regDetails() {
        if ("3".equalsIgnoreCase(getRegistration())) {
            typeValue.clear();
            typeValue.add("Data Entry");
        } else {
            typeValue.clear();
            typeValue.add("Data Upload");
            typeValue.add("Data Entry");
        }
    }

    public boolean isLut() {
        return lut;
    }

    public void setLut(boolean lut) {
        this.lut = lut;
    }

    public UploadedFile getCsv() {
        return csv;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public void setCsv(UploadedFile csv) {
        this.csv = csv;
    }

    public boolean isPut() {
        return put;
    }

    public void setPut(boolean put) {
        this.put = put;
    }

    public boolean isBut() {
        return but;
    }

    public void setBut(boolean but) {
        this.but = but;
    }

    public boolean isStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(boolean studentStatus) {
        this.studentStatus = studentStatus;
    }

    public boolean isStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(boolean staffStatus) {
        this.staffStatus = staffStatus;
    }

    public List<String> getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(List<String> typeValue) {
        this.typeValue = typeValue;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public boolean isStudent() {
        return student;
    }

    public void setStudent(boolean student) {
        this.student = student;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isStaffStudent() {
        return staffStudent;
    }

    public void setStaffStudent(boolean staffStudent) {
        this.staffStudent = staffStudent;
    }

    public Map<String, String> getRoleGetter() {
        return roleGetter;
    }

    public void setRoleGetter(Map<String, String> roleGetter) {
        this.roleGetter = roleGetter;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

}
