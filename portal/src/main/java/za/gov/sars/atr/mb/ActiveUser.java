/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.atr.mb;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import za.gov.sars.common.Router;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserRole;

/**
 *
 * @author S2028398
 */
@ManagedBean
@SessionScoped
public class ActiveUser implements Serializable {

    private String sid;
    private String firstName;
    private String lastName;
    private UserRole userRole;
    private User user;
    private boolean userLoginIndicator;
    private String moduleWelcomeMessage;
    private String loggedOnUserFullName;
    private String fullName;
    private String uploadUrl;
    private String downloadUrl;
    private String idNumber;
    private Router router = new Router();

    public ActiveUser() {
        userLoginIndicator = Boolean.FALSE;
    }

    public void setLogonUserSession(User user) {
        if (user != null) {
            this.setUserRole(user.getUserRole());
            this.setLoggedOnUserFullName(user.getFullName());
            this.setFullName(user.getFullName());
            this.setSid(user.getSid());
            this.setIdNumber(user.getIdNumber());
            this.setUserLoginIndicator(true);
            this.setUser(user);
        }
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isUserLoginIndicator() {
        return userLoginIndicator;
    }

    public void setUserLoginIndicator(boolean userLoginIndicator) {
        this.userLoginIndicator = userLoginIndicator;
    }

    public String getModuleWelcomeMessage() {
        return moduleWelcomeMessage;
    }

    public void setModuleWelcomeMessage(String moduleWelcomeMessage) {
        this.moduleWelcomeMessage = moduleWelcomeMessage;
    }

    public String getLoggedOnUserFullName() {
        return loggedOnUserFullName;
    }

    public void setLoggedOnUserFullName(String loggedOnUserFullName) {
        this.loggedOnUserFullName = loggedOnUserFullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

   

}
