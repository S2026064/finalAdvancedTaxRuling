package za.gov.sars.service;

import java.util.Date;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.VisitCounter;
import za.gov.sars.persistence.VisitCounterRepository;

/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class VisitCounterService implements VisitCounterServiceLocal {

    private Long currentCount;
    @Autowired
    private VisitCounterRepository visitCounterRepository;

    @PostConstruct
    @Transactional
    @Override
    public void init() {
        VisitCounter counter = visitCounterRepository.initializeCounter();
        this.currentCount = counter.getCount();
    }

    @Transactional
    @Override
    public synchronized void incrementCounter() {
        visitCounterRepository.incrementCounter();
        this.currentCount++;
    }

    @Override
    public long getCount() {
        return this.currentCount;
    }

    @Override
    public VisitCounter save(VisitCounter visit) {
     return  visitCounterRepository.save(visit);
    }

    @Override
    public VisitCounter findById(Long id) {
     return visitCounterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public VisitCounter update(VisitCounter entity) {
       return visitCounterRepository.save(entity);
    }

    @Override
    public VisitCounter deleteById(Long id) {
         VisitCounter visitCounter = findById(id);
        if (visitCounter != null) {
            visitCounterRepository.delete(visitCounter);
        }
        return visitCounter;
    }

    @Override
    public boolean isExist(VisitCounter visit) {
      return visitCounterRepository.findById(visit.getId()) != null;
    }

    @Override
    public Page<VisitCounter> findAll(Specification specification, Pageable pageable) {
         return visitCounterRepository.findAll(specification, pageable);
    }
}
