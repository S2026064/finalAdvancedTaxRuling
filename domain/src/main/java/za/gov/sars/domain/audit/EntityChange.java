/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.domain.audit;

/**
 *
 * @author S2026080
 */
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ENTITY_CHANGES")
public class EntityChange implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "REV_ID", nullable = false)
    private CustomRevisionEntity revision;

    @Column(name = "ENTITY_CLASS", length = 200)
    private String entityClassName;

    @Column(name = "ENTITY_ID", length = 100)
    private String entityId;

    @Column(name = "ACTION", length = 10)
    private String action;

    public EntityChange() {
    }

    public EntityChange(CustomRevisionEntity revision, String entityClassName, String entityId, String action) {
        this.revision = revision;
        this.entityClassName = entityClassName;
        this.entityId = entityId;
        this.action = action;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomRevisionEntity getRevision() {
        return revision;
    }

    public void setRevision(CustomRevisionEntity revision) {
        this.revision = revision;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
