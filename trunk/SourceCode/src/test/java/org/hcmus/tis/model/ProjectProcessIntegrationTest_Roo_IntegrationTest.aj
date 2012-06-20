// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.List;
import org.hcmus.tis.model.ProjectProcessDataOnDemand;
import org.hcmus.tis.model.ProjectProcessIntegrationTest;
import org.hcmus.tis.service.ProjectProcessService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ProjectProcessIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ProjectProcessIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ProjectProcessIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ProjectProcessIntegrationTest: @Transactional;
    
    @Autowired
    private ProjectProcessDataOnDemand ProjectProcessIntegrationTest.dod;
    
    @Autowired
    ProjectProcessService ProjectProcessIntegrationTest.projectProcessService;
    
    @Test
    public void ProjectProcessIntegrationTest.testCountAllProjectProcesses() {
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to initialize correctly", dod.getRandomProjectProcess());
        long count = projectProcessService.countAllProjectProcesses();
        Assert.assertTrue("Counter for 'ProjectProcess' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ProjectProcessIntegrationTest.testFindProjectProcess() {
        ProjectProcess obj = dod.getRandomProjectProcess();
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to provide an identifier", id);
        obj = projectProcessService.findProjectProcess(id);
        Assert.assertNotNull("Find method for 'ProjectProcess' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ProjectProcess' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ProjectProcessIntegrationTest.testFindAllProjectProcesses() {
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to initialize correctly", dod.getRandomProjectProcess());
        long count = projectProcessService.countAllProjectProcesses();
        Assert.assertTrue("Too expensive to perform a find all test for 'ProjectProcess', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ProjectProcess> result = projectProcessService.findAllProjectProcesses();
        Assert.assertNotNull("Find all method for 'ProjectProcess' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ProjectProcess' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ProjectProcessIntegrationTest.testFindProjectProcessEntries() {
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to initialize correctly", dod.getRandomProjectProcess());
        long count = projectProcessService.countAllProjectProcesses();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ProjectProcess> result = projectProcessService.findProjectProcessEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ProjectProcess' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ProjectProcess' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ProjectProcessIntegrationTest.testFlush() {
        ProjectProcess obj = dod.getRandomProjectProcess();
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to provide an identifier", id);
        obj = projectProcessService.findProjectProcess(id);
        Assert.assertNotNull("Find method for 'ProjectProcess' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyProjectProcess(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ProjectProcess' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ProjectProcessIntegrationTest.testUpdateProjectProcessUpdate() {
        ProjectProcess obj = dod.getRandomProjectProcess();
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to provide an identifier", id);
        obj = projectProcessService.findProjectProcess(id);
        boolean modified =  dod.modifyProjectProcess(obj);
        Integer currentVersion = obj.getVersion();
        ProjectProcess merged = projectProcessService.updateProjectProcess(obj);
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ProjectProcess' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ProjectProcessIntegrationTest.testSaveProjectProcess() {
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to initialize correctly", dod.getRandomProjectProcess());
        ProjectProcess obj = dod.getNewTransientProjectProcess(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ProjectProcess' identifier to be null", obj.getId());
        projectProcessService.saveProjectProcess(obj);
        obj.flush();
        Assert.assertNotNull("Expected 'ProjectProcess' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ProjectProcessIntegrationTest.testDeleteProjectProcess() {
        ProjectProcess obj = dod.getRandomProjectProcess();
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProjectProcess' failed to provide an identifier", id);
        obj = projectProcessService.findProjectProcess(id);
        projectProcessService.deleteProjectProcess(obj);
        obj.flush();
        Assert.assertNull("Failed to remove 'ProjectProcess' with identifier '" + id + "'", projectProcessService.findProjectProcess(id));
    }
    
}