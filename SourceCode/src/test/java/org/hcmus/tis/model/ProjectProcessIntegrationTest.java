package org.hcmus.tis.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = ProjectProcess.class)
public class ProjectProcessIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }
    @Test
	public void testCountAllProjectsNotDeleted() {
		// TODO Auto-generated method stub
		long result = Project.countProjects();
		assertEquals(1, result);
	}
}
