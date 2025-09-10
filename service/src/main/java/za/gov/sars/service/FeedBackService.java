package za.gov.sars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.FeedBack;
import za.gov.sars.persistence.FeedBackRepository;

/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class FeedBackService implements FeedBackServiceLocal {

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Override
    public void deleteAll() {
        feedBackRepository.deleteAll();
    }

    @Override
    public List<FeedBack> findAll() {
        return feedBackRepository.findAll();
    }

    @Override
    public Page<FeedBack> listAll(Pageable pageable) {
        return feedBackRepository.findAll(pageable);
    }

    @Override
    public FeedBack save(FeedBack feedBack) {
        return feedBackRepository.save(feedBack);
    }

    @Override
    public FeedBack findById(Long id) {
          return feedBackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public FeedBack update(FeedBack feedBack) {
        return feedBackRepository.save(feedBack);
    }

    @Override
    public FeedBack deleteById(Long id) {
        FeedBack feedBack = findById(id);
        if (feedBack != null) {
            feedBackRepository.delete(feedBack);
        }
        return feedBack;
    }

    @Override
    public boolean isExist(FeedBack entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<FeedBack> findByAtrApplication(ATRApplication atrApplication) {
        return feedBackRepository.findByAtrApplication(atrApplication);
    }

    @Override
    public List<FeedBack> findUnreadByApplicant(ATRApplication app) {
        return feedBackRepository.findUnreadByApplicant(app);
    }

    @Override
    public List<FeedBack> findUnreadByAdmin(ATRApplication app) {
        return feedBackRepository.findUnreadByAdmin(app);
    }

    @Override
    public Long countUnreadByApplicant(ATRApplication app) {
        return feedBackRepository.countUnreadByApplicant(app);
    }

    @Override
    public Long countUnreadByAdmin(ATRApplication app) {
        return feedBackRepository.countUnreadByAdmin(app);
    }


}
