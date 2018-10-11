/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.login;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.filter.ThreadRunner;
import com.schlmgt.filter.ThreadRunnerEmail;
import com.schlmgt.logic.AESencrp;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "login")
@SessionScoped
public class Login implements Serializable {

    private String username;
    private String password;
    private String messangerOfTruth;
    private UserDetails dto = new UserDetails();
    private boolean roleAssigned;
    private boolean roleAssigned1;
    private boolean canupdateResult;
    private boolean canupdateSubject;
    private boolean cancreatestaff;
    private boolean cancreatestudent;
    private boolean cansendtext;
    private boolean canmanageschool;
    private boolean canviewprofile;
    private int assignedRole;

    public void loginpage() throws Exception {

        FacesContext context = FacesContext.getCurrentInstance();

        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            con = dbConnections.mySqlDBconnection();

            String queryProfile = "select * from user_details "
                    + "where username=? and Password=? and is_deleted=?";

            //encrypt the password entered and then compared with what you have in the DB
            String doEncPwd = AESencrp.encrypt(getPassword());
            //
            pstmt = con.prepareStatement(queryProfile);
            pstmt.setString(1, getUsername());
            pstmt.setString(2, doEncPwd);
            pstmt.setBoolean(3, false);

            //System.out.println(getUsername() + "," + doEncPwd  + "<>" + getPassword() );
            //pstmt.setString(2, "ok");
            rs = pstmt.executeQuery();

            if (rs.next()) {

                dto.setUser_name(getUsername());
                dto.setFirst_name(rs.getString("first_name"));
                dto.setId(rs.getInt("id"));
                dto.setLast_name(rs.getString("last_name"));
                dto.setUser_name(rs.getString("username"));
                dto.setImageId(rs.getString("image_name"));
                dto.setEmail(rs.getString("email_address"));
                dto.setRole_id(rs.getString("role_id"));
                dto.setCreatdBy(rs.getString("Created_by"));
                dto.setRoleAssigned(rs.getInt("roleassigned"));
                setAssignedRole(rs.getInt("roleassigned"));
                dto.setRoleid(rs.getInt("roleid"));
                dto.setCanRegisterStaff(rs.getBoolean("canregisterstaff"));
                dto.setCanRegisterStudent(rs.getBoolean("canregisterstudent"));
                dto.setCanUpdateResult(rs.getBoolean("canupdateresult"));
                dto.setCanUpdateSubject(rs.getBoolean("canupdatesubject"));
                dto.setCanSendMessage(rs.getBoolean("cansendtext"));
                dto.setCanViewProfile(rs.getBoolean("canviewprofile"));
                dto.setCanManageSchool(rs.getBoolean("canmanageschool"));
                dto.setSchoolName(rs.getInt("school_name"));

                context.getExternalContext().getSessionMap().put("sessn_nums", getDto());

                NavigationHandler nav = context.getApplication().getNavigationHandler();

                String url_ = "/pages/home/homepage.xhtml?faces-redirect=true";
                nav.handleNavigation(context, null, url_);
                context.renderResponse();

                if (getAssignedRole() == 3) {
                    setRoleAssigned(true);
                    setRoleAssigned1(false);
                }
                if (getAssignedRole() == 2 && dto.isCanUpdateResult()) {
                    setRoleAssigned(false);
                    setRoleAssigned1(false);
                    setCanupdateResult(true);
                }

                if (getAssignedRole() == 2 && dto.isCanSendMessage()) {
                    setRoleAssigned(false);
                    setRoleAssigned1(false);
                    setCansendtext(true);
                }

                if (getAssignedRole() == 2 && dto.isCanUpdateSubject()) {
                    setRoleAssigned(false);
                    setRoleAssigned1(false);
                    setCanupdateSubject(true);
                }

                if (getAssignedRole() == 2 && (dto.isCanRegisterStaff() || dto.isCanRegisterStudent())) {
                    setRoleAssigned(false);
                    setRoleAssigned1(false);
                    setCancreatestaff(true);
                }
                if (getAssignedRole() == 2 && dto.isCanManageSchool()) {
                    setRoleAssigned(false);
                    setRoleAssigned1(false);
                    setCanmanageschool(true);
                }
                
                 if (getAssignedRole() == 2 && dto.isCanViewProfile()) {
                    setRoleAssigned(false);
                    setRoleAssigned1(false);
                    setCanviewprofile(true);
                }
            } else {

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid login details", "Invalid login details"));

                //System.out.println("Failed.");
            }

        } catch (NullPointerException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage().toString(), "System unavailable111, please try again later."));
        } catch (Exception e) {

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage().toString(), "System unavailable, please try again later."));
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

    }//end activities...

    public void noactivity(ActionEvent evt) {
        getLogout();
    }

    public boolean getLogout() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {

            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            session.invalidate();

            // pageMover.setValue01("page1.xhtml");
            //context.getExternalContext().getSessionMap().clear();
            NavigationHandler nav = context.getApplication().getNavigationHandler();

            nav.handleNavigation(context, null, "/index.xhtml?faces-redirect=true");
            //nav.handleNavigation(context, null, "/../../login.xhtml?faces-redirect=true");

            context.renderResponse();

            if (context.getExternalContext().getSessionMap().isEmpty()) {

                //System.out.println("Why:" + dto.getUsername());
                return true;

            } else {

                context.addMessage(null, new FacesMessage("App. cannot close at this time,try later."));
                //System.out.println("Why:" + dto.getUsername());
                return false;
            }

        } catch (Exception e) {

            context.addMessage(null, new FacesMessage("System Unavailable."));
            e.printStackTrace();
            return false;

        }

    }//end getLogout

    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    public UserDetails getDto() {
        return dto;
    }

    public void setDto(UserDetails dto) {
        this.dto = dto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isRoleAssigned1() {
        return roleAssigned1;
    }

    public void setRoleAssigned1(boolean roleAssigned1) {
        this.roleAssigned1 = roleAssigned1;
    }

    public boolean isCanupdateResult() {
        return canupdateResult;
    }

    public void setCanupdateResult(boolean canupdateResult) {
        this.canupdateResult = canupdateResult;
    }

    public boolean isCanupdateSubject() {
        return canupdateSubject;
    }

    public void setCanupdateSubject(boolean canupdateSubject) {
        this.canupdateSubject = canupdateSubject;
    }

    public boolean isCancreatestaff() {
        return cancreatestaff;
    }

    public void setCancreatestaff(boolean cancreatestaff) {
        this.cancreatestaff = cancreatestaff;
    }

    public boolean isCancreatestudent() {
        return cancreatestudent;
    }

    public void setCancreatestudent(boolean cancreatestudent) {
        this.cancreatestudent = cancreatestudent;
    }

    public boolean isCansendtext() {
        return cansendtext;
    }

    public void setCansendtext(boolean cansendtext) {
        this.cansendtext = cansendtext;
    }

    public boolean isCanmanageschool() {
        return canmanageschool;
    }

    public void setCanmanageschool(boolean canmanageschool) {
        this.canmanageschool = canmanageschool;
    }

    public boolean isCanviewprofile() {
        return canviewprofile;
    }

    public void setCanviewprofile(boolean canviewprofile) {
        this.canviewprofile = canviewprofile;
    }

}
