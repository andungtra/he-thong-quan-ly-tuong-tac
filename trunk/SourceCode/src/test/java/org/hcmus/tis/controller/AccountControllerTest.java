package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.hcmus.tis.dto.NonEditableEvent;
import org.hcmus.tis.dto.datatables.DSRestResponse;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.model.Calendar;
import org.hcmus.tis.model.Event;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.repository.AccountRepository;
import org.hcmus.tis.repository.EventRepository;
import org.hcmus.tis.service.AccountService;
import org.hcmus.tis.service.DuplicateException;
import org.hcmus.tis.service.EmailService;
import org.hcmus.tis.service.EmailService.SendMailException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static  org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public class AccountControllerTest extends AbstractShiroTest {
	@Mock
	private Account account;
	private AccountController aut;
	@Mock
	private AccountService mockedAccountService;
	@Mock
	private Model uiModel;
	@Mock
	private EventRepository eventRepository;
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private Event event;
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		doReturn("email.com").when(account).getEmail();
		doReturn("firstName").when(account).getFirstName();
		doReturn("lastName").when(account).getLastName();
		doReturn("pass").when(account).getPassword();
		doReturn((long)1).when(account).getId();
		doReturn((long)1).when(event).getId();
		doReturn("name").when(event).getName();
		aut = new AccountController();
		aut.setAccountService(mockedAccountService);
		aut.setEventRepository(eventRepository);
		aut.setAccountRepository(accountRepository);
	}

	@Test
	public void testCreate() throws DuplicateException, SendMailException {

		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);
		Model uiModel = mock(Model.class);
		HttpServletRequest request = mock(HttpServletRequest.class);

		String result = aut.create(account, bindingResult, uiModel, request);

		verify(mockedAccountService).createAccount(account);
		assertEquals("accounts/list", result);
	}

	@Test
	public void testCreateDuplicateEmail() throws DuplicateException,
			SendMailException {
		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);

		doThrow(new DuplicateException()).when(mockedAccountService)
				.createAccount(account);	
		AccountController spyAut = spy(aut);
		doNothing().when(spyAut).populateEditForm(any(Model.class), any(Account.class));
		
		String result = spyAut.create(account, bindingResult, uiModel, request);

		verify(mockedAccountService).createAccount(account);
		verify(uiModel).addAttribute("existedEmail", true);
		Assert.assertTrue(result.contains("accounts/create"));
	}

	@Test
	public void testCreateSendEmailError() throws DuplicateException,
			SendMailException {
		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.hasErrors()).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);

		doThrow(new SendMailException()).when(mockedAccountService)
				.createAccount(account);
		AccountController spyAut = spy(aut);
		doNothing()
				.when(spyAut)
				.populateEditForm(any(Model.class),
						any(Account.class));
		String result = spyAut.create(account, bindingResult, uiModel, request);

		verify(mockedAccountService).createAccount(account);
		verify(uiModel).addAttribute("sendEmailError", true);
		Assert.assertTrue(result.contains("accounts/create"));
	}

	@Test
	public void testActiveAccountFormToInactiveAccount() {
		doReturn(AccountStatus.INACTIVE).when(account).getStatus();
		String activeKey = UUID.randomUUID().toString();
		doReturn(activeKey).when(account).getPassword();
		long id = account.getId();
		doReturn(account).when(mockedAccountService).findAccount(id);
		AccountController spyAut = spy(aut);
		doNothing()
				.when(spyAut)
				.populateEditForm(any(Model.class),
						any(Account.class));

		String result = spyAut.activeForm(account.getId(), activeKey,
				uiModel);

		Assert.assertEquals("accounts/active", result);
	}

	@Test
	public void testActiveAccountFormToActivedAccount() {
		doReturn(AccountStatus.ACTIVE).when(account).getStatus();
		String activeKey = UUID.randomUUID().toString();
		doReturn(activeKey).when(account).getPassword();
		long id = account.getId();
		doReturn(account).when(mockedAccountService).findAccount(id);

		String result = aut.activeForm(account.getId(), activeKey, uiModel);

		Assert.assertEquals("accounts/invalidactive", result);
	}

	@Test
	public void testActiveAccountFormWithInvalidActiveKey() {
		account.setStatus(AccountStatus.INACTIVE);
		String activeKey = UUID.randomUUID().toString();
		account.setPassword("password");
		long id = account.getId();
		doReturn(account).when(mockedAccountService).findAccount(id);

		String result = aut.activeForm(account.getId(), activeKey, uiModel);

		Assert.assertEquals("accounts/invalidactive", result);
	}

	@Test
	public void testActiveAccountValidActiveKey() {
		doReturn(AccountStatus.INACTIVE).when(account).getStatus();
		String activeKey = UUID.randomUUID().toString();
		doReturn(activeKey).when(account).getPassword();
		long id = account.getId();
		doReturn(account).when(mockedAccountService).findAccount(id);

		Account updatedAccount = new Account();
		updatedAccount.setId((long) 1);
		updatedAccount.setPassword("password");
		updatedAccount.setFirstName("firstName");
		updatedAccount.setLastName("lastName");

		doReturn(account).when(mockedAccountService)
				.findAccount(updatedAccount.getId());

		String result = aut.active(updatedAccount, activeKey, uiModel);

		verify(mockedAccountService).updateAccount(
				any(Account.class));
		Assert.assertEquals("accounts/home", result);
	}

	@Test
	public void testActiveAccountInvalidActiveKey() {
		doReturn(AccountStatus.INACTIVE).when(account).getStatus();
		String activeKey = UUID.randomUUID().toString();
		doReturn(activeKey + "jlsdjl").when(account).getPassword();
		long id = account.getId();
		doReturn(account).when(mockedAccountService).findAccount(id);

		Account updatedAccount = new Account();
		updatedAccount.setId((long) 1);
		updatedAccount.setPassword("password");
		updatedAccount.setFirstName("firstName");
		updatedAccount.setLastName("lastName");

		doReturn(account).when(mockedAccountService)
				.findAccount(updatedAccount.getId());

		String result = aut.active(updatedAccount, activeKey, uiModel);
		verify(mockedAccountService, times(0)).updateAccount(
				any(Account.class));
		Assert.assertEquals("accounts/activeFailure", result);
	}

	@Test
	public void testActiveForActivedAccount() {
		account.setStatus(AccountStatus.ACTIVE);
		String activeKey = UUID.randomUUID().toString();
		account.setPassword(activeKey);
		long id = account.getId();
		doReturn(account).when(mockedAccountService).findAccount(id);

		Account updatedAccount = new Account();
		updatedAccount.setId((long) 1);
		updatedAccount.setPassword("password");
		updatedAccount.setFirstName("firstName");
		updatedAccount.setLastName("lastName");

		doReturn(account).when(mockedAccountService)
				.findAccount(updatedAccount.getId());

		String result = aut.active(updatedAccount, activeKey, uiModel);
		verify(mockedAccountService, times(0)).updateAccount(
				any(Account.class));
		Assert.assertEquals("accounts/activeFailure", result);
	}

