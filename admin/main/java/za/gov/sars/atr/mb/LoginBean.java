package za.gov.sars.atr.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.springframework.beans.factory.annotation.Autowired;
import za.gov.sars.common.UserStatus;
import za.gov.sars.domain.User;
import za.gov.sars.service.UserServiceLocal;

/**
 *
 * @author S2026080
 */
@ManagedBean
@RequestScoped
public class LoginBean extends BaseBean<User> {

    @Autowired
    private UserServiceLocal userService;

    private String sidParam;

    public void signIn() {
        User user = userService.findBySid(sidParam);
        if (user != null) {
            if (user.getUserStatus().equals(UserStatus.ACTIVE)) {
                getActiveUser().setLogonUserSession(user);
                getActiveUser().setUserLoginIndicator(true);
                redirect("home");
            } else {
                addErrorMessage("System user with SID number", sidParam, " is not active");
            }
        } else {
            addErrorMessage("System user with SID number", sidParam, "does not exist");
        }
    }

    public String getSidParam() {
        return sidParam;
    }

    public void setSidParam(String sidParam) {
        this.sidParam = sidParam;
    }

}
