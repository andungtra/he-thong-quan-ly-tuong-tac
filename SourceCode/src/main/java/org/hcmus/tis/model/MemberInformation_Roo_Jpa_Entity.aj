// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.hcmus.tis.model.MemberInformation;

privileged aspect MemberInformation_Roo_Jpa_Entity {
    
    declare @type: MemberInformation: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long MemberInformation.id;
    
    @Version
    @Column(name = "version")
    private Integer MemberInformation.version;
    
    public Long MemberInformation.getId() {
        return this.id;
    }
    
    public void MemberInformation.setId(Long id) {
        this.id = id;
    }
    
    public Integer MemberInformation.getVersion() {
        return this.version;
    }
    
    public void MemberInformation.setVersion(Integer version) {
        this.version = version;
    }
    
}
