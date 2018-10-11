/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.schlmgt.register.*;
import java.io.Serializable;

/**
 *
 * @author Gold
 */
public class ClassModel implements Serializable {
    
    private int id;
    private String tbclass;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTbclass() {
        return tbclass;
    }

    public void setTbclass(String tbclass) {
        this.tbclass = tbclass;
    }
    
    
}
