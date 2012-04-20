// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.List;
import org.hcmus.tis.model.IterationDataOnDemand;
import org.hcmus.tis.model.IterationIntegrationTest;
import org.hcmus.tis.service.IterationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect IterationIntegrationTest_Roo_IntegrationTest {
    
    declare @type: IterationIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: IterationIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: IterationIntegrationTest: @Transactional;
    
    @Autowired
    private IterationDataOnDemand IterationIntegrationTest.dod;
    
    @Autowired
    IterationService IterationIntegrationTest.iterationService;
    
    @Test
    public void IterationIntegrationTest.testCountAllIterations() {
        Assert.assertNotNull("Data on demand for 'Iteration' failed to initialize correctly", dod.getRandomIteration());
        long count = iterationService.countAllIterations();
        Assert.assertTrue("Counter for 'Iteration' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void IterationIntegrationTest.testFindIteration() {
        Iteration obj = dod.getRandomIteration();
        Assert.assertNotNull("Data on demand for 'Iteration' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Iteration' failed to provide an identifier", id);
        obj = iterationService.findIteration(id);
        Assert.assertNotNull("Find method for 'Iteration' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Iteration' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void IterationIntegrationTest.testFindAllIterations() {
        Assert.assertNotNull("Data on demand for 'Iteration' failed to initialize correctly", dod.getRandomIteration());
        long count = iterationService.countAllIterations();
        Assert.assertTrue("Too expensive to perform a find all test for 'Iteration', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Iteration> result = iterationService.findAllIterations();
        Assert.assertNotNull("Find all method for 'Iteration' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Iteration' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void IterationIntegrationTest.testFindIterationEntries() {
        Assert.assertNotNull("Data on demand for 'Iteration' failed to initialize correctly", dod.getRandomIteration());
        long count = iterationService.countAllIterations();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Iteration> result = iterationService.findIterationEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Iteration' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Iteration' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void IterationIntegrationTest.testFlush() {
        Iteration obj = dod.getRandomIteration();
        Assert.assertNotNull("Data on demand for 'Iteration' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Iteration' failed to provide an identifier", id);
        obj = iterationService.findIteration(id);
        Assert.assertNotNull("Find method for 'Iteration' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyIteration(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Iteration' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void IterationIntegrationTest.testUpdateIterationUpdate() {
        Iteration obj = dod.getRandomIteration();
        Assert.assertNotNull("Data on demand for 'Iteration' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Iteration' failed to provide an identifier", id);
        obj = iterationService.findIteration(id);
        boolean modified =  dod.modifyIteration(obj);
        Integer currentVersion = obj.getVersion();
        Iteration merged = (Iteration)iterationService.updateIteration(obj);
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Iteration' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void IterationIntegrationTest.testSaveIteration() {
        Assert.assertNotNull("Data on demand for 'Iteration' failed to initialize correctly", dod.getRandomIteration());
        Iteration obj = dod.getNewTransientIteration(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Iteration' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Iteration' identifier to be null", obj.getId());
        iterationService.saveIteration(obj);
        obj.flush();
        Assert.assertNotNull("Expected 'Iteration' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void IterationIntegrationTest.testDeleteIteration() {
        Iteration obj = dod.getRandomIteration();
        Assert.assertNotNull("Data on demand for 'Iteration' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Iteration' failed to provide an identifier", id);
        obj = iterationService.findIteration(id);
        iterationService.deleteIteration(obj);
        obj.flush();
        Assert.assertNull("Failed to remove 'Iteration' with identifier '" + id + "'", iterationService.findIteration(id));
    }
    
}
