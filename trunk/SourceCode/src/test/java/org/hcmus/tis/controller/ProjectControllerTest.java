package org.hcmus.tis.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import javax.persistence.TypedQuery;

import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectDataOnDemand;
import org.hcmus.tis.util.Parameter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.ui.Model;
@RunWith(PowerMockRunner.class)
@PrepareForTest(Project.class)
@MockStaticEntityMethods
public class ProjectControllerTest {
	private ProjectController aut;
	private Model uiModel;
	@Before
	public void setUp(){
		aut = new ProjectController();
		uiModel = Mockito.mock(Model.class);
		PowerMockito.mockStatic(Project.class);
	}
	@Test
	public void testFindProjectsByNameLikeWhenNameEmpty() {
		ProjectController controller = new ProjectController();
		PowerMockito.when(Project.findAllProjects()).thenReturn(null);
		controller.findProjectsQuickly("", uiModel, 1, 2);
		PowerMockito.verifyStatic();
		Project.findProjectEntries(0, 2);
		Mockito.verify(uiModel).addAttribute(Mockito.eq("parameters"), Mockito.anyCollection());
	}
	@Test
	public void testFindProjectByNameWhenNameNotNull(){
		ProjectController controller = new ProjectController();
		PowerMockito.mockStatic(Project.class);
		String name = "name";
		TypedQuery<Project> mockResult =  Mockito.mock(TypedQuery.class);
		Mockito.when(mockResult.getResultList()).thenReturn(new Vector<Project>());
		PowerMockito.when(Project.findProjectsByNameLike(Mockito.eq(name), Mockito.anyInt(), Mockito.anyInt())).thenReturn(mockResult);
		Model mockUIModel = Mockito.mock(Model.class);
		
		controller.findProjectsQuickly(name, mockUIModel, null, null);
		
		PowerMockito.verifyStatic();
		Project.findProjectsByNameLike(name, 0, 10);
		Mockito.verify(mockUIModel).addAttribute("query", name);
		Mockito.verify(mockUIModel).addAttribute(Mockito.eq("parameters"), Mockito.anyCollectionOf(Parameter.class));
	}
	@Test
	public void testListMembers(){
	
		Project mockedProject = Mockito.mock(Project.class);
		Mockito.doReturn(new HashSet<MemberInformation>()).when(mockedProject).getMemberInformations();
		PowerMockito.when(Project.findProject((long)1)).thenReturn(mockedProject);
		String result = aut.listMembers((long)1, uiModel);
		
		Mockito.verify(uiModel).addAttribute(Mockito.eq("memberinformations"), Mockito.anyCollection());
	}
}
