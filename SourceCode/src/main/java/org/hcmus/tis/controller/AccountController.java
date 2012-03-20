package org.hcmus.tis.controller;

import org.hcmus.tis.model.Account;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/accounts")
@Controller
@RooWebScaffold(path = "accounts", formBackingObject = Account.class)
public class AccountController {
	@RequestMapping(value = "/test",produces = "text/html")
    public String test() {     
        return "accounts/test";
    }
}
