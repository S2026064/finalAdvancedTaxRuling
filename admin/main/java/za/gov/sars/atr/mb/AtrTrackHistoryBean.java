/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.atr.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import za.gov.sars.common.ApplicationType;
import za.gov.sars.common.ClassDescription;
import za.gov.sars.common.EntityType;
import za.gov.sars.common.Province;
import za.gov.sars.common.RepresentativeType;
import za.gov.sars.common.ResourceType;
import za.gov.sars.common.ResponseOption;
import za.gov.sars.common.RulingArea;
import za.gov.sars.common.RulingType;
import za.gov.sars.common.CloseType;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.AtrApplicationQuestion;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.dto.AtrApplicationRevisionDTO;
import za.gov.sars.service.AtrApplicationQuestionServiceLocal;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.UserAtrApplicationServiceLocal;
import za.gov.sars.service.UserServiceLocal;

/**
 *
 * @author S2030707
 */
@ManagedBean
@ViewScoped
public class AtrTrackHistoryBean extends BaseBean<ATRApplication> {

    @Autowired
    private AtrApplicationServiceLocal atrApplicationService;

    private String applicantName;
    private List<AtrApplicationRevisionDTO> revisions;
    private ATRApplication selectedApplication;
    private ATRApplication selectedRevision;
    private Long selectedRevisionNumber;
//
    @Autowired
    private AtrApplicationQuestionServiceLocal atrApplicationQuestionService;

    @Autowired
    private UserAtrApplicationServiceLocal userAtrApplicationService;

    @Autowired
    private UserServiceLocal userService;

    private Long applicationId;

    private List<UserAtrApplication> userDetails;
    private UserAtrApplication userApplication;
    private User userDetail;
    private List<RulingArea> rulingAreas = new ArrayList<>();
    private List<RulingType> rulingTypes = new ArrayList<>();
    private List< ApplicationType> applicationTypes = new ArrayList<>();
    private List< EntityType> entityTypes = new ArrayList<>();
    private List<ClassDescription> descriptions = new ArrayList<>();
    private List<CloseType> saResidents = new ArrayList<>();
    private List<RepresentativeType> representativeTypes = new ArrayList<>();
    private List<Province> provinces = new ArrayList<>();
    private List<SelectItem> responsesList = new ArrayList<>();
    private Integer tabIndex = 0;
    private String naturalPerson;

    private List<AtrApplicationQuestion> applicationQuestions = new ArrayList<>();
    private String globalFilter;
    private Page<ATRApplication> slices;

    @PostConstruct
    public void init() {
        reset().setList(true);
        rulingTypes.addAll(Arrays.asList(RulingType.values()));
        applicationTypes.addAll(Arrays.asList(ApplicationType.values()));
        entityTypes.addAll(Arrays.asList(EntityType.values()));
        provinces.addAll(Arrays.asList(Province.values()));
        saResidents.addAll(Arrays.asList(CloseType.values()));
         Arrays.asList(ResponseOption.values()).stream()
                .forEach(responseOption -> {
                    responsesList.add(new SelectItem(responseOption, responseOption.toString()));
                });
        Pageable pageable = PageRequest.of(0, 30);
        slices = atrApplicationService.findAllApplications(pageable);
        addCollections(slices.toList());

    }

    public void loadRevisions() {
        if (selectedApplication != null) {
         applicationId = selectedApplication.getId();
            revisions = atrApplicationService.findRevisionsByApplicationId(applicationId);
            applicantName = selectedApplication.getApplicantName();
        }
    }

    public void loadRevisionData(Number thisRevisionNumber) {
        reset().setViewHistory(true);
        if (selectedApplication != null && thisRevisionNumber != null) {

            selectedRevision = atrApplicationService.findApplicationAtRevision(
                    selectedApplication.getId(),
                    thisRevisionNumber.intValue()
            );
            addEntity(selectedRevision);
        }
        applicationQuestions = atrApplicationQuestionService.findByAtrApplication(selectedRevision);
        userApplication = userAtrApplicationService.findUsersByAtrApplicationIdAndResourceType(selectedApplication, ResourceType.APPLICANT);
        userDetail = userApplication.getUser();

        addEntity(selectedRevision);
    }

    public void onRowSelect(SelectEvent<ATRApplication> event) {
        ATRApplication atrApplication = event.getObject();
        addEntity(atrApplication);
        reset().setAdd(true);
        loadRevisions();
    }

