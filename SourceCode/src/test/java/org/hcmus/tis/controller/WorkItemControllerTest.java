package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import java.lang.reflect.Member;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.SearchConditionsDTO;
import org.hcmus.tis.dto.datatables.WorkItemDTO;
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
import org.hcmus.tis.util.NotifyAboutWorkItemTask;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@RunWith(PowerMockRunner.class)
@MockStaticEntityMethods
public class WorkItemControllerTest extends AbstractShiroTest {
	@Mock
	private Model mockedUIModel;
	@Mock
	private BindingResult mockedBindingResult;
	@Mock
	private HttpServletRequest mockedHttpRequest;
	@Mock
	private Subject mockedSubject;
	@Mock
	private Session mockedSession;
	@Mock
	private Account mockedLoginedAccount;
	@Mock
	private WorkItem mockedWorkItem;
	@Mock
	private WorkItemType mockedWorkItemType;
	@Mock
	private Project mockedProject;
	@Mock
	private Priority priority;
	@Mock
	private WorkItemStatus workItemStatus;
	@Mock
	Iteration iteration;
	@Mock
	private SearchConditionsDTO searchConditions;
	@Mock
	private TaskExecutor mockedTaskExecutor;
	private WorkItemController aut;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		aut = new WorkItemController();
		doReturn(mockedSession).when(mockedSubject).getSession();
		doReturn((long) 1).when(mockedWorkItem).getId();
		doReturn((long) 2).when(mockedWorkItemType).getId();
		doReturn((long) 3).when(mockedProject).getId();
		doReturn(mockedProject).when(mockedProject).getParentProjectOrMyself();
		doReturn(mockedWorkItemType).when(mockedWorkItem).getWorkItemType();
		doReturn(workItemStatus).when(mockedWorkItem).getStatus();
		doReturn(priority).when(mockedWorkItem).getPriority();
		doReturn(mockedLoginedAccount).when(mockedSession).getAttribute(
				"account");
		doReturn("statusname").when(workItemStatus).getName();
		doReturn("workitemtype").when(mockedWorkItemType).getName();
		doReturn("priorityname").when(priority).getName();
		doReturn(true).when(mockedSubject).isAuthenticated();
		doNothing().when(mockedSubject).checkPermissions(any(String[].class));
		setSubject(mockedSubject);
		aut.setTaskExecutor(mockedTaskExecutor);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				workItemNotifyTask = (NotifyAboutWorkItemTask) invocation
						.getArguments()[0];
				return null;
			}
		}).when(mockedTaskExecutor).execute(any(Runnable.class));
		doReturn("name").when(iteration).getName();
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
		Principal mockedPrincipal = mock(Principal.class);
		doReturn("email").when(mockedPrincipal).getName();
		WorkItemController spyAut = spy(aut);
		doNothing().when(spyAut).populateEditFormCustomly(eq(mockedUIModel),
				any(WorkItem.class));

		Account account = new Account();
		TypedQuery<Account> mockedAccountTypedQuery = mock(TypedQuery.class);
		doReturn(account).when(mockedAccountTypedQuery).getSingleResult();
		PowerMockito.mockStatic(Account.class);
		PowerMockito.when(Account.findAccountsByEmailEquals(eq("email")))
				.thenReturn(mockedAccountTypedQuery);

		TypedQuery<MemberInformation> mockedMemberInformation = mock(TypedQuery.class);
		MemberInformation memberInformation = new MemberInformation();
		doReturn(memberInformation).when(mockedMemberInformation)
				.getSingleResult();
		PowerMockito.mockStatic(MemberInformation.class);
		PowerMockito.when(
				MemberInformation.findMemberInformationsByAccountAndProject(
						eq(account), any(Project.class))).thenReturn(
				mockedMemberInformation);

		spyAut.createForm((long) 1, (long) 1, null, mockedUIModel,
				mockedPrincipal);

		verify(mockedUIModel).addAttribute(eq("projectId"), anyLong());
		verify(mockedUIModel)
				.addAttribute(eq("memberInformationId"), anyLong());
		PowerMockito.verifyStatic();
		WorkItemType.findWorkItemType(anyLong());
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

		TypedQuery<Iteration> mockedTypedQuery = mock(TypedQuery.class);
		List<Iteration> iterations = new ArrayList<Iteration>();
		doReturn(iterations).when(mockedTypedQuery).getResultList();
		PowerMockito.mockStatic(Iteration.class);
		PowerMockito.when(
				Iteration.findIterationsByParentContainer(workItemContainer))
				.thenReturn(mockedTypedQuery);
		aut.populateEditFormCustomly(mockedUIModel, workItem);

		verify(mockedUIModel).addAttribute(eq("workItem"), eq(workItem));
		verify(mockedUIModel).addAttribute(eq("memberinformations"),
				anyCollectionOf(MemberInformation.class));
		verify(mockedUIModel).addAttribute(eq("prioritys"),
				anyCollectionOf(Priority.class));
		verify(mockedUIModel).addAttribute(eq("workitemcontainers"),
				anyCollectionOf(Iteration.class));
		verify(mockedUIModel).addAttribute(eq("workitemstatuses"),
				anyCollectionOf(WorkItemStatus.class));
		verify(mockedUIModel)
				.addAttribute(eq("workItemType"), eq(workItemType));

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
		WorkItemType mockedWorkItemType = mock(WorkItemType.class);
		doReturn(fieldDefines).when(mockedWorkItemType)
				.getAdditionalFieldDefines();
		PowerMockito.mockStatic(WorkItemType.class);
		PowerMockito.when(WorkItemType.findWorkItemType(anyLong())).thenReturn(
				mockedWorkItemType);
		WorkItem spyWorkItem = spy(workItem);
		doNothing().when(spyWorkItem).persist();

		doReturn("utf-8").when(mockedHttpRequest).getCharacterEncoding();
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				finalField = (List<Field>) invocation.getArguments()[0];
				return null;
			}
		}).when(spyWorkItem).setAdditionFiels(any(List.class));

		doReturn("value").when(mockedHttpRequest).getParameter("name");
		Long attachmentIds[] = { (long) 1, (long) 2 };
		PowerMockito.mockStatic(Attachment.class);
		Attachment mockedAttachment1 = mock(Attachment.class);
		Attachment mocedAttachment2 = mock(Attachment.class);
		PowerMockito.when(Attachment.findAttachment((long) 1)).thenReturn(
				mocedAttachment2);
		PowerMockito.when(Attachment.findAttachment((long) 2)).thenReturn(
				mockedAttachment1);

		aut.create(spyWorkItem, mockedBindingResult, (long) 1, mockedUIModel,
				attachmentIds, mockedHttpRequest);

		verify(mockedHttpRequest).getParameter("name");
		verify(spyWorkItem).persist();
		verify(mockedAttachment1).setWorkItem(spyWorkItem);
		verify(mockedAttachment1).flush();
		verify(mocedAttachment2).setWorkItem(spyWorkItem);
		verify(mocedAttachment2).flush();
		assertEquals(1, finalField.size());
		assertEquals("name", finalField.get(0).getName());
		assertEquals("value", finalField.get(0).getValue());
	}

	private NotifyAboutWorkItemTask workItemNotifyTask;

	@Test
	@PrepareForTest({ WorkItemType.class, WorkItem.class })
	public void testUpdate() throws JAXBException {
		PowerMockito.mockStatic(WorkItem.class);
		WorkItem oldWorkItem = Mockito.mock(WorkItem.class);
		PowerMockito.when(WorkItem.findWorkItem(mockedWorkItem.getId()))
				.thenReturn(oldWorkItem);
		doReturn(mockedProject).when(mockedWorkItem).getWorkItemContainer();
		List<FieldDefine> fieldDefines = new ArrayList<FieldDefine>();
		FieldDefine fieldDefine = new FieldDefine();
		fieldDefine.setRefName("name");
		fieldDefines.add(fieldDefine);
		doReturn(fieldDefines).when(mockedWorkItemType)
				.getAdditionalFieldDefines();
		PowerMockito.mockStatic(WorkItemType.class);
		PowerMockito.when(
				WorkItemType.findWorkItemType(mockedWorkItem.getWorkItemType()
						.getId())).thenReturn(mockedWorkItemType);
		doReturn("utf-8").when(mockedHttpRequest).getCharacterEncoding();
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				finalField = (List<Field>) invocation.getArguments()[0];
				return null;
			}
		}).when(mockedWorkItem).setAdditionFiels(any(List.class));

		doReturn("value").when(mockedHttpRequest).getParameter("name");

		aut.update(mockedWorkItem, mockedBindingResult, mockedUIModel,
				mockedHttpRequest);

		verify(mockedHttpRequest).getParameter("name");
		verify(mockedWorkItem).merge();
		assertEquals(1, finalField.size());
		verify(mockedWorkItem).setSubcribers(oldWorkItem.getSubcribers());
		assertEquals("name", finalField.get(0).getName());
		assertEquals("value", finalField.get(0).getValue());
		verify(mockedTaskExecutor).execute(any(Runnable.class));
		assertEquals(mockedWorkItem, workItemNotifyTask.getWorkItem());
		assertEquals("updated", workItemNotifyTask.getAction());
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
		Project mockedProject = mock(Project.class);
		PowerMockito.when(Project.findProject(projectId)).thenReturn(
				mockedProject);
		WorkItem mockedWorkItem = mock(WorkItem.class);
		PowerMockito.when(WorkItem.findWorkItem(workItemId)).thenReturn(
				mockedWorkItem);
		MemberInformation mockedMember = mock(MemberInformation.class);
		TypedQuery<MemberInformation> mockedQuery = mock(TypedQuery.class);
		doReturn(mockedMember).when(mockedQuery).getSingleResult();
		Account mockedAccount = mock(Account.class);
		TypedQuery<Account> mockedAccountQuery = mock(TypedQuery.class);
		doReturn(mockedAccount).when(mockedAccountQuery).getSingleResult();
		PowerMockito.when(Account.findAccountsByEmailEquals(anyString()))
				.thenReturn(mockedAccountQuery);
		PowerMockito.when(
				MemberInformation.findMemberInformationsByAccountAndProject(
						mockedAccount, mockedProject)).thenReturn(mockedQuery);
		Collection<MemberInformation> mockedSubscriber = mock(Collection.class);
		doReturn(mockedSubscriber).when(mockedWorkItem).getSubcribers();
		doReturn(false).when(mockedSubscriber).contains(mockedMember);
		HttpServletRequest mockedHttpServletRequest = mock(HttpServletRequest.class);
		doReturn(mockedProject).when(mockedWorkItem).getWorkItemContainer();
		doReturn(mockedProject).when(mockedProject).getParentProjectOrMyself();
		doReturn((long) 1).when(mockedProject).getId();
		doReturn((long) 1).when(mockedWorkItem).getId();
		String result = aut.subscribe(projectId, workItemId,
				mockedHttpServletRequest);

		Assert.assertEquals("redirect:/projects/1/workitems/1?form", result);
		verify(mockedSubscriber).add(mockedMember);
		verify(mockedWorkItem).flush();

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
		Project mockedProject = mock(Project.class);
		PowerMockito.when(Project.findProject(projectId)).thenReturn(
				mockedProject);
		WorkItem mockedWorkItem = mock(WorkItem.class);
		PowerMockito.when(WorkItem.findWorkItem(workItemId)).thenReturn(
				mockedWorkItem);
		MemberInformation mockedMember = mock(MemberInformation.class);
		TypedQuery<MemberInformation> mockedQuery = mock(TypedQuery.class);
		doReturn(mockedMember).when(mockedQuery).getSingleResult();
		Account mockedAccount = mock(Account.class);
		TypedQuery<Account> mockedAccountQuery = mock(TypedQuery.class);
		doReturn(mockedAccount).when(mockedAccountQuery).getSingleResult();
		PowerMockito.when(Account.findAccountsByEmailEquals(anyString()))
				.thenReturn(mockedAccountQuery);
		PowerMockito.when(
				MemberInformation.findMemberInformationsByAccountAndProject(
						mockedAccount, mockedProject)).thenReturn(mockedQuery);
		Collection<MemberInformation> mockedSubscriber = mock(Collection.class);
		doReturn(mockedSubscriber).when(mockedWorkItem).getSubcribers();
		doReturn(true).when(mockedSubscriber).contains(mockedMember);

		HttpServletRequest mockedHttpServletRequest = mock(HttpServletRequest.class);
		doReturn(mockedProject).when(mockedWorkItem).getWorkItemContainer();
		doReturn(mockedProject).when(mockedProject).getParentProjectOrMyself();
		doReturn((long) 1).when(mockedProject).getId();
		doReturn((long) 1).when(mockedWorkItem).getId();
		String result = aut.subscribe(projectId, workItemId,
				mockedHttpServletRequest);

		assertEquals("redirect:/projects/1/workitems/1?form", result);
		verify(mockedSubscriber, times(0)).add(any(MemberInformation.class));

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
		Account mockedAccount = mock(Account.class);
		Project mockedProject = mock(Project.class);
		WorkItem mockedWorkItem = mock(WorkItem.class);
		MemberInformation mockedMember = mock(MemberInformation.class);
		MemberInformation member = mock(MemberInformation.class);
		PowerMockito.when(WorkItem.findWorkItem(workItemId)).thenReturn(
				mockedWorkItem);
		PowerMockito.when(Project.findProject(projectId)).thenReturn(
				mockedProject);
		TypedQuery<Account> mockedAccountQuery = mock(TypedQuery.class);
		doReturn(mockedAccount).when(mockedAccountQuery).getSingleResult();
		PowerMockito.when(Account.findAccountsByEmailEquals(anyString()))
				.thenReturn(mockedAccountQuery);
		TypedQuery<MemberInformation> mockedMemberQuery = mock(TypedQuery.class);
		doReturn(mockedMember).when(mockedMemberQuery).getSingleResult();
		PowerMockito.when(
				MemberInformation.findMemberInformationsByAccountAndProject(
						mockedAccount, mockedProject)).thenReturn(
				mockedMemberQuery);
		Collection<MemberInformation> mockedSubscribers = mock(Collection.class);
		doReturn(true).when(mockedSubscribers).contains(mockedMember);
		doReturn(mockedSubscribers).when(mockedWorkItem).getSubcribers();

		WorkItemController spy = spy(aut);
		doNothing().when(spy).populateEditFormCustomly(any(Model.class),
				any(WorkItem.class));
		String result = spy.updateForm(projectId, workItemId, mockedUIModel);
		verify(mockedSubscribers).contains(mockedMember);
		verify(mockedUIModel).addAttribute("subscribed", true);
		Assert.assertEquals("workitems/update", result);

	}

	@Test
	@PrepareForTest({ Account.class, Project.class, MemberInformation.class,
			WorkItem.class })
	public void testUnSubscribe() {
		PowerMockito.mockStatic(Account.class);
		PowerMockito.mockStatic(Project.class);
		PowerMockito.mockStatic(MemberInformation.class);
		PowerMockito.mockStatic(WorkItem.class);
		Long projectId = (long) 1;
		Long workItemId = (long) 1;
		Project mockedProject = mock(Project.class);
		PowerMockito.when(Project.findProject(projectId)).thenReturn(
				mockedProject);
		WorkItem mockedWorkItem = mock(WorkItem.class);
		PowerMockito.when(WorkItem.findWorkItem(workItemId)).thenReturn(
				mockedWorkItem);
		MemberInformation mockedMember = mock(MemberInformation.class);
		TypedQuery<MemberInformation> mockedQuery = mock(TypedQuery.class);
		doReturn(mockedMember).when(mockedQuery).getSingleResult();
		Account mockedAccount = mock(Account.class);
		TypedQuery<Account> mockedAccountQuery = mock(TypedQuery.class);
		doReturn(mockedAccount).when(mockedAccountQuery).getSingleResult();
		PowerMockito.when(Account.findAccountsByEmailEquals(anyString()))
				.thenReturn(mockedAccountQuery);
		PowerMockito.when(
				MemberInformation.findMemberInformationsByAccountAndProject(
						mockedAccount, mockedProject)).thenReturn(mockedQuery);
		Collection<MemberInformation> mockedSubscriber = mock(Collection.class);
		doReturn(mockedSubscriber).when(mockedWorkItem).getSubcribers();
		doReturn(true).when(mockedSubscriber).contains(mockedMember);
		HttpServletRequest mockedHttpServletRequest = mock(HttpServletRequest.class);
		doReturn(mockedProject).when(mockedWorkItem).getWorkItemContainer();
		doReturn(mockedProject).when(mockedProject).getParentProjectOrMyself();
		doReturn((long) 1).when(mockedProject).getId();
		doReturn((long) 1).when(mockedWorkItem).getId();
		String result = aut.unSubscribe(projectId, workItemId,
				mockedHttpServletRequest);

		Assert.assertEquals("redirect:/projects/1/workitems/1?form", result);
		verify(mockedSubscriber).remove(mockedMember);
		verify(mockedWorkItem).flush();
	}

	@Test
	@PrepareForTest({ WorkItem.class, Project.class })
	public void testListWorkItemByProjectWithIteration() {
		PowerMockito.mockStatic(Project.class);
		doReturn(iteration).when(searchConditions).getContainer();
		PowerMockito.when(Project.findProject(mockedProject.getId()))
				.thenReturn(mockedProject);
		PowerMockito.mockStatic(WorkItem.class);
		long filteredRecord = (long) 2;
		long totalRecord = (long) 3;
		int startDisplay = 10;
		int displayLength = 10;
		doReturn(iteration).when(mockedWorkItem).getWorkItemContainer();
		HashSet<WorkItem> workItems = new HashSet<WorkItem>();
		workItems.add(mockedWorkItem);
		String globalSearch = "";
		PowerMockito.when(
				WorkItem.findWorkItem(globalSearch, searchConditions,
						startDisplay, displayLength)).thenReturn(workItems);
		PowerMockito.when(WorkItem.getTotalRecord(searchConditions))
				.thenReturn(totalRecord);
		PowerMockito.when(
				WorkItem.getFilteredRecord(globalSearch, searchConditions))
				.thenReturn(filteredRecord);
		String sEcho = "";
		String sSearch = "";
	

		DtReply result = aut.listWorkItemByProject(mockedProject.getId(),
				startDisplay, displayLength, sEcho, sSearch, searchConditions);

		PowerMockito.verifyStatic();
		WorkItem.getTotalRecord(searchConditions);
		PowerMockito.verifyStatic();
		WorkItem.getFilteredRecord(globalSearch, searchConditions);
		PowerMockito.verifyStatic();
		WorkItem.findWorkItem(globalSearch, searchConditions, startDisplay,
				displayLength);
		verify(searchConditions, times(0)).setContainer(any(WorkItemContainer.class));
		assertNotNull(result);
		assertEquals(totalRecord, result.getiTotalRecords());
		assertEquals(filteredRecord, result.getiTotalDisplayRecords());
		assertEquals(workItems.size(), result.getAaData().size());
		WorkItemDTO workItemDto = (WorkItemDTO) result.getAaData().get(0);
		assertEquals((long) mockedWorkItem.getId(), workItemDto.DT_RowId);
		assertEquals(workItemStatus.getName(), workItemDto.getsStatus());
		assertEquals(mockedWorkItemType.getName(), workItemDto.getsType());
		assertEquals(priority.getName(), workItemDto.getPriority());
	}
	@Test
	@PrepareForTest({ WorkItem.class, Project.class })
	public void testListWorkItemByProjectWithNonIteration() {
		PowerMockito.mockStatic(Project.class);
		PowerMockito.when(Project.findProject(mockedProject.getId()))
				.thenReturn(mockedProject);
		PowerMockito.mockStatic(WorkItem.class);
		long filteredRecord = (long) 2;
		long totalRecord = (long) 3;
		int startDisplay = 10;
		int displayLength = 10;
		doReturn(mockedProject).when(mockedWorkItem).getWorkItemContainer();
		HashSet<WorkItem> workItems = new HashSet<WorkItem>();
		workItems.add(mockedWorkItem);
		String globalSearch = "";
		PowerMockito.when(
				WorkItem.findWorkItem(globalSearch, searchConditions,
						startDisplay, displayLength)).thenReturn(workItems);
		PowerMockito.when(WorkItem.getTotalRecord(searchConditions))
				.thenReturn(totalRecord);
		PowerMockito.when(
				WorkItem.getFilteredRecord(globalSearch, searchConditions))
				.thenReturn(filteredRecord);
		String sEcho = "";
		String sSearch = "";

		DtReply result = aut.listWorkItemByProject(mockedProject.getId(),
				startDisplay, displayLength, sEcho, sSearch, searchConditions);
		verify(searchConditions).setContainer(mockedProject);
		
	}
}
