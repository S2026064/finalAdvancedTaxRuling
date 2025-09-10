package za.gov.sars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.UserRole;
import za.gov.sars.persistence.UserRoleRepository;


/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class UserRoleService implements UserRoleServiceLocal {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole findById(Long id) {
        return userRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public void deleteAll() {
        userRoleRepository.deleteAll();
    }

    @Override
    public UserRole update(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public boolean isExist(UserRole userRole) {
        return userRoleRepository.findByDescription(userRole.getDescription()) != null;
    }

    @Override
    public UserRole findByDescription(String description) {
        return userRoleRepository.findByDescription(description);
    }

    @Override
    public UserRole deleteById(Long id) {
        UserRole userRole = findById(id);

        if (userRole != null) {
            userRoleRepository.delete(userRole);
        }
        return userRole;
    }

    @Override
    public List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }

    @Override
    public Page<UserRole> listAll(Pageable pageable) {
        return userRoleRepository.findAll(pageable);
    }

    @Override
    public Page<UserRole> findAll(Specification specification, Pageable pageable) {
        return userRoleRepository.findAll(specification, pageable);
    }
}
