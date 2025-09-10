package za.gov.sars.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author S2026987
 * @param <T>
 */
public interface GenericServiceLocal<T> {

    T save(T entity);

    T findById(Long id);

    T update(T entity);

    T deleteById(Long id);

    boolean isExist(T entity);

    Page<T> findAll(Specification specification, Pageable pageable);
}
