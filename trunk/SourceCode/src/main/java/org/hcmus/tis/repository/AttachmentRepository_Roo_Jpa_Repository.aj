// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.repository;

import org.hcmus.tis.model.Attachment;
import org.hcmus.tis.repository.AttachmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect AttachmentRepository_Roo_Jpa_Repository {
    
    declare parents: AttachmentRepository extends JpaRepository<Attachment, Long>;
    
    declare parents: AttachmentRepository extends JpaSpecificationExecutor<Attachment>;
    
    declare @type: AttachmentRepository: @Repository;
    
}