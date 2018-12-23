/**
 * @author Abobade Oludayo Michael
 * @email:pagims2003@yahoo.com , michael.abobade@lolikshouse.com
 * @version 1.0
 * @mobile 08065711043, 08077792196
 * @Date 2017-01-28
 */
package com.schlmgt.dbconn;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import com.schlmgt.logic.LoadPPTfile;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class DbConnectionX implements Serializable {

    LoadPPTfile loadPPTfile = new LoadPPTfile();
    private String messangerOfTruth;
    private boolean testconnection;

//    public Connection mySqlDBconnection() {
//
//        try {
//
////            String dburl = "jdbc:mysql://node37892-schlmgt.atl.jelastic.vps-host.net/schlmgt";
////            String username = "root";
////            String password = "KQCTNBD99C";
////            Class.forName("com.mysql.jdbc.Driver");
//            // Connection con = DriverManager.getConnection(dburl, username, password);
//            //Class.forName("com.mysql.jdbc.Driver");
//            //Connection con = DriverManager.getConnection("jdbc:mysql://schlmgtuser.cc4ncfbykvkk.us-east-2.rds.amazonaws.com/schlmgt", "schlmgtUser", "rootgold");            
//            if (!(loadPPTfile.isLoadPPtFile())) {
//                setMessangerOfTruth("Cannot load configuration file...");
//                setTestconnection(false);
//                return null;
//            }
//
//            Properties ppt = loadPPTfile.getPptFile();
//            String url = ppt.getProperty("mysql_db_url");
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection(url);
//
//            setTestconnection(true);
//
//            return con;
//
//        } catch (Exception e) {
//
//            setTestconnection(false);
//            setMessangerOfTruth("Error from DbConnection.class " + e.getMessage());
//            return null;
//
//        }
//
//    }//end myConnection()

    public Connection mySqlDBconnection() {
        try {

            Properties prop = new Properties();
            //System.out.println("test");
            prop.load(new FileInputStream(System.getProperty("user.home") + "/mydb.cfg"));
            //System.out.println("user.home: " + System.getProperty("user.home"));
            String host = prop.getProperty("host").toString();
            String username = prop.getProperty("username").toString();
            String password = prop.getProperty("password").toString();
            String driver = prop.getProperty("driver").toString();

            //System.out.println("host: " + host + "\nusername: " + username + "\npassword: " + password + "\ndriver: " + driver);
            Class.forName(driver);
            // System.out.println("--------------------------");
            //System.out.println("DRIVER: " + driver);
            Connection connection = DriverManager.getConnection(host, username, password);
            //System.out.println("CONNECTION: " + connection);

            return connection;
        } catch (Exception e) {

            setTestconnection(false);
            setMessangerOfTruth("Error from DbConnection.class " + e.getMessage());
            e.printStackTrace();
            return null;

        }
    }

    /**
     * @return the messangerOfTruth
     */
    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    /**
     * @param messangerOfTruth the messangerOfTruth to set
     */
    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }

    /**
     * @return the testconnection
     */
    public boolean isTestconnection() {
        return testconnection;
    }

    /**
     * @param testconnection the testconnection to set
     */
    public void setTestconnection(boolean testconnection) {
        this.testconnection = testconnection;
    }

}
