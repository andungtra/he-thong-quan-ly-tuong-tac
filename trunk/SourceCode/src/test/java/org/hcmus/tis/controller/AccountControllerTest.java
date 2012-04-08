package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
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
	public void testCreateDuplicateEmail() throws DuplicateException, SendMailException {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

		Mockito.doThrow(new DuplicateException()).when(mockedAccountService).createAccount(spyAccount);
		String result = aut.create(spyAccount, bindingResult, uiModel, request);

		Mockito.verify(mockedAccountService).createAccount(spyAccount);
		Mockito.verify(uiModel).addAttribute("existedEmail", true);
		Assert.assertTrue(result.contains("accounts/create"));
	}
	
	@Test
	public void testCreateSendEmailError() throws DuplicateException, SendMailException {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

		Mockito.doThrow(new SendMailException()).when(mockedAccountService).createAccount(spyAccount);
		String result = aut.create(spyAccount, bindingResult, uiModel, request);

		Mockito.verify(mockedAccountService).createAccount(spyAccount);
		Mockito.verify(uiModel).addAttribute("sendEmailError", true);
		Assert.assertTrue(result.contains("accounts/create"));
	}
	@Test
	public void testActiveAccountFormToInactiveAccount(){
		spyAccount.setStatus(AccountStatus.INACTIVE);
		String activeKey = UUID.randomUUID().toString();
		spyAccount.setPassword(activeKey);
		long id = spyAccount.getId();
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(id);
		
		String result = aut.activeForm(spyAccount.getId(), activeKey, uiModel);
		
		Assert.assertEquals("accounts/active", result);
	}
	@Test
	public void testActiveAccountFormToActivedAccount(){
		spyAccount.setStatus(AccountStatus.ACTIVE);
		String activeKey = UUID.randomUUID().toString();
		spyAccount.setPassword(activeKey);
		long id = spyAccount.getId();
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(id);
		
		String result = aut.activeForm(spyAccount.getId(), activeKey, uiModel);
		
		Assert.assertEquals("accounts/invalidactive", result);
	}
	@Test
	public void testActiveAccountFormWithInvalidActiveKey(){
		spyAccount.setStatus(AccountStatus.INACTIVE);
		String activeKey = UUID.randomUUID().toString();
		spyAccount.setPassword("password");
		long id = spyAccount.getId();
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(id);
		
		String result = aut.activeForm(spyAccount.getId(), activeKey, uiModel);
		
		Assert.assertEquals("accounts/invalidactive", result);
	}
	@Test
	public void testActiveAccountValidActiveKey(){
		spyAccount.setStatus(AccountStatus.INACTIVE);
		String activeKey = UUID.randomUUID().toString();
		spyAccount.setPassword(activeKey);
		long id = spyAccount.getId();
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(id);
		
		Account updatedAccount = new Account();
		updatedAccount.setId((long)1);
		updatedAccount.setPassword("password");
		updatedAccount.setFirstName("firstName");
		updatedAccount.setLastName("lastName");
		
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(updatedAccount.getId());
		

		String result = aut.active(updatedAccount, activeKey, uiModel);
		
		Mockito.verify(mockedAccountService).updateAccount(Mockito.any(Account.class));
		Assert.assertEquals("accounts/home", result);		
	}
	@Test
	public void testActiveAccountInvalidActiveKey(){
		spyAccount.setStatus(AccountStatus.INACTIVE);
		String activeKey = UUID.randomUUID().toString();
		spyAccount.setPassword("password");
		long id = spyAccount.getId();
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(id);
		
		Account updatedAccount = new Account();
		updatedAccount.setId((long)1);
		updatedAccount.setPassword("password");
		updatedAccount.setFirstName("firstName");
		updatedAccount.setLastName("lastName");
		
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(updatedAccount.getId());
		
		
		String result = aut.active(updatedAccount, activeKey, uiModel);
		Mockito.verify(mockedAccountService, Mockito.times(0)).updateAccount(Mockito.any(Account.class));
		Assert.assertEquals("accounts/activeFailure", result);		
	}
	@Test
	public void testActiveForActivedAccount(){
		spyAccount.setStatus(AccountStatus.ACTIVE);
		String activeKey = UUID.randomUUID().toString();
		spyAccount.setPassword(activeKey);
		long id = spyAccount.getId();
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(id);
		
		Account updatedAccount = new Account();
		updatedAccount.setId((long)1);
		updatedAccount.setPassword("password");
		updatedAccount.setFirstName("firstName");
		updatedAccount.setLastName("lastName");
		
		Mockito.doReturn(spyAccount).when(mockedAccountService).findAccount(updatedAccount.getId());
		
		
		String result = aut.active(updatedAccount, activeKey, uiModel);
		Mockito.verify(mockedAccountService, Mockito.times(0)).updateAccount(Mockito.any(Account.class));
		Assert.assertEquals("accounts/activeFailure", result);		
	}

}
