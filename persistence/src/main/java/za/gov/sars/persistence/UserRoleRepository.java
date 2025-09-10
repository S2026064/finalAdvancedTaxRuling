package za.gov.sars.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import za.gov.sars.domain.UserRole;

/**
 *
 * @author S2026064
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> , JpaSpecificationExecutor<UserRole>{
    UserRole findByDescription(String description);
}
