package org.hcmus.tis.controller;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.map.HashedMap;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.service.AccountService;
import org.hcmus.tis.service.DuplicateException;
import org.hcmus.tis.service.EmailService;
import org.hcmus.tis.service.EmailService.SendMailException;
import org.junit.runner.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/accounts")
@Controller
@RooWebScaffold(path = "accounts", formBackingObject = Account.class)
public class AccountController {
	@RequestMapping(value = "/test", produces = "text/html")
	public String test() {
		return "accounts/test";
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Account account, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, account);
			return "accounts/create";
		}
		uiModel.asMap().clear();
		try {
			getAccountService().createAccount(account);
		} catch (DuplicateException e) {
			populateEditForm(uiModel, account);
			uiModel.addAttribute("existedEmail", true);
			return "accounts/create";
		} catch (SendMailException e) {
			populateEditForm(uiModel, account);
			uiModel.addAttribute("sendEmailError", true);
			return "accounts/create";
		}
		return "redirect:/accounts/"
				+ encodeUrlPathSegment(account.getId().toString(),
						httpServletRequest);
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@RequestMapping(value = "/{id}", params = "activeKey", method = RequestMethod.GET, produces = "text/html")
	public String activeForm(@PathVariable("id") Long id, String activeKey,
			Model uiModel) {
		Account account = accountService.findAccount(id);
		if (account.getStatus() == AccountStatus.INACTIVE && account.getPassword().compareTo(activeKey) == 0) {
			populateEditForm(uiModel, account);
			return "accounts/active";
		}
		return "accounts/invalidactive";
	}

	@RequestMapping(value = "/{id}", params = "activeKey", method = RequestMethod.PUT, produces = "text/html")
	public String active(@Valid Account account, String activeKey, Model uiModel) {
		Account oldAccount = accountService.findAccount(account.getId());
		if (oldAccount.getPassword().compareTo(activeKey) == 0 && oldAccount.getStatus() == AccountStatus.INACTIVE){
			oldAccount.setPassword(account.getPassword());
			oldAccount.setFirstName(account.getFirstName());
			oldAccount.setLastName(account.getLastName());
			oldAccount.setStatus(AccountStatus.ACTIVE);
			oldAccount.setIsEnable(true);
			accountService.updateAccount(oldAccount);
			return "accounts/home";
		}
		return "accounts/activeFailure";
	}
}
