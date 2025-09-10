package za.gov.sars.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.FeedBack;


/**
 *
 * @author S2026064
 */
@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, Long> , JpaSpecificationExecutor<FeedBack>{
    FeedBack findByDescription(String description);
    
    List<FeedBack> findByAtrApplication(ATRApplication atrApplication);

    @Query("SELECT q FROM FeedBack q WHERE q.atrApplication = :app AND q.unreadByAdmin = true")
    List<FeedBack> findUnreadByAdmin(@Param("app") ATRApplication app);

    @Query("SELECT q FROM FeedBack q WHERE q.atrApplication = :app AND q.unreadByApplicant = true")
    List<FeedBack> findUnreadByApplicant(@Param("app") ATRApplication app);

    @Query("SELECT COUNT(q) FROM FeedBack q WHERE q.atrApplication = :app AND q.unreadByAdmin = true")
    Long countUnreadByAdmin(@Param("app") ATRApplication app);

    @Query("SELECT COUNT(q) FROM FeedBack q WHERE q.atrApplication = :app AND q.unreadByApplicant = true")
    Long countUnreadByApplicant(@Param("app") ATRApplication app);
}
