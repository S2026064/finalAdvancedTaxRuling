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
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import za.gov.sars.listener.CustomRevisionListener;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "REVINFO")
@RevisionEntity(CustomRevisionListener.class)
public class CustomRevisionEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "REV")
    private int id;

    @RevisionTimestamp
    @Column(name = "REVTSTMP")
    private long timestamp;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "OPERATION", length = 20)
    private String operation;

    @OneToMany(mappedBy = "revision", cascade = CascadeType.ALL)
    private Set<EntityChange> changes = new HashSet<>();

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Date getRevisionDate() {
        return new Date(timestamp);
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Set<EntityChange> getChanges() {
        return changes;
    }

    public void setChanges(Set<EntityChange> changes) {
        this.changes = changes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addChange(String entityClassName, String entityId, String action) {
        this.changes.add(new EntityChange(this, entityClassName, entityId, action));
    }
}
