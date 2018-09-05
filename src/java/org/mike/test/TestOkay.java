/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mike.test;

import com.schlmgt.filter.ThreadRunner;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.mike.test.ThreadTest;

/**
 *
 * @author Gold
 */
@ManagedBean(name = "hello")
@ViewScoped
public class TestOkay implements Serializable {

    private boolean valueForThread;

    @PostConstruct
    public void init() {

        try {
            ExecutorService service = Executors.newCachedThreadPool();

            Future<Boolean> future = service.submit(new ThreadTest());

            Boolean val = future.get();
            System.out.println(" this is val " + val);
            if (!val) {
                System.out.println("This is false");
                service.shutdown();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goldDude() {
        FacesMessage msg;
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println(isValueForThread() + " what is this");
        setValueForThread(true);
        valueForThread = true;
        System.out.println("Helo Gold");
        System.out.println(isValueForThread() + " This is it");

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Excel is in wrong format. It should be in this format: ", "Excel is in wrong format. It should be in this format: ");
        context.addMessage(null, message);
    }

    public boolean isValueForThread() {
        return valueForThread;
    }

    public void setValueForThread(boolean valueForThread) {
        this.valueForThread = valueForThread;
    }

}
