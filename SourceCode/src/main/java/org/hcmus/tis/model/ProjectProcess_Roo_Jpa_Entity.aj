// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.hcmus.tis.model.ProjectProcess;

privileged aspect ProjectProcess_Roo_Jpa_Entity {
    
    declare @type: ProjectProcess: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long ProjectProcess.id;
    
    @Version
    @Column(name = "version")
    private Integer ProjectProcess.version;
    
    public Long ProjectProcess.getId() {
        return this.id;
    }
    
    public void ProjectProcess.setId(Long id) {
        this.id = id;
    }
    
    public Integer ProjectProcess.getVersion() {
        return this.version;
    }
    
    public void ProjectProcess.setVersion(Integer version) {
        this.version = version;
    }
    
}