    public void next(Integer index) {
        if (!getEntity().isTermsAndConditions()) {
            addWarningMessage("Please accept Terms and Conditions before you continue.");
            return;
        }
        applicationQuestions.stream().filter(response -> (response.getQuestion().getDescription().equalsIgnoreCase("16. Is the Applicant a SMME (Small, Medium and Micro Enterprise)?"))).forEachOrdered(response -> {
            naturalPerson = response.getResponseOption().toString();
        });
        setTabIndex(index);
    }

    public Boolean companyQuestionSelectedYes(String description) {
        AtrApplicationQuestion foundQuestion = findQuestionByDescription(description);

        if (isResponseOptionNotNull(foundQuestion)) {
            return foundQuestion.getResponseOption().equals(ResponseOption.YES);
        }
        return false;
    }

    private boolean isResponseOptionNotNull(AtrApplicationQuestion question) {
        return question != null && question.getResponseOption() != null;
    }

    private AtrApplicationQuestion findQuestionByDescription(String description) {
        return this.applicationQuestions.stream()
                .filter(res -> res.getQuestion().getDescription().equalsIgnoreCase(description))
                .findFirst()
                .orElse(null);
    }
 public boolean isSubQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            return false;
        }

        char firstChar = question.trim().charAt(0);
        return Character.isLetter(firstChar);
    }
    public List<ATRApplication> getAtrApplications() {
        return this.getCollections();
    }

    public void back() {
        reset().setList(true);
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public List<AtrApplicationRevisionDTO> getRevisions() {
        return revisions;
    }

    public void setRevisions(List<AtrApplicationRevisionDTO> revisions) {
        this.revisions = revisions;
    }

    public String getGlobalFilter() {
        return globalFilter;
    }

    public void setGlobalFilter(String globalFilter) {
        this.globalFilter = globalFilter;
    }

    public Page<ATRApplication> getSlices() {
        return slices;
    }

    public void setSlices(Page<ATRApplication> slices) {
        this.slices = slices;
    }

    public ATRApplication getSelectedApplication() {
        return selectedApplication;
    }

    public void setSelectedApplication(ATRApplication selectedApplication) {
        this.selectedApplication = selectedApplication;
    }



    public void setSelectedRevision(ATRApplication selectedRevision) {
        this.selectedRevision = selectedRevision;
    }

    public Long getSelectedRevisionNumber() {
        return selectedRevisionNumber;
    }

    public void setSelectedRevisionNumber(Long selectedRevisionNumber) {
        this.selectedRevisionNumber = selectedRevisionNumber;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public List<AtrApplicationQuestion> getApplicationQuestions() {
        return applicationQuestions;
    }

    public void setApplicationQuestions(List<AtrApplicationQuestion> applicationQuestions) {
        this.applicationQuestions = applicationQuestions;
    }

    public List<UserAtrApplication> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<UserAtrApplication> userDetails) {
        this.userDetails = userDetails;
    }


    public List<RulingArea> getRulingAreas() {
        return rulingAreas;
    }

    public void setRulingAreas(List<RulingArea> rulingAreas) {
        this.rulingAreas = rulingAreas;
    }

    public List<RulingType> getRulingTypes() {
        return rulingTypes;
    }

    public void setRulingTypes(List<RulingType> rulingTypes) {
        this.rulingTypes = rulingTypes;
    }

    public List<ApplicationType> getApplicationTypes() {
        return applicationTypes;
    }

    public void setApplicationTypes(List<ApplicationType> applicationTypes) {
        this.applicationTypes = applicationTypes;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(List<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public List<ClassDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<ClassDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public List<CloseType> getSaResidents() {
        return saResidents;
    }

    public void setSaResidents(List<CloseType> saResidents) {
        this.saResidents = saResidents;
    }

    public List<RepresentativeType> getRepresentativeTypes() {
        return representativeTypes;
    }

    public void setRepresentativeTypes(List<RepresentativeType> representativeTypes) {
        this.representativeTypes = representativeTypes;
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    public List<SelectItem> getResponsesList() {
        return responsesList;
    }

    public void setResponsesList(List<SelectItem> responsesList) {
        this.responsesList = responsesList;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getNaturalPerson() {
        return naturalPerson;
    }

    public void setNaturalPerson(String naturalPerson) {
        this.naturalPerson = naturalPerson;
    }

    public User getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(User applicant) {
        this.userDetail = applicant;
    }

    public UserAtrApplication getUserApplication() {
        return userApplication;
    }

    public void setUserApplication(UserAtrApplication userApplication) {
        this.userApplication = userApplication;
    }

    
}
