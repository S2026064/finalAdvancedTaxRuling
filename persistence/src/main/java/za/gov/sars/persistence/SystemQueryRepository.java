package za.gov.sars.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.SystemQuery;

/**
 *
 * @author S2026064
 */
@Repository
public interface SystemQueryRepository extends JpaRepository<SystemQuery, Long>, JpaSpecificationExecutor<SystemQuery> {

    SystemQuery findByDescription(String description);

    List<SystemQuery> findByAtrApplication(ATRApplication atrApplication);

    @Query("SELECT q FROM SystemQuery q WHERE q.atrApplication = :app AND q.unreadByAdmin = true")
    List<SystemQuery> findUnreadByAdmin(@Param("app") ATRApplication app);

    @Query("SELECT q FROM SystemQuery q WHERE q.atrApplication = :app AND q.unreadByApplicant = true")
    List<SystemQuery> findUnreadByApplicant(@Param("app") ATRApplication app);

    @Query("SELECT COUNT(q) FROM SystemQuery q WHERE q.atrApplication = :app AND q.unreadByAdmin = true")
    Long countUnreadByAdmin(@Param("app") ATRApplication app);

    @Query("SELECT COUNT(q) FROM SystemQuery q WHERE q.atrApplication = :app AND q.unreadByApplicant = true")
    Long countUnreadByApplicant(@Param("app") ATRApplication app);

}
