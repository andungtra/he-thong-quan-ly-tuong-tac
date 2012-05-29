package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import java.lang.reflect.Member;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.hcmus.tis.dto.DtReply;
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
import org.junit.After;
import org.junit.Assert;
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
public class WorkItemControllerTest extends AbstractShiroTest {
	private Model uiModel;
	private WorkItemController aut;
	private BindingResult bindingResult;
	private HttpServletRequest httpServletRequest;
	private Subject mockedSubject;
	private Session mockedSession;
	private Account mockedLoginedAccount;

	@Before
	public void setUp() {

		uiModel = Mockito.mock(Model.class);
		aut = new WorkItemController();
		bindingResult = Mockito.mock(BindingResult.class);
		httpServletRequest = Mockito.mock(HttpServletRequest.class);
		mockedSubject = Mockito.mock(Subject.class);
		mockedSession = Mockito.mock(Session.class);
		mockedLoginedAccount = Mockito.mock(Account.class);
		Mockito.doReturn(mockedSession).when(mockedSubject).getSession();
		Mockito.doReturn(mockedLoginedAccount).when(mockedSession)
				.getAttribute("account");
		Mockito.doReturn(true).when(mockedSubject).isAuthenticated();
		Mockito.doNothing().when(mockedSubject)
				.checkPermissions(Mockito.any(String[].class));
		setSubject(mockedSubject);
	}

	@After
	public void tearDown() {
		clearSubject();
	}

	@Test
	@PrepareForTest({ Account.class, MemberInformation.class, Project.class,
			WorkItemType.class, Priority.class, WorkItemStatus.class })
	public void testCreateForm() throws NotPermissionException {
		PowerMockito.mockStatic(Project.class);
		PowerMockito.mockStatic(Priority.class);
		PowerMockito.mockStatic(WorkItemType.class);
		PowerMockito.mockStatic(WorkItemStatus.class);
		Principal mockedPrincipal = Mockito.mock(Principal.class);
		Mockito.doReturn("email").when(mockedPrincipal).getName();
		WorkItemController spyAut = Mockito.spy(aut);
		Mockito.doNothing()
				.when(spyAut)
				.populateEditFormCustomly(Mockito.eq(uiModel),
						Mockito.any(WorkItem.class));

		Account account = new Account();
		TypedQuery<Account> mockedAccountTypedQuery = Mockito
				.mock(TypedQuery.class);
		Mockito.doReturn(account).when(mockedAccountTypedQuery)
				.getSingleResult();
		PowerMockito.mockStatic(Account.class);
		PowerMockito.when(
				Account.findAccountsByEmailEquals(Mockito.eq("email")))
				.thenReturn(mockedAccountTypedQuery);

		TypedQuery<MemberInformation> mockedMemberInformation = Mockito
				.mock(TypedQuery.class);
		MemberInformation memberInformation = new MemberInformation();
		Mockito.doReturn(memberInformation).when(mockedMemberInformation)
				.getSingleResult();
		PowerMockito.mockStatic(MemberInformation.class);
		PowerMockito.when(
				MemberInformation.findMemberInformationsByAccountAndProject(
						Mockito.eq(account), Mockito.any(Project.class)))
				.thenReturn(mockedMemberInformation);

		spyAut.createForm((long) 1, (long) 1, null, uiModel, mockedPrincipal);

		Mockito.verify(uiModel).addAttribute(Mockito.eq("projectId"),
				Mockito.anyLong());
		Mockito.verify(uiModel).addAttribute(Mockito.eq("memberInformationId"),
				Mockito.anyLong());
		PowerMockito.verifyStatic();
		WorkItemType.findWorkItemType(Mockito.anyLong());
	}

