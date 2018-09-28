/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.login;

import com.schlmgt.dbconn.DbConnectionX;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
public class SchoolNameGet implements Serializable {

    public String schoolName(int schoolId) {
        DbConnectionX dbConnections = new DbConnectionX();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dbConnections.mySqlDBconnection();
            String testguid = "Select * from tbschlmgt where id=?";
            pstmt = con.prepareStatement(testguid);
            pstmt.setInt(1, schoolId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("tablename");

            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
