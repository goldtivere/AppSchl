/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.imgupload;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 *
 * @author Gold
 */
public class ExcelUpload {
    
    public static String cellToString(HSSFCell cell)
    {
        int type = 0;
        Object result;
        
        switch(type)
        {
            case 0:
                result=cell.getNumericCellValue();
                break;
            case 1:
                result=cell.getStringCellValue();
                break;
            default:
                throw new RuntimeException("There are no support for this type of cell");
            
        }
        return result.toString();
        
    }
    
}
