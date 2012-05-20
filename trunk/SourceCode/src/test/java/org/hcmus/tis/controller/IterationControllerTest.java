package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItemContainer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
@RunWith(PowerMockRunner.class)
@MockStaticEntityMethods
public class IterationControllerTest {
	private IterationController aut;
	private Model mockedUIModel;
	private  HttpServletRequest mockedHttpServletRequest;
	@Before
	public void setUp(){
		aut = new IterationController();
		mockedUIModel = Mockito.mock(Model.class);
		mockedHttpServletRequest  = Mockito.mock(HttpServletRequest.class);
	}
	@Test
	public void testPopulateEditForm() {
		Iteration mockedIteration = Mockito.mock(Iteration.class);
		
		aut.populateEditForm(mockedUIModel, mockedIteration);
		Mockito.verify(mockedUIModel).addAttribute("iteration", mockedIteration);
	}
	@Test
	@PrepareForTest({WorkItemContainer.class, IterationController.class})
	public void testCreateForm() throws Exception{
		Long parentId = (long)1;
		WorkItemContainer mockedParentContainer = Mockito.mock(WorkItemContainer.class);
		PowerMockito.mockStatic(WorkItemContainer.class);
		PowerMockito.when(WorkItemContainer.findWorkItemContainer(parentId)).thenReturn(mockedParentContainer);
		Iteration mockedIteration = Mockito.mock(Iteration.class);
		PowerMockito.whenNew(Iteration.class).withNoArguments().thenReturn(mockedIteration);
		
		IterationController spyAUT = Mockito.spy(aut);
		Mockito.doNothing().when(spyAUT).populateEditForm(mockedUIModel,mockedIteration);
		spyAUT.createForm(parentId,mockedUIModel);
		
		Mockito.verify(mockedIteration).setParentContainer(mockedParentContainer);
		Mockito.verify(spyAUT).populateEditForm(mockedUIModel, mockedIteration);
		
		
	}
	@Test
	public void testCreate(){
		BindingResult mockedBindingResult = Mockito.mock(BindingResult.class);
		Iteration mockedIteration = Mockito.mock(Iteration.class);
		Project mockedProjec = Mockito.mock(Project.class);
		Mockito.doReturn(mockedProjec).when(mockedIteration).getParentContainer();
		Mockito.doReturn((long)1).when(mockedProjec).getId();
		String result = aut.create(mockedIteration, mockedBindingResult, mockedUIModel, mockedHttpServletRequest);
		
		Assert.assertEquals("/projects/1/roadmap", result);
	}

}