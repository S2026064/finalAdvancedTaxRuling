package za.gov.sars.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.FeedBack;


/**
 *
 * @author S2024726
 */
public interface FeedBackServiceLocal {

    public FeedBack save(FeedBack feedBack);

    public FeedBack findById(Long id);

    public void deleteAll();

    FeedBack deleteById(Long id);

    public FeedBack update(FeedBack feedBack);

    public List<FeedBack> findAll();

    Page<FeedBack> listAll(Pageable pageable);

    public boolean isExist(FeedBack feedBack);
    
    List<FeedBack> findByAtrApplication(ATRApplication atrApplication);
    
    List<FeedBack> findUnreadByApplicant(ATRApplication app);
    
    List<FeedBack> findUnreadByAdmin(ATRApplication app);
    
    Long countUnreadByApplicant(ATRApplication app);
    
    Long countUnreadByAdmin(ATRApplication app);
}
