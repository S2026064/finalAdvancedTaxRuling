package za.gov.sars.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.SystemQuery;


/**
 *
 * @author S2024726
 */
public interface SystemQueryServiceLocal extends GenericServiceLocal<SystemQuery> {

    public void deleteAll();

    public List<SystemQuery> findAll();

    Page<SystemQuery> listAll(Pageable pageable);
    
    List<SystemQuery> findByAtrApplication(ATRApplication atrApplication);
    
    List<SystemQuery> findUnreadByApplicant(ATRApplication app);
    
    List<SystemQuery> findUnreadByAdmin(ATRApplication app);
    
    Long countUnreadByApplicant(ATRApplication app);
    
    Long countUnreadByAdmin(ATRApplication app);
}
