package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.aspectj.lang.annotation.After;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.Attachment;
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
	@Test
	@PrepareForTest({Account.class, MemberInformation.class, Project.class, WorkItemType.class, Priority.class, WorkItemStatus.class})
	public void testCreateForm() throws NotPermissionException {
		PowerMockito.mockStatic(Project.class);
		PowerMockito.mockStatic(Priority.class);
		PowerMockito.mockStatic(WorkItemType.class);
		PowerMockito.mockStatic(WorkItemStatus.class);
		Principal mockedPrincipal  = Mockito.mock(Principal.class);
		Mockito.doReturn("email").when(mockedPrincipal).getName();
		WorkItemController spyAut = Mockito.spy(aut);
		Mockito.doNothing().when(spyAut).populateEditFormCustomly(Mockito.eq(uiModel), Mockito.any(WorkItem.class));
		
		Account account = new Account();
		TypedQuery<Account> mockedAccountTypedQuery = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(account).when(mockedAccountTypedQuery).getSingleResult();
		PowerMockito.mockStatic(Account.class);
		PowerMockito.when(Account.findAccountsByEmailEquals(Mockito.eq("email"))).thenReturn(mockedAccountTypedQuery);
		
		TypedQuery<MemberInformation> mockedMemberInformation = Mockito.mock(TypedQuery.class);
		MemberInformation memberInformation = new MemberInformation();
		Mockito.doReturn(memberInformation).when(mockedMemberInformation).getSingleResult();
		PowerMockito.mockStatic(MemberInformation.class);
		PowerMockito.when(MemberInformation.findMemberInformationsByAccountAndProject(Mockito.eq(account), Mockito.any(Project.class))).thenReturn(mockedMemberInformation);
		
		spyAut.createForm((long)1, (long) 1,null,  uiModel, mockedPrincipal);
		
		Mockito.verify(uiModel).addAttribute(Mockito.eq("projectId"), Mockito.anyLong());
		Mockito.verify(uiModel).addAttribute(Mockito.eq("memberInformationId"), Mockito.anyLong());
		PowerMockito.verifyStatic();
		WorkItemType.findWorkItemType(Mockito.anyLong());
	}
	@Test
	@PrepareForTest({MemberInformation.class,Priority.class, WorkItemContainer.class, Iteration.class, WorkItemStatus.class })
	public void testPopulateEditFormCustomly(){
		Long projectId = (long)1;
		WorkItem workItem = new WorkItem();
		WorkItemContainer workItemContainer = new Project();
		workItemContainer.setId(projectId);
		WorkItemType workItemType = new WorkItemType();
		workItem.setWorkItemType(workItemType);
		workItem.setWorkItemContainer(workItemContainer);		
		PowerMockito.mockStatic(MemberInformation.class);
		PowerMockito.mockStatic(Priority.class);
		PowerMockito.mockStatic(WorkItemStatus.class);		
		PowerMockito.mockStatic(WorkItemContainer.class);
		PowerMockito.when(WorkItemContainer.findWorkItemContainer(workItemContainer.getId())).thenReturn(workItemContainer);
		
		TypedQuery<Iteration> mockedTypedQuery = Mockito.mock(TypedQuery.class);
		List<Iteration> iterations = new ArrayList<Iteration>();
		Mockito.doReturn(iterations).when(mockedTypedQuery).getResultList();
		PowerMockito.mockStatic(Iteration.class);
		PowerMockito.when(Iteration.findIterationsByParentContainer(workItemContainer)).thenReturn(mockedTypedQuery);
		aut.populateEditFormCustomly(uiModel, workItem);
		
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workItem"), Mockito.eq(workItem));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("memberinformations"), Mockito.anyCollectionOf(MemberInformation.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("prioritys"), Mockito.anyCollectionOf(Priority.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workitemcontainers"), Mockito.anyCollectionOf(Iteration.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workitemstatuses"), Mockito.anyCollectionOf(WorkItemStatus.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workItemType"), Mockito.eq(workItemType));
		
	}
	private List<Field> finalField;
	@Test
	@PrepareForTest({WorkItemType.class, Attachment.class})
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
		Long attachmentIds[] = {(long)1, (long)2};
		PowerMockito.mockStatic(Attachment.class);
		Attachment mockedAttachment1 = Mockito.mock(Attachment.class);
		Attachment mocedAttachment2 = Mockito.mock(Attachment.class);
		PowerMockito.when(Attachment.findAttachment((long)1)).thenReturn(mocedAttachment2);
		PowerMockito.when(Attachment.findAttachment((long)2)).thenReturn(mockedAttachment1);
		 
		aut.create(spyWorkItem, bindingResult, uiModel, attachmentIds, httpServletRequest);
		
		Mockito.verify(httpServletRequest).getParameter("name");
		Mockito.verify(spyWorkItem).persist();
		Mockito.verify(mockedAttachment1).setWorkItem(spyWorkItem);
		Mockito.verify(mockedAttachment1).flush();
		Mockito.verify(mocedAttachment2).setWorkItem(spyWorkItem);
		Mockito.verify(mocedAttachment2).flush();
		assertEquals(1, finalField.size());
		assertEquals("name", finalField.get(0).getName());
		assertEquals("value", finalField.get(0).getValue());
	}
	@Test
	@PrepareForTest({WorkItemType.class})
	public void testUpdate() throws JAXBException{
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
		Mockito.doReturn(null).when(spyWorkItem).merge();

		Mockito.doReturn("utf-8").when(httpServletRequest).getCharacterEncoding();
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				finalField = (List<Field>)invocation.getArguments()[0];
				return null;
			}
		}).when(spyWorkItem).setAdditionFiels(Mockito.any(List.class));
		
		Mockito.doReturn("value").when(httpServletRequest).getParameter("name");
		 
		aut.update(spyWorkItem, bindingResult, uiModel, httpServletRequest);
		
		Mockito.verify(httpServletRequest).getParameter("name");
		Mockito.verify(spyWorkItem).merge();
		assertEquals(1, finalField.size());
		assertEquals("name", finalField.get(0).getName());
		assertEquals("value", finalField.get(0).getValue());
	}
}
