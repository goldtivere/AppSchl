/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.filter;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.register.StudentModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Gold
 */
public class ThreadRunnerEmail implements Runnable {

    private SITembeddedImageEmailUtil embeddedImageEmailUtil = new SITembeddedImageEmailUtil();
    private SITembeddedImageEmailUtil embeddedImageEmailUtils = new SITembeddedImageEmailUtil();
    private boolean message1;
    private boolean message2;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    DbConnectionX dbCon = new DbConnectionX();

    public void run() {

       try
       {
           doTransaction();
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }

    }//end of run method...

    public void doTransaction() throws Exception {

        try {

            StaffEmailModel staffdetail = staffDetails();

            if (staffDetails() != null) {
                con = dbCon.mySqlDBconnection();
                String updateData = "";
                System.out.println("guid:" + staffdetail.getFullname() + ",request time:" + new java.util.Date().toString());

                String message = "Dear " + staffdetail.getFullname() + ", \n Please click on the link or copy to your browser to create an account."
                        + "Link expires in 3days.  "
                        + staffdetail.getLink()
                        + "\n Login ID:" + staffdetail.getPhone();

                String subject = "Account Details";
                String title_ = "<title>Account Details</title>";

                //StringBuffer body = new StringBuffer("<html>Dear Customer, Please find the attached with this mail a PDF file containing <br>");
                StringBuffer body = new StringBuffer("<html>");
                body.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
                body.append(title_);
                body.append("<style type=\text/css\">");
                body.append(".t {font-size: 24px;font-family: Verdana, Geneva, sans-serif;}");
                body.append(".b {font-weight: bold;}");
                body.append(".sdads {color: #808080;}");
                body.append(".deepred {	color: #370000;}");
                body.append("</style></head>");

                body.append("<br>");
                body.append("<body>");
                body.append("<div align=\"center\">");
                body.append("<h4 align=\"center\"");
                body.append("class=\"sdads\">");
                body.append("<h3 align=\"center\" class=\"sdads\"><strong>Account Details</strong></h3>");

                body.append("<table width=\"409\" border=\"0\" align=\"center\" cellpadding=\"1\" cellspacing=\"1\">");

                //
                body.append("<tr>");
                body.append("<td align=\"left\" bgcolor=\"#F4FFF8\"><strong class=\"sdads\"></strong></td>");
                body.append("<td bgcolor=\"#F4FFF8\"><strong class=\"deepred\">" + message + "</strong></td>");
                body.append("</tr>");
                //
                body.append("</table>");

                //body.append(message);
                //body.append("<img src=\"cid:image2\" width=\"30%\" height=\"30%\" /><br>");
                body.append("</div>");
                body.append("</body>");
                body.append("</html>");

                if (embeddedImageEmailUtil.sendOut(staffdetail.getEmail(),
                        subject, body.toString())) {

                    updateData = "update staffstatus set status=true "
                            + "where staffphone='" + staffdetail.getPhone() + "' and "
                            + "fullname='" + staffdetail.getFullname() + "' and id='" + staffdetail.getId() + "'";
                    pstmt = con.prepareStatement(updateData);
                    pstmt.executeUpdate();
                    System.out.println("Hi");
                } else {

                }

            } else {

            }
            //return true;

        } catch (Exception e) {

            System.out.println("Exception from doTransaction method.....");
            e.printStackTrace();

        } finally {

            //System.out.println("inside finally...");
            if (pstmt != null) {
                pstmt.close();
                //System.out.println("inside finally...close stmtUpdate");
            }

            if (pstmt != null) {
                pstmt.close();
                //System.out.println("inside finally...close stmt1");
            }

            if (rs != null) {
                rs.close();
                //System.out.println("inside finally...close rs1");
            }

        }

    }//end doTransact()

