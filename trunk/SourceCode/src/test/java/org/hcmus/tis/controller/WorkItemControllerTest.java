package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.aspectj.lang.annotation.After;
import org.hcmus.tis.model.Field;
import org.hcmus.tis.model.FieldDefine;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.model.WorkItemType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
@RunWith(PowerMockRunner.class)
@MockStaticEntityMethods
@PrepareForTest({WorkItemStatus.class, Priority.class, Iteration.class, MemberInformation.class, WorkItemContainer.class, WorkItemType.class})
public class WorkItemControllerTest {
	private Model uiModel;
	private WorkItemController aut;
	private BindingResult bindingResult;
	private HttpServletRequest httpServletRequest;
	@Before
	public void setUp(){
		
		uiModel = Mockito.mock(Model.class);
		aut = new WorkItemController();
		bindingResult = Mockito.mock(BindingResult.class);
		httpServletRequest = Mockito.mock(HttpServletRequest.class);
	}
	public void testCreateForm() throws NotPermissionException {
		Principal mockedPrincipal  = Mockito.mock(Principal.class);
		Mockito.doReturn("jldsfj").when(mockedPrincipal).getName();
		WorkItemContainer workItem = new Project();
		PowerMockito.when(WorkItemContainer.findWorkItemContainer(Mockito.anyLong())).thenReturn(workItem);
		
		aut.createForm((long)1, (long) 1,null,  uiModel, mockedPrincipal);
		
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workitemstatuses"), Mockito.anyCollectionOf(WorkItemStatus.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("prioritys"), Mockito.anyCollectionOf(Priority.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workitemcontainers"), Mockito.anyCollectionOf(Iteration.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("memberinformations"), Mockito.anyCollectionOf(MemberInformation.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workItemTypeId"), Mockito.anyLong());
		Mockito.verify(uiModel).addAttribute(Mockito.eq("projectId"), Mockito.anyLong());
		Mockito.verify(uiModel).addAttribute(Mockito.eq("memberInformationId"), Mockito.anyLong());
	}
	@Test
	public void testPopulateEditFormCustomly(){
		
	}
	private List<Field> finalField;
	@Test
	public void testCreate() throws JAXBException{
		WorkItem workItem = new WorkItem();
		workItem.setId((long)4);
		WorkItemType workItemType = new WorkItemType();
		workItemType.setId((long)1);
		workItem.setWorkItemType(workItemType);
		List<FieldDefine> fieldDefines = new ArrayList<FieldDefine>();
		FieldDefine fieldDefine = new FieldDefine();
		fieldDefine.setRefName("name");
		fieldDefines.add(fieldDefine);
		WorkItemType mockedWorkItemType = Mockito.mock(WorkItemType.class);
		Mockito.doReturn(fieldDefines).when(mockedWorkItemType).getAdditionalFieldDefines();
		PowerMockito.mockStatic(WorkItemType.class);
		PowerMockito.when(WorkItemType.findWorkItemType(Mockito.anyLong())).thenReturn(mockedWorkItemType);
		WorkItem spyWorkItem = Mockito.spy(workItem);
		Mockito.doNothing().when(spyWorkItem).persist();

		Mockito.doReturn("utf-8").when(httpServletRequest).getCharacterEncoding();
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				finalField = (List<Field>)invocation.getArguments()[0];
				return null;
			}
		}).when(spyWorkItem).setAdditionFiels(Mockito.any(List.class));
		
		Mockito.doReturn("value").when(httpServletRequest).getParameter("name");
		 
		aut.create(spyWorkItem, bindingResult, uiModel, httpServletRequest);
		
		Mockito.verify(httpServletRequest).getParameter("name");
		Mockito.verify(spyWorkItem).persist();
		assertEquals(1, finalField.size());
		assertEquals("name", finalField.get(0).getName());
		assertEquals("value", finalField.get(0).getValue());
	}
}
