// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.repository;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.repository.IterationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect IterationRepository_Roo_Jpa_Repository {
    
    declare parents: IterationRepository extends JpaRepository<Iteration, Long>;
    
    declare parents: IterationRepository extends JpaSpecificationExecutor<Iteration>;
    
    declare @type: IterationRepository: @Repository;
    
}
