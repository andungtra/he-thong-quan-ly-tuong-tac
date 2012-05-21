package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.hcmus.tis.dto.DSRestResponse;
import org.hcmus.tis.dto.NonEditableEvent;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.model.Calendar;
import org.hcmus.tis.model.Event;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.service.AccountService;
import org.hcmus.tis.service.DuplicateException;
import org.hcmus.tis.service.EmailService;
import org.hcmus.tis.service.EmailService.SendMailException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


@RunWith(PowerMockRunner.class)
@PrepareForTest(Account.class)
@MockStaticEntityMethods
public class AccountControllerTest {
	private Account spyAccount;
	private AccountController aut;
	private AccountService mockedAccountService;
	private Model uiModel;

	@Before
	public void setUp() {
		spyAccount = Mockito.spy(new Account());
		Mockito.doNothing().when(spyAccount).persist();
		spyAccount.setEmail("email.com");
		spyAccount.setFirstName("firstName");
		spyAccount.setLastName("lastName");
		spyAccount.setId((long) 1);

		aut = new AccountController();
		mockedAccountService = Mockito.mock(AccountService.class);
		aut.setAccountService(mockedAccountService);

		uiModel = Mockito.mock(Model.class);
	}

	@Test
	public void testCreate() throws DuplicateException, SendMailException {

		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		Model uiModel = Mockito.mock(Model.class);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

		String result = aut.create(spyAccount, bindingResult, uiModel, request);

		Mockito.verify(mockedAccountService).createAccount(spyAccount);
		Assert.assertTrue(result.contains("redirect:/accounts/"));
	}

	@Test
	public void testCreateDuplicateEmail() throws DuplicateException,
			SendMailException {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

		Mockito.doThrow(new DuplicateException()).when(mockedAccountService)
				.createAccount(spyAccount);	
		AccountController spyAut = Mockito.spy(aut);
		Mockito.doNothing().when(spyAut).populateEditForm(Mockito.any(Model.class), Mockito.any(Account.class));
		
		String result = spyAut.create(spyAccount, bindingResult, uiModel, request);

		Mockito.verify(mockedAccountService).createAccount(spyAccount);
		Mockito.verify(uiModel).addAttribute("existedEmail", true);
		Assert.assertTrue(result.contains("accounts/create"));
	}

	@Test
	public void testCreateSendEmailError() throws DuplicateException,
			SendMailException {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

		Mockito.doThrow(new SendMailException()).when(mockedAccountService)
				.createAccount(spyAccount);
		AccountController spyAut = Mockito.spy(aut);
		Mockito.doNothing()
				.when(spyAut)
				.populateEditForm(Mockito.any(Model.class),
						Mockito.any(Account.class));
		String result = spyAut.create(spyAccount, bindingResult, uiModel, request);

		Mockito.verify(mockedAccountService).createAccount(spyAccount);
		Mockito.verify(uiModel).addAttribute("sendEmailError", true);
		Assert.assertTrue(result.contains("accounts/create"));
	}

	@Test
	public void testActiveAccountFormToInactiveAccount() {
		spyAccount.setStatus(AccountStatus.INACTIVE);
		String activeKey = UUID.randomUUID().toString();
		spyAccount.setPassword(activeKey);
		long id = spyAccount.getId();
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(id);
		AccountController spyAut = Mockito.spy(aut);
		Mockito.doNothing()
				.when(spyAut)
				.populateEditForm(Mockito.any(Model.class),
						Mockito.any(Account.class));

		String result = spyAut.activeForm(spyAccount.getId(), activeKey,
				uiModel);

		Assert.assertEquals("accounts/active", result);
	}

	@Test
	public void testActiveAccountFormToActivedAccount() {
		spyAccount.setStatus(AccountStatus.ACTIVE);
		String activeKey = UUID.randomUUID().toString();
		spyAccount.setPassword(activeKey);
		long id = spyAccount.getId();
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(id);

		String result = aut.activeForm(spyAccount.getId(), activeKey, uiModel);

		Assert.assertEquals("accounts/invalidactive", result);
	}

