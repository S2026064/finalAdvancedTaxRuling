package za.gov.sars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.Timesheet;
import za.gov.sars.domain.User;
import za.gov.sars.persistence.TimesheetRepository;

/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class TimesheetService implements TimesheetServiceLocal {

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Override
    public void deleteAll() {
        timesheetRepository.deleteAll();
    }

    @Override
    public List<Timesheet> findAll() {
        return timesheetRepository.findAll();
    }

    @Override
    public Page<Timesheet> listAll(Pageable pageable) {
        return timesheetRepository.findAll(pageable);
    }

    @Override
    public Timesheet save(Timesheet entity) {
        return timesheetRepository.save(entity);
    }

    @Override
    public Timesheet findById(Long id) {
        return timesheetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                                "The requested id [" + id
                                + "] does not exist."));
    }

    @Override
    public Timesheet update(Timesheet entity) {
        return timesheetRepository.save(entity);
    }

    @Override
    public Timesheet deleteById(Long id) {
        Timesheet timesheet = findById(id);
        if (timesheet != null) {
            timesheetRepository.delete(timesheet);
        }
        return timesheet;
    }

    @Override
    public Page<Timesheet> findAll(Specification specification, Pageable pageable) {
        return timesheetRepository.findAll(specification, pageable);
    }

    @Override
    public boolean isExist(Timesheet timesheet) {
        return timesheetRepository.findById(timesheet.getId()) != null;
    }

    @Override
    public Timesheet findByResource(User resource) {
        return timesheetRepository.findByResource(resource);
    }

}
