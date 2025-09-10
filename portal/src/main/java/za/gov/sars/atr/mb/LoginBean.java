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

    private String idParam;

    public void signIn() {
        User user = userService.findByIdNumber(idParam);
        if (user != null) {
            if (user.getUserStatus().equals(UserStatus.ACTIVE)) {
                getActiveUser().setLogonUserSession(user);
                getActiveUser().setUserLoginIndicator(true);
                redirect("home");
            } else {
                addErrorMessage("System user with id number", idParam, " is not active");
            }
        } else {
            addErrorMessage("System user with id number", idParam, "does not exist");
        }
    }

    public String getIdParam() {
        return idParam;
    }

    public void setIdParam(String idParam) {
        this.idParam = idParam;
    }

}