	@Test
	public void testActiveAccountFormWithInvalidActiveKey() {
		spyAccount.setStatus(AccountStatus.INACTIVE);
		String activeKey = UUID.randomUUID().toString();
		spyAccount.setPassword("password");
		long id = spyAccount.getId();
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(id);

		String result = aut.activeForm(spyAccount.getId(), activeKey, uiModel);

		Assert.assertEquals("accounts/invalidactive", result);
	}

	@Test
	public void testActiveAccountValidActiveKey() {
		spyAccount.setStatus(AccountStatus.INACTIVE);
		String activeKey = UUID.randomUUID().toString();
		spyAccount.setPassword(activeKey);
		long id = spyAccount.getId();
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(id);

		Account updatedAccount = new Account();
		updatedAccount.setId((long) 1);
		updatedAccount.setPassword("password");
		updatedAccount.setFirstName("firstName");
		updatedAccount.setLastName("lastName");

		Mockito.doReturn(spyAccount).when(mockedAccountService)
				.findAccount(updatedAccount.getId());

		String result = aut.active(updatedAccount, activeKey, uiModel);

		Mockito.verify(mockedAccountService).updateAccount(
				Mockito.any(Account.class));
		Assert.assertEquals("accounts/home", result);
	}

	@Test
	public void testActiveAccountInvalidActiveKey() {
		spyAccount.setStatus(AccountStatus.INACTIVE);
		String activeKey = UUID.randomUUID().toString();
		spyAccount.setPassword("password");
		long id = spyAccount.getId();
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(id);

		Account updatedAccount = new Account();
		updatedAccount.setId((long) 1);
		updatedAccount.setPassword("password");
		updatedAccount.setFirstName("firstName");
		updatedAccount.setLastName("lastName");

		Mockito.doReturn(spyAccount).when(mockedAccountService)
				.findAccount(updatedAccount.getId());

		String result = aut.active(updatedAccount, activeKey, uiModel);
		Mockito.verify(mockedAccountService, Mockito.times(0)).updateAccount(
				Mockito.any(Account.class));
		Assert.assertEquals("accounts/activeFailure", result);
	}

	@Test
	public void testActiveForActivedAccount() {
		spyAccount.setStatus(AccountStatus.ACTIVE);
		String activeKey = UUID.randomUUID().toString();
		spyAccount.setPassword(activeKey);
		long id = spyAccount.getId();
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(id);

		Account updatedAccount = new Account();
		updatedAccount.setId((long) 1);
		updatedAccount.setPassword("password");
		updatedAccount.setFirstName("firstName");
		updatedAccount.setLastName("lastName");

		Mockito.doReturn(spyAccount).when(mockedAccountService)
				.findAccount(updatedAccount.getId());

		String result = aut.active(updatedAccount, activeKey, uiModel);
		Mockito.verify(mockedAccountService, Mockito.times(0)).updateAccount(
				Mockito.any(Account.class));
		Assert.assertEquals("accounts/activeFailure", result);
	}

	@Test
	public void testFindAccount() {
		PowerMockito.mockStatic(Account.class);
		String term = "term";
		TypedQuery<Account> mockedQuery = Mockito.mock(TypedQuery.class);
		// PowerMockito.doReturn(mockedQuery).when(Account.findAccount(Mockito.eq(term),
		// Mockito.anyInt(), Mockito.anyInt()));
		aut.findAccount(term);

		PowerMockito.verifyStatic();
		Account.findAccount(Mockito.eq(term), Mockito.anyInt(),
				Mockito.anyInt());
	}

	@Test
	@PrepareForTest(Account.class)
	public void testCreatEvent() {
		Event event = new Event();
		event.setName("name");
		Event spyEvent = Mockito.spy(event);
		Mockito.doNothing().when(spyEvent).persist();
		Long accountId = (long) 1;
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.doReturn(false).when(bindingResult).hasErrors();
		Account mockedAccount = Mockito.mock(Account.class);
		PowerMockito.mockStatic(Account.class);
		PowerMockito.when(Account.findAccount(accountId)).thenReturn(
				mockedAccount);
		Calendar mockedCalender = Mockito.mock(Calendar.class);
		Mockito.doReturn(mockedCalender).when(mockedAccount).getCalendar();
		Collection<Event> events = new HashSet<Event>();
		Mockito.doReturn(events).when(mockedCalender).getEvents();

		DSRestResponse result = aut.creatEvent(accountId, spyEvent,
				bindingResult);
		Mockito.verify(spyEvent).persist();
		assertTrue(events.contains(spyEvent));
		assertEquals(0, result.getResponse().getStatus());
		assertEquals(1, result.getResponse().getData().size());
		Event resultEvent = (Event) result.getResponse().getData().get(0);
		assertEquals("name", resultEvent.getName());
	}

