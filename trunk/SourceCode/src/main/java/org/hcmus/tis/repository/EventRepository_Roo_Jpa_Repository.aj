// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.repository;

import org.hcmus.tis.model.Event;
import org.hcmus.tis.repository.EventRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect EventRepository_Roo_Jpa_Repository {
    
    declare parents: EventRepository extends JpaRepository<Event, Long>;
    
    declare parents: EventRepository extends JpaSpecificationExecutor<Event>;
    
    declare @type: EventRepository: @Repository;
    
}
