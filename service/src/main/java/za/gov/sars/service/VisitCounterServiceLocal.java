package za.gov.sars.service;

import za.gov.sars.domain.VisitCounter;

/**
 *
 * @author S2024726
 */
public interface VisitCounterServiceLocal extends GenericServiceLocal<VisitCounter> {

    public long getCount();

    public void init();
    
     public  void incrementCounter();
}
