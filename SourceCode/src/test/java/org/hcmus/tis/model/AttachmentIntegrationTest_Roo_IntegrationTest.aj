// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.List;
import org.hcmus.tis.model.Attachment;
import org.hcmus.tis.model.AttachmentDataOnDemand;
import org.hcmus.tis.model.AttachmentIntegrationTest;
import org.hcmus.tis.repository.AttachmentRepository;
import org.hcmus.tis.service.AccountService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AttachmentIntegrationTest_Roo_IntegrationTest {
    
    declare @type: AttachmentIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: AttachmentIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: AttachmentIntegrationTest: @Transactional;
    
    @Autowired
    private AttachmentDataOnDemand AttachmentIntegrationTest.dod;
    @Autowired
    AttachmentRepository AttachmentIntegrationTest.attachmentRepository;
    
}