	@Test
	@PrepareForTest({ MemberInformation.class, Priority.class,
			WorkItemContainer.class, Iteration.class, WorkItemStatus.class })
	public void testPopulateEditFormCustomly() {
		Long projectId = (long) 1;
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
		PowerMockito.when(
				WorkItemContainer.findWorkItemContainer(workItemContainer
						.getId())).thenReturn(workItemContainer);

		TypedQuery<Iteration> mockedTypedQuery = Mockito.mock(TypedQuery.class);
		List<Iteration> iterations = new ArrayList<Iteration>();
		Mockito.doReturn(iterations).when(mockedTypedQuery).getResultList();
		PowerMockito.mockStatic(Iteration.class);
		PowerMockito.when(
				Iteration.findIterationsByParentContainer(workItemContainer))
				.thenReturn(mockedTypedQuery);
		aut.populateEditFormCustomly(uiModel, workItem);

		Mockito.verify(uiModel).addAttribute(Mockito.eq("workItem"),
				Mockito.eq(workItem));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("memberinformations"),
				Mockito.anyCollectionOf(MemberInformation.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("prioritys"),
				Mockito.anyCollectionOf(Priority.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workitemcontainers"),
				Mockito.anyCollectionOf(Iteration.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workitemstatuses"),
				Mockito.anyCollectionOf(WorkItemStatus.class));
		Mockito.verify(uiModel).addAttribute(Mockito.eq("workItemType"),
				Mockito.eq(workItemType));

	}

	private List<Field> finalField;

	@Test
	@PrepareForTest({ WorkItemType.class, Attachment.class })
	public void testCreate() throws JAXBException {
		WorkItem workItem = new WorkItem();
		workItem.setId((long) 4);
		WorkItemType workItemType = new WorkItemType();
		workItemType.setId((long) 1);
		workItem.setWorkItemType(workItemType);
		List<FieldDefine> fieldDefines = new ArrayList<FieldDefine>();
		FieldDefine fieldDefine = new FieldDefine();
		fieldDefine.setRefName("name");
		fieldDefines.add(fieldDefine);
		WorkItemType mockedWorkItemType = Mockito.mock(WorkItemType.class);
		Mockito.doReturn(fieldDefines).when(mockedWorkItemType)
				.getAdditionalFieldDefines();
		PowerMockito.mockStatic(WorkItemType.class);
		PowerMockito.when(WorkItemType.findWorkItemType(Mockito.anyLong()))
				.thenReturn(mockedWorkItemType);
		WorkItem spyWorkItem = Mockito.spy(workItem);
		Mockito.doNothing().when(spyWorkItem).persist();

		Mockito.doReturn("utf-8").when(httpServletRequest)
				.getCharacterEncoding();
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				finalField = (List<Field>) invocation.getArguments()[0];
				return null;
			}
		}).when(spyWorkItem).setAdditionFiels(Mockito.any(List.class));

		Mockito.doReturn("value").when(httpServletRequest).getParameter("name");
		Long attachmentIds[] = { (long) 1, (long) 2 };
		PowerMockito.mockStatic(Attachment.class);
		Attachment mockedAttachment1 = Mockito.mock(Attachment.class);
		Attachment mocedAttachment2 = Mockito.mock(Attachment.class);
		PowerMockito.when(Attachment.findAttachment((long) 1)).thenReturn(
				mocedAttachment2);
		PowerMockito.when(Attachment.findAttachment((long) 2)).thenReturn(
				mockedAttachment1);

