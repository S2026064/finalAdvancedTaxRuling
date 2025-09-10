/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import za.gov.sars.common.ApplicationType;
import za.gov.sars.common.ClassDescription;
import za.gov.sars.common.EntityType;
import za.gov.sars.common.ReportType;
import za.gov.sars.common.RepresentativeType;
import za.gov.sars.common.RulingArea;
import za.gov.sars.common.RulingType;
import za.gov.sars.common.Status;
import za.gov.sars.common.YesNo;

/**
 *
 * @author S2026080
 */
@Audited
@Getter
@Setter
@Entity
@Table(name = "atr_application")
public class ATRApplication extends BaseEntity {

    @Column(name = "case_number")
    private long caseNum;

    @Column(name = "applicant_name")
    private String applicantName;

//    @Column(name = "grand_total")
//    private double grandTotal;
    @Column(name = "income_tax_ref")
    private String incomeTaxReference;

    @Column(name = "vat_ref_number")
    private String vatReference;

    @Column(name = "paye_ref")
    private String payeReference;

    @Column(name = "case_status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "suggestion")
    private String suggestion;

    @Column(name = "tax_practitioner_number")
    private String taxPractitionerNumber;

    @Column(name = "amount_paid")
    private double amountPaid;

    @Column(name = "ruling_type")
    @Enumerated(EnumType.STRING)
    private RulingType rulingType;

    @Column(name = "application_type")
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    @Column(name = "rsa_resident")
    @Enumerated(EnumType.STRING)
    private YesNo saResident;

    @Column(name = "country")
    private String country;

    @Column(name = "audited_by_sars")
    @Enumerated(EnumType.STRING)
    private YesNo auditedBySars;

    @Column(name = "view_obtained")
    @Enumerated(EnumType.STRING)
    private YesNo viewObtained;

    
    @OneToMany(mappedBy = "atrApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstimatePlan> estimationPlans;

    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "chargesheet_id")
    private ChargeSheet chargeSheet;

    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "validity_details_id")
    private ValidityDetails validityDetail;

    @Column(name = "connected_person")
    @Enumerated(EnumType.STRING)
    private YesNo connectedPerson;

    @Column(name = "entity_type")
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "foreign_entity_id")
    private ForeignEntity foreignEntity;

    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "company_details_id")
    private CompanyDetails companyDetails;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "business_address_id")
    private Address address;

    @Column(name = "connected_parties")
    @Enumerated(EnumType.STRING)
    private YesNo connectedParties;

    @Column(name = "trust_name")
    private String trustName;

    @Column(name = "trust_number")
    private String trustNumber;

    @Column(name = "applicant_email")
    private String applicantEmail;

    @Column(name = "partnership_name")
    private String partnershipName;

    @Column(name = "representative_name")
    private String representativeName;

    @Column(name = "representative_cell")
    private String representativeCell;

    @Column(name = "representative_Tell")
    private String representativeTell;

    @Column(name = "representative_email")
    private String representativeEmail;

    @Column(name = "representative_id")
    private String representativeId;

    @Column(name = "other_description")
    private String otherDescription;

    @Column(name = "terms_and_conditions")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean termsAndConditions;

    @Column(name = "first_reg_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstRegDate;

    @Column(name = "applicant_type")
    @Enumerated(EnumType.STRING)
    private ApplicationType applicantType;

    @Column(name = "class_description")
    @Enumerated(EnumType.STRING)
    private ClassDescription classDescription;

    @Column(name = "report_type")
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(name = "ruling_area")
    @Enumerated(EnumType.STRING)
    private RulingArea rulingArea;

    @Column(name = "representative_type")
    @Enumerated(EnumType.STRING)
    private RepresentativeType representativeType;

    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "tax_type_id")
    private TaxType taxType;

    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    @OneToMany()
    //  @JoinColumn(name = "atr_app_id")
    private List<UserAtrApplication> userAtrApplications;

    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "aTRApplication")
//    @JoinColumn(name = "atr_app_id")
    private List<SupportingDocument> supportingDocuments;

    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "atrApplication")
    private List<RulingActSection> rulingActSections;

    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    @OneToMany
    @JoinColumn(name = "atr_app_id")
    private List<Payment> payments;

    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    @OneToMany
    @JoinColumn(name = "atr_app_id")
    private List<TrustBeneficiary> beneficiaries;

    public void addUserAtrApplication(Integer index, UserAtrApplication userAtrApplication) {
        userAtrApplication.setAtrApplication(this);
        this.userAtrApplications.add(index, userAtrApplication);
    }

    public void removeUserAtrApplication(UserAtrApplication userAtrApplication) {
        this.userAtrApplications.remove(userAtrApplication);
        userAtrApplication.setAtrApplication(null);
    }

    public void addSupportingDocument(SupportingDocument supportingDocument) {
        this.supportingDocuments.add(supportingDocument);
    }

    public void addSupportingDocument(Integer index, SupportingDocument supportingDocument) {
        this.supportingDocuments.add(0, supportingDocument);
    }

    public void removeSupportingDocument(SupportingDocument supportingDocument) {
        this.supportingDocuments.remove(supportingDocument);
    }

    public void removeSupportingDocuments(List<SupportingDocument> documents) {
        this.supportingDocuments.clear();
        for (SupportingDocument document : documents) {
            this.supportingDocuments.remove(document);
        }
    }

    public void addRulingActSection(RulingActSection rulingActSection) {
        this.rulingActSections.add(rulingActSection);
    }

    public void addRulingActSection(Integer index, RulingActSection actSection) {
        this.rulingActSections.add(0, actSection);
    }

    public void removeRulingActSection(RulingActSection actSection) {
        this.rulingActSections.remove(actSection);
    }

    public void removeRulingActSections(List<RulingActSection> actSections) {
        this.rulingActSections.clear();
        for (RulingActSection actSection : actSections) {
            this.rulingActSections.remove(actSection);
        }
    }

    public void addPayment(Integer index, Payment payment) {
        this.payments.add(index, payment);
    }

    public void removePayment(Payment payment) {
        this.payments.remove(payment);
    }

    public void addTrustBeneficiary(TrustBeneficiary beneficiary) {
        this.beneficiaries.add(beneficiary);
    }

    public void removeTrustBeneficiary(TrustBeneficiary beneficiary) {
        this.beneficiaries.remove(beneficiary);

    }

    public void addEstimatePlan(Integer index, EstimatePlan estimatePlan) {
        estimatePlan.setAtrApplication(this);
        this.estimationPlans.add(index, estimatePlan);
    }

    public void addEstimatePlan(EstimatePlan estimatePlan) {
        estimatePlan.setAtrApplication(this);
        this.estimationPlans.add(estimatePlan);
    }

    public void removeEstimatePlan(EstimatePlan estimatePlan) {
        this.estimationPlans.remove(estimatePlan);
        estimatePlan.setAtrApplication(null);
    }

    

    public List<EstimatePlan> getEditablePlans() {
        return estimationPlans.stream()
                .filter(EstimatePlan::isEditable)
                .collect(Collectors.toList());
    }

}
