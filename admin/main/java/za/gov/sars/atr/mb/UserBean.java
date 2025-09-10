/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.atr.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.omnifaces.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import za.gov.sars.common.UserStatus;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserRole;
import za.gov.sars.service.UserInformationService;
import za.gov.sars.service.UserRoleServiceLocal;
import za.gov.sars.service.UserServiceLocal;

/**
 *
 * @author S2030707
 */
@ManagedBean
@ViewScoped
public class UserBean extends BaseBean<User> {

    @Autowired
    private UserServiceLocal userService;
    @Autowired
    private UserRoleServiceLocal userRoleService;
    @Autowired
    private UserInformationService userInformationService;

    private List<UserStatus> userStatuses = new ArrayList();
    private List<UserRole> userRoles = new ArrayList();
    private List<User> managers = new ArrayList();

    private boolean managerField;
    private String sid;
    private String searchParameter;

    @PostConstruct
    public void init() {
        reset().setList(true);
        managerField = Boolean.FALSE;
        userStatuses.addAll(Arrays.asList(UserStatus.values()));
        userRoles.addAll(userRoleService.findAll());
        addCollections(userService.findAll());
    }

    public void addUser() {
        reset().setSearch(true);
    }

    public void addorUpdate(User user) {
        reset().setAdd(true);
        if (user == null) {
            addWarningMessage("User selected does not exist, please try again");
            return;
        }
        setPanelTitleName("Update user");
        managers.clear();
        if (user.getUserRole().getDescription().equalsIgnoreCase("Resource")) {
            managerField = Boolean.TRUE;
            managers.addAll(userService.findUserByUserRoleDescription("Manager"));
        }
        user.setUpdatedBy(getActiveUser().getSid());
        user.setUpdatedDate(new Date());
        addEntity(user);
    }

    public void searchUser() {
        if (this.sid.isEmpty() || this.sid == null || this.sid.equals("")) {
            addWarningMessage("Please enter user SID");
            return;
        }
        User existingUser = userService.findBySid(sid);

        if (existingUser != null) {
            addWarningMessage("User with sid " + sid + " already exist");
            return;
        }
        // if the user does not exist in the local DB we fectch it on SAP DB
        User sapUser = userInformationService.getUserBySid(sid, getActiveUser().getSid());
        if (sapUser == null) {
            addWarningMessage("User with sid " + sid + " does not exist as a SARS employee");
            return;
        }
        addCollection(sapUser);
        addEntity(sapUser);
        reset().setAdd(true);
        managerField = Boolean.FALSE;
    }

    public void save(User user) {
        if (user.getId() != null) {
            userService.update(user);
            addInformationMessage("User updated successfully");

        } else {
            userService.save(user);
            addInformationMessage("User has been saved successfully");
        }
        reset().setList(true);
    }

    public void cancel(User user) {
        if (user.getId() == null && getUsers().contains(user)) {
            remove(user);
        }
        reset().setList(true);
    }

    public void onSystemUserSearchListener() {
        getCollections().clear();
        if (searchParameter.isEmpty()) {
            addWarningMessage("Enter search criteria");
            return;
        }
        addCollections(userService.findBySidOrFirstNameOrLastName(searchParameter));
        if (getCollections().isEmpty()) {
            addInformationMessage("User is not registered on the system");
            return;
        }
        setPanelTitleName("Users");
    }

    public List<User> getUsers() {
        return this.getCollections();
    }

    public void userRoleManagers() {
        managers.clear();
        switch (getEntity().getUserRole().getDescription()) {
            case "Resource":
                managerField = Boolean.TRUE;
                managers.addAll(userService.findUserByUserRoleDescription("Manager"));
                break;
                case "Manager":
                managerField = Boolean.TRUE;
                managers.addAll(userService.findUserByUserRoleDescription("Senior Manager"));
                break;
            default:
                managerField = Boolean.FALSE;
                break;
        }
    }

    public List<UserStatus> getUserStatuses() {
        return userStatuses;
    }

    public void setUserStatuses(List<UserStatus> userStatuses) {
        this.userStatuses = userStatuses;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

    public List<User> getManagers() {
        return managers;
    }

    public void setManagers(List<User> managers) {
        this.managers = managers;
    }

    public boolean isManagerField() {
        return managerField;
    }

    public void setManagerField(boolean managerField) {
        this.managerField = managerField;
    }

}
