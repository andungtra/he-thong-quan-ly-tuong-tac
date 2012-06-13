package org.hcmus.tis.service;

import static org.junit.Assert.*;

import java.lang.reflect.Member;

import javax.persistence.TypedQuery;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.repository.AccountRepository;
import org.hcmus.tis.repository.SpecificAccountRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
@RunWith(PowerMockRunner.class)
public class CustomAuthorizingRealmTest {
	private CustomAuthorizingRealm aut;
	@Mock
	SpecificAccountRepository accountRepository;
	@Mock
	Account account;
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		doReturn("email").when(account).getEmail();
		doReturn("password").when(account).getPassword();
		aut = new CustomAuthorizingRealm();
		aut.setAccountRepository(accountRepository);
	}
	@Test
	@PrepareForTest({SimpleAuthenticationInfo.class, CustomAuthorizingRealm.class})
	public void testDoGetAuthenticationInfo() throws Exception {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setUsername("userName");
		doReturn(account).when(accountRepository).getByEmail(token.getUsername());
		SimpleAuthenticationInfo expectedReturnInfo = mock(SimpleAuthenticationInfo.class);
		PowerMockito.mock(SimpleAuthenticationInfo.class);
		PowerMockito.whenNew(SimpleAuthenticationInfo.class).withArguments(account.getEmail(), account.getPassword(), aut.getName()).thenReturn(expectedReturnInfo);
		
		AuthenticationInfo info =  aut.doGetAuthenticationInfo(token);
		
		Assert.assertNotNull(info);
		Assert.assertSame(expectedReturnInfo, info);
		
	}
	@Test
	@PrepareForTest({SimpleAuthenticationInfo.class, CustomAuthorizingRealm.class})
	public void testDoGetAuthenticationInfoWithWrongUserName() throws Exception {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setUsername("userName");
		doReturn(account).when(accountRepository).getByEmail(token.getUsername());
		SimpleAuthenticationInfo expectedReturnInfo = mock(SimpleAuthenticationInfo.class);
		PowerMockito.mock(SimpleAuthenticationInfo.class);
		PowerMockito.whenNew(SimpleAuthenticationInfo.class).withArguments(account.getEmail(), account.getPassword(), aut.getName()).thenReturn(expectedReturnInfo);
		
		AuthenticationInfo info =  aut.doGetAuthenticationInfo(token);
		
		Assert.assertNotNull(info);
		Assert.assertSame(expectedReturnInfo, info);
		
	}
	@Test
	@PrepareForTest({SecurityUtils.class})
	public void testDoGetAuthorizationInfoWithAdministratorRole(){
		String email = "email";
		doReturn(true).when(account).getIsAdmin();
		doReturn(account).when(accountRepository).getByEmail(email);
		
		PowerMockito.mockStatic(SecurityUtils.class);

		SimplePrincipalCollection simplePrincipal = new SimplePrincipalCollection(email, aut.getName());
		AuthorizationInfo result =  aut.doGetAuthorizationInfo(simplePrincipal);
		assertNotNull(result);
		assertEquals(true, result.getRoles().contains("administrator"));
	}

}
