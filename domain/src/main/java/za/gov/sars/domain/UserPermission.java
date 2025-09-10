/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

/**
 *
 * @author S2026095
 */
@Audited
@Entity
@Getter
@Setter
@Table(name = "user_perm")
public class UserPermission extends BaseEntity {
    @Column(name = "view_record")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean read;

    @Column(name = "update_record")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean update;

    @Column(name = "delete_record")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean delete;

    @Column(name = "search_record")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean search;

    @Column(name = "add_record")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean add;

    @Column(name = "create_record")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean write;

   

    public UserPermission() {
        this.read = Boolean.FALSE;
        this.update = Boolean.FALSE;
        this.delete = Boolean.FALSE;
        this.search = Boolean.FALSE;
        this.write = Boolean.FALSE;
        this.add = Boolean.FALSE;
        
    }
   
  
    public UserPermission reset() {
        this.setDelete(false);
        this.setRead(false);
        this.setUpdate(false);
        this.setSearch(false);
        this.setAdd(false);
        this.setWrite(false);
        return this;
    }
}
