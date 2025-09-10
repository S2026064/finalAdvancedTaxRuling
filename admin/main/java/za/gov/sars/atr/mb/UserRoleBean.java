/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.atr.mb;

import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import za.gov.sars.domain.AdministrationSettings;
import za.gov.sars.domain.AtrApplicationSettings;
//import za.gov.sars.domain.TimesheetSettings;
import za.gov.sars.domain.UserPermission;
import za.gov.sars.domain.UserRole;
import za.gov.sars.service.UserRoleServiceLocal;

/**
 *
 * @author S2030707
 */
@ManagedBean
@ViewScoped
public class UserRoleBean extends BaseBean<UserRole> {

    @Autowired
    private UserRoleServiceLocal userRoleService;

    @PostConstruct
    public void init() {
        reset().setList(true);
        addCollections(userRoleService.findAll());
    }

    public void addorUpdate(UserRole userRole) {
        reset().setAdd(true);
        if (userRole != null) {
            userRole.setUpdatedBy(getActiveUser().getSid());
            userRole.setUpdatedDate(new Date());
        } else {
            userRole = new UserRole();
            userRole.setCreatedBy(getActiveUser().getSid());
            userRole.setCreatedDate(new Date());

            AtrApplicationSettings artAtrApplicationSettings = new AtrApplicationSettings();
            artAtrApplicationSettings.setCreatedBy(getActiveUser().getSid());
            artAtrApplicationSettings.setCreatedDate(new Date());
            userRole.setAtrApplicationSettings(artAtrApplicationSettings);

            AdministrationSettings administrationSettings = new AdministrationSettings();
            administrationSettings.setCreatedBy(getActiveUser().getSid());
            administrationSettings.setCreatedDate(new Date());
            userRole.setAdministrationSettings(administrationSettings);

            UserPermission userPermission = new UserPermission();
            userPermission.setCreatedBy(getActiveUser().getSid());
            userPermission.setCreatedDate(new Date());
            userRole.setUserPermission(userPermission);
            addCollection(userRole);
        }
        addEntity(userRole);
    }

    public void save(UserRole userRole) {
        if (userRole.getId() != null) {
            userRoleService.update(userRole);
            addInformationMessage("User role successfully updated");
        } else {
            userRoleService.save(userRole);
            addInformationMessage("User role successfully added");
        }
        reset().setList(true);
    }

    public void cancel(UserRole userRole) {
        if (userRole.getId() == null && getUserRoles().contains(userRole)) {
            remove(userRole);
        }
        reset().setList(true);
    }

    public void delete(UserRole userRole) {
        userRoleService.deleteById(userRole.getId());
        remove(userRole);
        addInformationMessage("User role successfully deleted");
        reset().setList(true);
    }

    public List<UserRole> getUserRoles() {
        return this.getCollections();
    }

}