/*	@Test
	public void testFindAccount() {
		PowerMockito.mockStatic(Account.class);
		String term = "term";
		TypedQuery<Account> mockedQuery = mock(TypedQuery.class);
		// PowerMockito.doReturn(mockedQuery).when(Account.findAccount(eq(term),
		// anyInt(), anyInt()));
		aut.findAccount(term);

		PowerMockito.verifyStatic();
		Account.findAccount(eq(term), anyInt(),
				anyInt());
	}*/

	@Test
	public void testCreatEvent() {
		BindingResult bindingResult = mock(BindingResult.class);
		doReturn(false).when(bindingResult).hasErrors();
		Long accountId = account.getId();
		doReturn(account).when(accountRepository).findOne(accountId);
		Calendar mockedCalender = mock(Calendar.class);
		doReturn(mockedCalender).when(account).getCalendar();
		Collection<Event> events = new HashSet<Event>();
		doReturn(events).when(mockedCalender).getEvents();

		DSRestResponse result = aut.creatEvent(account.getId(), event,
				bindingResult);
		eventRepository.save(event);
		assertTrue(events.contains(event));
		assertEquals(0, result.getResponse().getStatus());
		assertEquals(1, result.getResponse().getData().size());
		Event resultEvent = (Event) result.getResponse().getData().get(0);
		assertEquals(event.getName(), resultEvent.getName());
	}

	@Test
	public void testUpdateEvent() {
		Event returnEvent = mock(Event.class);
		Long eventId  = event.getId();
		doReturn(returnEvent).when(eventRepository).findOne(eventId);
		Long accountId = (long) 1;
		BindingResult bindingResult = mock(BindingResult.class);
		doReturn(false).when(bindingResult).hasErrors();
		DSRestResponse result = aut.updateEvent(accountId, (long) 10, event,
				bindingResult);
		verify(eventRepository).save(event);
		verify(event).setId((long) 10);
		assertEquals(0, result.getResponse().getStatus());
		assertEquals(1, result.getResponse().getData().size());
		assertSame(returnEvent, (Event) result.getResponse().getData().get(0));
	}

	@Test
	@PrepareForTest(Account.class)
	public void testGetEvents() {
		Long accountId = account.getId();
		Calendar calendar = new Calendar();
		doReturn(calendar).when(account).getCalendar();
		Collection<Event> events = new HashSet<Event>();
		calendar.setEvents(events);
		Event event = new Event();
		event.setCalendars(new HashSet<Calendar>());
		event.getCalendars().add(calendar);
		events.add(event);
		doReturn(account).when(accountRepository).findOne(accountId);
		
		DSRestResponse restResponse = aut.getEvents(accountId);
		
		assertEquals(0, restResponse.getResponse().getStatus());
		assertEquals(1, restResponse.getResponse().getData().size());
		assertSame(event, restResponse.getResponse().getData().get(0));
	}
	
	@Test
	@PrepareForTest({Account.class, AccountController.class})
	public void testGetSharedEvents() {
		Calendar accountCalendar = new Calendar();
		doReturn(accountCalendar).when(account).getCalendar();
		accountCalendar.setEvents(new HashSet<Event>());
		Event event = new Event();
		event.setCalendars(new HashSet<Calendar>());
		Calendar projectCalendar = new Calendar();
		projectCalendar.setEvents(new HashSet<Event>());
		Project project = new Project();
		project.setName("name");
		projectCalendar.setProject(project);
		Event sharedEvent = new Event();
		sharedEvent.setDescription("des");
		sharedEvent.setCalendars(new HashSet<Calendar>());
		sharedEvent.getCalendars().add(accountCalendar);
		sharedEvent.getCalendars().add(projectCalendar);
		event.getCalendars().add(accountCalendar);
		accountCalendar.getEvents().add(event);
		accountCalendar.getEvents().add(sharedEvent);
		projectCalendar.getEvents().add(sharedEvent);
		Long accountId = account.getId();
		doReturn(account).when(accountRepository).findOne(accountId);
		
		DSRestResponse restResponse = aut.getEvents(account.getId());
		
		assertEquals(0, restResponse.getResponse().getStatus());
		assertEquals(2, restResponse.getResponse().getData().size());
		assertTrue(restResponse.getResponse().getData().contains(event));
		assertTrue(restResponse.getResponse().getData().get(1) instanceof NonEditableEvent || restResponse.getResponse().getData().get(0) instanceof NonEditableEvent);
	}

	@Test
	public void testDeleteEvents() {
		Long id = (long) 1;
		Calendar mockedCalendar = mock(Calendar.class);
		Collection<Calendar> calendars = new HashSet<Calendar>();
		calendars.add(mockedCalendar);
		doReturn(calendars).when(event).getCalendars();
		Collection<Event> mockedEvents = mock(Collection.class);
		doReturn(mockedEvents).when(mockedCalendar).getEvents();
		doReturn(event).when(eventRepository).findOne(id);

		DSRestResponse result = aut.deleteEvent((long) 1, id);

		verify(eventRepository).findOne(id);
		verify(mockedEvents).remove(event);
		verify(eventRepository).delete(event);
		assertSame(event, result.getResponse().getData().get(0));
	}

}
