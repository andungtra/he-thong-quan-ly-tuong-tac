package org.hcmus.tis.service;

import static org.junit.Assert.*;

import org.hcmus.tis.controller.AbstractShiroTest;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemActivity;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemCreation;
import org.hcmus.tis.model.WorkItemUpdation;
import org.hcmus.tis.repository.WorkItemActivityRepository;
import org.hcmus.tis.repository.WorkItemRepository;
import org.hcmus.tis.repository.WorkItemVersionRepository;
import org.hcmus.tis.util.AsigneeNotificationTask;
import org.hcmus.tis.util.WorkItemNotification;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

public class WorkItemServiceImplTest extends AbstractShiroTest {
	@Autowired
	WorkItemServiceImpl aut;
	@Mock
	WorkItemRepository workItemRepository;
	@Mock
	WorkItemVersionRepository workItemVersionRepository;
	@Mock
	WorkItemActivityRepository workItemActivityRepository;
	@Mock
	WorkItem workItem;
	@Mock
	WorkItemContainer workItemContainer;
	@Mock
	SecurityService securityService;
	@Mock
	TaskExecutor taskExecutor;
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		aut = new WorkItemServiceImpl();
		aut.setWorkItemRepository(workItemRepository);
		aut.setWorkItemVersionRepository(workItemVersionRepository);
		aut.setSecurityService(securityService);
		aut.setWorkItemActivityRepository(workItemActivityRepository);
		aut.setTaskExecutor(taskExecutor);
		doReturn(workItemContainer).when(workItem).getWorkItemContainer();
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				savedWorkItemActivity = (WorkItemActivity) invocation.getArguments()[0];
				return null;
			}
			
		}).when(workItemActivityRepository).save(any(WorkItemActivity.class));
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				excutedNotification = (WorkItemNotification) invocation.getArguments()[0];
				return null;
			}
		}).when(taskExecutor).execute(any(WorkItemNotification.class));
	}
	private WorkItemActivity savedWorkItemActivity;
	private WorkItemNotification excutedNotification;
	@Test
	public void testSaveNewWorkItem() {
		doReturn(null).when(workItem).getId();
		aut.save(workItem);
		assertEquals(WorkItemCreation.class, savedWorkItemActivity.getClass());
		verify(workItemRepository, atLeastOnce()).save(workItem);
	}
	@Test
	public void testSaveNewWorkItemWithAssignee() {
		doReturn(null).when(workItem).getId();
		doReturn(mock(MemberInformation.class)).when(workItem).getAsignee();
		aut.save(workItem);
		assertEquals(WorkItemCreation.class, savedWorkItemActivity.getClass());
		verify(workItemRepository, atLeastOnce()).save(workItem);
		assertEquals(AsigneeNotificationTask.class, excutedNotification.getClass());
	}
	@Test
	public void testSaveExistedWorkItem(){
		doReturn((long)1).when(workItem).getId();
		aut.save(workItem);
		assertEquals(WorkItemUpdation.class, savedWorkItemActivity.getClass());
		verify(workItemRepository, atLeastOnce()).save(workItem);
	}

}
