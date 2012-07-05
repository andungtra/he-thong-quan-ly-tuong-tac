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
import org.hcmus.tis.repository.AccountRepository;
import org.hcmus.tis.repository.AttachmentRepository;
import org.hcmus.tis.repository.IterationRepository;
import org.hcmus.tis.repository.MemberInformationRepository;
import org.hcmus.tis.repository.PriorityRepository;
import org.hcmus.tis.repository.ProjectRepository;
import org.hcmus.tis.repository.WorkItemRepository;
import org.hcmus.tis.repository.WorkItemStatusRepository;
import org.hcmus.tis.repository.WorkItemTypeRepository;
import org.hcmus.tis.util.UpdateWorkitemNotification;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@RunWith(PowerMockRunner.class)
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
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private PriorityRepository priorityRepository;
	@Mock
	private WorkItemStatusRepository workItemStatusRepository;
	@Mock
	private WorkItemTypeRepository workItemTypeRepository;
	@Mock
	private MemberInformationRepository memberInformationRepository;
	@Mock
	private AttachmentRepository attachmentRepository;
	@Mock
	private IterationRepository iterationRepository;
	@Mock
	private ProjectRepository projectRepository;
	@Mock
	private WorkItemRepository workItemRepository;
	@Mock
	private Account account;
	private WorkItemController aut;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		aut = new WorkItemController();
		aut.setPriorityRepository(priorityRepository);
		aut.setAccountRepository(accountRepository);
		doReturn(mockedSession).when(mockedSubject).getSession();
		aut.setAttachmentRepository(attachmentRepository);
		aut.setIterationRepository(iterationRepository);
		aut.setProjectRepository(projectRepository);
		aut.setWorkItemRepository(workItemRepository);
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
		doReturn("email").when(mockedSubject).getPrincipal();
		aut.setTaskExecutor(mockedTaskExecutor);
		aut.setWorkItemStatusRepository(workItemStatusRepository);
		aut.setWorkItemTypeRepository(workItemTypeRepository);
		aut.setMemberInformationRepository(memberInformationRepository);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				workItemNotifyTask = (UpdateWorkitemNotification) invocation
						.getArguments()[0];
				return null;
			}
		}).when(mockedTaskExecutor).execute(any(Runnable.class));
		doReturn("name").when(iteration).getName();
		
		doReturn((long)1).when(account).getId();
		doReturn("email").when(account).getEmail();
	}

	@After
	public void tearDown() {
		clearSubject();
	}

	@Test
	public void testCreateForm() throws NotPermissionException {
		PowerMockito.mockStatic(Project.class);
		WorkItemController spyAut = spy(aut);
		doNothing().when(spyAut).populateEditForm(eq(mockedUIModel),
				any(WorkItem.class));
		String email = (String) mockedSubject.getPrincipal();
		doReturn(account).when(accountRepository).getByEmail(email);

		MemberInformation memberInformation = new MemberInformation();
		doReturn(memberInformation).when(memberInformationRepository).findByAccountAndProjectAndDeleted(eq(account), any(Project.class), eq(false));

		spyAut.createForm((long) 1, (long) 1, null, mockedUIModel);

		verify(mockedUIModel).addAttribute(eq("projectId"), anyLong());
		verify(mockedUIModel)
				.addAttribute(eq("memberInformationId"), anyLong());
	}

	@Test
	@PrepareForTest({WorkItemContainer.class})
	public void testPopulateEditFormCustomly() {
		Long projectId = (long) 1;
		WorkItem workItem = new WorkItem();
		WorkItemContainer workItemContainer = new Project();
		workItemContainer.setId(projectId);
		WorkItemType workItemType = new WorkItemType();
		workItem.setWorkItemType(workItemType);
		workItem.setWorkItemContainer(workItemContainer);
		PowerMockito.mockStatic(WorkItemContainer.class);
		//PowerMockito.when(
				//WorkItemContainer.findWorkItemContainer(workItemContainer
					//	.getId())).thenReturn(workItemContainer);

		List<Iteration> iterations = new ArrayList<Iteration>();
		doReturn(iterations).when(iterationRepository).findByAncestor(workItemContainer);
		aut.populateEditForm(mockedUIModel, workItem);

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
	public void testCreate() throws JAXBException {
		WorkItemType workItemType = new WorkItemType();
		workItemType.setId((long) 1);
		doReturn(workItemType).when(mockedWorkItem).getWorkItemType();
		List<FieldDefine> fieldDefines = new ArrayList<FieldDefine>();
		FieldDefine fieldDefine = new FieldDefine();
		fieldDefine.setRefName("name");
		fieldDefines.add(fieldDefine);
		WorkItemType mockedWorkItemType = mock(WorkItemType.class);
		doReturn(fieldDefines).when(mockedWorkItemType)
				.getAdditionalFieldDefines();
		Long workItemTypeId = mockedWorkItem.getWorkItemType().getId();
		doReturn(mockedWorkItemType).when(workItemTypeRepository).findOne(workItemTypeId);

		doReturn("utf-8").when(mockedHttpRequest).getCharacterEncoding();
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				finalField = (List<Field>) invocation.getArguments()[0];
				return null;
			}
		}).when(mockedWorkItem).setAdditionFiels(any(List.class));

		doReturn("value").when(mockedHttpRequest).getParameter("name");
		Long attachmentIds[] = { (long) 1, (long) 2 };
		Attachment mockedAttachment1 = mock(Attachment.class);
		Attachment mocedAttachment2 = mock(Attachment.class);
		doReturn(mocedAttachment2).when(attachmentRepository).findOne((long)1);
		doReturn(mockedAttachment1).when(attachmentRepository).findOne((long)2);

		aut.create(mockedWorkItem, mockedBindingResult, (long)1, mockedUIModel,
				attachmentIds, mockedHttpRequest);

		verify(mockedHttpRequest).getParameter("name");
		verify(workItemRepository).save(mockedWorkItem);
		verify(mockedAttachment1).setWorkItem(mockedWorkItem);
		verify(attachmentRepository, atLeastOnce()).flush();
		verify(mocedAttachment2).setWorkItem(mockedWorkItem);
		assertEquals(1, finalField.size());
		assertEquals("name", finalField.get(0).getName());
		assertEquals("value", finalField.get(0).getValue());
	}

	private UpdateWorkitemNotification workItemNotifyTask;

	@Test
	public void testUpdate() throws JAXBException {
		
		WorkItem oldWorkItem = Mockito.mock(WorkItem.class);
		Long workItemId = mockedWorkItem.getId();
		doReturn(oldWorkItem).when(workItemRepository).findOne(workItemId);
		doReturn(mockedProject).when(mockedWorkItem).getWorkItemContainer();
		List<FieldDefine> fieldDefines = new ArrayList<FieldDefine>();
		FieldDefine fieldDefine = new FieldDefine();
		fieldDefine.setRefName("name");
		fieldDefines.add(fieldDefine);
		doReturn(fieldDefines).when(mockedWorkItemType)
				.getAdditionalFieldDefines();
		Long workItemTyeId = mockedWorkItem.getWorkItemType()
				.getId();
		doReturn(mockedWorkItemType).when(workItemTypeRepository).findOne(workItemTyeId);
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
				mockedHttpRequest, (long)1);

		verify(mockedHttpRequest).getParameter("name");
		verify(workItemRepository).save(mockedWorkItem);
		assertEquals(1, finalField.size());
		verify(mockedWorkItem).setSubcribers(oldWorkItem.getSubcribers());
		assertEquals("name", finalField.get(0).getName());
		assertEquals("value", finalField.get(0).getValue());
		verify(mockedTaskExecutor).execute(any(Runnable.class));
		assertEquals(mockedWorkItem, workItemNotifyTask.getWorkItem());
		//assertEquals("updated", workItemNotifyTask.getAction());
	}

	@Test
	public void testSubscibeWithNonSubscibedMember() {
		Long projectId = (long) 1;
		Long workItemId = (long) 1;
		doReturn(mockedProject).when(projectRepository).findOne(projectId);
		WorkItem mockedWorkItem = mock(WorkItem.class);
		doReturn(mockedWorkItem).when(workItemRepository).findOne(workItemId);
		MemberInformation mockedMember = mock(MemberInformation.class);
		String email = (String) mockedSubject.getPrincipal();
		doReturn(account).when(accountRepository).getByEmail(email);
		doReturn(mockedMember).when(memberInformationRepository).findByAccountAndProjectAndDeleted(account, mockedProject, false);
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
		verify(workItemRepository).save(mockedWorkItem);

	}

	@Test
	public void testSubscibeWithSubscibedMember() {
		Long projectId = (long) 1;
		Long workItemId = (long) 1;
		doReturn(mockedProject).when(projectRepository).findOne(projectId);
		WorkItem mockedWorkItem = mock(WorkItem.class);
		doReturn(mockedWorkItem).when(workItemRepository).findOne(workItemId);
		MemberInformation mockedMember = mock(MemberInformation.class);
		Account mockedAccount = mock(Account.class);
		String email = (String) mockedSubject.getPrincipal();
		doReturn(account).when(accountRepository).getByEmail(email);
		doReturn(mockedMember).when(memberInformationRepository).findByAccountAndProjectAndDeleted(account, mockedProject, false);
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
	public void testUpdateForm() {
		Long projectId = (long) 1;
		Long workItemId = (long) 2;
		Project mockedProject = mock(Project.class);
		WorkItem mockedWorkItem = mock(WorkItem.class);
		MemberInformation mockedMember = mock(MemberInformation.class);
		MemberInformation member = mock(MemberInformation.class);
		doReturn(mockedWorkItem).when(workItemRepository).findOne(workItemId);
		doReturn(mockedProject).when(projectRepository).findOne(projectId);
		String email = (String) mockedSubject.getPrincipal();
		doReturn(account).when(accountRepository).getByEmail(email);
		doReturn(mockedMember).when(memberInformationRepository).findByAccountAndProjectAndDeleted(account, mockedProject, false);
		Collection<MemberInformation> mockedSubscribers = mock(Collection.class);
		doReturn(true).when(mockedSubscribers).contains(mockedMember);
		doReturn(mockedSubscribers).when(mockedWorkItem).getSubcribers();

		WorkItemController spy = spy(aut);
		doNothing().when(spy).populateEditForm(any(Model.class),
				any(WorkItem.class));
		String result = spy.updateForm(projectId, workItemId, mockedUIModel);
		verify(mockedSubscribers).contains(mockedMember);
		verify(mockedUIModel).addAttribute("subscribed", true);
		Assert.assertEquals("workitems/update", result);

	}

	@Test
	public void testUnSubscribe() {
		Long projectId = (long) 1;
		Long workItemId = (long) 1;
		Project mockedProject = mock(Project.class);
		doReturn(mockedProject).when(projectRepository).findOne(projectId);
		WorkItem mockedWorkItem = mock(WorkItem.class);
		doReturn(mockedWorkItem).when(workItemRepository).findOne(workItemId);
		MemberInformation mockedMember = mock(MemberInformation.class);
		String email = (String) mockedSubject.getPrincipal();
		doReturn(account).when(accountRepository).getByEmail(email);
	doReturn(mockedMember).when(memberInformationRepository).findByAccountAndProjectAndDeleted(account, mockedProject, false);
		Collection<MemberInformation> mockedSubscriber = mock(Collection.class);
		doReturn(true).when(mockedSubscriber).contains(mockedMember);
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
		verify(workItemRepository).save(mockedWorkItem);
	}

	@Test
	public void testListWorkItemByProjectWithIteration() {
		doReturn(iteration).when(searchConditions).getContainer();
		Long projectId = mockedProject.getId();
		doReturn(mockedProject).when(projectRepository).findOne(projectId);
		long filteredRecord = (long) 2;
		long totalRecord = (long) 3;
		int startDisplay = 10;
		int displayLength = 10;
		doReturn(iteration).when(mockedWorkItem).getWorkItemContainer();
		ArrayList<WorkItem> workItems = new ArrayList<WorkItem>();
		workItems.add(mockedWorkItem);
		String globalSearch = "";
		//doReturn(workItems).when(workItemRepository).findBy(globalSearch, searchConditions, startDisplay, displayLength);
		doReturn(totalRecord).when(workItemRepository).countBy(null, searchConditions);
		doReturn(filteredRecord).when(workItemRepository).countBy(globalSearch, searchConditions);
		String sEcho = "";
		String sSearch = "";
	

		DtReply result = aut.listWorkItemByProject(mockedProject.getId(),
				startDisplay, displayLength, sEcho, sSearch, searchConditions, null);
		verify(workItemRepository).countBy(null, searchConditions);
		verify(workItemRepository).countBy(globalSearch, searchConditions);
		//verify(workItemRepository).findBy(globalSearch, searchConditions, startDisplay, displayLength);
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
	public void testListWorkItemByProjectWithNonIteration() {
		Long projectId = mockedProject.getId();
		doReturn(mockedProject).when(projectRepository).findOne(projectId);
		long filteredRecord = (long) 2;
		long totalRecord = (long) 3;
		int startDisplay = 10;
		int displayLength = 10;
		doReturn(mockedProject).when(mockedWorkItem).getWorkItemContainer();
		ArrayList<WorkItem> workItems = new ArrayList<WorkItem>();
		workItems.add(mockedWorkItem);
		String globalSearch = "";
		//doReturn(workItems).when(workItemRepository).findBy(globalSearch, searchConditions, startDisplay, displayLength);
		doReturn(totalRecord).when(workItemRepository).countBy(null, searchConditions);
		doReturn(filteredRecord).when(workItemRepository).countBy(globalSearch, searchConditions);
		String sEcho = "";
		String sSearch = "";

		DtReply result = aut.listWorkItemByProject(mockedProject.getId(),
				startDisplay, displayLength, sEcho, sSearch, searchConditions, null);
		verify(searchConditions).setContainer(mockedProject);
		
	}
}
