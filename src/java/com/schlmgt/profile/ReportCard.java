/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.profile;

import com.schlmgt.updateResult.ResultModel;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "report")
@ViewScoped
public class ReportCard implements Serializable {

    private List<ResultModel> mode;
    private SessionModel sec;    
    private String name;

    @PostConstruct
    public void init() {
        resultDetails();
    }

    public void resultDetails() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        List<ResultModel> secResult = (List<ResultModel>) ctx.getExternalContext().getApplicationMap().get("examRecord");
        SessionModel sesModel = (SessionModel) ctx.getExternalContext().getApplicationMap().get("studentprofile");
        //test for null...
        mode = secResult;
        sec = sesModel;

        if (mode != null) {
            for (ResultModel m : mode) {
                setName(m.getStudentId());
                System.out.println("This is it: " + m.getSubject() + " again: " + sesModel.getStudentClass());
            }
        }
    }

    public List<ResultModel> getMode() {
        return mode;
    }

    public void setMode(List<ResultModel> mode) {
        this.mode = mode;
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SessionModel getSec() {
        return sec;
    }

    public void setSec(SessionModel sec) {
        this.sec = sec;
    }

}
