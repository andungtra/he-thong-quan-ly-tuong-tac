package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.Comment;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.repository.AccountRepository;
import org.hcmus.tis.repository.CommentRepository;
import org.hcmus.tis.repository.MemberInformationRepository;
import org.hcmus.tis.repository.ProjectRepository;
import org.hcmus.tis.repository.WorkItemRepository;
import org.hcmus.tis.util.UpdateWorkitemNotification;

import static org.junit.Assert.*;

import org.junit.After;
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
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public class CommentControllerTest extends AbstractShiroTest {
	private CommentController aut;
	@Mock
	private Model uiModel;
	@Mock
	private Comment comment;
	@Mock
	private Subject subject;
	@Mock
	private Session session;
	@Mock
	private Account account;
	@Mock
	private WorkItem workItem;
	@Mock
	List<Comment> comments;
	@Mock
	Project project;
	@Mock
	MemberInformation member;
	@Mock
	TaskExecutor taskExecutor;
	@Mock
	BindingResult bindingResult;
	@Mock
	MemberInformationRepository memberInformationRepository;
	@Mock
	AccountRepository accountRepository;
	@Mock
	CommentRepository commentRepository;
	@Mock
	WorkItemRepository workItemRepository;
	@Mock
	Page<Comment> pageComment;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		doReturn(session).when(subject).getSession();
		doReturn("email").when(subject).getPrincipal();
		doReturn(comments).when(pageComment).getContent();
		doReturn((long) 1).when(project).getId();
		doReturn((long) 2).when(account).getId();
		doReturn((long) 3).when(member).getId();
		doReturn((long) 4).when(workItem).getId();
		doReturn(project).when(project).getParentProjectOrMyself();
		doReturn(project).when(workItem).getWorkItemContainer();
		doReturn(workItem).when(comment).getWorkItem();
		aut = new CommentController();
		aut.setAccountRepository(accountRepository);
		aut.setTaskExecutor(taskExecutor);
		aut.setMemberInformationRepository(memberInformationRepository);
		aut.setCommentRepository(commentRepository);
		aut.setWorkItemRepository(workItemRepository);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				notifyTask = (UpdateWorkitemNotification) invocation
						.getArguments()[0];
				return null;
			}
		}).when(taskExecutor).execute(any(Runnable.class));
		setSubject(subject);
	}

	@After
	public void tearDown() {
		clearSubject();
	}
	@Mock
	Pageable pageable;
	@Mock
	ProjectRepository projectRepository;
	@Test
	public void testList() {
		Long workItemId = (long) 1;
		Integer firstResult = 1;
		Integer maxResult = 5;
		doReturn(workItem).when(workItemRepository).findOne(workItemId);
		doReturn(pageComment).when(commentRepository).findByWorkItem(eq(workItem), any(Pageable.class) );
		String result = aut.listCommentsByWorkItem(workItemId, firstResult,
				maxResult, uiModel);
		assertEquals("comments/listByWorkItem", result);
		verify(uiModel).addAttribute(eq("comments"), any());
		verify(uiModel).addAttribute("workitem", workItem);
	}

	private UpdateWorkitemNotification notifyTask;

	@Test
	public void testCreate() {
		Long workItemId = workItem.getId();
		doReturn(workItem).when(workItemRepository).findOne(workItemId);
		Long projectId = project.getId();
		doReturn(project).when(projectRepository).findOne(projectId);
		String email = (String) subject.getPrincipal();
		doReturn(account).when(accountRepository).getByEmail(email);
		doReturn(member).when(memberInformationRepository).findByAccountAndProjectAndDeleted(account, project, false);
		doReturn(false).when(bindingResult).hasErrors();
		String result = aut.create(comment, bindingResult, uiModel);
		verify(taskExecutor).execute(any(Runnable.class));
		assertEquals(workItem, notifyTask.getWorkItem());
		//assertEquals("commented", notifyTask.getAction());
		assertEquals("redirect:/projects/" + project.getId() + "/workitems/"
				+ workItem.getId() + "/comments", result);

	}

}
