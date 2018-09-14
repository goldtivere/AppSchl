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

public class DbConnectionX implements Serializable {

    LoadPPTfile loadPPTfile = new LoadPPTfile();
    private String messangerOfTruth;
    private boolean testconnection;

    public Connection mySqlDBconnection() {

        try {

//            String dburl = "jdbc:mysql://node36144-schlmgt.atl.jelastic.vps-host.net/schlmgt";
//            String username = "schlmgt";
//            String password = "LqVs89ZeZIbQNltB";
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection(dburl, username, password);
//            Class.forName("com.mysql.jdbc.Driver");
            //Connection con = DriverManager.getConnection("jdbc:mysql://schlmgtuser.cc4ncfbykvkk.us-east-2.rds.amazonaws.com/schlmgt", "schlmgtUser", "rootgold");            
            if (!(loadPPTfile.isLoadPPtFile())) {
                setMessangerOfTruth("Cannot load configuration file...");
                setTestconnection(false);
                return null;
            }

            Properties ppt = loadPPTfile.getPptFile();
            String url = ppt.getProperty("mysql_db_url");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);

            setTestconnection(true);

            return con;

        } catch (Exception e) {

            setTestconnection(false);
            setMessangerOfTruth("Error from DbConnection.class " + e.getMessage());
            return null;

        }

    }//end myConnection()

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
