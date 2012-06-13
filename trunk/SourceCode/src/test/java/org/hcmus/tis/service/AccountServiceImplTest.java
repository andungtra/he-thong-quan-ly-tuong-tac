package org.hcmus.tis.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.ServletContext;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.repository.AccountRepository;
import org.hcmus.tis.service.EmailService.SendMailException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
public class AccountServiceImplTest {
	@Mock
	private EmailServiceImpl mockEmailService;
	@Mock
	private Account mockedAccount;
	@Mock
	private AccountRepository accountRepository;
	AccountServiceImpl aut;
	@Mock
	private ServletContext servletContext;
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		doReturn((long)1).when(mockedAccount).getId();
		doReturn("email").when(mockedAccount).getEmail();
		doReturn("firstName").when(mockedAccount).getFirstName();
		doReturn("lastName").when(mockedAccount).getLastName();
		aut = new AccountServiceImpl();
		aut.setAccountRepository(accountRepository);
		aut.setEmailService(mockEmailService);
		doReturn("/tis").when(servletContext).getContextPath();
		aut.setServletContext(servletContext);
		
	}
	@Test
	public void testCreateAccount() throws DuplicateException, SendMailException {
		String email = mockedAccount.getEmail();
		doReturn((long)0).when(accountRepository).countByEmail(email);
		
		aut.createAccount(mockedAccount);
		
		verify(mockedAccount).setPassword(anyString());
		verify(mockedAccount).setStatus(AccountStatus.INACTIVE);
		verify(accountRepository).save(mockedAccount);
		verify(mockEmailService).sendEmail(eq(mockedAccount.getEmail()), eq("account-confirmation.vm"), anyMap(), anyString());
	}
	@Test(expected=DuplicateException.class)
	public void testCreateDuplicateAccount() throws DuplicateException, SendMailException {
		String email = mockedAccount.getEmail();
		doReturn((long)1).when(accountRepository).countByEmail(email);

		
		aut.createAccount(mockedAccount);
		
	}

}
