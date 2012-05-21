// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.List;
import org.hcmus.tis.model.Calendar;
import org.hcmus.tis.model.CalendarDataOnDemand;
import org.hcmus.tis.model.CalendarIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CalendarIntegrationTest_Roo_IntegrationTest {
    
    declare @type: CalendarIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: CalendarIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: CalendarIntegrationTest: @Transactional;
    
    @Autowired
    private CalendarDataOnDemand CalendarIntegrationTest.dod;
    
    @Test
    public void CalendarIntegrationTest.testCountCalendars() {
        Assert.assertNotNull("Data on demand for 'Calendar' failed to initialize correctly", dod.getRandomCalendar());
        long count = Calendar.countCalendars();
        Assert.assertTrue("Counter for 'Calendar' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void CalendarIntegrationTest.testFindCalendar() {
        Calendar obj = dod.getRandomCalendar();
        Assert.assertNotNull("Data on demand for 'Calendar' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Calendar' failed to provide an identifier", id);
        obj = Calendar.findCalendar(id);
        Assert.assertNotNull("Find method for 'Calendar' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Calendar' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void CalendarIntegrationTest.testFindAllCalendars() {
        Assert.assertNotNull("Data on demand for 'Calendar' failed to initialize correctly", dod.getRandomCalendar());
        long count = Calendar.countCalendars();
        Assert.assertTrue("Too expensive to perform a find all test for 'Calendar', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Calendar> result = Calendar.findAllCalendars();
        Assert.assertNotNull("Find all method for 'Calendar' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Calendar' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void CalendarIntegrationTest.testFindCalendarEntries() {
        Assert.assertNotNull("Data on demand for 'Calendar' failed to initialize correctly", dod.getRandomCalendar());
        long count = Calendar.countCalendars();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Calendar> result = Calendar.findCalendarEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Calendar' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Calendar' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void CalendarIntegrationTest.testFlush() {
        Calendar obj = dod.getRandomCalendar();
        Assert.assertNotNull("Data on demand for 'Calendar' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Calendar' failed to provide an identifier", id);
        obj = Calendar.findCalendar(id);
        Assert.assertNotNull("Find method for 'Calendar' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyCalendar(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Calendar' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void CalendarIntegrationTest.testMergeUpdate() {
        Calendar obj = dod.getRandomCalendar();
        Assert.assertNotNull("Data on demand for 'Calendar' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Calendar' failed to provide an identifier", id);
        obj = Calendar.findCalendar(id);
        boolean modified =  dod.modifyCalendar(obj);
        Integer currentVersion = obj.getVersion();
        Calendar merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Calendar' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void CalendarIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Calendar' failed to initialize correctly", dod.getRandomCalendar());
        Calendar obj = dod.getNewTransientCalendar(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Calendar' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Calendar' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Calendar' identifier to no longer be null", obj.getId());
    }
    
}
