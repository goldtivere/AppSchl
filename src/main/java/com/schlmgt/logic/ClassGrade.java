/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.logic;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.register.ClassModel;
import com.schlmgt.register.GradeModel;
import com.schlmgt.register.TermModel;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "classmode")
@ViewScoped
public class ClassGrade implements Serializable {

    private List<ClassModel> classmodel;
    private List<GradeModel> grademodels;
    private List<TermModel> termList;
    private List<String> term;
    private List<String> arm;
    private boolean viis;
    private boolean bis;

    @PostConstruct
    public void init() {
        try {
            viis = false;
            bis = false;
            
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public List<String> yearDropdown(String term) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT distinct year FROM yearterm where term=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, term);
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

    public void onyearchange() {
        setBis(true);
        setViis(true);
    }

    public String countryOrigin(String countryValue) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbcountry where id=?";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(countryValue));
            rs = pstmt.executeQuery();
            //
            String value = null;
            if (rs.next()) {

                value = rs.getString("country");

            }

            return value;
        } catch (NumberFormatException e) {
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

    public String stateOrigin(String stateValue) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbstates where id=?";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(stateValue));
            rs = pstmt.executeQuery();
            //
            String value = null;
            if (rs.next()) {

                value = rs.getString("states");

            }

            return value;
        } catch (NumberFormatException e) {
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

    public String lgaValue(String lgaValue) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbstatelga where id=?";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(lgaValue));
            rs = pstmt.executeQuery();
            //
            String value = null;
            if (rs.next()) {

                value = rs.getString("lga");

            }

            return value;
        } catch (NumberFormatException e) {
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

    public String classGet(String classValue) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbclass where id=?";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(classValue));
            rs = pstmt.executeQuery();
            //
            String value = null;
            if (rs.next()) {

                value = rs.getString("class");

            }

            return value;
        } catch (NumberFormatException e) {
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

    public String gradeGet(String gradeValue) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbgrade where id=?";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(gradeValue));
            rs = pstmt.executeQuery();
            //
            String value = null;
            if (rs.next()) {

                value = rs.getString("grade");

            }

            return value;
        } catch (NumberFormatException e) {
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

    public String disabilityGet(String disabilityValue) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbdisability where id=?";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(disabilityValue));
            rs = pstmt.executeQuery();
            //
            String value = null;
            if (rs.next()) {

                value = rs.getString("disability");

            }

            return value;
        } catch (NumberFormatException e) {
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

    public String bloodGet(String bloodValue) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbbloodgroup where id=?";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(bloodValue));
            rs = pstmt.executeQuery();
            //
            String value = null;
            if (rs.next()) {

                value = rs.getString("bloodgroup");

            }

            return value;
        } catch (NumberFormatException e) {
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

    public String termGet(String termValue) throws SQLException {

        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbterm where id=?";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(termValue));
            rs = pstmt.executeQuery();
            //
            String value = null;
            if (rs.next()) {

                value = rs.getString("term");

            }

            return value;
        } catch (NumberFormatException e) {

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

    public String relationshipGet(String relationshipValue) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM relationship where id=?";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(relationshipValue));
            rs = pstmt.executeQuery();
            //
            String value = null;
            if (rs.next()) {

                value = rs.getString("relation");

            }

            return value;
        } catch (NumberFormatException e) {
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

    public void ontermChanges() throws Exception {

        term = yearDropdown();

    }

    public void ontermChan() throws Exception {

        term = yearDropdown();

    }

    public void ontermChanges(String tbclass) throws Exception {

        term = yearDropdown(tbclass);

    }
     public void onterm(String tbclass) throws Exception {

        term = yearDropdown(tbclass);

    }

    public void onarmChanges() throws Exception {

        arm = armDropdown();

    }

    public List<GradeModel> gradeDropdowns(String tbclass) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbgrade where class=?";
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, tbclass);
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

    public List<TermModel> termDropdown() throws Exception {

        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {

            con = dbConnections.mySqlDBconnection();
            String query = "SELECT * FROM tbterm";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            //
            List<TermModel> lst = new ArrayList<>();
            while (rs.next()) {

                TermModel coun = new TermModel();
                coun.setId(rs.getInt("id"));
                coun.setTerm(rs.getString("term"));

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

    public void onTermDropDown() {

    }

    public List<String> armDropdown() throws Exception {

        //
        try {
            List<String> lst = new ArrayList<>();
            lst.add("A");
            lst.add("B");
            lst.add("C");
            lst.add("D");
            return lst;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    public List<String> getArm() {
        return arm;
    }

    public void setArm(List<String> arm) {
        this.arm = arm;
    }

    public void ongradeChanges() throws Exception {

        termList = termDropdown();

    }

    public void onClassChanges(String tbclass) throws Exception {

        grademodels = gradeDropdowns(tbclass);

    }
    
     public void ClassChangesOn(String tbclass) throws Exception {

        grademodels = gradeDropdowns(tbclass);

    }

    public void onClassChangeStaff() {

    }

    public List<String> getTerm() {
        return term;
    }

    public void setTerm(List<String> term) {
        this.term = term;
    }

    public List<TermModel> getTermList() {
        return termList;
    }

    public void setTermList(List<TermModel> termList) {
        this.termList = termList;
    }

    public List<GradeModel> getGrademodels() {
        return grademodels;
    }

    public void setGrademodels(List<GradeModel> grademodels) {
        this.grademodels = grademodels;
    }

    public List<ClassModel> getClassmodel() {
        return classmodel;
    }

    public void setClassmodel(List<ClassModel> classmodel) {
        this.classmodel = classmodel;
    }

    public boolean isViis() {
        return viis;
    }

    public void setViis(boolean viis) {
        this.viis = viis;
    }

    public boolean isBis() {
        return bis;
    }

    public void setBis(boolean bis) {
        this.bis = bis;
    }

}
