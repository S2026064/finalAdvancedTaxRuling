package za.gov.sars.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import za.gov.sars.common.DocumentType;
import za.gov.sars.domain.SupportingDocument;

/**
 *
 * @author S2026064
 */
@Repository
public interface SupportingDocumentRepository extends JpaRepository<SupportingDocument, Long> , JpaSpecificationExecutor<SupportingDocument>{
    SupportingDocument findByDescription(String description);
    List<SupportingDocument> findByDocumentType(DocumentType documentType);
}
