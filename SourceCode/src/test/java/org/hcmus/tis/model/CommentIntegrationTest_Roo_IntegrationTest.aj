// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.List;
import org.hcmus.tis.model.Comment;
import org.hcmus.tis.model.CommentDataOnDemand;
import org.hcmus.tis.model.CommentIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CommentIntegrationTest_Roo_IntegrationTest {
    
    declare @type: CommentIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: CommentIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: CommentIntegrationTest: @Transactional;
    
    @Autowired
    private CommentDataOnDemand CommentIntegrationTest.dod;
    
    
}