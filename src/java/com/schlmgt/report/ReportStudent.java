/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.report;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.logic.ClassGrade;
import com.schlmgt.profile.ClassModel;
import com.schlmgt.school.SchoolGetterMethod;
import com.schlmgt.updateResult.ResultModel;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "stuReport")
@ViewScoped
public class ReportStudent implements Serializable {

    private String schools;
    private String school;
    private String student_class;
    private String student_grade;
    private String year;
    private String arm;
    private String term;
    private List<ClassModel> classmodel;
    private SchoolGetterMethod schlGetterMethod = new SchoolGetterMethod();
    private boolean vis;
    private boolean bis;
    private List<String> tableHeaderNames;
    private StreamedContent exportFile;

    @PostConstruct
    public void init() {
        setVis(false);
        setBis(false);
    }

    public void onyearchange() throws Exception {
        System.out.println("HI");
        setVis(true);
        setBis(true);

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

    public void onItemSelect(SelectEvent event) {
        try {
            setSchool(schlGetterMethod.tableNameDisplay(event.getObject().toString()).replaceAll("\\s", "_"));
            classmodel = classDropdown();
            setSchool(schlGetterMethod.tableNameDisplay(event.getObject().toString()).replaceAll("\\s", "_"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> displaySubject(String table_name) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        // String val = getSchool().replaceAll("\\s", "_");
        try {
            con = dbConnections.mySqlDBconnection();
            String query = "select distinct(subject) from " + table_name + "_tbstudentresult where studentclass=? and Term=? and arm=? and year=? and isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getStudent_grade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getArm());
            pstmt.setString(4, getYear());
            pstmt.setBoolean(5, false);
            rs = pstmt.executeQuery();
            //
            List<String> lst = new ArrayList<>();
            List<String> suHead = new ArrayList<>();
            while (rs.next()) {
                lst.add(rs.getString("subject"));
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

    public List<String> displaySub(String table_name) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            List<String> lst = new ArrayList<>();
            for (int i = 0; i < studentNum(table_name).size(); i++) {
                String query = "select totalscore,grade from " + table_name + "_tbstudentresult where studentclass=? and Term=? and arm=? and year=? and studentreg=? and isdeleted=?";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, getStudent_grade());
                pstmt.setString(2, getTerm());
                pstmt.setString(3, getArm());
                pstmt.setString(4, getYear());
                pstmt.setString(5, studentNum(table_name).get(i));
                pstmt.setBoolean(6, false);
                rs = pstmt.executeQuery();
                //                
                while (rs.next()) {

                    ResultModel coun = new ResultModel();
                    lst.add(String.valueOf(rs.getDouble("totalscore")));
                    lst.add(rs.getString("grade"));
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

    public List<PositionModel> scoreSums(String table_name) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        //String val = getSchool().replaceAll("\\s", "_");
        String value = schlGetterMethod.tableNameDisplay(getSchools());
        try {

            con = dbConnections.mySqlDBconnection();
            List<PositionModel> lst = new ArrayList<>();
            for (int i = 0; i < studentNum(table_name).size(); i++) {
                String query = "select * from " + table_name + "_tbresultcompute where studentclass=? and Term=? and arm=? and year=? and isdeleted=? order by average desc";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, getStudent_grade());
                pstmt.setString(2, getTerm());
                pstmt.setString(3, getArm());
                pstmt.setString(4, getYear());
                pstmt.setBoolean(5, false);
                rs = pstmt.executeQuery();
                //                
                while (rs.next()) {

                    PositionModel coun = new PositionModel();
                    coun.settSum(rs.getDouble("totalscore"));
                    coun.setAverage(rs.getDouble("average"));
                    coun.setPosition(rs.getInt("positionarm"));
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

    public List<PostionTermModel> scoreSumsTerm(String table_name) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        //String val = getSchool().replaceAll("\\s", "_");        
        try {

            con = dbConnections.mySqlDBconnection();
            List<PostionTermModel> lst = new ArrayList<>();
            for (int i = 0; i < studentNum(table_name).size(); i++) {
                String query = "select * from " + table_name + "_tbfinalCompute where studentclass=? and Term=? and year=? and isdeleted=? order by average desc";
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, getStudent_grade());
                pstmt.setString(2, getTerm());
                pstmt.setString(3, getYear());
                pstmt.setBoolean(4, false);
                rs = pstmt.executeQuery();
                //                
                while (rs.next()) {

                    PostionTermModel coun = new PostionTermModel();
                    coun.setFirstTerm(rs.getDouble("firstterm"));
                    coun.setSecondTerm(rs.getDouble("secondterm"));
                    coun.setThirdTerm(rs.getDouble("thirdTerm"));
                    coun.setTotalscore(rs.getDouble("totalscore"));
                    coun.setAverage(rs.getDouble("average"));
                    coun.setPosition(rs.getInt("position"));
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

    public List<String> studentNum(String table_name) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "select a.*,b.full_name from " + table_name + "_tbresultcompute a inner join " + table_name + "_tbstudentclass b on a.studentreg=b.studentid where a.studentclass=? and a.Term=? and a.arm=? and a.year=? and a.isdeleted=? order by a.average desc";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getStudent_grade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getArm());
            pstmt.setString(4, getYear());
            pstmt.setBoolean(5, false);
            rs = pstmt.executeQuery();
            //
            List<String> lst = new ArrayList<>();
            List<String> suHead = new ArrayList<>();
            while (rs.next()) {

                lst.add(rs.getString("studentreg"));
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

    public List<String> studentNumTerm(String table_name) throws Exception {

        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "select a.*,b.full_name from " + table_name + "_tbfinalCompute a inner join " + table_name + "_tbstudentclass b on a.studentreg=b.studentid where a.studentclass=? and a.Term=? and a.year=? and a.isdeleted=? order by a.average desc";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getStudent_grade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            pstmt.setBoolean(4, false);
            rs = pstmt.executeQuery();
            //
            List<String> lst = new ArrayList<>();
            List<String> suHead = new ArrayList<>();
            while (rs.next()) {

                lst.add(rs.getString("studentreg"));
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

    public List<String> studentName(String table_name) throws Exception {

        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "select a.*,b.full_name from " + table_name + "_tbresultcompute a inner join " + table_name + "_tbstudentclass b on a.studentreg=b.studentid where a.studentclass=? and a.Term=? and a.arm=? and a.year=? and a.isdeleted=? order by a.average desc";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getStudent_grade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getArm());
            pstmt.setString(4, getYear());
            pstmt.setBoolean(5, false);
            rs = pstmt.executeQuery();
            //
            List<String> lst = new ArrayList<>();
            List<String> suHead = new ArrayList<>();
            while (rs.next()) {

                lst.add(rs.getString("full_name"));
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

    public List<String> studentNameTerm(String table_name) throws Exception {

        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "select a.*,b.full_name from " + table_name + "_tbfinalCompute a inner join " + table_name + "_tbstudentclass b on a.studentreg=b.studentid where a.studentclass=? and a.Term=? and a.year=? and a.isdeleted=? order by a.average desc";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, getStudent_grade());
            pstmt.setString(2, getTerm());
            pstmt.setString(3, getYear());
            pstmt.setBoolean(4, false);
            rs = pstmt.executeQuery();
            //
            List<String> lst = new ArrayList<>();
            List<String> suHead = new ArrayList<>();
            while (rs.next()) {

                lst.add(rs.getString("full_name"));
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

    public void printReport() throws Exception {
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String table_names = schlGetterMethod.tableNameDisplay(getSchools());
        if (displaySub(table_names).size() > 0) {
            writeToExcel(table_names);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Report Generated", "Report Generated");
            context.addMessage(null, msg);
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "No Report Found", "No Report Found");
            context.addMessage(null, msg);
        }
    }

    public void printReportTerm() throws Exception {
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext cont = RequestContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String table_names = schlGetterMethod.tableNameDisplay(getSchools());

        writeToExcelTerm(table_names);
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Report Generated", "Report Generated");
        context.addMessage(null, msg);

    }

    public void writeToExcel(String table_name) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("resultSheet");
        ClassGrade mode = new ClassGrade();
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight((short) 10);
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        Font headerFontT = workbook.createFont();
        headerFontT.setBoldweight((short) 30);
        headerFontT.setFontHeightInPoints((short) 30);
        headerFontT.setColor(IndexedColors.BLACK.getIndex());

        CellStyle headerCellStyleT = workbook.createCellStyle();
        headerCellStyleT.setFont(headerFontT);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        tableHeaderNames = displaySubject(table_name);

        List<String> subHeading = new ArrayList<>();
        int valu = tableHeaderNames.size() * 2;
        //write code for subheads
        for (int i = 0; i < tableHeaderNames.size(); i++) {
            subHeading.add("Total");
            subHeading.add("Grade");
        }
        int val = 2;
        int lav = 3;
        Row title = sheet.createRow(3);
        Cell ces = title.createCell(7);
        ces.setCellValue(getSchools() + " " + mode.gradeGet(getStudent_grade()) + " " + getArm() + " Result BroadSheet, " + getYear());
        ces.setCellStyle(headerCellStyleT);
//        sheet.addMergedRegion(new CellRangeAddress(
//                3, //first row (0-based)
//                4, //last row  (0-based)
//                3, //first column (0-based)
//                20 //last column  (0-based)                     
//        ));

        Row headerRow = sheet.createRow(6);

        Cell cells = headerRow.createCell(0);
        cells.setCellValue("Student Number");
        Cell cellss = headerRow.createCell(1);
        cellss.setCellValue("Student Name");
        cells.setCellStyle(headerCellStyle);
        for (int i = 0; i < tableHeaderNames.size(); i++) {
            Cell cell = headerRow.createCell(val);
            cell.setCellValue(tableHeaderNames.get(i));
            cell.setCellStyle(headerCellStyle);
            sheet.addMergedRegion(new CellRangeAddress(
                    6, //first row (0-based)
                    6, //last row  (0-based)
                    val, //first column (0-based)
                    lav //last column  (0-based)                     
            ));

            val = val + 2;
            lav = lav + 2;
        }

        Cell cel = headerRow.createCell(lav);
        cel.setCellValue("Grand Total");
        cel.setCellStyle(headerCellStyle);
        Cell ce = headerRow.createCell(lav + 1);
        ce.setCellValue("Class Average");
        ce.setCellStyle(headerCellStyle);
        Cell c = headerRow.createCell(lav + 2);
        c.setCellValue("Position");
        c.setCellStyle(headerCellStyle);

        // write code for subheading
        Row headerRows = sheet.createRow(7);
        int nn = 2;
        for (int i = 0; i < subHeading.size(); i++) {
            Cell cell = headerRows.createCell(nn);
            cell.setCellValue(subHeading.get(i));
            nn++;
        }
        int rowNum = 8;
        int rowNums = 8;
        int cellNum = 0;
        int valNum = 1;
        int numCell = 2;
        Row row;
        for (int i = 0; i < studentNum(table_name).size(); i++) {
            row = sheet.createRow(rowNums);

            row.createCell(0).setCellValue(studentNum(table_name).get(i));
            row.createCell(1).setCellValue(studentName(table_name).get(i));
            rowNums++;
        }
        int vala = displaySubject(table_name).size() * 2;
        for (int i = 0; i < studentNum(table_name).size(); i++) {
            //paste score and grade of each student in excel
            row = sheet.getRow(rowNum);

            for (int p = 0; p < displaySub(table_name).size(); p++) {

                Cell cell = row.createCell(numCell);
                cell.setCellValue(displaySub(table_name).get(cellNum));

                cellNum++;
                if (p == vala - 1) {
                    numCell = 2;
                    break;
                }
                numCell++;

            }
            rowNum++;
        }

        int rowValue = 8;

        for (int i = 0; i < studentNum(table_name).size(); i++) {
            Row header = sheet.getRow(rowValue);
            header.createCell(lav).setCellValue(scoreSums(table_name).get(i).gettSum());
            header.createCell(lav + 1).setCellValue(String.format("%.2f", scoreSums(table_name).get(i).getAverage()));
            header.createCell(lav + 2).setCellValue(scoreSums(table_name).get(i).getPosition());

            rowValue++;
        }

//        for (int i = 0; i < tableHeaderNames.size() + 4; i++) {
//            sheet.autoSizeColumn(i, true);
//        }
//        try (OutputStream fileOut = new FileOutputStream("C:/woook.xlsx")) {
//            workbook.write(fileOut);
//        }
        String timeStamp = new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime());
        String filename = timeStamp + mode.gradeGet(getStudent_grade()) + mode.termGet(getTerm()) + getYear() + "Report.xlsx";
        FileOutputStream fileOut = new FileOutputStream(filename);
        workbook.write(fileOut);
        fileOut.close();
        System.out.println("***Done***" + timeStamp);

        InputStream stream = new BufferedInputStream(new FileInputStream(filename));
        exportFile = new DefaultStreamedContent(stream, "application/xlsx", filename);
//        Workbook wb = new XSSFWorkbook();
//        Sheet sheet = wb.createSheet("new sheet");
//
//        Row row = sheet.createRow(0);
//        Cell cell = row.createCell(0);
//       
//        cell.setCellValue("This is a test of merging");
//
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row (0-based)
//                0, //last row  (0-based)
//                0, //first column (0-based)
//                1 //last column  (0-based)
//        ));
//        
//         Cell cells = row.createCell(2);
//       
//        cells.setCellValue("test of merging");
//
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row (0-based)
//                0, //last row  (0-based)
//                2, //first column (0-based)
//                3 //last column  (0-based)
//        ));
//        
//        
//        // Write the output to a file
//        try (OutputStream fileOut = new FileOutputStream("C:/workbook.xlsx")) {
//            wb.write(fileOut);
//        }
    }

    public void writeToExcelTerm(String table_name) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("resultSheet");
        
        ClassGrade mode = new ClassGrade();
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight((short) 10);
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        Font headerFontT = workbook.createFont();
        headerFontT.setBoldweight((short) 30);
        headerFontT.setFontHeightInPoints((short) 30);
        headerFontT.setColor(IndexedColors.BLACK.getIndex());

        CellStyle headerCellStyleT = workbook.createCellStyle();
        headerCellStyleT.setFont(headerFontT);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        tableHeaderNames = displaySubject(table_name);

        int val = 2;
        int lav = 3;
        Row title = sheet.createRow(3);
        Cell ces = title.createCell(7);
        ces.setCellValue(getSchools() + " " + mode.gradeGet(getStudent_grade()) + " Result BroadSheet, " + getYear());
        ces.setCellStyle(headerCellStyleT);
//        sheet.addMergedRegion(new CellRangeAddress(
//                3, //first row (0-based)
//                4, //last row  (0-based)
//                3, //first column (0-based)
//                20 //last column  (0-based)                     
//        ));

        Row headerRow = sheet.createRow(6);

        Cell cells = headerRow.createCell(0);
        cells.setCellValue("Student Number");
        cells.setCellStyle(headerCellStyle);
        Cell cellss = headerRow.createCell(1);
        cellss.setCellValue("Student Name");
        cellss.setCellStyle(headerCellStyle);
        Cell cell1 = headerRow.createCell(2);
        cell1.setCellValue("First Term");
        cell1.setCellStyle(headerCellStyle);
        Cell cell2 = headerRow.createCell(3);
        cell2.setCellValue("Second Term");
        cell2.setCellStyle(headerCellStyle);
        Cell cell3 = headerRow.createCell(4);
        cell3.setCellValue("Third Term");
        cell3.setCellStyle(headerCellStyle);

        Cell cel = headerRow.createCell(7);
        cel.setCellValue("Grand Total");
        cel.setCellStyle(headerCellStyle);
        Cell ce = headerRow.createCell(8);
        ce.setCellValue("Class Average");
        ce.setCellStyle(headerCellStyle);
        Cell c = headerRow.createCell(9);
        c.setCellValue("Position");
        c.setCellStyle(headerCellStyle);

        int rowNum = 8;
        int rowNums = 8;
        int cellNum = 0;
        int valNum = 1;
        int numCell = 2;
        Row row;
        for (int i = 0; i < studentNumTerm(table_name).size(); i++) {
            row = sheet.createRow(rowNums);

            row.createCell(0).setCellValue(studentNumTerm(table_name).get(i));
            row.createCell(1).setCellValue(studentNameTerm(table_name).get(i));
            rowNums++;
        }

        int rowValue = 8;

        for (int i = 0; i < studentNumTerm(table_name).size(); i++) {
            Row header = sheet.getRow(rowValue);
            header.createCell(2).setCellValue(String.format("%.2f", scoreSumsTerm(table_name).get(i).getFirstTerm()));
            header.createCell(3).setCellValue(String.format("%.2f", scoreSumsTerm(table_name).get(i).getSecondTerm()));
            header.createCell(4).setCellValue(String.format("%.2f", scoreSumsTerm(table_name).get(i).getThirdTerm()));

            header.createCell(7).setCellValue(String.format("%.2f", scoreSumsTerm(table_name).get(i).getTotalscore()));
            header.createCell(8).setCellValue(String.format("%.2f", scoreSumsTerm(table_name).get(i).getAverage()));
            header.createCell(9).setCellValue(scoreSumsTerm(table_name).get(i).getPosition());

            rowValue++;
        }
//        for (int i = 0; i < tableHeaderNames.size() + 4; i++) {
//            sheet.autoSizeColumn(i, true);
//        }
//        try (OutputStream fileOut = new FileOutputStream("C:/woook.xlsx")) {
//            workbook.write(fileOut);
//        }
        String timeStamp = new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime());
        String filename = timeStamp + mode.gradeGet(getStudent_grade()) + "sessionReport.xlsx";
        FileOutputStream fileOut = new FileOutputStream(filename);
        workbook.write(fileOut);
        fileOut.close();
        System.out.println("***Done***" + timeStamp);

        InputStream stream = new BufferedInputStream(new FileInputStream(filename));
        exportFile = new DefaultStreamedContent(stream, "application/xlsx", filename);
//        Workbook wb = new XSSFWorkbook();
//        Sheet sheet = wb.createSheet("new sheet");
//
//        Row row = sheet.createRow(0);
//        Cell cell = row.createCell(0);
//       
//        cell.setCellValue("This is a test of merging");
//
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row (0-based)
//                0, //last row  (0-based)
//                0, //first column (0-based)
//                1 //last column  (0-based)
//        ));
//        
//         Cell cells = row.createCell(2);
//       
//        cells.setCellValue("test of merging");
//
//        sheet.addMergedRegion(new CellRangeAddress(
//                0, //first row (0-based)
//                0, //last row  (0-based)
//                2, //first column (0-based)
//                3 //last column  (0-based)
//        ));
//        
//        
//        // Write the output to a file
//        try (OutputStream fileOut = new FileOutputStream("C:/workbook.xlsx")) {
//            wb.write(fileOut);
//        }
    }

    public String getSchools() {
        return schools;
    }

    public void setSchools(String schools) {
        this.schools = schools;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getStudent_grade() {
        return student_grade;
    }

    public void setStudent_grade(String student_grade) {
        this.student_grade = student_grade;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public SchoolGetterMethod getSchlGetterMethod() {
        return schlGetterMethod;
    }

    public void setSchlGetterMethod(SchoolGetterMethod schlGetterMethod) {
        this.schlGetterMethod = schlGetterMethod;
    }

    public List<ClassModel> getClassmodel() {
        return classmodel;
    }

    public void setClassmodel(List<ClassModel> classmodel) {
        this.classmodel = classmodel;
    }

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public boolean isVis() {
        return vis;
    }

    public void setVis(boolean vis) {
        this.vis = vis;
    }

    public boolean isBis() {
        return bis;
    }

    public void setBis(boolean bis) {
        this.bis = bis;
    }

    public List<String> getTableHeaderNames() {
        return tableHeaderNames;
    }

    public StreamedContent getExportFile() {
        return exportFile;
    }

    public void setExportFile(StreamedContent exportFile) {
        this.exportFile = exportFile;
    }

}