	@Test
	public void testUpdateEvent() {
		Event event = new Event();
		event.setName("name");
		Event spyEvent = Mockito.spy(event);
		Event returnEvent = new Event();
		Mockito.doReturn(returnEvent).when(spyEvent).merge();
		Mockito.doNothing().when(spyEvent).setId((long) 10);
		Long accountId = (long) 1;
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.doReturn(false).when(bindingResult).hasErrors();
		DSRestResponse result = aut.updateEvent(accountId, (long) 10, spyEvent,
				bindingResult);
		Mockito.verify(spyEvent).merge();
		Mockito.verify(spyEvent).setId((long) 10);
		assertEquals(0, result.getResponse().getStatus());
		assertEquals(1, result.getResponse().getData().size());
		assertSame(returnEvent, (Event) result.getResponse().getData().get(0));
	}

	@Test
	@PrepareForTest(Account.class)
	public void testGetEvents() {
		Long accountId = (long)1;
		Account mockedAccount = Mockito.mock(Account.class);
		Calendar calendar = new Calendar();
		Mockito.doReturn(calendar).when(mockedAccount).getCalendar();
		Collection<Event> events = new HashSet<Event>();
		calendar.setEvents(events);
		Event event = new Event();
		event.setCalendars(new HashSet<Calendar>());
		event.getCalendars().add(calendar);
		events.add(event);
		PowerMockito.mockStatic(Account.class);
		PowerMockito.when(Account.findAccount(accountId)).thenReturn(mockedAccount);
		
		DSRestResponse restResponse = aut.getEvents(accountId);
		
		assertEquals(0, restResponse.getResponse().getStatus());
		assertEquals(1, restResponse.getResponse().getData().size());
		assertSame(event, restResponse.getResponse().getData().get(0));
	}
	
	@Test
	@PrepareForTest({Account.class, AccountController.class})
	public void testGetSharedEvents() {
		Long accountId = (long)1;
		Account mockedAccount = Mockito.mock(Account.class);
		Calendar accountCalendar = new Calendar();
		Mockito.doReturn(accountCalendar).when(mockedAccount).getCalendar();
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
		PowerMockito.mockStatic(Account.class);
		PowerMockito.when(Account.findAccount(accountId)).thenReturn(mockedAccount);
		
		DSRestResponse restResponse = aut.getEvents(accountId);
		
		assertEquals(0, restResponse.getResponse().getStatus());
		assertEquals(2, restResponse.getResponse().getData().size());
		assertTrue(restResponse.getResponse().getData().contains(event));
		assertEquals(NonEditableEvent.class, restResponse.getResponse().getData().get(1).getClass());
	}

	@Test
	@PrepareForTest(Event.class)
	public void testDeleteEvents() {
		Long id = (long) 1;
		PowerMockito.mockStatic(Event.class);
		Event mockedEvent = Mockito.mock(Event.class);
		Calendar mockedCalendar = Mockito.mock(Calendar.class);
		Collection<Calendar> calendars = new HashSet<Calendar>();
		calendars.add(mockedCalendar);
		Mockito.doReturn(calendars).when(mockedEvent).getCalendars();
		Collection<Event> mockedEvents = Mockito.mock(Collection.class);
		Mockito.doReturn(mockedEvents).when(mockedCalendar).getEvents();
		PowerMockito.when(Event.findEvent(id)).thenReturn(mockedEvent);

		DSRestResponse result = aut.deleteEvent((long) 1, id);

		PowerMockito.verifyStatic();
		Event.findEvent(id);
		Mockito.verify(mockedEvents).remove(mockedEvent);
		Mockito.verify(mockedEvent).remove();
		assertSame(mockedEvent, result.getResponse().getData().get(0));
	}

}
