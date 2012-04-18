package org.hcmus.tis.controller;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.service.AccountService;
import org.hcmus.tis.service.DuplicateException;
import org.hcmus.tis.service.EmailService.SendMailException;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/accounts")
@Controller
@RooWebScaffold(path = "accounts", formBackingObject = Account.class)
public class AccountController {
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
		return "redirect:/accounts/ID/"
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
		if (account.getStatus() == AccountStatus.INACTIVE
				&& account.getPassword().compareTo(activeKey) == 0) {
			populateEditForm(uiModel, account);
			return "accounts/active";
		}
		return "accounts/invalidactive";
	}

	@RequestMapping(value = "/{id}", params = "activeKey", method = RequestMethod.PUT, produces = "text/html")
	public String active(@Valid Account account, String activeKey, Model uiModel) {
		Account oldAccount = accountService.findAccount(account.getId());
		if (oldAccount.getPassword().compareTo(activeKey) == 0
				&& oldAccount.getStatus() == AccountStatus.INACTIVE) {
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			String encodePassword = encoder.encodePassword(account.getPassword(), null);
			oldAccount.setPassword(encodePassword) ;
			oldAccount.setFirstName(account.getFirstName());
			oldAccount.setLastName(account.getLastName());
			oldAccount.setStatus(AccountStatus.ACTIVE);
			oldAccount.setIsEnable(true);
			accountService.updateAccount(oldAccount);
			uiModel.addAttribute("account", oldAccount);
			uiModel.addAttribute("itemId", oldAccount.getId());
			return "accounts/home";
		}
		return "accounts/activeFailure";
	}

	@RequestMapping(params = "term")
	public @ResponseBody
	Collection<String> findAccount(String term) {
		Collection<Account> accounts = Account.findAccount(term, 0, 50);
		Collection<String> result = new ArrayList<String>();
		for (Account account : accounts) {
			result.add(account.getEmail());
		}
		return result;
	}

	@RequestMapping(value = "/ID/{id}", produces = "text/html")
	public String showAccount(@PathVariable("id") Long id, Model uiModel) {
		//uiModel.addAttribute("account", accountService.findAccount(id));
		//uiModel.addAttribute("itemId", id);
		//return "accounts/show";
		return "accounts/redirect";
	}

	@RequestMapping(value = "/home", produces = "text/html")
	public String homepage(HttpServletRequest request, Model uiModel) {
		return "accounts/redirect";
	}

	@RequestMapping(value = "/email/{email}", produces = "text/html")
	public String homepage(@PathVariable("email") String email,Model uiModel) {
		
		if(!email.endsWith(".com"))
			email = email+".com";
		Account acc = Account.getAccountbyEmail(email);
		if(acc!=null)
			return "redirect:/accounts/"+ acc.getId()+"/home";
		else
			return "../login";
	}
	
	@RequestMapping(value = "/{id}/home", produces = "text/html")
	public String home(@PathVariable("id") Long id, Model uiModel, HttpSession session) {
		uiModel.addAttribute("account", accountService.findAccount(id));
		uiModel.addAttribute("itemId", id);
		session.setAttribute("account", accountService.findAccount(id));
		return "accounts/home";
		//return "accounts/redirect";
	}

	@RequestMapping(value = "/{id}/projects", produces = "text/html")
	public String showProjects(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("account", accountService.findAccount(id));
		uiModel.addAttribute("itemId", id);
		uiModel.addAttribute("projects", Project.findProjectsByAccount(id));
		return "accounts/projects";
	}

	@RequestMapping(value = "/redirect", produces = "text/html")
	public String redirect() {
		return "accounts/redirect";
	}
	
	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        //uiModel.addAttribute("account", accountService.findAccount(id));
        //uiModel.addAttribute("itemId", id);
        //return "accounts/show";
		return "accounts/redirect";
    }
	
	@RequestMapping(value = "/{id}", params = "userform", produces = "text/html")
    public String userupdateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, accountService.findAccount(id));
        return "accounts/user-update";
    }
}
