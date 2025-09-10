package za.gov.sars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserRole;
import za.gov.sars.persistence.UserRepository;

/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class UserService implements UserServiceLocal {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean isExist(User user) {
        return userRepository.findBySid(user.getSid()) != null;
    }

    @Override
    public User findBySid(String sid) {
        return userRepository.findBySid(sid);
    }

    @Override
    public User findByIdNumberAndPassPortNumber(String idNumber, String passPortNumber) {
        return userRepository.findByIdNumberAndPassPortNumber(idNumber, passPortNumber);
    }

    @Override
    public Page<User> findByUserRole(UserRole userRole, Pageable pageable) {
        return userRepository.findByUserRole(userRole, pageable);
    }

    @Override
    public User deleteById(Long id) {
        User user = findById(id);
        if (user != null) {
            userRepository.delete(user);
        }
        return user;
    }

    @Override
    public List<User> searchForSystemUsers(String searchParam) {
        return userRepository.searchForSystemUsers(searchParam);
    }

    @Override
    public List<User> findBySidOrFirstNameOrLastName(String searchName) {
        return userRepository.findBySidOrFirstNameOrLastName(searchName, searchName,searchName);
    }

    @Override
    public List<User> findUserByUserRoleDescription(String description) {
        return userRepository.findByUserRoleDescription(description);
    }

    @Override
    public Page<User> listAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findAll(Specification specification, Pageable pageable) {
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public User findByIdNumber(String idNumber) {
        return userRepository.findByIdNumber(idNumber);
    }

    @Override
    public List<User> findByUserRole(List<String> descriptions) {
       return userRepository.findByUserRole_DescriptionIgnoreCaseIn(descriptions);
    }

}
