package za.gov.sars.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.VisitCounter;

/**
 *
 * @author S2026064
 */
@Repository
public interface VisitCounterRepository extends JpaRepository<VisitCounter, Long>, JpaSpecificationExecutor<VisitCounter> {

    @Query("SELECT v FROM VisitCounter v")
    VisitCounter findCounter();

    @Query("SELECT v FROM VisitCounter v ORDER BY v.id ASC")
    VisitCounter findFirstCounter();

    @Modifying
    @Transactional
     @Query("UPDATE VisitCounter v SET v.count = v.count + 1 WHERE v.id = (SELECT MIN(v2.id) FROM VisitCounter v2)")
    void incrementCounter();
    
     default VisitCounter initializeCounter() {
        VisitCounter counter = findFirstCounter();
        if (counter == null) {
            counter = new VisitCounter();
            save(counter);
        }
        return counter;
    }
}
