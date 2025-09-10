package za.gov.sars.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserRole;

/**
 *
 * @author S2024726
 */
public interface UserServiceLocal extends GenericServiceLocal<User> {

    public void deleteAll();

    public List<User> findAll();

    Page<User> listAll(Pageable pageable);

    User findByIdNumberAndPassPortNumber(String idNumber, String passPortNumber);

    User findBySid(String sid);

    User findByIdNumber(String idNumber);

    List<User> findByUserRole(List<String> descriptions);

    Page<User> findByUserRole(UserRole userRole, Pageable pageable);

    List<User> findUserByUserRoleDescription(String description);

    List<User> findBySidOrFirstNameOrLastName(String searchName);

    List<User> searchForSystemUsers(String searchParam);
}
