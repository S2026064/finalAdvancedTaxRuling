package za.gov.sars.atr.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import za.gov.sars.domain.User;

/**
 *
 * @author S2028398
 */
@ManagedBean
@RequestScoped
public class HomeBean extends BaseBean<User> {

    private static final String LANDING_PAGE = "/landing.xhtml";
    private static final String EXPIRY_PAGE = "/expired.xhtml?faces-redirect=true";

    public String routeToAdministration() {
        if (getActiveUser() != null) {
            getActiveUser().setModuleWelcomeMessage("Welcome to Administration Page");
            getActiveUser().getRouter().reset().setAdministrator(true);
            return LANDING_PAGE;
        }
        return EXPIRY_PAGE;
    }

    public String routeToApplication() {
        if (getActiveUser() != null) {
            getActiveUser().setModuleWelcomeMessage("Welcome to ATR Application Page");
            getActiveUser().getRouter().reset().setApplication(true);
            return LANDING_PAGE;
        }
        return EXPIRY_PAGE;
    }

    public String routeToReport() {
        if (getActiveUser() != null) {
            getActiveUser().setModuleWelcomeMessage("Welcome to ATR report Page");
            getActiveUser().getRouter().reset().setReport(true);
            return LANDING_PAGE;
        }
        return EXPIRY_PAGE;
    }

}
