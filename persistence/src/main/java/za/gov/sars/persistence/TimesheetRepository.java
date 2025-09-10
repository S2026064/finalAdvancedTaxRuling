package za.gov.sars.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import za.gov.sars.domain.Timesheet;
import za.gov.sars.domain.User;

/**
 *
 * @author S2026064
 */
@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long>, JpaSpecificationExecutor<Timesheet> {

    Timesheet findByResource(User resource);
}