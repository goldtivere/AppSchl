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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
    private String messangerOfTruth;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    DbConnectionX dbConnections = new DbConnectionX();

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

//    public List<SchoolManagementModel> displaySubject() throws Exception {
//        FacesContext context = FacesContext.getCurrentInstance();
//
//        try {
//
//            con = dbConnections.mySqlDBconnection();
//            String query = "SELECT * FROM tbschlmgt where isdeleted=?";           
//            pstmt.setBoolean(1, false);
//            rs = pstmt.executeQuery();
//            //
//            List<SchoolManagementModel> lst = new ArrayList<>();
//            while (rs.next()) {
//
//                SchoolManagementModel coun = new SchoolManagementModel();
//                coun.setId(rs.getInt("id"));
//                coun.setSclass(rs.getString("class"));
//                coun.setSubject(rs.getString("subject"));
//                coun.setTerm(rs.getString("term"));
//                coun.setGrade(rs.getString("grade"));
//
//                //
//                lst.add(coun);
//            }
//
//            return lst;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//
//        } finally {
//
//            if (!(con == null)) {
//                con.close();
//                con = null;
//            }
//            if (!(pstmt == null)) {
//                pstmt.close();
//                pstmt = null;
//            }
//
//        }
//    }
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
                    setMessangerOfTruth(success + " School(s) Data Upload Successful");

                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                    setCsv(null);
                } else {
                    setMessangerOfTruth("oad Successful");

                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                    System.out.println("it is : " + val);
                }
            }

        } catch (Exception e) {

        }

    }

    public void insert(String tablename) {
        FacesMessage msg;
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        String smstable = tablename + "_smstable";
        String studentDetails = tablename + "_student_Details";
        String studentClass = tablename + "_tbstudentclass";
        String studentResult = tablename + "_tbstudentresult";
        String studentResultCompute = tablename + "_tbresultcompute";
        String finalCompute = tablename + "_tbfinalCompute";

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
                    + "(schlname,tablename,schoolheadname,designation,emailaddress,phonenumber,isdeleted,createdby,createdid,"
                    + "datecreated)"
                    + "values"
                    + "(?,?,?,?,?,"
                    + "?,?,?,?,?)";
            pstmt = con.prepareStatement(insertStudentDetails);
            pstmt.setString(1, mode.getSchoolName());
            pstmt.setString(2, tableName);
            pstmt.setString(3, mode.getSchoolHeadName());
            pstmt.setString(4, mode.getDesignation());
            pstmt.setString(5, mode.getEmailAdd());
            pstmt.setString(6, mode.getPnum());
            pstmt.setBoolean(7, false);
            pstmt.setString(8, createdby);
            pstmt.setInt(9, createdId);
            pstmt.setString(10, DateManipulation.dateAndTime());

            pstmt.executeUpdate();

            String insertSchoolStructure = "insert into tbschltablestructure "
                    + "(schoolname,dbname) "
                    + "values"
                    + "(?,?)";
            pstmt = con.prepareStatement(insertSchoolStructure);
            pstmt.setString(1, mode.getSchoolName());
            pstmt.setString(2, tableName);           
            pstmt.executeUpdate();
            insert(tableName);
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

}
