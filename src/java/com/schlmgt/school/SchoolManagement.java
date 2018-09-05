/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.school;

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
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gold
 */
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
        String testemail = "Select * from tbschlmgt where phonenumber=? and is_deleted=?";
        pstmt = con.prepareStatement(testemail);
        pstmt.setString(1, pnum);    
        pstmt.setBoolean(2, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;
        }

        return false;
    }

    public boolean SchoolNameCheck(String name, Connection con) throws SQLException {       
        String testemail = "Select * from tbschlmgt where schlname=? and is_deleted=?";
        pstmt = con.prepareStatement(testemail);
        pstmt.setString(1, name);   
        pstmt.setBoolean(2, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;

        }
        return false;
    }
     public boolean SchoolHeadNameCheck(String name, Connection con) throws SQLException {       
        String testemail = "Select * from tbschlmgt where schoolheadname=? and is_deleted=?";
        pstmt = con.prepareStatement(testemail);
        pstmt.setString(1, name);   
        pstmt.setBoolean(2, false);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            return true;

        }
        return false;
    }

    public List<SessionTable> displaySubject() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM sessiontable where class=? and Grade=? and term=? and year=? and isdeleted=? order by id asc";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getStudentClass());
            pstmt.setString(2, getStudentGrade());
            pstmt.setString(3, getTerm());
            pstmt.setString(4, getYear());
            pstmt.setBoolean(5, false);
            rs = pstmt.executeQuery();
            //
            List<SessionTable> lst = new ArrayList<>();
            while (rs.next()) {

                SessionTable coun = new SessionTable();
                coun.setId(rs.getInt("id"));
                coun.setSclass(rs.getString("class"));
                coun.setSubject(rs.getString("subject"));
                coun.setTerm(rs.getString("term"));
                coun.setGrade(rs.getString("grade"));

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

                                setMessangerOfTruth("School Name: " + mode.getSchoolName()+ " already exists. Row " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (SchoolHeadNameCheck(mode.getSchoolHeadName(), con)) {
                                setMessangerOfTruth("School head name, "+ mode.getSchoolHeadName()+ " already exist. Row: " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } else if (PhoneCheck(mode.getPnum(), con)) {
                                setMessangerOfTruth("Phone Number " + mode.getPnum() +" Already Exist!! Please enter a different Phone Number. Row: " + (i + 1));
                                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                                context.addMessage(null, msg);
                                break;
                            } 
                            else if (EmailCheck(mode.getEmailAdd(), con)) {
                                setMessangerOfTruth("Email Address " + mode.getEmailAdd()+" Already Exist!! Please enter a different Email. Row: " + (i + 1));
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
                }
            }

        } catch (Exception e) {

        }

    }
    
     public void createStudent(SchoolManagementModel mode, String fullname, String dob, String gfullname, String createdby, int studentId) {

        try {
            con = dbConnections.mySqlDBconnection();
            UUID idOne = UUID.randomUUID();
            String insertStudentDetails = "insert into tbschlmgt"
                    + "(schlname,tablename,schoolheadname,designation,emailaddress,phonenumber,isdeleted,createdby,createdid,"
                    + "datecreated)"
                    + "values"
                    + "(?,?,?,?,?,"
                    + "?,?,?,?,?)";
            pstmt = con.prepareStatement(insertStudentDetails);
            pstmt.setString(1, mode.getSchoolName());
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
            String insertEmail = "insert into studentstatus (guid,full_name,status,datelogged,studentemail,date_time,studentId,link)"
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
