// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.List;
import org.hcmus.tis.model.MemberInformationIntegrationTest;
import org.hcmus.tis.repository.MemberInformationRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MemberInformationIntegrationTest_Roo_IntegrationTest {
    
    declare @type: MemberInformationIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: MemberInformationIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: MemberInformationIntegrationTest: @Transactional;
    
    @Autowired
    MemberInformationRepository MemberInformationIntegrationTest.memberInformationRepository;
}
