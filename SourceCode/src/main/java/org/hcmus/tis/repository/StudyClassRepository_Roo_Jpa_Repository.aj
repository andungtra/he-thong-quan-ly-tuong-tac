// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.repository;

import org.hcmus.tis.model.StudyClass;
import org.hcmus.tis.repository.StudyClassRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect StudyClassRepository_Roo_Jpa_Repository {
    
    declare parents: StudyClassRepository extends JpaRepository<StudyClass, Long>;
    
    declare parents: StudyClassRepository extends JpaSpecificationExecutor<StudyClass>;
    
    declare @type: StudyClassRepository: @Repository;
    
}
