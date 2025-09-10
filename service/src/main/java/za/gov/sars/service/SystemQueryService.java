package za.gov.sars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.SystemQuery;
import za.gov.sars.persistence.SystemQueryRepository;


/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class SystemQueryService implements SystemQueryServiceLocal {

    @Autowired
    private SystemQueryRepository systemQueryRepository;

    @Override
    public void deleteAll() {
        systemQueryRepository.deleteAll();
    }

    @Override
    public List<SystemQuery> findAll() {
       return systemQueryRepository.findAll();
    }

    @Override
    public Page<SystemQuery> listAll(Pageable pageable) {
         return systemQueryRepository.findAll(pageable);
    }

    @Override
    public SystemQuery save(SystemQuery systemQuery) {
        return systemQueryRepository.save(systemQuery);
    }

    @Override
    public SystemQuery findById(Long id) {
       return systemQueryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public SystemQuery update(SystemQuery systemQuery) {
        return systemQueryRepository.save(systemQuery);
    }

    @Override
    public SystemQuery deleteById(Long id) {
         SystemQuery systemQuery = findById(id);
        if (systemQuery != null) {
            systemQueryRepository.delete(systemQuery);
        }
        return systemQuery; 
    }

    @Override
    public boolean isExist(SystemQuery entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<SystemQuery> findAll(Specification specification, Pageable pageable) {
          return systemQueryRepository.findAll(specification, pageable);
    }

    @Override
    public List<SystemQuery> findByAtrApplication(ATRApplication atrApplication) {
        return systemQueryRepository.findByAtrApplication(atrApplication);
    }

    @Override
    public List<SystemQuery> findUnreadByApplicant(ATRApplication app) {
        return systemQueryRepository.findUnreadByApplicant(app);
    }

    @Override
    public List<SystemQuery> findUnreadByAdmin(ATRApplication app) {
        return systemQueryRepository.findUnreadByAdmin(app);
    }

    @Override
    public Long countUnreadByApplicant(ATRApplication app) {
        return systemQueryRepository.countUnreadByApplicant(app);
    }

    @Override
    public Long countUnreadByAdmin(ATRApplication app) {
        return systemQueryRepository.countUnreadByAdmin(app);
    }


   
}
