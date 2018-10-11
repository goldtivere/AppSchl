/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.report;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Gold
 */
public class ColumnModel implements Serializable {

    private String key;
    private String value;

    public ColumnModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

   
}
