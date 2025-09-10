/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.service;

import java.util.Date;
import java.util.List;
import za.gov.sars.common.ApplicationType;
import za.gov.sars.common.EntityType;
import za.gov.sars.common.NotificationType;
import za.gov.sars.common.QuestionType;
import za.gov.sars.common.ReportType;
import za.gov.sars.common.RepresentativeType;
import za.gov.sars.common.RulingArea;
import za.gov.sars.common.RulingType;
import za.gov.sars.common.Status;
import za.gov.sars.common.TaxAct;
import za.gov.sars.common.UserStatus;
import za.gov.sars.common.YesNo;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.ActSection;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.AdministrationSettings;
import za.gov.sars.domain.AtrApplicationSettings;
import za.gov.sars.domain.ChargeOutRateCategory;
import za.gov.sars.domain.EmailNotification;
import za.gov.sars.domain.Person;
import za.gov.sars.domain.Question;
import za.gov.sars.domain.SystemQuery;
import za.gov.sars.domain.Timesheet;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserApplicationActivity;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.domain.UserPermission;
import za.gov.sars.domain.UserRole;

/**
 *
 * @author s2024726
 */
public class BootStrapHelper {

    public static UserRole getUserRole(String description) {
        UserRole userRole = new UserRole();
        userRole.setCreatedBy("s2026080");
        userRole.setLoggedOnUserFullName("Andile Qumbisa");
        userRole.setCreatedDate(new Date());
        userRole.setDescription(description);

        AdministrationSettings administrationSettings = new AdministrationSettings();
        administrationSettings.setCreatedBy("s2026080");
        administrationSettings.setLoggedOnUserFullName("Tshepo Mahlangu");
        administrationSettings.setCreatedDate(new Date());
        administrationSettings.setUsers(true);
        administrationSettings.setUserRoles(true);
        administrationSettings.setActivity(true);
        administrationSettings.setQuestion(true);
        administrationSettings.setDocumentum(true);
        userRole.setAdministrationSettings(administrationSettings);

        AtrApplicationSettings atrApplicationSettings = new AtrApplicationSettings();
        atrApplicationSettings.setCreatedBy("s2026080");
        atrApplicationSettings.setLoggedOnUserFullName("Tshepo Mahlangu");
        atrApplicationSettings.setCreatedDate(new Date());
        atrApplicationSettings.setEstimationPlan(true);
        atrApplicationSettings.setAllocation(true);
        atrApplicationSettings.setPayment(true);
        atrApplicationSettings.setDocument(true);
        atrApplicationSettings.setReAssign(true);
        atrApplicationSettings.setQuery(true);
        atrApplicationSettings.setQuery(true);
        atrApplicationSettings.setAssign(true);
        atrApplicationSettings.setRuling(true);
        atrApplicationSettings.setApplication(true);
        atrApplicationSettings.setTimesheet(true);
        atrApplicationSettings.setTimesheetReview(true);
        userRole.setAtrApplicationSettings(atrApplicationSettings);

        UserPermission permission = new UserPermission();
        permission.setCreatedBy("s2026080");
        permission.setLoggedOnUserFullName("Andile Qumbisa");
        permission.setCreatedDate(new Date());
        permission.setRead(true);
        permission.setAdd(true);
        permission.setDelete(true);
        permission.setUpdate(true);
        permission.setWrite(true);
        permission.setSearch(true);
        userRole.setUserPermission(permission);
        return userRole;
    }

    public static Person getPerson(String idNumber, String name, String cellphone, String telephone, String incomeTaxNum) {
        Person person = new Person();
        person.setCreatedBy("s2026080");
        person.setLoggedOnUserFullName("Andile Qumbisa");
        person.setCreatedDate(new Date());
        person.setIdNumber(idNumber);
        person.setFirstName(name);
        person.setLastName("Jiyeza");
        person.setCellphone(cellphone);
        person.setTelephone(telephone);
        person.setIncomeTaxNum(incomeTaxNum);
        return person;
    }

    public static User getUser(String fullname, String firstName, String lastName, String sid, String username, String email, String cellphone, String telephone, String incomeTaxNum, String IdNumber) {
        User user = new User();
        user.setUserStatus(UserStatus.ACTIVE);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setFullName(fullname);
        user.setCreatedBy("s2024726");
        user.setLoggedOnUserFullName("Vongani Maluleke");
        user.setCreatedDate(new Date());
        user.setEmailAddress(email);
        user.setCellphone(cellphone);
        user.setTelephone(telephone);
        user.setIncomeTaxNum(incomeTaxNum);
        user.setIdNumber(IdNumber);
        user.setSid(sid);
        return user;

    }

