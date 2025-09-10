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
import za.gov.sars.domain.ChargeOutRateCategory;
import za.gov.sars.service.ChargeOutRateCategoryServiceLocal;

/**
 *
 * @author S2028398
 */
@ManagedBean
@ViewScoped
public class ChargeOutRateCategoryBean extends BaseBean<ChargeOutRateCategory> {

    @Autowired
    private ChargeOutRateCategoryServiceLocal chargeOutRateCategoryService;

    @PostConstruct
    public void init() {
        reset().setList(true);
        addCollections(chargeOutRateCategoryService.findAll());
    }

    public void addorUpdate(ChargeOutRateCategory category) {
        reset().setAdd(true);
        if (category != null) {
            category.setUpdatedBy(getActiveUser().getSid());
            category.setUpdatedDate(new Date());
        } else {
            category = new ChargeOutRateCategory();
            category.setCreatedBy(getActiveUser().getSid());
            category.setCreatedDate(new Date());
            addCollection(category);
        }
        addEntity(category);

    }

    public void save(ChargeOutRateCategory category) {
        if (category.getId() != null) {
            chargeOutRateCategoryService.update(category);
            addInformationMessage("Charge Out Rate Category successfully updated");
        } else {
            chargeOutRateCategoryService.save(category);
            addInformationMessage("Charge Out Rate Category successfully saved");
        }
        reset().setList(true);
    }

    public void cancel(ChargeOutRateCategory category) {
        if (category.getId() == null && getCategories().contains(category)){
            remove(category);
        }
           reset().setList(true);
    }

    public void delete(ChargeOutRateCategory category) {
        chargeOutRateCategoryService.deleteById(category.getId());
        remove(category);
        addInformationMessage("Charge Out Rate Category successfully deleted");
        reset().setList(true);
    }

    public List<ChargeOutRateCategory> getCategories() {
        return this.getCollections();
    }

    

}
