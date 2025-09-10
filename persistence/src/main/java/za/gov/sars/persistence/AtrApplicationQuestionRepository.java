package za.gov.sars.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.AtrApplicationQuestion;

/**
 *
 * @author S2026064
 */
@Repository
public interface AtrApplicationQuestionRepository extends JpaRepository<AtrApplicationQuestion, Long>, JpaSpecificationExecutor<AtrApplicationQuestion> {
    

  public  List<AtrApplicationQuestion> findByAtrApplication(ATRApplication atrApplication);
}
