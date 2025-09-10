package za.gov.sars.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.domain.UserRole;


/**
 *
 * @author S2028873
 */
public interface UserRoleServiceLocal extends GenericServiceLocal<UserRole>{
    public void deleteAll();
    public List<UserRole> findAll();
    Page<UserRole> listAll(Pageable pageable);
    public UserRole findByDescription(String description);

}
