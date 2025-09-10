/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

/**
 *
 * @author S2026095
 */

@DiscriminatorValue("baseEntity")
@MappedSuperclass
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Audited
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false, updatable = false, insertable = false)
    private Long id;

    
    @Column(name = "created_by")
    private String createdBy;


    @Column(name = "updated_by")
    private String updatedBy;
  
    @Column(name = "logged_on_user")
    private String loggedOnUserFullName;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Transient
    private boolean renderMode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isRenderMode() {
        return renderMode;
    }

    public void setRenderMode(boolean renderMode) {
        this.renderMode = renderMode;
    }

    public String getLoggedOnUserFullName() {
        return loggedOnUserFullName;
    }

    public void setLoggedOnUserFullName(String loggedOnUserFullName) {
        this.loggedOnUserFullName = loggedOnUserFullName;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(83, 7)
                .append(this.id)
                .append(this.createdDate)
                .append(this.createdBy)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BaseEntity other = (BaseEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.createdBy, other.createdBy)) {
            return false;
        }
        return Objects.equals(this.createdDate, other.createdDate);
    }
}
