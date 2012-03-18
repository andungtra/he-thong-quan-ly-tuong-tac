package org.hcmus.tis.controller;

import org.hcmus.tis.model.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.ui.Model;
@RunWith(PowerMockRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/applicationContext.xml"})
public class ProjectControllerTest extends AbstractJUnit4SpringContextTests {
	@Test
	public void testFindProjectsByNameLike() {
		ProjectController controller = new ProjectController();
		Mockito.when(Project.findProjectsByNameLike("name")).thenReturn(null);
		Mockito.when(Project.findAllProjects()).thenReturn(null);
		Model mockUIModel = null;
		controller.findProjectsByNameLike(null, mockUIModel);
		PowerMockito.verifyStatic();
		Project.findAllProjects();
		PowerMockito.verifyStatic(Mockito.times(0));
		Project.findProjectsByNameLike(null);
		
	}

}
