package org.hcmus.tis.util;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.service.EmailService;
import org.hcmus.tis.service.EmailService.SendMailException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class NotifyAboutWorkItemTest {
	private NotifyAboutWorkItemTask aut;
	@Mock
	private WorkItem mockedWorkItem;
	private String action = "action";
	@Mock
	private MemberInformation member1;
	@Mock
	private MemberInformation member2;
	@Mock
	private Account account1;
	@Mock
	private Account account2;
	@Mock
	private Account lastEdit;
	@Mock
	private EmailService mockMailService;

	@Before
	public void setUp() throws SendMailException {
		MockitoAnnotations.initMocks(this);
		aut = new NotifyAboutWorkItemTask(mockedWorkItem, action,mockMailService);
		doReturn(account1).when(member1).getAccount();
		doReturn(account2).when(member2).getAccount();
		doReturn("email1").when(account1).getEmail();
		doReturn("email2").when(account2).getEmail();
		doReturn(new Date()).when(mockedWorkItem).getDateLastEdit();
		Collection<MemberInformation> subscribers = new HashSet<MemberInformation>();
		subscribers.add(member1);
		subscribers.add(member2);
		doReturn(subscribers).when(mockedWorkItem).getSubcribers();
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				map = (Map) invocation.getArguments()[2];
				return null;
			}
		}).when(mockMailService)
				.sendEmail(anyString(), anyString(), anyMap(), anyString());
		doReturn("firstName").when(lastEdit).getFirstName();
		doReturn("lastName").when(lastEdit).getLastName();
		doReturn(lastEdit).when(mockedWorkItem).getUserLastEdit();
	}

	Map map;

	@Test
	public void testRun() throws SendMailException {
		aut.run();
		verify(mockMailService, times(2)).sendEmail(anyString(),
				eq("workitem-notification.vm"), anyMap(), anyString());
		verify(mockMailService).sendEmail(eq(account1.getEmail()),
				eq("workitem-notification.vm"), anyMap(), anyString());
		verify(mockMailService).sendEmail(eq(account2.getEmail()),
				eq("workitem-notification.vm"), anyMap(), anyString());
		assertEquals(mockedWorkItem.getUserLastEdit().getFirstName() + " " + mockedWorkItem.getUserLastEdit().getLastName(), (String)map.get("member"));
		assertEquals(action, map.get("action"));
		assertEquals(mockedWorkItem.getId(), map.get("workitem"));
		assertEquals(mockedWorkItem.getDateLastEdit(), map.get("time"));
	}
}