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
import org.hcmus.tis.util.NotifyAboutWorkItemTask;

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
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@MockStaticEntityMethods
@RunWith(PowerMockRunner.class)
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
	TypedQuery<MemberInformation> memberQuery;
	@Mock
	TypedQuery<Comment> commentQuery;
	@Mock
	TypedQuery<Account> accountQuery;
	@Mock
	TaskExecutor taskExecutor;
	@Mock
	BindingResult bindingResult;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		doReturn(session).when(subject).getSession();
		doReturn("email").when(subject).getPrincipal();
		doReturn(comments).when(commentQuery).getResultList();
		doReturn(member).when(memberQuery).getSingleResult();
		doReturn(account).when(accountQuery).getSingleResult();
		doReturn((long) 1).when(project).getId();
		doReturn((long) 2).when(account).getId();
		doReturn((long) 3).when(member).getId();
		doReturn((long) 4).when(workItem).getId();
		doReturn(project).when(project).getParentProjectOrMyself();
		doReturn(project).when(workItem).getWorkItemContainer();
		doReturn(workItem).when(comment).getWorkItem();
		aut = new CommentController();
		aut.setTaskExecutor(taskExecutor);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				notifyTask = (NotifyAboutWorkItemTask) invocation
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

	@Test
	@PrepareForTest({ Comment.class, WorkItem.class })
	public void testList() {
		Long workItemId = (long) 1;
		Integer firstResult = 1;
		Integer maxResult = 5;
		PowerMockito.mockStatic(WorkItem.class);
		PowerMockito.when(WorkItem.findWorkItem(workItemId)).thenReturn(
				workItem);
		PowerMockito.mockStatic(Comment.class);
		PowerMockito.when(Comment.findCommentsByWorkItem(any(WorkItem.class)))
				.thenReturn(commentQuery);
		String result = aut.listCommentsByWorkItem(workItemId, firstResult,
				maxResult, uiModel);
		assertEquals("comments/listByWorkItem", result);
		verify(uiModel).addAttribute(eq("comments"), any());
		verify(uiModel).addAttribute("workitem", workItem);
	}

	private NotifyAboutWorkItemTask notifyTask;

	@Test
	@PrepareForTest({ WorkItem.class, Project.class, Account.class,
			MemberInformation.class })
	public void testCreate() {
		PowerMockito.mockStatic(WorkItem.class);
		PowerMockito.when(WorkItem.findWorkItem(workItem.getId())).thenReturn(
				workItem);
		PowerMockito.mockStatic(Project.class);
		PowerMockito.when(Project.findProject(project.getId())).thenReturn(
				project);
		PowerMockito.mockStatic(Account.class);
		PowerMockito.when(
				Account.findAccountsByEmailEquals((String) subject
						.getPrincipal())).thenReturn(accountQuery);
		PowerMockito.mockStatic(MemberInformation.class);
		PowerMockito.when(
				MemberInformation.findMemberInformationsByAccountAndProject(
						account, project)).thenReturn(memberQuery);
		doReturn(false).when(bindingResult).hasErrors();
		String result = aut.create(comment, bindingResult, uiModel);
		verify(taskExecutor).execute(any(Runnable.class));
		assertEquals(workItem, notifyTask.getWorkItem());
		assertEquals("commented", notifyTask.getAction());
		assertEquals("redirect:/projects/" + project.getId() + "/workitems/"
				+ workItem.getId() + "/comments", result);

	}

}
