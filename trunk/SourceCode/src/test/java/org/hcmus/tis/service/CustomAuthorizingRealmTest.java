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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
@RunWith(PowerMockRunner.class)
public class CustomAuthorizingRealmTest {
	private CustomAuthorizingRealm aut;
	@Before
	public void setUp(){
		aut = new CustomAuthorizingRealm();
	}
	@Test
	@PrepareForTest({Account.class, SimpleAuthenticationInfo.class, CustomAuthorizingRealm.class})
	public void testDoGetAuthenticationInfo() throws Exception {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setUsername("userName");
		PowerMockito.mockStatic(Account.class);
		Account mockedAccount = Mockito.mock(Account.class);
		Mockito.doReturn("email").when(mockedAccount).getEmail();
		Mockito.doReturn("password").when(mockedAccount).getPassword();
		TypedQuery<Account> mockedTypedQuery = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(mockedAccount).when(mockedTypedQuery).getSingleResult();
		PowerMockito.when(Account.findAccountsByEmailEquals(token.getUsername())).thenReturn(mockedTypedQuery);
		SimpleAuthenticationInfo expectedReturnInfo = Mockito.mock(SimpleAuthenticationInfo.class);
		PowerMockito.mock(SimpleAuthenticationInfo.class);
		PowerMockito.whenNew(SimpleAuthenticationInfo.class).withArguments(mockedAccount.getEmail(), mockedAccount.getPassword(), aut.getName()).thenReturn(expectedReturnInfo);
		
		AuthenticationInfo info =  aut.doGetAuthenticationInfo(token);
		
		Assert.assertNotNull(info);
		Assert.assertSame(expectedReturnInfo, info);
		
	}
	@Test
	@PrepareForTest({Account.class, SecurityUtils.class})
	public void testDoGetAuthorizationInfoWithAdministratorRole(){
		String email = "email";
		Account mockedAccount = Mockito.mock(Account.class);
		Mockito.doReturn(true).when(mockedAccount).getIsAdmin();
		TypedQuery<Account> mockedQuery = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(mockedAccount).when(mockedQuery).getSingleResult();
		PowerMockito.mockStatic(Account.class);
		PowerMockito.when(Account.findAccountsByEmailEquals(email)).thenReturn(mockedQuery);
		
		PowerMockito.mockStatic(SecurityUtils.class);

		SimplePrincipalCollection simplePrincipal = new SimplePrincipalCollection(email, aut.getName());
		AuthorizationInfo result =  aut.doGetAuthorizationInfo(simplePrincipal);
		assertNotNull(result);
		assertEquals(true, result.getRoles().contains("administrator"));
	}
	@Test
	@PrepareForTest({Account.class, SecurityUtils.class, CustomAuthorizingRealm.class, Project.class, MemberInformation.class})
	public void testDoGetAuthorizationInfoWithProjectManager(){
//		Account account = new Account();
//		Project project = new Project();
//		MemberInformation member = new MemberInformation();
//		MemberRole memberRole = new MemberRole();
//		member.setMemberRole(memberRole);
//		member.setAccount(account);
//		member.setProject(project);
//		String email = "email"; 
//		TypedQuery<Account> mockedQuery = Mockito.mock(TypedQuery.class);
//		Mockito.doReturn(account).when(mockedQuery).getSingleResult();
//		
//		PowerMockito.mockStatic(Account.class);
//		PowerMockito.when(Account.findAccountsByEmailEquals(email)).thenReturn(mockedQuery);
//		
//		Session session = Mockito.mock(Session.class);
//		Long projectId = (long)1;
//		Mockito.doReturn(projectId).when(session).getAttribute("projectId");
//		Subject subject = Mockito.mock(Subject.class);
//		Mockito.doReturn(session).when(subject).getSession();
//		PowerMockito.mockStatic(SecurityUtils.class);
//		PowerMockito.when(SecurityUtils.getSubject()).thenReturn(subject);
//		
//		PowerMockito.mockStatic(Project.class);
//		TypedQuery<MemberInformation> memberQuery = Mockito.mock(TypedQuery.class);
//		PowerMockito.mockStatic(MemberInformation.class);
//		Mockito.doReturn(member).when(memberQuery).getSingleResult();
//		PowerMockito.when(MemberInformation.findMemberInformationsByAccountAndProject(Mockito.any(Account.class), Mockito.any(Project.class))).thenReturn(memberQuery);
//		SimplePrincipalCollection simplePrincipal = new SimplePrincipalCollection(email, aut.getName());
//		AuthorizationInfo result =  aut.doGetAuthorizationInfo(simplePrincipal);
//		assertNotNull(result);
//		PowerMockito.verifyStatic(Mockito.atLeastOnce());
//		SecurityUtils.getSubject();
//		Mockito.verify(subject, Mockito.atLeastOnce()).getSession();
//		Mockito.verify(session, Mockito.atLeastOnce()).getAttribute("projectId");
//		assertTrue(result.getRoles().contains("projectmanager"));
	}

}
