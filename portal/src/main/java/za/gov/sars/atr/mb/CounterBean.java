/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.atr.mb;

import java.util.Date;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.gov.sars.domain.VisitCounter;
import za.gov.sars.service.VisitCounterServiceLocal;

/**
 *
 * @author S2028398
 */
@Component
@ManagedBean
@ApplicationScoped
public class CounterBean extends BaseBean<VisitCounter> {

    @Autowired
    private VisitCounterServiceLocal visitCounterService;
    
    public void incrementCounter() {
        visitCounterService.incrementCounter();
    }
    
    public long getVisitCount() {
        return visitCounterService.getCount();
    }

}
