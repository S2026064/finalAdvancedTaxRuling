package za.gov.sars.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserRole;

/**
 *
 * @author S2026064
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    //This Query was not neccessary, the framework provides for this query by default
    @Query("SELECT e FROM User e WHERE e.idNumber=:idNumber OR e.passPortNumber=:passPortNumber")
    User findByIdNumberOrPassPortNumber(@Param("idNumber") String idNumber, @Param("passPortNumber") String passPortNumber);

    User findByIdNumberAndPassPortNumber(String idNumber, String passPortNumber);

    //This Query was not neccessary, the framework provides for this query by default
    @Query("SELECT e FROM User e WHERE e.sid LIKE %:sid%")
    User searchBySid(@Param("sid") String sid);

    User findBySid(String sid);

    User findByIdNumber(String idNumber);

    Page<User> findByUserRole(UserRole userRole, Pageable pageable);

    List<User> findByUserRole_DescriptionIgnoreCaseIn(List<String> descriptions);

    //This Query was not neccessary, the framework provides for this query by default
    @Query("SELECT e FROM User e WHERE e.userRole.description=:descript")
    List<User> searchUserByUserRoleDescription(@Param("descript") String description);

    List<User> findByUserRoleDescription(String description);

    //This Query was not neccessary, the framework provides for this query by default
    @Query("SELECT e FROM User e WHERE e.sid LIKE %:sid%")
    List<User> searchForSystemUsers(String sid);

    List<User> findBySidContaining(String sid);

    //This Query was not neccessary, the framework provides for this query by default
    @Query("SELECT e FROM User e WHERE e.sid LIKE %:sid% OR e.firstName LIKE %:fullname% OR e.lastName LIKE %:fullname%")
    List<User> searchBySidOrFirstNameOrLastName(@Param("sid") String sid, @Param("fullname") String fullname);

    List<User> findBySidOrFirstNameOrLastName(String sid, String firstName, String lastName);

}
