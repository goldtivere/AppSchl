/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.school;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.login.UserDetails;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Gold
 */
public class SchoolGetterMethod implements Serializable {

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

    public SchoolManagementModel tableNameDisplayValue(String tablename) throws SQLException {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            SchoolManagementModel mdl = new SchoolManagementModel();
            con = dbConnections.mySqlDBconnection();
            String query = "SELECT student.*, stu.dbname,stu.totalstudent,stu.totalmale,totalfemale FROM tbschlmgt student inner "
                    + "join tbschltablestructure stu on stu.schoolname=student.schlname where student.schlname=? and student.isdeleted=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, tablename);
            pstmt.setBoolean(2, false);
            rs = pstmt.executeQuery();
            //           
            if (rs.next()) {                
                mdl.setId(rs.getInt("id"));
                mdl.setLga(rs.getString("lga"));
                mdl.setPnum(rs.getString("phonenumber"));
                mdl.setSchoolHeadName(rs.getString("schoolheadname"));
                mdl.setSchoolName(rs.getString("schlname"));
                mdl.setTableName(rs.getString("tablename"));               

            }
            return mdl;
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
}
