/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.atr.mb;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
//import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author S2028398
 */
@ManagedBean
@RequestScoped
public class MenuBean extends BaseBean {

    private static final Logger LOG = Logger.getLogger(MenuBean.class.getName());

    @PostConstruct
    public void init() {

    }

    public String route(String page) {
        System.out.println("selected page =" + page);
        return defaultRouter(page);
    }

    public String routing(String page) {
        if (page.equalsIgnoreCase("/application")) {
            getActiveUser().getRouter().reset().setApplicant(true);
        }

        return defaultRouter(page);
    }

    public String routeToApplicant(String page) {
        getActiveUser().setModuleWelcomeMessage("Welcome To Application");
        getActiveUser().getRouter().reset().setApplicant(true);
        return defaultRouter(page);
    }

}
