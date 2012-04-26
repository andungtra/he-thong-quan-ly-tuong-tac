package org.hcmus.tis.service;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
public class IterationImplTest {
	IterationServiceImpl aut;
	@Before
	public void setUp() throws Exception {
		aut = new IterationServiceImpl();
	}

	@Test
	public void testGetParentProject() {
		Iteration iteration = new Iteration();
		Iteration parentIteration = new Iteration();
		iteration.setParentContainer(parentIteration);
		Project project = new Project();
		parentIteration.setParentContainer(project);
		Project result = aut.getParentProject(iteration);
		Assert.assertEquals(project, result);
	}

}