    public void doUrlSend() throws Exception {

        try {
            StudentEmailModel sudentEmail = studentDetails();
            if (studentDetails() != null) {

                String updateData = "";

                System.out.println("guid:" + sudentEmail.getFullname() + ",request time:" + new java.util.Date().toString());

                String message = "Dear " + sudentEmail.getFullname() + ", \n Please click on the link or copy to your browser to create an account."
                        + "Link expires in 3days.  "
                        + sudentEmail.getSlink()
                        + "\n Student Registration Number:" + sudentEmail.getStudentId();

                String subject = "Account Registration";
                String title_ = "<title>Account Details</title>";

                //StringBuffer body = new StringBuffer("<html>Dear Customer, Please find the attached with this mail a PDF file containing <br>");
                StringBuffer body = new StringBuffer("<html>");
                body.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
                body.append(title_);
                body.append("<style type=\text/css\">");
                body.append(".t {font-size: 24px;font-family: Verdana, Geneva, sans-serif;}");
                body.append(".b {font-weight: bold;}");
                body.append(".sdads {color: #808080;}");
                body.append(".deepred {	color: #370000;}");
                body.append("</style></head>");

                body.append("<br>");
                body.append("<body>");
                body.append("<div align=\"center\">");
                body.append("<h4 align=\"center\"");
                body.append("class=\"sdads\">");
                body.append("<h3 align=\"center\" class=\"sdads\"><strong>Account Opening</strong></h3>");

                body.append("<table width=\"409\" border=\"0\" align=\"center\" cellpadding=\"1\" cellspacing=\"1\">");

                //
                body.append("<tr>");
                body.append("<td align=\"left\" bgcolor=\"#F4FFF8\"><strong class=\"sdads\"></strong></td>");
                body.append("<td bgcolor=\"#F4FFF8\"><strong class=\"deepred\">" + message + "</strong></td>");
                body.append("</tr>");
                //
                body.append("</table>");

                //body.append(message);
                //body.append("<img src=\"cid:image2\" width=\"30%\" height=\"30%\" /><br>");
                body.append("</div>");
                body.append("</body>");
                body.append("</html>");

                if (embeddedImageEmailUtils.sendOut(sudentEmail.getStudentEmail(),
                        subject, body.toString())) {

                    updateData = "update studentstatus set status=1 "
                            + "where guid='" + sudentEmail.getGuid() + "' and "
                            + "link='" + sudentEmail.getSlink() + "'";
                    pstmt = con.prepareStatement(updateData);
                    pstmt.executeUpdate();

                } else {

                    updateData = "update studentstatus set status=0 "
                            + "where guid='" + sudentEmail.getGuid() + "' and "
                            + "link='" + sudentEmail.getSlink() + "'";

                    pstmt = con.prepareStatement(updateData);
                    pstmt.executeUpdate();

                }

            } else {

            }

            //return true;
            //end while
        } catch (Exception e) {

            System.out.println("Exception from doTransaction method.....");
            e.printStackTrace();

        } finally {

            //System.out.println("inside finally...");
            if (pstmt != null) {
                pstmt.close();
                //System.out.println("inside finally...close stmtUpdate");
            }

            if (pstmt != null) {
                pstmt.close();
                //System.out.println("inside finally...close stmt1");
            }

            if (rs != null) {
                rs.close();
                //System.out.println("inside finally...close rs1");
            }

        }

    }//end doTransact()

    public StudentEmailModel studentDetails() throws Exception {

        try {
            con = dbCon.mySqlDBconnection();
            String querySMSDetails = "select * from studentstatus where status=?";
            pstmt = con.prepareStatement(querySMSDetails);
            pstmt.setBoolean(1, false);
            rs = pstmt.executeQuery();
            String updateData = null;
            StudentEmailModel studentEmailModel = new StudentEmailModel();
            if (rs.next()) {

                studentEmailModel.setSlink(rs.getString("link"));
                studentEmailModel.setGuid(rs.getString("guid"));
                studentEmailModel.setFullname(rs.getString("full_name"));
                studentEmailModel.setStudentId(rs.getString("studentid"));

                studentEmailModel.setStudentEmail(rs.getString("studentemail"));//email account
                return studentEmailModel;
            } else {
                return null;
            }

        } catch (Exception e) {

            System.out.print("Exception from studentDetails method.....");
            e.printStackTrace();
            return null;

        } finally {

            if (!(con == null)) {
                con.close();
            }

            if (!(pstmt == null)) {
                pstmt.close();
            }

            if (!(rs == null)) {
                rs.close();
            }

        }

    }//end doTransaction...

    public StaffEmailModel staffDetails() throws Exception {

        String sms_url;

        try {
            con = dbCon.mySqlDBconnection();
            String querySMSDetails = "select * from Staffstatus where status=?";
            pstmt = con.prepareStatement(querySMSDetails);
            pstmt.setBoolean(1, false);
            rs = pstmt.executeQuery();
            //

            StaffEmailModel staffEmailModel = new StaffEmailModel();
            if (rs.next()) {
                System.out.println("I got here");

                staffEmailModel.setPhone(rs.getString("staffPhone"));
                staffEmailModel.setFullname(rs.getString("fullname"));
                staffEmailModel.setLink(rs.getString("link"));
                staffEmailModel.setId(rs.getInt("id"));

                staffEmailModel.setEmail(rs.getString("staffemail"));//email account

                return staffEmailModel;
            } else {
                return null;
            }

        } catch (Exception e) {

            System.out.print("Exception from staffDetails method.....");
            e.printStackTrace();
            return null;

        } finally {

            if (!(con == null)) {
                con.close();
            }

            if (!(pstmt == null)) {
                pstmt.close();
            }

            if (!(rs == null)) {
                rs.close();
            }

        }

    }//end doTransaction...

    public boolean isMessage1() {
        return message1;
    }

    public void setMessage1(boolean message1) {
        this.message1 = message1;
    }

    public boolean isMessage2() {
        return message2;
    }

    public void setMessage2(boolean message2) {
        this.message2 = message2;
    }

}
