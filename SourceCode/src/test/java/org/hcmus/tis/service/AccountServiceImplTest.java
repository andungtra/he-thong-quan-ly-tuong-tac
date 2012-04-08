package org.hcmus.tis.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.ServletContext;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.service.EmailService.SendMailException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
@RunWith(PowerMockRunner.class)

@PrepareForTest(Account.class)
@MockStaticEntityMethods
public class AccountServiceImplTest {
	private EmailServiceImpl mockEmailService;
	private Account mockedAccount;
	AccountServiceImpl aut;
	private ServletContext servletContext;
	@Before
	public void setUp()
	{
		mockedAccount = Mockito.spy(new Account());
		mockedAccount.setId((long)1);
		mockedAccount.setFirstName("firstName");
		mockedAccount.setLastName("lastName");
		mockedAccount.setEmail("email@email.com");
		
		mockEmailService = Mockito.mock(EmailServiceImpl.class);
		aut = new AccountServiceImpl();
		aut.setEmailService(mockEmailService);
		servletContext = Mockito.mock(ServletContext.class);
		Mockito.doReturn("/tis").when(servletContext).getContextPath();
		aut.setServletContext(servletContext);
		
	}
	@Test
	public void testSaveAccount() throws DuplicateException, SendMailException {
		Mockito.doNothing().when(mockedAccount).persist();
		

		TypedQuery<Account> mockResult = Mockito.mock(TypedQuery.class);
		Mockito.when(mockResult.getResultList()).thenReturn(new ArrayList<Account>());
		String email = mockedAccount.getEmail();
		PowerMockito.mockStatic(Account.class);
		PowerMockito.when(Account.findAccountsByEmailEquals(Mockito.anyString())).thenReturn(mockResult);
		
		aut.createAccount(mockedAccount);
		
		Mockito.verify(mockedAccount).setPassword(Mockito.anyString());
		Mockito.verify(mockedAccount).setStatus(AccountStatus.INACTIVE);
		Mockito.verify(mockedAccount).persist();
		Mockito.verify(mockEmailService).sendEmail(Mockito.eq(mockedAccount.getEmail()), Mockito.eq("account-confirmation.vm"), Mockito.anyMap(), Mockito.anyString());
	}
	@Test(expected=DuplicateException.class)
	public void testSaveDuplicateAccount() throws DuplicateException, SendMailException {
		List<Account> accountList = new ArrayList<Account>();
		accountList.add(new Account());
		TypedQuery<Account> mockTypedQuery = Mockito.mock(TypedQuery.class);
		Mockito.when(mockTypedQuery.getResultList()).thenReturn(accountList);
		
		PowerMockito.mockStatic(Account.class);
		PowerMockito.when(Account.findAccountsByEmailEquals(Mockito.anyString())).thenReturn(mockTypedQuery);
		 
		aut.createAccount(mockedAccount);
		 
		Mockito.verify(mockedAccount).setPassword(Mockito.anyString());
		Mockito.verify(mockedAccount).setStatus(AccountStatus.INACTIVE);
	}

}