		aut.create(spyWorkItem, bindingResult, (long) 1, uiModel,
				attachmentIds, httpServletRequest);

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
	@PrepareForTest({ WorkItemType.class })
	public void testUpdate() throws JAXBException {
		WorkItem workItem = new WorkItem();
		workItem.setId((long) 4);
		WorkItemType workItemType = new WorkItemType();
		workItemType.setId((long) 1);
		workItem.setWorkItemType(workItemType);
		Project project = new Project();
		workItem.setWorkItemContainer(project);
		List<FieldDefine> fieldDefines = new ArrayList<FieldDefine>();
		FieldDefine fieldDefine = new FieldDefine();
		fieldDefine.setRefName("name");
		fieldDefines.add(fieldDefine);
		WorkItemType mockedWorkItemType = Mockito.mock(WorkItemType.class);
		Mockito.doReturn(fieldDefines).when(mockedWorkItemType)
				.getAdditionalFieldDefines();
		PowerMockito.mockStatic(WorkItemType.class);
		PowerMockito.when(WorkItemType.findWorkItemType(Mockito.anyLong()))
				.thenReturn(mockedWorkItemType);
		WorkItem spyWorkItem = Mockito.spy(workItem);
		Mockito.doReturn(null).when(spyWorkItem).merge();

		Mockito.doReturn("utf-8").when(httpServletRequest)
				.getCharacterEncoding();
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				finalField = (List<Field>) invocation.getArguments()[0];
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

	@Test
	@PrepareForTest({ Project.class, WorkItem.class, WorkItemController.class })
	public void testListWorkItemByProject() {
		Long projectId = (long) 1;
		int iDisplayStart = 0;
		int iDisplayLength = 10;
		String sEcho = "echo";
		int iSortCol_0 = 1;
		String sSortDir_0 = "";
		String sSearch = "";
		List<WorkItem> workItems = new ArrayList<WorkItem>();
		workItems.add(Mockito.mock(WorkItem.class));
		workItems.add(Mockito.mock(WorkItem.class));
		WorkItemStatus mockedStatus = Mockito.mock(WorkItemStatus.class);
		WorkItemType mockedType = Mockito.mock(WorkItemType.class);
		for (WorkItem workItem : workItems) {
			Mockito.doReturn(mockedStatus).when(workItem).getStatus();
			Mockito.doReturn(mockedType).when(workItem).getWorkItemType();
		}
		WorkItemStatus testStatus = workItems.get(0).getStatus();
		Project mockedProject = Mockito.mock(Project.class);
		PowerMockito.mockStatic(Project.class);
		PowerMockito.when(Project.findProject(projectId)).thenReturn(
				mockedProject);
		TypedQuery<WorkItem> mockedQuery = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(workItems).when(mockedQuery).getResultList();
		Mockito.doReturn(mockedQuery).when(mockedQuery)
				.setFirstResult(Mockito.anyInt());
		Mockito.doReturn(mockedQuery).when(mockedQuery)
				.setMaxResults(Mockito.anyInt());
		PowerMockito.mockStatic(WorkItem.class);
		PowerMockito.when(WorkItem.countWorkItemByProject(mockedProject))
				.thenReturn((long) 2);
		PowerMockito.when(WorkItem.findWorkItemsByProject(mockedProject))
				.thenReturn(mockedQuery);

		DtReply dtReply = aut.listWorkItemByProject(projectId, iDisplayStart,
				iDisplayLength, sEcho, iSortCol_0, sSortDir_0, sSearch, "", "",
				"", "");
		PowerMockito.verifyStatic(Mockito.atLeastOnce());
		WorkItem.countWorkItemByProject(mockedProject);
		PowerMockito.verifyStatic();
		// WorkItem.findWorkItemsByProject(mockedProject);
		Assert.assertEquals(2, dtReply.getiTotalRecords());
		Assert.assertEquals(2, dtReply.getiTotalDisplayRecords());
		Assert.assertEquals(2, dtReply.getAaData().size());
	}

	@Test
	@PrepareForTest({ Account.class, Project.class, MemberInformation.class,
			WorkItem.class })
	public void testSubscibeWithNonSubscibedMember() {
		PowerMockito.mockStatic(Account.class);
		PowerMockito.mockStatic(Project.class);
		PowerMockito.mockStatic(MemberInformation.class);
		PowerMockito.mockStatic(WorkItem.class);
		Long projectId = (long) 1;
		Long workItemId = (long) 1;
		Project mockedProject = Mockito.mock(Project.class);
		PowerMockito.when(Project.findProject(projectId)).thenReturn(
				mockedProject);
		WorkItem mockedWorkItem = Mockito.mock(WorkItem.class);
		PowerMockito.when(WorkItem.findWorkItem(workItemId)).thenReturn(
				mockedWorkItem);
		MemberInformation mockedMember = Mockito.mock(MemberInformation.class);
		TypedQuery<MemberInformation> mockedQuery = Mockito
				.mock(TypedQuery.class);
		Mockito.doReturn(mockedMember).when(mockedQuery).getSingleResult();
		Account mockedAccount = Mockito.mock(Account.class);
		TypedQuery<Account> mockedAccountQuery = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(mockedAccount).when(mockedAccountQuery)
				.getSingleResult();
		PowerMockito.when(
				Account.findAccountsByEmailEquals(Mockito.anyString()))
				.thenReturn(mockedAccountQuery);
		PowerMockito.when(
				MemberInformation.findMemberInformationsByAccountAndProject(
						mockedAccount, mockedProject)).thenReturn(mockedQuery);
		Collection<MemberInformation> mockedSubscriber = Mockito
				.mock(Collection.class);
		Mockito.doReturn(mockedSubscriber).when(mockedWorkItem).getSubcribers();
		Mockito.doReturn(false).when(mockedSubscriber).contains(mockedMember);
		HttpServletRequest mockedHttpServletRequest = Mockito
				.mock(HttpServletRequest.class);
		Mockito.doReturn(mockedProject).when(mockedWorkItem)
				.getWorkItemContainer();
		Mockito.doReturn(mockedProject).when(mockedProject)
				.getParentProjectOrMyself();
		Mockito.doReturn((long) 1).when(mockedProject).getId();
		Mockito.doReturn((long) 1).when(mockedWorkItem).getId();
		String result = aut.subscribe(projectId, workItemId,
				mockedHttpServletRequest);

		Assert.assertEquals("redirect:/projects/1/workitems/1?form", result);
		Mockito.verify(mockedSubscriber).add(mockedMember);
		Mockito.verify(mockedWorkItem).flush();

	}

	@Test
	@PrepareForTest({ Account.class, Project.class, MemberInformation.class,
			WorkItem.class })
	public void testSubscibeWithSubscibedMember() {
		PowerMockito.mockStatic(Account.class);
		PowerMockito.mockStatic(Project.class);
		PowerMockito.mockStatic(MemberInformation.class);
		PowerMockito.mockStatic(WorkItem.class);
		Long projectId = (long) 1;
		Long workItemId = (long) 1;
		Project mockedProject = Mockito.mock(Project.class);
		PowerMockito.when(Project.findProject(projectId)).thenReturn(
				mockedProject);
		WorkItem mockedWorkItem = Mockito.mock(WorkItem.class);
		PowerMockito.when(WorkItem.findWorkItem(workItemId)).thenReturn(
				mockedWorkItem);
		MemberInformation mockedMember = Mockito.mock(MemberInformation.class);
		TypedQuery<MemberInformation> mockedQuery = Mockito
				.mock(TypedQuery.class);
		Mockito.doReturn(mockedMember).when(mockedQuery).getSingleResult();
		Account mockedAccount = Mockito.mock(Account.class);
		TypedQuery<Account> mockedAccountQuery = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(mockedAccount).when(mockedAccountQuery)
				.getSingleResult();
		PowerMockito.when(
				Account.findAccountsByEmailEquals(Mockito.anyString()))
				.thenReturn(mockedAccountQuery);
		PowerMockito.when(
				MemberInformation.findMemberInformationsByAccountAndProject(
						mockedAccount, mockedProject)).thenReturn(mockedQuery);
		Collection<MemberInformation> mockedSubscriber = Mockito
				.mock(Collection.class);
		Mockito.doReturn(mockedSubscriber).when(mockedWorkItem).getSubcribers();
		Mockito.doReturn(true).when(mockedSubscriber).contains(mockedMember);

		HttpServletRequest mockedHttpServletRequest = Mockito
				.mock(HttpServletRequest.class);
		Mockito.doReturn(mockedProject).when(mockedWorkItem)
				.getWorkItemContainer();
		Mockito.doReturn(mockedProject).when(mockedProject)
				.getParentProjectOrMyself();
		Mockito.doReturn((long) 1).when(mockedProject).getId();
		Mockito.doReturn((long) 1).when(mockedWorkItem).getId();
		String result = aut.subscribe(projectId, workItemId,
				mockedHttpServletRequest);

		assertEquals("redirect:/projects/1/workitems/1?form", result);
		Mockito.verify(mockedSubscriber, Mockito.times(0)).add(
				Mockito.any(MemberInformation.class));

	}

	@Test
	@PrepareForTest({ WorkItem.class, WorkItemController.class, Account.class,
			Project.class, MemberInformation.class })
	public void testUpdateForm() {
		Long projectId = (long) 1;
		Long workItemId = (long) 2;
		PowerMockito.mockStatic(WorkItem.class);
		PowerMockito.mockStatic(Account.class);
		PowerMockito.mockStatic(Project.class);
		PowerMockito.mockStatic(MemberInformation.class);
		Account mockedAccount = Mockito.mock(Account.class);
		Project mockedProject = Mockito.mock(Project.class);
		WorkItem mockedWorkItem = Mockito.mock(WorkItem.class);
		MemberInformation mockedMember = Mockito.mock(MemberInformation.class);
		MemberInformation member = Mockito.mock(MemberInformation.class);
		PowerMockito.when(WorkItem.findWorkItem(workItemId)).thenReturn(
				mockedWorkItem);
		PowerMockito.when(Project.findProject(projectId)).thenReturn(
				mockedProject);
		TypedQuery<Account> mockedAccountQuery = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(mockedAccount).when(mockedAccountQuery)
				.getSingleResult();
		PowerMockito.when(
				Account.findAccountsByEmailEquals(Mockito.anyString()))
				.thenReturn(mockedAccountQuery);
		TypedQuery<MemberInformation> mockedMemberQuery = Mockito
				.mock(TypedQuery.class);
		Mockito.doReturn(mockedMember).when(mockedMemberQuery)
				.getSingleResult();
		PowerMockito.when(
				MemberInformation.findMemberInformationsByAccountAndProject(
						mockedAccount, mockedProject)).thenReturn(
				mockedMemberQuery);
		Collection<MemberInformation> mockedSubscribers = Mockito
				.mock(Collection.class);
		Mockito.doReturn(true).when(mockedSubscribers).contains(mockedMember);
		Mockito.doReturn(mockedSubscribers).when(mockedWorkItem)
				.getSubcribers();

		WorkItemController spy = Mockito.spy(aut);
		Mockito.doNothing()
				.when(spy)
				.populateEditFormCustomly(Mockito.any(Model.class),
						Mockito.any(WorkItem.class));
		String result = spy.updateForm(projectId, workItemId, uiModel);
		Mockito.verify(mockedSubscribers).contains(mockedMember);
		Mockito.verify(uiModel).addAttribute("subscribed", true);
		Assert.assertEquals("workitems/update", result);

	}
	@Test
	@PrepareForTest({ Account.class, Project.class, MemberInformation.class,
		WorkItem.class })
	public void testUnSubscribe(){
		PowerMockito.mockStatic(Account.class);
		PowerMockito.mockStatic(Project.class);
		PowerMockito.mockStatic(MemberInformation.class);
		PowerMockito.mockStatic(WorkItem.class);
		Long projectId = (long) 1;
		Long workItemId = (long) 1;
		Project mockedProject = Mockito.mock(Project.class);
		PowerMockito.when(Project.findProject(projectId)).thenReturn(
				mockedProject);
		WorkItem mockedWorkItem = Mockito.mock(WorkItem.class);
		PowerMockito.when(WorkItem.findWorkItem(workItemId)).thenReturn(
				mockedWorkItem);
		MemberInformation mockedMember = Mockito.mock(MemberInformation.class);
		TypedQuery<MemberInformation> mockedQuery = Mockito
				.mock(TypedQuery.class);
		Mockito.doReturn(mockedMember).when(mockedQuery).getSingleResult();
		Account mockedAccount = Mockito.mock(Account.class);
		TypedQuery<Account> mockedAccountQuery = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(mockedAccount).when(mockedAccountQuery)
				.getSingleResult();
		PowerMockito.when(
				Account.findAccountsByEmailEquals(Mockito.anyString()))
				.thenReturn(mockedAccountQuery);
		PowerMockito.when(
				MemberInformation.findMemberInformationsByAccountAndProject(
						mockedAccount, mockedProject)).thenReturn(mockedQuery);
		Collection<MemberInformation> mockedSubscriber = Mockito
				.mock(Collection.class);
		Mockito.doReturn(mockedSubscriber).when(mockedWorkItem).getSubcribers();
		Mockito.doReturn(true).when(mockedSubscriber).contains(mockedMember);
		HttpServletRequest mockedHttpServletRequest = Mockito
				.mock(HttpServletRequest.class);
		Mockito.doReturn(mockedProject).when(mockedWorkItem)
				.getWorkItemContainer();
		Mockito.doReturn(mockedProject).when(mockedProject)
				.getParentProjectOrMyself();
		Mockito.doReturn((long) 1).when(mockedProject).getId();
		Mockito.doReturn((long) 1).when(mockedWorkItem).getId();
		String result = aut.unSubscribe(projectId, workItemId,
				mockedHttpServletRequest);

		Assert.assertEquals("redirect:/projects/1/workitems/1?form", result);
		Mockito.verify(mockedSubscriber).remove(mockedMember);
		Mockito.verify(mockedWorkItem).flush();
	}
}
