/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.school;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.imgupload.UploadImagesX;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.login.UserDetails;
import com.schlmgt.profile.SecondaryModel;
import com.schlmgt.register.StudentModel;
import com.schlmgt.updateSubject.SessionTable;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gold
 */
@ManagedBean
@ViewScoped
public class SchoolManagement implements Serializable {

    private UploadedFile csv;
    private List<SchoolManagementModel> schlmgtModel;
    private SchoolManagementModel modeSchool = new SchoolManagementModel();
    private String messangerOfTruth;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    DbConnectionX dbConnections = new DbConnectionX();

    @PostConstruct
    public void init() {
        try {
            schlmgtModel = displaySchool();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean EmailCheck(String email, Connection con) throws SQLException {
        String testemail = "Select * from tbschlmgt where emailaddress=? and isdeleted=?";
        pstmt = con.prepareStatement(testemail);
        pstmt.setString(1, email);
        pstmt.setBoolean(2, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;

        }
        return false;
    }

    public boolean PhoneCheck(String pnum, Connection con) throws SQLException {
        String testemail = "Select * from tbschlmgt where phonenumber=? and isdeleted=?";
        pstmt = con.prepareStatement(testemail);
        pstmt.setString(1, pnum);
        pstmt.setBoolean(2, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;
        }

        return false;
    }

    public boolean SchoolNameCheck(String name, Connection con) {
        try {
            String testemail = "Select * from tbschlmgt where schlname=? and isdeleted=?";
            pstmt = con.prepareStatement(testemail);
            pstmt.setString(1, name);
            pstmt.setBoolean(2, false);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return true;

            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    public boolean SchoolHeadNameCheck(String name, Connection con) throws SQLException {
        String testemail = "Select * from tbschlmgt where schoolheadname=? and isdeleted=?";
        pstmt = con.prepareStatement(testemail);
        pstmt.setString(1, name);
        pstmt.setBoolean(2, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;

        }
        return false;
    }

    public List<SchoolManagementModel> displaySchool() throws SQLException {
        try {
            studentCount();
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
                coun.setLga(rs.getString("lga"));
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

    public void studentCount() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            int i = 0;
            System.out.println("Value: " + (i++));
            int countmale = 0;
            int countfemale = 0;
            int counttotal = 0;
            con = dbConnections.mySqlDBconnection();
            String query = "SELECT student.*, stu.dbname FROM tbschlmgt student inner "
                    + "join tbschltablestructure stu on stu.schoolname=student.schlname where student.isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setBoolean(1, false);
            rs = pstmt.executeQuery();
            //
            List<SchoolManagementModel> lst = new ArrayList<>();
            while (rs.next()) {

                SchoolManagementModel coun = new SchoolManagementModel();
                coun.setId(rs.getInt("id"));
                coun.setTableName(rs.getString("dbname"));

                //
                lst.add(coun);
            }

            for (SchoolManagementModel val : lst) {
                String queryCount = "SELECT count(*) studentCount from " + val.getTableName() + "_student_details where is_deleted=false";
                pstmt = con.prepareStatement(queryCount);
                rs = pstmt.executeQuery();
                rs.next();
                counttotal = rs.getInt("studentCount");
                String queryCount1 = "SELECT count(*) studentCount1 from " + val.getTableName() + "_student_details where sex='1' and is_deleted=false";
                pstmt = con.prepareStatement(queryCount1);
                rs = pstmt.executeQuery();
                rs.next();
                countmale = rs.getInt("studentCount1");
                String queryCount2 = "SELECT count(*) studentCount2 from " + val.getTableName() + "_student_details where sex='2' and is_deleted=false";
                pstmt = con.prepareStatement(queryCount2);
                rs = pstmt.executeQuery();
                rs.next();
                countfemale = rs.getInt("studentCount2");

                String updateCount = "update tbschltablestructure set totalstudent=?, totalmale=?, totalfemale=? where dbname=?";
                pstmt = con.prepareStatement(updateCount);
                pstmt.setInt(1, counttotal);
                pstmt.setInt(2, countmale);
                pstmt.setInt(3, countfemale);
                pstmt.setString(4, val.getTableName());
                pstmt.executeUpdate();
                System.out.println("Total: " + counttotal + " male: " + countmale + " Female: " + countfemale);
            }

        } catch (Exception e) {
            e.printStackTrace();

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
        FacesMessage msg;
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        UploadImagesX uploadImagesX = new UploadImagesX();
        try {
            con = dbConnections.mySqlDBconnection();
            SchoolManagementModel mode = new SchoolManagementModel();
            List<String> schoolDetails = new ArrayList<>();
            schoolDetails.add("SchoolName");
            schoolDetails.add("LGA");
            schoolDetails.add("SchoolHead");
            schoolDetails.add("Designation");
            schoolDetails.add("EmailAddress");
            schoolDetails.add("PhoneNumber");

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
            System.out.println("Physical row= " + row.getPhysicalNumberOfCells());
            if (schoolDetails.size() == row.getPhysicalNumberOfCells()) {
                for (int i = 0; i < schoolDetails.size(); i++) {
                    if (row.getCell(i).toString().equalsIgnoreCase(schoolDetails.get(i))) {
                        val++;
                    } else {
                        setMessangerOfTruth("Excel is in wrong format. It should be in this format: " + schoolDetails.toString() + ".Or click on Upload Format to download the correct format for upload");
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, message);
                        break;
                    }
                }
                System.out.println("value is: " + val);
                if (val == row.getPhysicalNumberOfCells()) {
                    Row ro = null;
                    for (int i = 1; i < rowNum; i++) {
                        try {
                            ro = (Row) ws.getRow(i);
                            if (ro.getCell(0) != null) {
                                mode.setSchoolName(ro.getCell(0).getStringCellValue());
                            } else {
                                setMessangerOfTruth("School Name is required. Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            }

                            if (ro.getCell(1) != null) {
                                mode.setLga(ro.getCell(1).getStringCellValue());
                            } else {
                                setMessangerOfTruth("LGA is required. Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            }

                            if (ro.getCell(2) != null) {
                                mode.setSchoolHeadName(ro.getCell(2).getStringCellValue());
                            } else {
                                setMessangerOfTruth("School Head Name is required Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            }
                            if (ro.getCell(3) != null) {
                                mode.setDesignation(ro.getCell(3).getStringCellValue());
                            } else {
                                setMessangerOfTruth("Designation is required Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            }
                            if (ro.getCell(4) != null) {
                                mode.setEmailAdd(ro.getCell(4).getStringCellValue());
                            } else {
                                setMessangerOfTruth("Email Address is required. Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            }
                            if (ro.getCell(5) != null) {
                                mode.setPnum(df.formatCellValue(ro.getCell(5)));
                            } else {
                                setMessangerOfTruth("Phone Number is required. Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            }

                            if (SchoolNameCheck(mode.getSchoolName(), con)) {

                                setMessangerOfTruth("School Name: " + mode.getSchoolName() + " already exists. Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (SchoolHeadNameCheck(mode.getSchoolHeadName(), con)) {
                                setMessangerOfTruth("School head name, " + mode.getSchoolHeadName() + " already exist. Row: " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (PhoneCheck(mode.getPnum(), con)) {
                                setMessangerOfTruth("Phone Number " + mode.getPnum() + " Already Exist!! Please enter a different Phone Number. Row: " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (EmailCheck(mode.getEmailAdd(), con)) {
                                setMessangerOfTruth("Email Address " + mode.getEmailAdd() + " Already Exist!! Please enter a different Email. Row: " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else {
                                createSchool(mode, createdby, createdId);
                                success++;
                            }
                        } catch (IllegalStateException e) {
                            setMessangerOfTruth("Please check that phone number and sex and Date of Birth is in the correct format. Row " + (i + 1));
                            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, message);
                            break;
                        }
                    }
                    studentCount();
                    schlmgtModel = displaySchool();
                    setMessangerOfTruth(success + " School(s) Data Upload Successful");

                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                    setCsv(null);
                } else {
                    setMessangerOfTruth("Error !! Please check excel file");

                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                    System.out.println("it is : " + val);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void selectReco(SchoolManagementModel schlDetails) {

        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            NavigationHandler nav = ctx.getApplication().getNavigationHandler();
            ctx.getExternalContext().getApplicationMap().remove("schlData");
            ctx.getExternalContext().getApplicationMap().put("schlData", schlDetails);
            String url = "editprofile.xhtml?faces-redirect=true";
            nav.handleNavigation(ctx, null, url);
            ctx.renderResponse();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void insert(String tablename) {
        FacesMessage msg;
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        String smstable = tablename + "_smstable";
        String studentDetails = tablename + "_student_details";
        String studentClass = tablename + "_tbstudentclass";
        String studentResult = tablename + "_tbstudentresult";
        String studentResultCompute = tablename + "_tbresultcompute";
        String finalCompute = tablename + "_tbfinalCompute";
        String studentStatus = tablename + "_studentstatus";
        String sessionTable = tablename + "_sessiontable";
        String studentLogin = tablename + "_studentLogin";

        try {

            con = dbConnections.mySqlDBconnection();

            String smstablename = "CREATE TABLE " + smstable + " ("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `body` text,"
                    + "  `phoneNumbers` varchar(200) DEFAULT NULL,"
                    + "  `status` tinyint(4) DEFAULT NULL,"
                    + "  `dateSent` date DEFAULT NULL,"
                    + "  `sentby` varchar(100) DEFAULT NULL,"
                    + "  `datetimeSent` datetime DEFAULT NULL,"
                    + "  `statusCode` int(11) DEFAULT NULL,"
                    + "  `statusDescription` varchar(200) DEFAULT NULL,"
                    + "  `DateMessageSent` date DEFAULT NULL,"
                    + "  `dateSentTime` datetime DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`)"
                    + ")";

            pstmt = con.prepareStatement(smstablename);
            pstmt.executeUpdate();

            String sessionTab = "CREATE TABLE " + sessionTable + " ("
                    + "  `Id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `Term` varchar(100) DEFAULT NULL,"
                    + "  `Class` varchar(100) DEFAULT NULL,"
                    + "  `Grade` varchar(100) DEFAULT NULL,"
                    + "  `Year` varchar(100) DEFAULT NULL,"
                    + "  `Subject` varchar(200) DEFAULT NULL,"
                    + "  `CreatedBy` varchar(100) DEFAULT NULL,"
                    + "  `DateCreated` datetime DEFAULT NULL,"
                    + "  `updatedBy` varchar(100) DEFAULT NULL,"
                    + "  `updateTime` datetime DEFAULT NULL,"
                    + "  `isdeleted` tinyint(4) DEFAULT NULL,"
                    + "  `deletedby` varchar(200) DEFAULT NULL,"
                    + "  `dateDeleted` datetime DEFAULT NULL,"
                    + "  PRIMARY KEY (`Id`)"
                    + ") ";

            pstmt = con.prepareStatement(sessionTab);
            pstmt.executeUpdate();

            String studentDetailsName = "CREATE TABLE " + studentDetails + " ("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `first_name` varchar(100) DEFAULT NULL,"
                    + "  `middle_name` varchar(100) DEFAULT NULL,"
                    + "  `last_name` varchar(100) DEFAULT NULL,"
                    + "  `fullname` varchar(200) DEFAULT NULL,"
                    + "  `DOB` date DEFAULT NULL,"
                    + "  `student_phone` varchar(100) DEFAULT NULL,"
                    + "  `student_email` varchar(100) DEFAULT NULL,"
                    + "  `sex` varchar(100) DEFAULT NULL,"
                    + "  `Guardian_firstname` varchar(100) DEFAULT NULL,"
                    + "  `Guardian_middlename` varchar(100) DEFAULT NULL,"
                    + "  `Guardian_lastname` varchar(100) DEFAULT NULL,"
                    + "  `Guardian_fullname` varchar(200) DEFAULT NULL,"
                    + "  `relationship` varchar(100) DEFAULT NULL,"
                    + "  `relationship_other` varchar(200) DEFAULT NULL,"
                    + "  `Guardian_phone` varchar(100) DEFAULT NULL,"
                    + "  `Guardian_email` varchar(100) DEFAULT NULL,"
                    + "  `Guardian_country` varchar(100) DEFAULT NULL,"
                    + "  `Guardian_state` varchar(100) DEFAULT NULL,"
                    + "  `Guardian_Lga` varchar(100) DEFAULT NULL,"
                    + "  `Guardian_address` longtext,"
                    + "  `Previous_school` longtext,"
                    + "  `previous_class` varchar(100) DEFAULT NULL,"
                    + "  `previous_grade` varchar(100) DEFAULT NULL,"
                    + "  `current_class` varchar(100) DEFAULT NULL,"
                    + "  `current_grade` varchar(100) DEFAULT NULL,"
                    + "  `Arm` varchar(100) DEFAULT NULL,"
                    + "  `Disability` varchar(100) DEFAULT NULL,"
                    + "  `other_disability` varchar(100) DEFAULT NULL,"
                    + "  `bgroup` varchar(100) DEFAULT NULL,"
                    + "  `image` longtext,"
                    + "  `created_by` varchar(100) DEFAULT NULL,"
                    + "  `date_created` date DEFAULT NULL,"
                    + "  `datetime_created` datetime DEFAULT NULL,"
                    + "  `is_deleted` tinyint(4) DEFAULT NULL,"
                    + "  `studentId` bigint(20) DEFAULT NULL,"
                    + "  `updated_by` varchar(100) DEFAULT NULL,"
                    + "  `updated_id` int(11) DEFAULT NULL,"
                    + "  `date_updated` datetime DEFAULT NULL,"
                    + "  `imgLocation` varchar(200) DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`)"
                    + ")";

            pstmt = con.prepareStatement(studentDetailsName);
            pstmt.executeUpdate();

            String tbfinalcompute = "CREATE TABLE " + finalCompute + " ("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `studentreg` varchar(100) DEFAULT NULL,"
                    + "  `studentClass` varchar(100) DEFAULT NULL,"
                    + "  `term` varchar(100) DEFAULT NULL,"
                    + "  `year` varchar(100) DEFAULT NULL,"
                    + "  `firstTerm` double DEFAULT NULL,"
                    + "  `secondterm` double DEFAULT NULL,"
                    + "  `thirdterm` double DEFAULT NULL,"
                    + "  `totalscore` double DEFAULT NULL,"
                    + "  `average` double DEFAULT NULL,"
                    + "  `position` int(11) DEFAULT NULL,"
                    + "  `datecreated` datetime DEFAULT NULL,"
                    + "  `createdby` varchar(100) DEFAULT NULL,"
                    + "  `dateupdated` datetime DEFAULT NULL,"
                    + "  `updatedby` varchar(100) DEFAULT NULL,"
                    + "  `isdeleted` tinyint(4) DEFAULT NULL,"
                    + "  `datedeleted` datetime DEFAULT NULL,"
                    + "  `deletedby` varchar(100) DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`)"
                    + ") ";

            pstmt = con.prepareStatement(tbfinalcompute);
            pstmt.executeUpdate();

            String computeResult = "CREATE TABLE " + studentResultCompute + " ("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `studentReg` varchar(100) DEFAULT NULL,"
                    + "  `studentClass` varchar(100) DEFAULT NULL,"
                    + "  `Term` varchar(100) DEFAULT NULL,"
                    + "  `arm` varchar(10) DEFAULT NULL,"
                    + "  `Year` varchar(100) DEFAULT NULL,"
                    + "  `TotalScore` double DEFAULT NULL,"
                    + "  `NumberOfSubject` int(11) DEFAULT NULL,"
                    + "  `Average` double DEFAULT NULL,"
                    + "  `Postion` varchar(100) DEFAULT NULL,"
                    + "  `PositionArm` varchar(100) DEFAULT NULL,"
                    + "  `updatedBy` varchar(100) DEFAULT NULL,"
                    + "  `DateUpdated` datetime DEFAULT NULL,"
                    + "  `createdBy` varchar(100) DEFAULT NULL,"
                    + "  `dateCreated` datetime DEFAULT NULL,"
                    + "  `datedeletedAlone` date DEFAULT NULL,"
                    + "  `dateAlone` date DEFAULT NULL,"
                    + "  `isdeleted` tinyint(4) DEFAULT NULL,"
                    + "  `deletedby` varchar(100) DEFAULT NULL,"
                    + "  `datedeleted` datetime DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`)"
                    + ") ";

            pstmt = con.prepareStatement(computeResult);
            pstmt.executeUpdate();

            String computeResultStatus = "CREATE TABLE " + studentStatus + " ("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `guid` varchar(200) DEFAULT NULL,\n"
                    + "  `full_name` varchar(200) DEFAULT NULL,"
                    + "  `status` tinyint(4) DEFAULT NULL,"
                    + "  `datelogged` date DEFAULT NULL,\n"
                    + "  `studentEmail` varchar(200) DEFAULT NULL,"
                    + "  `date_time` datetime DEFAULT NULL,"
                    + "  `studentid` bigint(20) DEFAULT NULL,"
                    + "  `link` varchar(200) DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`)"
                    + ")  ";

            pstmt = con.prepareStatement(computeResultStatus);
            pstmt.executeUpdate();

            String classStudent = "CREATE TABLE " + studentClass + " ("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `studentid` varchar(100) DEFAULT NULL,"
                    + "  `first_name` varchar(100) DEFAULT NULL,"
                    + "  `middle_name` varchar(100) DEFAULT NULL,"
                    + "  `last_name` varchar(100) DEFAULT NULL,"
                    + "  `full_name` varchar(200) DEFAULT NULL,"
                    + "  `class` varchar(100) DEFAULT NULL,"
                    + "  `classtype` varchar(100) DEFAULT NULL,"
                    + "  `isdeleted` tinyint(4) DEFAULT NULL,"
                    + "  `datecreated` date DEFAULT NULL,"
                    + "  `datetime_created` datetime DEFAULT NULL,"
                    + "  `createdby` varchar(100) DEFAULT NULL,"
                    + "  `promoted` tinyint(4) DEFAULT NULL,"
                    + "  `imagelink` longtext,"
                    + "  `Arm` varchar(45) DEFAULT NULL,"
                    + "  `updatedby` varchar(100) DEFAULT NULL,"
                    + "  `updaterId` int(11) DEFAULT NULL,"
                    + "  `dateupdated` datetime DEFAULT NULL,"
                    + "  `currentclass` tinyint(4) DEFAULT NULL,"
                    + "  `term` varchar(100) DEFAULT NULL,"
                    + "  `year` varchar(100) DEFAULT NULL,"
                    + "  `NoOfTimesRepeated` int(11) DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`)"
                    + ") ";

            pstmt = con.prepareStatement(classStudent);
            pstmt.executeUpdate();

            String stuResult = "CREATE TABLE " + studentResult + " ("
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,"
                    + "  `studentReg` varchar(100) DEFAULT NULL,"
                    + "  `studentClass` varchar(100) DEFAULT NULL,"
                    + "  `term` varchar(100) DEFAULT NULL,"
                    + "  `Arm` varchar(100) DEFAULT NULL,"
                    + "  `year` varchar(100) DEFAULT NULL,"
                    + "  `Subject` text,"
                    + "  `firstTest` double DEFAULT NULL,"
                    + "  `secondTest` double DEFAULT NULL,"
                    + "  `Exam` double DEFAULT NULL,"
                    + "  `TotalScore` double DEFAULT NULL,"
                    + "  `grade` varchar(100) DEFAULT NULL,"
                    + "  `createdby` varchar(100) DEFAULT NULL,"
                    + "  `datecreated` date DEFAULT NULL,"
                    + "  `datetimecreated` datetime DEFAULT NULL,"
                    + "  `updatedby` varchar(100) DEFAULT NULL,"
                    + "  `dateupdated` date DEFAULT NULL,"
                    + "  `datetimeupdated` datetime DEFAULT NULL,"
                    + "  `isdeleted` tinyint(4) DEFAULT NULL,"
                    + "  `deletedby` varchar(100) DEFAULT NULL,"
                    + "  `datetimedeleted` datetime DEFAULT NULL,"
                    + "  PRIMARY KEY (`id`)"
                    + ") ";

            pstmt = con.prepareStatement(stuResult);
            pstmt.executeUpdate();

            String studentL = "CREATE TABLE " + studentLogin + " ("
                    + "`id` INT NOT NULL,"
                    + " `RegNumber` NVARCHAR(255) NULL,"
                    + " `PinNumber` NVARCHAR(255) NULL,"
                    + "`serialNumber` NVARCHAR(255) NULL,"
                    + "`timesUsed` INT NULL,"
                    + "`DateLastLoggedIn` DATETIME NULL,"
                    + "PRIMARY KEY (`id`))";
            pstmt = con.prepareStatement(studentL);
            pstmt.executeUpdate();

        } catch (MySQLSyntaxErrorException ex) {
            setMessangerOfTruth(" Table already exist");
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, message);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public void createSchool(SchoolManagementModel mode, String createdby, int createdId) {

        try {
            con = dbConnections.mySqlDBconnection();
            UUID idOne = UUID.randomUUID();
            String tableName = mode.getSchoolName().replaceAll("\\s", "_");
            String insertStudentDetails = "insert into tbschlmgt"
                    + "(schlname,tablename,schoolheadname,lga,designation,emailaddress,phonenumber,isdeleted,createdby,createdid,"
                    + "datecreated)"
                    + "values"
                    + "(?,?,?,?,?,?,"
                    + "?,?,?,?,?)";
            pstmt = con.prepareStatement(insertStudentDetails);
            pstmt.setString(1, mode.getSchoolName());
            pstmt.setString(2, tableName);
            pstmt.setString(3, mode.getSchoolHeadName());
            pstmt.setString(4, mode.getLga());
            pstmt.setString(5, mode.getDesignation());
            pstmt.setString(6, mode.getEmailAdd());
            pstmt.setString(7, mode.getPnum());
            pstmt.setBoolean(8, false);
            pstmt.setString(9, createdby);
            pstmt.setInt(10, createdId);
            pstmt.setString(11, DateManipulation.dateAndTime());

            pstmt.executeUpdate();

            String insertSchoolStructure = "insert into tbschltablestructure "
                    + "(schoolname,dbname,isdeleted) "
                    + "values"
                    + "(?,?,?)";
            pstmt = con.prepareStatement(insertSchoolStructure);
            pstmt.setString(1, mode.getSchoolName());
            pstmt.setString(2, tableName);
            pstmt.setBoolean(3, false);
            pstmt.executeUpdate();
            insert(tableName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int schoolNameCheck(String schoolName, int id) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testflname = "Select count(*) schoolCount from tbschlmgt where schlname=? and id=? and isdeleted=?";
        pstmt = con.prepareStatement(testflname);
        pstmt.setString(1, schoolName);
        pstmt.setInt(2, id);
        pstmt.setBoolean(3, false);
        rs = pstmt.executeQuery();

        rs.next();
        return rs.getInt("schoolCount");

    }

    public int schoolHNameCheck(String schoolHName, int id) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testflname = "Select count(*) schoolCount from tbschlmgt where schoolheadname=? and id=? and isdeleted=?";
        pstmt = con.prepareStatement(testflname);
        pstmt.setString(1, schoolHName);
        pstmt.setInt(2, id);
        pstmt.setBoolean(3, false);
        rs = pstmt.executeQuery();

        rs.next();
        return rs.getInt("schoolCount");

    }

    public int schoolEmail(String schoolHName, int id) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testflname = "Select count(*) schoolCount from tbschlmgt where emailaddress=? and id=? and isdeleted=?";
        pstmt = con.prepareStatement(testflname);
        pstmt.setString(1, schoolHName);
        pstmt.setInt(2, id);
        pstmt.setBoolean(3, false);
        rs = pstmt.executeQuery();

        rs.next();
        return rs.getInt("schoolCount");

    }

    public int schoolPhoneCheck(String schoolHName, int id) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        con = dbConnections.mySqlDBconnection();
        String testflname = "Select count(*) schoolCount from tbschlmgt where phonenumber=? and id=? and isdeleted=?";
        pstmt = con.prepareStatement(testflname);
        pstmt.setString(1, schoolHName);
        pstmt.setInt(2, id);
        pstmt.setBoolean(3, false);
        rs = pstmt.executeQuery();

        rs.next();
        return rs.getInt("schoolCount");

    }

    public void updateSchoolDetails() {
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
            System.out.println(schoolEmail(modeSchool.getEmailAdd(), modeSchool.getId()) + " this is serious " + EmailCheck(modeSchool.getEmailAdd(), con));
            if (schoolNameCheck(modeSchool.getSchoolName(), modeSchool.getId()) == 1 || !SchoolNameCheck(modeSchool.getSchoolName(), con)) {
                if (schoolHNameCheck(modeSchool.getSchoolHeadName(), modeSchool.getId()) == 1 || !SchoolHeadNameCheck(modeSchool.getSchoolHeadName(), con)) {

                    if (schoolEmail(modeSchool.getEmailAdd(), modeSchool.getId()) == 1 || !EmailCheck(modeSchool.getEmailAdd(), con)) {
                        if (schoolPhoneCheck(modeSchool.getPnum(), modeSchool.getId()) == 1 || !PhoneCheck(modeSchool.getPnum(), con)) {
                            String schoolDetails = "update tbschlmgt set schlname=? ,schoolheadname=?, designation=?, emailaddress=?, phonenumber=?, "
                                    + "updatedby=?, updatedid=?,dateupdated=? where id=? and tablename=?";

                            pstmt = con.prepareStatement(schoolDetails);

                            pstmt.setString(1, modeSchool.getSchoolName());
                            pstmt.setString(2, modeSchool.getSchoolHeadName());
                            pstmt.setString(3, modeSchool.getDesignation());
                            pstmt.setString(4, modeSchool.getEmailAdd());
                            pstmt.setString(5, modeSchool.getPnum());
                            pstmt.setString(6, createdby);
                            pstmt.setInt(7, createdId);
                            pstmt.setString(8, DateManipulation.dateAndTime());
                            pstmt.setInt(9, modeSchool.getId());
                            pstmt.setString(10, modeSchool.getTableName());
                            pstmt.executeUpdate();

                            String struc = "update tbschltablestructure set schoolname=? where dbname=?";

                            pstmt = con.prepareStatement(struc);

                            pstmt.setString(1, modeSchool.getSchoolName());
                            pstmt.setString(2, modeSchool.getTableName());
                            pstmt.executeUpdate();

                            setMessangerOfTruth("School details Updated!!");
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                            cont.addCallbackParam("loggedIn", loggedIn);
                        } else {
                            setMessangerOfTruth("School Head Name Phone number already Exist!!");
                            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, msg);
                        }
                    } else {
                        setMessangerOfTruth("School Head Name Email already Exist!!");
                        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, msg);
                    }

                } else {
                    setMessangerOfTruth("School Head Name already Exist!!");
                    msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, msg);
                }
            } else {
                setMessangerOfTruth("School Name already exist!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public UploadedFile getCsv() {
        return csv;
    }

    public void setCsv(UploadedFile csv) {
        this.csv = csv;
    }

    public List<SchoolManagementModel> getSchlmgtModel() {
        return schlmgtModel;
    }

    public void setSchlmgtModel(List<SchoolManagementModel> schlmgtModel) {
        this.schlmgtModel = schlmgtModel;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public SchoolManagementModel getModeSchool() {
        return modeSchool;
    }

    public void setModeSchool(SchoolManagementModel modeSchool) {
        this.modeSchool = modeSchool;
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public PreparedStatement getPstmt() {
        return pstmt;
    }

    public void setPstmt(PreparedStatement pstmt) {
        this.pstmt = pstmt;
    }

    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    public DbConnectionX getDbConnections() {
        return dbConnections;
    }

    public void setDbConnections(DbConnectionX dbConnections) {
        this.dbConnections = dbConnections;
    }

}
