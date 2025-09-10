/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.atr.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import za.gov.sars.common.NotificationType;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.SystemQuery;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.EmailNotificationSenderServiceLocal;
import za.gov.sars.service.SystemQueryServiceLocal;

/**
 *
 * @author S2030702
 */
@ManagedBean
@ViewScoped
public class QueryBean extends BaseBean<SystemQuery> {

    @Autowired
    private AtrApplicationServiceLocal applicationService;

    @Autowired
    private SystemQueryServiceLocal queryService;
    
    @Autowired
    private EmailNotificationSenderServiceLocal emailNotificationSenderService;

    private Slice<ATRApplication> atrApplications;

    private List<SystemQuery> queries = new ArrayList<>();

    private ATRApplication selectedAtrApplication;

    @PostConstruct
    public void init() {
        reset().setList(true);

        Pageable pageable = PageRequest.of(0, 20, Sort.by("createdDate").descending());
//        atrApplications = applicationService.findByIdNumberOrPassPortNumber(getActiveUser().getUser().getIdNumber(), getActiveUser().getUser().getPassPortNumber(), pageable);
        atrApplications = applicationService.listAll(pageable);
    }

    public void createQuery(ATRApplication atrApplication) {
        reset().setAdd(true);
        List<SystemQuery> unreadQueries = queryService.findUnreadByApplicant(atrApplication);

        for (SystemQuery query : unreadQueries) {
            query.setUnreadByApplicant(false);
            queryService.save(query); 
        }
        
        queries = queryService.findByAtrApplication(atrApplication);
        selectedAtrApplication = atrApplication;
        instantiateQueryAndLinkAtrApplication();

    }

    public void submitQuery(SystemQuery query) {
        if(query.getDescription().chars().count() >= 4001) {
            addWarningMessage("Query description characters must not be more than 4000!");
            return;
        }
        query.setUnreadByAdmin(true);
        queryService.save(query);
        queries = queryService.findByAtrApplication(selectedAtrApplication);
        emailNotificationSenderService.sendEmailNotification(NotificationType.SYSTEM_QUERY_FROM_APPLICANT,selectedAtrApplication);
        addInformationMessage("Query sent successfully!");
        instantiateQueryAndLinkAtrApplication();
        reset().setAdd(true);
    }

    public void instantiateQueryAndLinkAtrApplication() {
        SystemQuery query = new SystemQuery();
        query.setCreatedBy("Applicant");
        query.setCreatedDate(new Date());
        query.setAtrApplication(selectedAtrApplication);
        addEntity(query);
    }
    
    public void cancel(SystemQuery query){
        if(query.getId() ==  null){
            remove(query);
            reset().setList(true);
        }
        
    }
    
    public Long getUnreadCount(ATRApplication app) {
        return queryService.countUnreadByApplicant(app);
    }

    public Slice<ATRApplication> getAtrApplications() {
        return atrApplications;
    }

    public void setAtrApplications(Slice<ATRApplication> atrApplications) {
        this.atrApplications = atrApplications;
    }

    public List<SystemQuery> getQueries() {
        return queries;
    }

    public void setQueries(List<SystemQuery> queries) {
        this.queries = queries;
    }

    public ATRApplication getSelectedAtrApplication() {
        return selectedAtrApplication;
    }

    public void setSelectedAtrApplication(ATRApplication selectedAtrApplication) {
        this.selectedAtrApplication = selectedAtrApplication;
    }

}
