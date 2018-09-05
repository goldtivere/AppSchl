/**
 *
 * @author: micheal abobade
 * @email: pagims2003@yahoo.com
 * @mobile: 234-8065-711-043
 * @date: 2016-07-17
 */
package com.schlmgt.menu;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "pgeNav")
@SessionScoped
public class PageNavigation implements Serializable {

    private String page;
    private String titlePane;

    public PageNavigation() {

        setPage("/templates/common/content.xhtml");
        setTitlePane(titlePane);

    }
    
    
    public void okay() {

        setTitlePane("active");
    }

    

    /**
     * @return the page
     */
    public String getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * @return the titlePane
     */
    public String getTitlePane() {
        return titlePane;
    }

    /**
     * @param titlePane the titlePane to set
     */
    public void setTitlePane(String titlePane) {
        this.titlePane = titlePane;
    }

}