    public static Activity getActivity(String description) {
        Activity activity = new Activity();
        activity.setCreatedDate(new Date());
        activity.setCreatedBy("s2026080");
        activity.setDescription(description);
        return activity;
    }

    public static Question getQuestion(String description, String questionDetail, QuestionType questionType) {
        Question question = new Question();
        question.setCreatedBy("S2026064");
        question.setQuestionType(questionType);
        question.setCreatedDate(new Date());
        question.setDescription(description);
        question.setQuestionDetail(questionDetail);

        return question;
    }

    public static ATRApplication getAtrApplication(Status status, RulingType rulingType, String applicantName, ApplicationType applicantType) {
        ATRApplication atrApplication = new ATRApplication();
        atrApplication.setCreatedDate(new Date());
        atrApplication.setCreatedBy("s2026080");
        atrApplication.setApplicantType(applicantType);

        atrApplication.setAuditedBySars(YesNo.YES);
        atrApplication.setConnectedParties(YesNo.YES);
        atrApplication.setConnectedPerson(YesNo.YES);
        atrApplication.setViewObtained(YesNo.YES);
        atrApplication.setSaResident(YesNo.YES);
        atrApplication.setApplicantName(applicantName);
        atrApplication.setApplicationType(ApplicationType.THIRD_PARTY);
        atrApplication.setEntityType(EntityType.COMPANY);
        atrApplication.setReportType(ReportType.CLASS_RULING);
        atrApplication.setRepresentativeType(RepresentativeType.CLASS);
        atrApplication.setRulingArea(RulingArea.CAPITAL_GAIN_TAX);
        atrApplication.setRulingType(RulingType.PRIVATE_RULING);
        atrApplication.setStatus(Status.CASE_INPROGRESS);

        return atrApplication;
    }

    public static Timesheet getTimesheet(Activity activity, User resource, ATRApplication atrApplication) {
        Timesheet timesheet = new Timesheet();
        timesheet.setCreatedBy("S2026064");
        timesheet.setCreatedDate(new Date());
//        timesheet.setActivity(activity);
        timesheet.setResource(resource);
//        timesheet.setAtrApplication(atrApplication);
        return timesheet;
    }

    public static UserApplicationActivity getUserApplicationActivity(User resource, Activity activity, ATRApplication atrApplication) {
        UserApplicationActivity userApplicationActivity = new UserApplicationActivity();
        userApplicationActivity.setCreatedDate(new Date());
        userApplicationActivity.setCreatedBy("s2026080");
        userApplicationActivity.setUser(resource);
        userApplicationActivity.setActivity(activity);
        userApplicationActivity.setAtrApplication(atrApplication);
        return userApplicationActivity;
    }

    public static UserAtrApplication getUserAtrApplication(User resource, ATRApplication atrApplication) {
        UserAtrApplication userAtrApplication = new UserAtrApplication();
        userAtrApplication.setCreatedDate(new Date());
        userAtrApplication.setCreatedBy("s2026080");
        return userAtrApplication;
    }

    public static ChargeOutRateCategory getChargeOutRateCategory() {
        ChargeOutRateCategory category = new ChargeOutRateCategory();
        category.setCreatedDate(new Date());
        category.setCreatedBy("s2026080");
        return category;
    }

    public static SystemQuery getQuery(String description) {
        SystemQuery query = new SystemQuery();
        query.setDescription(description);
        query.setCreatedDate(new Date());

        return query;
    }

    public static EmailNotification getNotification(NotificationType notificationType, UserRole primaryRecipient, List<UserRole> additionalRecipients, String subject, String body) {
        EmailNotification notification = new EmailNotification();
        notification.setCreatedBy("S2026064");
        notification.setCreatedDate(new Date());
        notification.setBody(body);
        notification.setNotificationType(notificationType);
        notification.setSubject(subject);
        notification.setPrimaryRecipient(primaryRecipient);
        notification.setAdditionalRecipients(additionalRecipients);

        return notification;
    }

    public static ActSection getActSection(TaxAct taxAct,String type ,String description) {
        ActSection actSection = new ActSection();
        actSection.setCreatedDate(new Date());
        actSection.setCreatedBy("S2030702");
        actSection.setTaxAct(taxAct);
        actSection.setDescription(description);
        actSection.setType(type);
        
        return actSection;
    }

}
