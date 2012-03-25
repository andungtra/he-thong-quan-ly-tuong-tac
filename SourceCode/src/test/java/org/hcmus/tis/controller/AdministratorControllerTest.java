package org.hcmus.tis.controller;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class AdministratorControllerTest {

	@Test
	public void testGoToAdministrator() {
		AdministratorController aut = new AdministratorController();
		Assert.assertEquals("getToAdministrator for AdminstratorController return wrong value", "administrator/adminstrator", aut.goToAdministrator());
	}

}
