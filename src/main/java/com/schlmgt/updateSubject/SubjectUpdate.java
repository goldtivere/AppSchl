/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.updateSubject;

import com.schlmgt.dbconn.DbConnectionX;
import static com.schlmgt.imgupload.ExcelUpload.cellToString;
import com.schlmgt.imgupload.UploadImagesX;
import com.schlmgt.logic.AESencrp;
import com.schlmgt.logic.DateManipulation;
import com.schlmgt.login.UserDetails;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.el.PropertyNotFoundException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
public class SubjectUpdate implements Serializable {

    private String studentClass;
    private String studentGrade;
    private String term;
    private String year;
    private List<SessionTable> sesTab;
    private List<SessionTable> sesTab1;
    private SessionTable tab = new SessionTable();
    private SessionTable tabValues = new SessionTable();
    private boolean status;
    private UploadedFile csv;
    private String csvUrl;
    private String ref_number;
    private String csvLocation;
    private String messangerOfTruth;

    @PostConstruct
    public void init() {
        setStatus(false);
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

    public void select(SessionTable e) {

        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        FacesMessage msg;

        tabValues.setGrade(e.getGrade());
        tabValues.setId(e.getId());
        tabValues.setSclass(e.getSclass());
        tabValues.setSubject(e.getSubject());
        tabValues.setTerm(e.getTerm());
        tabValues.setYear(e.getYear());
        System.out.println(e.getSubject());
        System.out.println(tabValues.getSubject());

    }

    public String checkSubjectExist(List<String> sub) {

        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String returnValue = "yes";
        con = dbConnections.mySqlDBconnection();

        try {

            String subjectDetail = "select * from sessiontable where grade=? and term=? and year=? and subject=? and isdeleted=?";
            pstmt = con.prepareStatement(subjectDetail);
            for (int i = 0; i < sub.size(); i++) {
                pstmt.setString(1, getStudentGrade());
                pstmt.setString(2, getTerm());
                pstmt.setString(3, getYear());
                pstmt.setString(4, sub.get(i));
                pstmt.setBoolean(5, false);
                rs = pstmt.executeQuery();
                System.out.println("a: " + getStudentGrade() + " b: " + getTerm() + " " + getYear() + " " + sub.get(i));

                if (rs.next()) {
                    returnValue = "Subject " + sub.get(i) + " already exist";
                    return returnValue;
                }
            }

            return returnValue;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void handleFileUpload(FileUploadEvent event) {

        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        UploadImagesX uploadImagesX = new UploadImagesX();
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean stat = false;

        con = dbConnections.mySqlDBconnection();

        try {

            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();

            InputStream mn = event.getFile().getInputstream();
            XSSFWorkbook wb = new XSSFWorkbook(mn);
            XSSFSheet ws = wb.getSheetAt(0);
            Row row;
            Row rows;
            row = (Row) ws.getRow(0);
            rows = (Row) ws.getRow(0);
            List<String> subjectValue = new ArrayList<>();
            for (int i = 1; i <= ws.getLastRowNum(); i++) {
                rows = (Row) ws.getRow(i);

                if (rows.getCell(0) == null) {
                    int s = i + 1;
                    setMessangerOfTruth("Cell in row " + String.valueOf(s) + " is empty");
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                    break;
                }
                if (rows.getLastCellNum() > 1) {
                    int s = i + 1;
                    setMessangerOfTruth("Incorrect Excel format");
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                    context.addMessage(null, message);
                    break;
                }

                subjectValue.add(rows.getCell(0).toString());

            }

            if (checkSubjectExist(subjectValue).equalsIgnoreCase("yes")) {
                if ("subject".equalsIgnoreCase(row.getCell(0).toString())) {
                    con.setAutoCommit(false);
                    String subjectDetail = "insert into sessiontable (term,class,grade,year,subject,createdby,datecreated,isdeleted) values("
                            + "?,?,?,?,?,?,?,?)";
                    pstmt = con.prepareStatement(subjectDetail);
                    for (int i = 1; i <= ws.getLastRowNum(); i++) {
                        row = (Row) ws.getRow(i);

                        if (row.getCell(0) == null) {
                            int s = i + 1;
                            setMessangerOfTruth("Cell in row " + String.valueOf(s) + " is empty");
                            message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                            context.addMessage(null, message);
                            break;
                        }

                        pstmt.setString(1, getTerm());
                        pstmt.setString(2, getStudentClass());
                        pstmt.setString(3, getStudentGrade());
                        pstmt.setString(4, getYear());
                        pstmt.setString(5, row.getCell(0).toString());
                        pstmt.setString(6, createdby);
                        pstmt.setString(7, DateManipulation.dateAndTime());
                        pstmt.setBoolean(8, false);
                        pstmt.executeUpdate();

                        stat = true;

                    }
                    if (stat == true) {
                        con.commit();
                        sesTab = displaySubject();
                        setMessangerOfTruth("File Upload Successful");
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, message);
                    } else {
                        setMessangerOfTruth("File Upload unSuccessful");
                        message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                        context.addMessage(null, message);
                    }
                } else {
                    setMessangerOfTruth("Excel not in correct format");

                }
            } else {
                setMessangerOfTruth("Please enter Valid data in excel");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, message);
            }

            setCsv(null);
            csv = null;
            mn.close();
        } catch (Exception ex) {

            ex.printStackTrace();
            message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }

    }

    public void updateSubject() {
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

            String updateSubject = "update sessiontable set subject=? ,updatedby=?,updatetime=? where id=?";

            pstmt = con.prepareStatement(updateSubject);

            pstmt.setString(1, tab.getSubject());
            pstmt.setString(2, createdby);
            pstmt.setString(3, DateManipulation.dateAndTime());
            pstmt.setInt(4, tab.getId());
            pstmt.executeUpdate();
            sesTab = displaySubject();
            setMessangerOfTruth("Subject Updated!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
            cont.addCallbackParam("loggedIn", loggedIn);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteRecord() {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();

        try {
            UserDetails userObj = (UserDetails) context.getExternalContext().getSessionMap().get("sessn_nums");
            String on = String.valueOf(userObj);
            String createdby = String.valueOf(userObj.getFirst_name() + " " + userObj.getLast_name());
            int createdId = userObj.getId();
            con = dbConnections.mySqlDBconnection();
            if (sesTab1 == null) {
                setMessangerOfTruth("Item(s) not selected!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            } else {

                String updateSubject = "update sessiontable set isdeleted=?,deletedby=?,dateDeleted=? where id=?";

                pstmt = con.prepareStatement(updateSubject);
                for (SessionTable ta : sesTab1) {
                    pstmt.setBoolean(1, true);
                    pstmt.setString(2, createdby);
                    pstmt.setString(3, DateManipulation.dateAndTime());
                    pstmt.setInt(4, ta.getId());
                    pstmt.executeUpdate();

                }
                sesTab = displaySubject();
                setMessangerOfTruth("Subject Deleted!!");
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
                context.addMessage(null, msg);
            }
        } catch (PropertyNotFoundException e) {

            setMessangerOfTruth("Item(s) not selected!!");
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessangerOfTruth(), getMessangerOfTruth());
            context.addMessage(null, msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onYearChange() throws Exception {
        setStatus(true);
        sesTab = displaySubject();
    }

    public List<SessionTable> getSesTab1() {
        return sesTab1;
    }

    public void setSesTab1(List<SessionTable> sesTab1) {
        this.sesTab1 = sesTab1;
    }

    public SessionTable getTabValues() {
        return tabValues;
    }

    public void setTabValues(SessionTable tabValues) {
        this.tabValues = tabValues;
    }

    public SessionTable getTab() {
        return tab;
    }

    public void setTab(SessionTable tab) {
        this.tab = tab;
    }

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public String getCsvLocation() {
        return csvLocation;
    }

    public void setCsvLocation(String csvLocation) {
        this.csvLocation = csvLocation;
    }

    public String getRef_number() {
        return ref_number;
    }

    public void setRef_number(String ref_number) {
        this.ref_number = ref_number;
    }

    public String getCsvUrl() {
        return csvUrl;
    }

    public void setCsvUrl(String csvUrl) {
        this.csvUrl = csvUrl;
    }

    public UploadedFile getCsv() {
        return csv;
    }

    public void setCsv(UploadedFile csv) {
        this.csv = csv;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<SessionTable> getSesTab() {
        return sesTab;
    }

    public void setSesTab(List<SessionTable> sesTab) {
        this.sesTab = sesTab;
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

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getStudentGrade() {
        return studentGrade;
    }

    public void setStudentGrade(String studentGrade) {
        this.studentGrade = studentGrade;
    }

}
