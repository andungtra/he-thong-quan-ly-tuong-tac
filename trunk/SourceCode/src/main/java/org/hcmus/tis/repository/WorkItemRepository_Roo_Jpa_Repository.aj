// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.repository;

import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.repository.WorkItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect WorkItemRepository_Roo_Jpa_Repository {
    
    declare parents: WorkItemRepository extends JpaRepository<WorkItem, Long>;
    
    declare parents: WorkItemRepository extends JpaSpecificationExecutor<WorkItem>;
    
    declare @type: WorkItemRepository: @Repository;
    
}