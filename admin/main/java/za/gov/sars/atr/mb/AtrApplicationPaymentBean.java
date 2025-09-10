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
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.Payment;
import za.gov.sars.service.AtrApplicationServiceLocal;

/**
 *
 * @author S2028398
 */
@ManagedBean
@ViewScoped
public class AtrApplicationPaymentBean extends BaseBean<ATRApplication> {
    
    @Autowired
    private AtrApplicationServiceLocal applicationService;
    
    @PostConstruct
    public void init() {
        reset().setList(true);
        addCollections(applicationService.findAll());
    }
    
    public void addOrUpdate(ATRApplication atrApplication) {
        reset().setAdd(true);
        if (atrApplication != null) {
            atrApplication.setUpdatedBy(getActiveUser().getSid());
            atrApplication.setUpdatedDate(new Date());
        } else {
            Payment payment = new Payment();
            payment.setCreatedBy(getActiveUser().getSid());
            payment.setCreatedDate(new Date());
            atrApplication.addPayment(Integer.SIZE, payment); // how it function
            addCollection(atrApplication);
        }
        addEntity(atrApplication);
    }
    
    public void save(ATRApplication atrApplication) {
        if (atrApplication.getId() != null) {
            applicationService.update(atrApplication);
            addInformationMessage("Application was successfully updated.");
        } else {
            applicationService.save(atrApplication);
            addInformationMessage("Application was successfully created.");
        }
        reset().setList(true);
    }
    
    public void cancel() {
        reset().setList(true);
    }
    
    public List<ATRApplication> getAtrApplicationPayments() {
        return this.getCollections();
    }
}
