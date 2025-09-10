/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.atr.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import za.gov.sars.common.NotificationType;
import za.gov.sars.domain.EmailNotification;
import za.gov.sars.domain.UserRole;
import za.gov.sars.service.EmailNotificationServiceLocal;
import za.gov.sars.service.UserRoleServiceLocal;

/**
 *
 * @author S2026064
 */
@ManagedBean
@ViewScoped
public class EmailNotificationBean extends BaseBean<EmailNotification> {

    @Autowired
    private EmailNotificationServiceLocal emailNotificationService;
    @Autowired
    private UserRoleServiceLocal userRoleService;

    private List<NotificationType> notificationTypes = new ArrayList<>();
    private List<UserRole> allRecipients;
    private List<UserRole> additionalRecipientsOptions;
    private List<UserRole> selectedAdditionalRecipients;

    private Page<EmailNotification> slices;

    @PostConstruct
    public void init() {
        reset().setList(true);
        setPanelTitleName("Email Notifications");
        Pageable pageable = PageRequest.of(0, 20);
        notificationTypes.addAll(Arrays.asList(NotificationType.values()));
        slices = emailNotificationService.findAll(pageable);
        addCollections(slices.toList());

        allRecipients = userRoleService.findAll().stream().filter(role -> !"System Administrator".equalsIgnoreCase(role.getDescription())).collect(Collectors.toList());
        selectedAdditionalRecipients = new ArrayList<>();
        additionalRecipientsOptions = new ArrayList<>();

    }

    public void addOrUpdate(EmailNotification emailNotification) {
        reset().setAdd(true);
        if (emailNotification != null) {
            setPanelTitleName("Update Email Notification");
            emailNotification.setUpdatedBy(getActiveUser().getSid());
            emailNotification.setUpdatedDate(new Date());
            additionalRecipientsOptions = emailNotification.getAdditionalRecipients();
        } else {
            setPanelTitleName("Add Email Notification");
            emailNotification = new EmailNotification();
            emailNotification.setCreatedBy(getActiveUser().getSid());
            emailNotification.setCreatedDate(new Date());
            addCollection(emailNotification);
        }
        addEntity(emailNotification);
    }

    public void save(EmailNotification emailNotification) {
         emailNotification.setAdditionalRecipients(selectedAdditionalRecipients);
        if (emailNotification.getId() != null) {
            emailNotificationService.update(emailNotification);
            addInformationMessage("Email Notification was successfully updated.");
        } else {
            emailNotificationService.save(emailNotification);
            addInformationMessage("Email Notification successfully created.");
        }
        reset().setList(true);
        setPanelTitleName("Email Notifications");
    }

    public void cancel() {
        reset().setList(true);
        setPanelTitleName("Email Notifications");
    }

    public void delete(EmailNotification emailNotification) {
        emailNotificationService.deleteById(emailNotification.getId());
        remove(emailNotification);
        addInformationMessage("Email Notification was successfully deleted");
        reset().setList(true);
        setPanelTitleName("Email Notifications");
    }

    public void onPrimaryRecipientChange() {
        UserRole primaryRecipient = getEntity().getPrimaryRecipient();
        if (primaryRecipient == null) {
            additionalRecipientsOptions = new ArrayList<>();
            return;
        }
        String primaryName = primaryRecipient.getDescription();

        additionalRecipientsOptions = allRecipients.stream()
                .filter(role -> !role.equals(primaryRecipient))
                .filter(role -> {
                    if ("applicant".equalsIgnoreCase(primaryName)) {
                        // exclude only applicant
                        return !"applicant".equalsIgnoreCase(role.getDescription());
                    } else {
                        // exclude primary and applicant
                        return !"applicant".equalsIgnoreCase(role.getDescription());
                    }
                })
                .collect(Collectors.toList());

        selectedAdditionalRecipients.clear();
    }
    
    public String stringifyAdditionalRecipients(List<UserRole> recipients){
        return recipients.stream()
            .map(UserRole::getDescription)
            .collect(Collectors.joining(";"));
    }

    public Integer getPageNumber() {
        return slices.getNumber() + 1;
    }

    public boolean isNextPage() {
        return slices.hasNext();
    }

    public boolean isPreviousPage() {
        return slices.hasPrevious();
    }

    public Integer getNumberOfPages() {
        return slices.getTotalPages();
    }

    public void firstPageDocuments() {
        slices = emailNotificationService.findAll(slices.previousOrFirstPageable());
    }

    public void lastPageDocuments() {
        slices = emailNotificationService.findAll(slices.nextOrLastPageable());
    }

    public void nextDocuments() {
        if (slices.hasNext()) {
            slices = emailNotificationService.findAll(slices.nextPageable());
        }
    }

    public void previousDocuments() {
        if (slices.hasPrevious()) {
            slices = emailNotificationService.findAll(slices.previousPageable());
        }
    }

    public List<EmailNotification> getEmailNotifications() {
        return this.getCollections();
    }

    public List<NotificationType> getNotificationTypes() {
        return notificationTypes;
    }

    public void setNotificationTypes(List<NotificationType> notificationTypes) {
        this.notificationTypes = notificationTypes;
    }

    public List<UserRole> getAllRecipients() {
        return allRecipients;
    }

    public void setAllRecipients(List<UserRole> allRecipients) {
        this.allRecipients = allRecipients;
    }

    public List<UserRole> getAdditionalRecipientsOptions() {
        return additionalRecipientsOptions;
    }

    public void setAdditionalRecipientsOptions(List<UserRole> additionalRecipientsOptions) {
        this.additionalRecipientsOptions = additionalRecipientsOptions;
    }

    public List<UserRole> getSelectedAdditionalRecipients() {
        return selectedAdditionalRecipients;
    }

    public void setSelectedAdditionalRecipients(List<UserRole> selectedAdditionalRecipients) {
        this.selectedAdditionalRecipients = selectedAdditionalRecipients;
    }

}
