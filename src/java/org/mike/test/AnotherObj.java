/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mike.test;

/**
 *
 * @author Gold
 */


import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "tstd2")
@ViewScoped
public class AnotherObj implements Serializable {
    
    private TestStudentXModel resultx = new TestStudentXModel();
    
    public AnotherObj(){
        //
         FacesContext ctx = FacesContext.getCurrentInstance();
          TestStudentXModel result = (TestStudentXModel)ctx.getExternalContext().getApplicationMap().get("somedata");
          //test for null...
          resultx = result;
          
          
          
          
    }

    /**
     * @return the resultx
     */
    public TestStudentXModel getResultx() {
        return resultx;
    }

    /**
     * @param resultx the resultx to set
     */
    public void setResultx(TestStudentXModel resultx) {
        this.resultx = resultx;
    }
    
}
