/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.filter;

import com.schlmgt.login.Login;
import com.schlmgt.login.UserDetails;
import static com.sun.faces.facelets.util.Path.context;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Gold
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})
public class filter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {

            HttpServletRequest reqt = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            HttpSession ses = reqt.getSession(false);
            String reqURI = reqt.getRequestURI();

            if (ses != null && ses.getAttribute("sessn_nums") != null) {
                UserDetails l = (UserDetails) ses.getAttribute("sessn_nums");
                if (l.getRoleAssigned() == 1 && (reqURI.contains("faces/pages/register/") || reqURI.contains("faces/pages/profile/") || reqURI.contains("faces/pages/report/"))) {
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }

                if (l.getRoleAssigned() == 2 && !l.isCanUpdateSubject() && reqURI.contains("faces/pages/student/")) {
                     resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } 
                
                if (l.getRoleAssigned() == 2 && !l.isCanUpdateResult()&& reqURI.contains("faces/pages/result/")) {
                     resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } 
                
                if (l.getRoleAssigned() == 2 && !l.isCanSendMessage()&& reqURI.contains("faces/pages/mail/")) {
                     resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } 
                
                 if (l.getRoleAssigned() == 2 && (!l.isCanRegisterStaff() || !l.isCanRegisterStudent()) && reqURI.contains("faces/pages/register/")) {
                     resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } 
                 
                 if (l.getRoleAssigned() == 2 && !l.isCanManageSchool()&& reqURI.contains("faces/pages/role/")) {
                     resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } 
               

            }
            if (reqURI.contains("/faces/index.xhtml") || (ses != null && ses.getAttribute("sessn_nums") != null) || reqURI.contains("javax.faces.resource")) {
                chain.doFilter(request, response);
            } else if (reqURI.contains("faces/pages/createStaff/index.xhtml") || reqURI.contains("faces/pages/create/index.xhtml")) {
                chain.doFilter(request, response);
            } else {
                resp.sendRedirect(reqt.getContextPath() + "/faces/index.xhtml");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void destroy() {
    }

}
