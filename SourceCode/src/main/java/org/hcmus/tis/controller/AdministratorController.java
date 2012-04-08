package org.hcmus.tis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/administrator")
@Controller
public class AdministratorController {
	@RequestMapping(value = "/homepage",produces = "text/html")
	public String goToAdministrator()
	{
		return "administrator/adminstrator";
	}

}