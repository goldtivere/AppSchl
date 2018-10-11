/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mike.test;

import com.opencsv.CSVReader;
import java.io.FileReader;
import javax.faces.bean.ManagedBean;


/**
 *
 * @author Gold
 */
@ManagedBean
public class TestCSV {

    public void testCSV(){
        String csvFilename = "C:/sample.csv";
        try
        {
            try (CSVReader csvRea = new CSVReader(new FileReader(csvFilename))) {
                String[] row = null;
                while ((row = csvRea.readNext()) != null) {
                    System.out.println(row[0]
                            + " # " + row[1]
                            + " #  " + row[2]);
                }
//...
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
