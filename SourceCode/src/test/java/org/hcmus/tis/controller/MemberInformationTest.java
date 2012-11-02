package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import javax.jws.WebParam.Mode;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.repository.MemberRoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.staticmock.MockStaticEntityMethods;
import org.springframework.ui.Model;
@RunWith(PowerMockRunner.class)
@MockStaticEntityMethods
public class MemberInformationTest {
	private Model mockedUiModel;
	private MemberInformationController aut;
	@Mock
	private MemberRoleRepository memberRoleRepository;
	@Before
	public void setUp() throws Exception {
		mockedUiModel = Mockito.mock(Model.class);
		aut = new MemberInformationController();
		aut.setMemberRoleRepository(memberRoleRepository);
	}

	@Test
	@PrepareForTest(MemberRole.class)
	public void testCreateForm() {
		PowerMockito.mockStatic(MemberRole.class);
		String redirectUrl = "redirect";
		String result = aut.createForm(mockedUiModel, (long) 1 );
		 
		assertEquals("memberinformations/createfromproject", result);
	}

}
