package org.hcmus.tis.model;

import java.util.Collection;

import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.hcmus.tis.dto.NonEditableEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Project.class)
public class ProjectIntegrationTest {
    @Autowired
    private ProjectDataOnDemand dod;
    @Test
    public void testMarkerMethod() {
    }
    @Test
    public void testCountProjectsByNameLike(){
    	
    	String name = dod.getRandomProject().getName();
    	long result = Project.countProjectsByNameLike(name);
    	Assert.assertTrue(result >= 1);
    }
    @Test
    public void testFindProjectsByNameLike(){
    	String name  = dod.getRandomProject().getName();
    	TypedQuery<Project> result = Project.findProjectsByNameLike(name, 0, 100);
    	Assert.assertTrue(result.getResultList().size() > 0);
    	
    }
    @Test
    public void testGetEventsOfMembers(){
    	Project project = Project.findProject((long)1);
    	Collection<Event> result = project.getEventsOfMembers();
    	Assert.assertEquals(1, result.size());
    }
    @Test
    public void testGetIterations(){
    }
}
