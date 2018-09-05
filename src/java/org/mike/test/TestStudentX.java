/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mike.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "tstd")
@ViewScoped
public class TestStudentX implements Serializable {

    private List<TestStudentXModel> listOfTestStudentXModel = new ArrayList<>();
    private TestStudentXModel selectedTestStudentXModel = new TestStudentXModel();

    @PostConstruct
    public void init() {
        listOfTestStudentXModel = data();
    }

    public List<TestStudentXModel> data() {

        List<TestStudentXModel> lst = new ArrayList<>();
        TestStudentXModel obj = new TestStudentXModel();

        //we will add two data....
        obj.setAge("10");
        obj.setDept("Math");
        obj.setName("Gold");
        //
        lst.add(obj);

        TestStudentXModel obj1 = new TestStudentXModel();
        obj1.setAge("11");
        obj1.setDept("CPT");
        obj1.setName("Peter");

        lst.add(obj1);

        return lst;

    }

    public void search() {
        listOfTestStudentXModel = data();
    }

    public void selectRecord(TestStudentXModel somerec) {

        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            NavigationHandler nav = ctx.getApplication().getNavigationHandler();
            ctx.getExternalContext().getApplicationMap().put("somedata", somerec);
            String url = "anotherpage.xhtml?faces-redirect=true";
            nav.handleNavigation(ctx, null, url);
            ctx.renderResponse();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * @return the listOfTestStudentXModel
     */
    public List<TestStudentXModel> getListOfTestStudentXModel() {
        return listOfTestStudentXModel;
    }

    /**
     * @param listOfTestStudentXModel the listOfTestStudentXModel to set
     */
    public void setListOfTestStudentXModel(List<TestStudentXModel> listOfTestStudentXModel) {
        this.listOfTestStudentXModel = listOfTestStudentXModel;
    }

    /**
     * @return the selectedTestStudentXModel
     */
    public TestStudentXModel getSelectedTestStudentXModel() {
        return selectedTestStudentXModel;
    }

    /**
     * @param selectedTestStudentXModel the selectedTestStudentXModel to set
     */
    public void setSelectedTestStudentXModel(TestStudentXModel selectedTestStudentXModel) {
        this.selectedTestStudentXModel = selectedTestStudentXModel;
    }

}
