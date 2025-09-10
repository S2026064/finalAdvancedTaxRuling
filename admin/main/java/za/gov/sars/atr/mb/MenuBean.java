package za.gov.sars.atr.mb;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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

//    public String routing(String page) {
//        if (page.equalsIgnoreCase("/Users")) {
//            getActiveUser().getRouter().reset().setAdministrator(true);
//        } else if (page.equalsIgnoreCase("/search")) {
//            getActiveUser().getRouter().reset().setTarrif(true);
//        }
//     
//        return defaultRouter(page);
//    }
    public String routeToAdmin(String page) {
        getActiveUser().setModuleWelcomeMessage("Welcome To Administration");
        getActiveUser().getRouter().reset().setAdministrator(true);
        return defaultRouter(page);
    }

    public String routeToApplication(String page) {
        getActiveUser().setModuleWelcomeMessage("Welcome to Application Process");
        getActiveUser().getRouter().reset().setApplication(true);
        return defaultRouter(page);
    }

    public String routeToReport(String page) {
        getActiveUser().setModuleWelcomeMessage("Welcome to Report");
        getActiveUser().getRouter().reset().setReport(true);
        return defaultRouter(page);
    }

}
