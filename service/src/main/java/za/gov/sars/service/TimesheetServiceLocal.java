package za.gov.sars.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.domain.Timesheet;
import za.gov.sars.domain.User;

/**
 *
 * @author S2024726
 */
public interface TimesheetServiceLocal extends GenericServiceLocal<Timesheet> {

    public void deleteAll();

    public List<Timesheet> findAll();

    Page<Timesheet> listAll(Pageable pageable);

    Timesheet findByResource(User resource);
}
