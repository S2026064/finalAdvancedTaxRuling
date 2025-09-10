/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.atr.util;

import java.time.LocalDate;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.springframework.beans.factory.annotation.Autowired;
import za.gov.sars.atr.mb.BaseBean;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.service.AtrApplicationServiceLocal;

/**
 *
 * @author S2026064
 */
@ManagedBean
@RequestScoped
public class UniqueReferenceGenerator extends BaseBean {

    @Autowired
    private AtrApplicationServiceLocal applicationService;

    private Long caseNumber;

    public Long generateNumber() {
        int year = LocalDate.now().getYear();
        ATRApplication application = applicationService.findLastInsertedRecord();
        if (application != null) {
            caseNumber = application.getCaseNum() + 1L;
        } else {
          // 
            caseNumber =  Long.parseLong(String.format("%d%06d", year, 1));
        }
        return caseNumber;
    }

    public Long getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(Long caseNumber) {
        this.caseNumber = caseNumber;
    }

}
