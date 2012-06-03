package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItemContainer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
@RunWith(PowerMockRunner.class)
@MockStaticEntityMethods
public class IterationControllerTest extends AbstractShiroTest {
	private IterationController aut;
	@Mock
	private Model uiModel;
	@Mock
	private  HttpServletRequest mockedHttpServletRequest;
	@Mock
	private Subject subject;
	@Mock
	private Session session;
	@Mock
	private Iteration subIteration;
	@Mock
	private Iteration parentIteration;
	@Mock
	private Iteration iteration;
	@Mock
	private Project project;
	@Mock
	BindingResult bindingResult;
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		aut = new IterationController();
		doReturn((long)1).when(project).getId();
		doReturn((long)2).when(parentIteration).getId();
		doReturn((long)3).when(subIteration).getId();
		doReturn(project).when(parentIteration).getParentContainer();
		doReturn(parentIteration).when(subIteration).getParentContainer();
		doReturn(project).when(subIteration).getParentProjectOrMyself();
		doReturn(project).when(parentIteration).getParentProjectOrMyself();
		setSubject(subject);
	}
	@Mock
	Collection<Iteration> iterations;
	@After
	public void tearDown(){
		clearSubject();
	}
	@Test
	@PrepareForTest({Iteration.class})
	public void testPopulateEditForm() {
		PowerMockito.mockStatic(Iteration.class);
		PowerMockito.when(Iteration.getdescendantIterations(project)).thenReturn(iterations);
		Iteration mockedIteration = mock(Iteration.class);
		
		aut.populateEditForm(uiModel, mockedIteration, project);
		PowerMockito.verifyStatic();
		Iteration.getdescendantIterations(project);
		verify(uiModel).addAttribute("iteration", mockedIteration);
		verify(uiModel).addAttribute("projectId", project.getId());
		verify(uiModel).addAttribute("iterations", iterations);
	}
	@Test
	@PrepareForTest({Project.class, IterationController.class})
	public void testCreateForm() throws Exception{
		PowerMockito.mockStatic(Project.class);
		PowerMockito.when(Project.findProject(project.getId())).thenReturn(project);
		PowerMockito.whenNew(Iteration.class).withNoArguments().thenReturn(iteration);
		
		IterationController spyAUT = spy(aut);
		doNothing().when(spyAUT).populateEditForm(uiModel,iteration, project);
		spyAUT.createForm(project.getId(),uiModel);
		
		verify(iteration).setParentContainer(project);
		verify(spyAUT).populateEditForm(uiModel, iteration, project);
		
		
	}
	@Test
	public void testCreateSubIteration(){
		String result = aut.create(project.getId(),subIteration , bindingResult, uiModel, mockedHttpServletRequest);
		
		verify(subIteration).persist();
		verify(iteration, times(0)).setParentContainer(any(WorkItemContainer.class));
		verify(subIteration).getParentProjectOrMyself();
		Assert.assertEquals("redirect:/projects/"+ subIteration.getParentProjectOrMyself().getId() +"/roadmap", result);
	}
	private Project projectForTestCreateTopIteration;
	@Test
	@PrepareForTest({Project.class})
	public void testCreateTopIteration(){
		PowerMockito.mockStatic(Project.class);
		PowerMockito.when(Project.findProject(project.getId())).thenReturn(project);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				projectForTestCreateTopIteration = (Project) invocation.getArguments()[0];
				return null;
			}
		}).when(iteration).setParentContainer(project);
		doAnswer(new Answer<Project>() {
			@Override
			public Project answer(InvocationOnMock invocation) throws Throwable {
				return projectForTestCreateTopIteration;
			}
		}).when(iteration).getParentProjectOrMyself();
		
		String result = aut.create(project.getId(),iteration , bindingResult, uiModel, mockedHttpServletRequest);
		
		verify(iteration).persist();
		verify(iteration).setParentContainer(project);
		verify(iteration).getParentProjectOrMyself();
		Assert.assertEquals("redirect:/projects/"+ iteration.getParentProjectOrMyself().getId() +"/roadmap", result);
	}
	@Test
	@PrepareForTest({Project.class, Iteration.class})
	public void testUpdateForm(){
		PowerMockito.mockStatic(Project.class);
		PowerMockito.mockStatic(Iteration.class);
		PowerMockito.when(Project.findProject(project.getId())).thenReturn(project);
		PowerMockito.when(Iteration.findIteration(subIteration.getId())).thenReturn(subIteration);
		IterationController spy = spy(aut);
		doNothing().when(spy).populateEditForm(uiModel, subIteration, project);
		String result = spy.updateForm(project.getId(), subIteration.getId(), uiModel);
		verify(spy).populateEditForm(uiModel, subIteration, project);
		assertEquals("iterations/update", result);
	}

}