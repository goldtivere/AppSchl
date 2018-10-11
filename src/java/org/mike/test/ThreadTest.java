/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mike.test;

import com.schlmgt.dbconn.DbConnectionX;
import com.schlmgt.filter.MessageModel;
import com.schlmgt.logic.DateManipulation;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.Callable;

/**
 *
 * @author Gold
 */
public class ThreadTest implements Callable<Boolean> {

    private boolean valueGet;

    @Override
    public Boolean call() throws Exception {
        doTransaction();
        if (isValueGet()) {
            runValue();
            return true;
        } else {
            return false;
        }
    }

    public MessageModel doTransaction() throws Exception {

        Connection con = null;
        DbConnectionX dbCon = new DbConnectionX();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sms_url;

        MessageModel messageModel = new MessageModel();

        try {

            con = dbCon.mySqlDBconnection();

            String querySMSDetails = "select * from smstable "
                    + "where status=false and statuscode is null";
            //
            pstmt = con.prepareStatement(querySMSDetails);
            rs = pstmt.executeQuery();

            //
            String _val = null;
            if (rs.next()) {

                String value = rs.getString("body");
                _val = value.replace(" ", "%20");
                _val = _val.replace(",", "%2C");
                _val = _val.replace(":", "%3A");
                _val = _val.replace(";", "%3B");
                _val = _val.replace("'", "%27");
                _val = _val.replace("(", "%28");
                _val = _val.replace(")", "%29");
                _val = _val.replace("#", "%23");
                messageModel.setBody(_val);
                messageModel.setPnum(rs.getString("phonenumbers"));
                messageModel.setDateSent(rs.getString("datesent"));
                messageModel.setStatus(true);
                messageModel.setId(rs.getInt("id"));
                setValueGet(true);
                return messageModel;

            } else {

                messageModel.setStatus(false);
                setValueGet(false);
                return messageModel;

            }

        } catch (Exception e) {

            System.out.print("Exception from doTransaction method.....");

            messageModel.setStatus(false);
            messageModel.setStatus_msg("Error:" + e.getMessage());

            return messageModel;

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

    public void updateSmsTable(String statusCode, String description, int id) {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dbConnections.mySqlDBconnection();

            String updateSmsTable = "update smstable set status=?,statuscode=?,statusdescription=?,datemessagesent=?,datesenttime=? where id=?";
            pstmt = con.prepareStatement(updateSmsTable);
            pstmt.setBoolean(1, true);
            pstmt.setString(2, statusCode);
            pstmt.setString(3, description);
            pstmt.setString(4, DateManipulation.dateAlone());
            pstmt.setString(5, DateManipulation.dateAndTime());
            pstmt.setInt(6, id);
            pstmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void runValue() {

        int i = 0;

        outer:

        while (true) {

            try {

                MessageModel messageModel = doTransaction();
                Thread t = new Thread();
                if (messageModel.getStatus() == false) {
                    System.out.println(messageModel.getStatus_msg());//error

                } else {
                    String val = null;
                    String sender = "GOTIT";
                    URL url = new URL("http://www.bulksmslive.com/tools/geturl/Sms.php?username=goldtive@gmail.com&password=GoldTivere94&sender=" + sender + "&message=" + messageModel.getBody() + "&flash=1&sendtime=" + messageModel.getDateSent() + "&listname=friends&recipients=" + messageModel.getPnum());
                    //http://www.bulksmslive.com/tools/geturl/Sms.php?username=abc&password=xyz&sender="+sender+"&message="+message+"&flash=0&sendtime=2009-10- 18%2006:30&listname=friends&recipients="+recipient; 
                    //URL gims_url = new URL("http://smshub.lubredsms.com/hub/xmlsmsapi/send?user=loliks&pass=GJP8wRTs&sender=nairabox&message=Acct%3A5073177777%20Amt%3ANGN1%2C200.00%20CR%20Desc%3ATesting%20alert%20Avail%20Bal%3ANGN%3A1%2C342%2C158.36&mobile=08065711040&flash=0");
                    final String USER_AGENT = "Mozilla/5.0";

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("User-Agent", USER_AGENT);
                    int responseCode = con.getResponseCode();
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    // System.out.println(messageModel.getBody() + " dude");
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    String responseCod = response.toString();

                    if (responseCod.equalsIgnoreCase("-1")) {
                        val = "Incorrect / badly formed URL data";
                    } else if (responseCod.equalsIgnoreCase("-2")) {
                        val = "Incorrect username and/or password";
                    } else if (responseCod.equalsIgnoreCase("-3")) {
                        val = "Not enough credit units in user account";
                    } else if (responseCod.equalsIgnoreCase("-4")) {
                        val = "Invalid sender name";
                    } else if (responseCod.equalsIgnoreCase("-5")) {
                        val = "No valid recipient ";
                    } else if (responseCod.equalsIgnoreCase("-6")) {
                        val = "Invalid message length/No message content";
                    } else if (responseCod.equalsIgnoreCase("-10")) {
                        val = "Unknown/Unspecified error";
                    } else if (responseCod.equalsIgnoreCase("100")) {
                        val = "Send successful";
                    }

                    // in.close(); unremark
                    //System.out.println("God is my Strength:" + i++  );
                    //  System.out.println("The URL:" + gims_url);
                    //doTransaction();
                    updateSmsTable(response.toString(), val, messageModel.getId());
                    System.out.println("ID: " + messageModel.getId() + " sent. Message: " + messageModel.getBody() + " Code" + responseCod);
                    t.sleep(20000);
                }
            } catch (Exception e) {

                e.printStackTrace();
                System.out.println("IOException error.....");

            }

        }//end of while...

    }//end of run method...

    public boolean isValueGet() {
        return valueGet;
    }

    public void setValueGet(boolean valueGet) {
        this.valueGet = valueGet;
    }

}
