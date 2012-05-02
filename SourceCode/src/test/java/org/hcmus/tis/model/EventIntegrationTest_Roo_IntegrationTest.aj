// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.List;
import org.hcmus.tis.model.Event;
import org.hcmus.tis.model.EventDataOnDemand;
import org.hcmus.tis.model.EventIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EventIntegrationTest_Roo_IntegrationTest {
    
    declare @type: EventIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: EventIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: EventIntegrationTest: @Transactional;
    
    @Autowired
    private EventDataOnDemand EventIntegrationTest.dod;
    
    @Test
    public void EventIntegrationTest.testCountEvents() {
        Assert.assertNotNull("Data on demand for 'Event' failed to initialize correctly", dod.getRandomEvent());
        long count = Event.countEvents();
        Assert.assertTrue("Counter for 'Event' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void EventIntegrationTest.testFindEvent() {
        Event obj = dod.getRandomEvent();
        Assert.assertNotNull("Data on demand for 'Event' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Event' failed to provide an identifier", id);
        obj = Event.findEvent(id);
        Assert.assertNotNull("Find method for 'Event' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Event' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void EventIntegrationTest.testFindAllEvents() {
        Assert.assertNotNull("Data on demand for 'Event' failed to initialize correctly", dod.getRandomEvent());
        long count = Event.countEvents();
        Assert.assertTrue("Too expensive to perform a find all test for 'Event', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Event> result = Event.findAllEvents();
        Assert.assertNotNull("Find all method for 'Event' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Event' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void EventIntegrationTest.testFindEventEntries() {
        Assert.assertNotNull("Data on demand for 'Event' failed to initialize correctly", dod.getRandomEvent());
        long count = Event.countEvents();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Event> result = Event.findEventEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Event' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Event' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void EventIntegrationTest.testFlush() {
        Event obj = dod.getRandomEvent();
        Assert.assertNotNull("Data on demand for 'Event' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Event' failed to provide an identifier", id);
        obj = Event.findEvent(id);
        Assert.assertNotNull("Find method for 'Event' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyEvent(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Event' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void EventIntegrationTest.testMergeUpdate() {
        Event obj = dod.getRandomEvent();
        Assert.assertNotNull("Data on demand for 'Event' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Event' failed to provide an identifier", id);
        obj = Event.findEvent(id);
        boolean modified =  dod.modifyEvent(obj);
        Integer currentVersion = obj.getVersion();
        Event merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Event' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void EventIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Event' failed to initialize correctly", dod.getRandomEvent());
        Event obj = dod.getNewTransientEvent(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Event' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Event' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Event' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void EventIntegrationTest.testRemove() {
        Event obj = dod.getRandomEvent();
        Assert.assertNotNull("Data on demand for 'Event' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Event' failed to provide an identifier", id);
        obj = Event.findEvent(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Event' with identifier '" + id + "'", Event.findEvent(id));
    }
    
}
