/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.atr.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author S2028398
 */
@ManagedBean
@RequestScoped
public class HomeBean extends BaseBean {

    private static final String LANDING_PAGE = "/landing.xhtml";
    private static final String EXPIRY_PAGE = "/expired.xhtml?faces-redirect=true";
    private static final String APPLICATION_PAGE = "/application.xhtml";

    public String routeToApplicant() {
        if (getActiveUser() != null) {
            getActiveUser().setModuleWelcomeMessage("Welcome to Application Page");
            getActiveUser().getRouter().reset().setApplicant(true);
            return APPLICATION_PAGE;
        }
        return EXPIRY_PAGE;
    }

}
