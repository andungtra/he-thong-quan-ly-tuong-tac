// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.List;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemDataOnDemand;
import org.hcmus.tis.model.WorkItemIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect WorkItemIntegrationTest_Roo_IntegrationTest {
    
    declare @type: WorkItemIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: WorkItemIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: WorkItemIntegrationTest: @Transactional;
    
    @Autowired
    private WorkItemDataOnDemand WorkItemIntegrationTest.dod;
    
    @Test
    public void WorkItemIntegrationTest.testCountWorkItems() {
        Assert.assertNotNull("Data on demand for 'WorkItem' failed to initialize correctly", dod.getRandomWorkItem());
        long count = WorkItem.countWorkItems();
        Assert.assertTrue("Counter for 'WorkItem' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void WorkItemIntegrationTest.testFindWorkItem() {
        WorkItem obj = dod.getRandomWorkItem();
        Assert.assertNotNull("Data on demand for 'WorkItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'WorkItem' failed to provide an identifier", id);
        obj = WorkItem.findWorkItem(id);
        Assert.assertNotNull("Find method for 'WorkItem' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'WorkItem' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void WorkItemIntegrationTest.testFindAllWorkItems() {
        Assert.assertNotNull("Data on demand for 'WorkItem' failed to initialize correctly", dod.getRandomWorkItem());
        long count = WorkItem.countWorkItems();
        Assert.assertTrue("Too expensive to perform a find all test for 'WorkItem', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<WorkItem> result = WorkItem.findAllWorkItems();
        Assert.assertNotNull("Find all method for 'WorkItem' illegally returned null", result);
        Assert.assertTrue("Find all method for 'WorkItem' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void WorkItemIntegrationTest.testFindWorkItemEntries() {
        Assert.assertNotNull("Data on demand for 'WorkItem' failed to initialize correctly", dod.getRandomWorkItem());
        long count = WorkItem.countWorkItems();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<WorkItem> result = WorkItem.findWorkItemEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'WorkItem' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'WorkItem' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void WorkItemIntegrationTest.testFlush() {
        WorkItem obj = dod.getRandomWorkItem();
        Assert.assertNotNull("Data on demand for 'WorkItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'WorkItem' failed to provide an identifier", id);
        obj = WorkItem.findWorkItem(id);
        Assert.assertNotNull("Find method for 'WorkItem' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyWorkItem(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'WorkItem' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void WorkItemIntegrationTest.testMergeUpdate() {
        WorkItem obj = dod.getRandomWorkItem();
        Assert.assertNotNull("Data on demand for 'WorkItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'WorkItem' failed to provide an identifier", id);
        obj = WorkItem.findWorkItem(id);
        boolean modified =  dod.modifyWorkItem(obj);
        Integer currentVersion = obj.getVersion();
        WorkItem merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'WorkItem' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void WorkItemIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'WorkItem' failed to initialize correctly", dod.getRandomWorkItem());
        WorkItem obj = dod.getNewTransientWorkItem(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'WorkItem' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'WorkItem' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'WorkItem' identifier to no longer be null", obj.getId());
    }
    
}
