package za.gov.sars.persistence;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.ATRApplication;

/**
 *
 * @author S2026064
 */
@Repository
@Transactional
public interface AtrApplicationRevisionRepository {

    public List<Object[]> findRevisions(Long applicationId);

    public ATRApplication findAtRevision(Long applicationId, Integer revisionNumber);

    @Query("SELECT e FROM [ATR_DB].[dbo].[atr_application_AUD] e WHERE e.revtype=:version  AND (CONVERT(date, a.createdDate) >=:startDate AND CONVERT(date, a.createdDate)<=:endDate)")
    List<ATRApplication> findByRevtypeAndDate(@Param("version") String version, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
