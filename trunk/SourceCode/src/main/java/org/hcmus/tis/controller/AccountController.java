package org.hcmus.tis.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hcmus.tis.dto.AccountDTO;
import org.hcmus.tis.dto.DSResponse;
import org.hcmus.tis.dto.DSRestResponse;
import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.NonEditableEvent;
import org.hcmus.tis.dto.ProjectDTO;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.model.Calendar;
import org.hcmus.tis.model.Event;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItem;
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
import org.springframework.web.bind.annotation.RequestParam;
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
			String encodePassword = encoder.encodePassword(
					account.getPassword(), null);
			oldAccount.setPassword(encodePassword);
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
		uiModel.addAttribute("account", accountService.findAccount(id));
		return "accounts/show";
	}

	@RequestMapping(value = "/home", produces = "text/html")
	public String homepage() {
		return "accounts/redirect";
	}

	@RequestMapping(value = "/email/{email}", produces = "text/html")
	public String homepage(@PathVariable("email") String email, Model uiModel) {

		if (!email.endsWith(".com"))
			email = email + ".com";
		Account acc = Account.getAccountbyEmail(email);
		if (acc != null)
			return "redirect:/accounts/" + acc.getId() + "/home";
		else
			return "../login";
	}

	@RequestMapping(value = "/{id}/home", produces = "text/html")
	public String home() {
		return "accounts/home";
	}

	@RequestMapping(value = "/{id}/projects", produces = "text/html")
	public String showProjects() {
		return "accounts/projects";
	}

	@RequestMapping(value = "/{id}/dashboard", produces = "text/html")
	public String showDashBoard(@PathVariable("id") Long id, Model uiModel) {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		long now = cal.get(java.util.Calendar.DAY_OF_YEAR);
		ArrayList<WorkItem> overdues = new ArrayList<WorkItem>();
		ArrayList<WorkItem> indues = new ArrayList<WorkItem>();
		List<WorkItem> workItemsList = WorkItem.findAllWorkItems();
		if (workItemsList.size() > 0) {
			for (WorkItem workItem : workItemsList) {
				if (workItem.getAsignee() != null
						&& workItem.getAsignee().getAccount().getId()
								.equals(id)) {
					if (workItem.getDueDate() != null) {
						java.util.Calendar dueTime = java.util.Calendar
								.getInstance();
						dueTime.setTime(workItem.getDueDate());
						long due = dueTime.get(java.util.Calendar.DAY_OF_YEAR);

						if (due < now) {
							if (overdues.size() < 10)
								overdues.add(workItem);
						} else if (due - now < 7)
							indues.add(workItem);
					}
				}
			}
		}
		uiModel.addAttribute("overdues", overdues);
		uiModel.addAttribute("indues", indues);

		Collection<Project> listProject = Project.findProjectsByAccount(id);
		ArrayList<Event> newEvent = new ArrayList<Event>();
		Date n = new Date();
		for (Project project : listProject) {
			Calendar c = project.getCalendar();
			Collection<Event> e = c.getEvents();
			for (Event event : e) {
				if (event.getStartDate() != null
						&& event.getStartDate().getTime() > n.getTime()
						&& (event.getStartDate().getTime() - n.getTime()) < 7) {
					newEvent.add(event);
				}
			}
		}
		uiModel.addAttribute("newEvent", newEvent);
		return "accounts/dashboard";
	}

	@RequestMapping(value = "mListProject", params = { "iDisplayStart",
			"iDisplayLength", "sEcho", "sSearch" })
	@ResponseBody
	public DtReply mListProject(int iDisplayStart, int iDisplayLength,
			String sEcho, String sSearch, HttpSession session) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		Account acc = (Account) session.getAttribute("account");
		List<MemberInformation> list = MemberInformation
				.findMemberInformationEntriesBaseProject(iDisplayStart,
						iDisplayLength, sSearch);
		for (MemberInformation item : list) {
			if (item.getAccount().getEmail().equals(acc.getEmail())
					&& item.getDeleted() == false) {
				ProjectDTO dto = new ProjectDTO();
				dto.DT_RowId = item.getProject().getId();
				dto.setName("<a href='../../../TIS/projects/"
						+ item.getProject().getId() + "?goto=true'>"
						+ item.getProject().getName() + "</a>");
				dto.setDescription(item.getProject().getDescription());
				reply.getAaData().add(dto);
			}

		}
		reply.setiTotalRecords(reply.getAaData().size());
		return reply;
	}

	@RequestMapping(value = "/redirect", produces = "text/html")
	public String redirect() {
		return "accounts/redirect";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		// uiModel.addAttribute("account", accountService.findAccount(id));
		// uiModel.addAttribute("itemId", id);
		// return "accounts/show";
		return "accounts/redirect";
	}

	@RequestMapping(value = "/{id}", params = "userform", produces = "text/html")
	public String userupdateForm(@PathVariable("id") Long id, Model uiModel) {
		populateEditForm(uiModel, accountService.findAccount(id));
		return "accounts/user-update";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Account account, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, account);
			return "accounts/update";
		}
		uiModel.asMap().clear();
		accountService.updateAccount(account);
		return "redirect:/accounts/home";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.PUT, produces = "text/html")
	public String userupdate(@Valid Account account, String firstName,
			String lastName, String email, Model uiModel,
			HttpServletRequest httpServletRequest) {
		Account exist = Account.getAccountbyEmail(email);
		if (account.getEmail() != email && exist != null) {
			uiModel.addAttribute("error", "Email is exist");
			return "accounts/user-update";
		}
		account.setFirstName(firstName);
		account.setLastName(lastName);
		account.setEmail(email);
		accountService.updateAccount(account);
		return "redirect:/accounts/home";
	}

	@RequestMapping(value = "mList", params = { "iDisplayStart",
			"iDisplayLength", "sEcho", "sSearch" })
	@ResponseBody
	public DtReply mList(int iDisplayStart, int iDisplayLength, String sEcho,
			String sSearch) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		List<Account> list = Account.findAccountEntries(iDisplayStart,
				iDisplayLength, sSearch);
		for (Account item : list) {
			AccountDTO dto = new AccountDTO();
			dto.DT_RowId = item.getId();
			dto.setFirstName("<a href='../accounts/ID/" + item.getId() + "'>"
					+ item.getFirstName() + "</a>");
			dto.setLastName(item.getLastName());
			dto.setEmail(item.getEmail());
			dto.setStatus(item.getStatus().name());
			reply.getAaData().add(dto);
		}
		reply.setiTotalRecords(reply.getAaData().size());
		return reply;
	}

	@RequestMapping(value = "/{id}/dumpcalendar")
	public String showDumpCalendar(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("id", id);
		return "accounts/dumpcalendar";
	}

	@RequestMapping(value = "/{id}/calendar")
	public String showCalendar(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("id", id);
		return "accounts/calendar";
	}

	@RequestMapping(value = "/{id}/calendar/events")
	@ResponseBody
	public DSRestResponse getEvents(@PathVariable("id") Long id) {
		DSRestResponse restResponse = new DSRestResponse();
		restResponse.setResponse(new DSResponse());
		Account account = Account.findAccount(id);
		List<Object> datas = new ArrayList<Object>();
		for (Event event : account.getCalendar().getEvents()) {
			if (event.getCalendars().size() > 1) {
				NonEditableEvent nonEditableEvent = new NonEditableEvent();
				nonEditableEvent.setName(event.getName());
				String des = "";
				for (Calendar calendar : event.getCalendars()) {
					if (calendar.getProject() != null) {
						des = "From : " + calendar.getProject().getName()
								+ " <br/>";
						break;
					}
				}

				des = des + event.getDescription();
				nonEditableEvent.setDescription(des);
				nonEditableEvent.setId(event.getId());
				nonEditableEvent.setVersion(event.getVersion());
				nonEditableEvent.setStartDate(event.getStartDate());
				nonEditableEvent.setEndDate(event.getEndDate());
				datas.add(nonEditableEvent);
			} else {
				datas.add(event);
			}
		}
		restResponse.getResponse().setStatus(0);
		restResponse.getResponse().setData(datas);
		return restResponse;
	}

	@RequestMapping(value = "/{id}/calendar/events", params = { "_operationType=add" })
	@ResponseBody
	public DSRestResponse creatEvent(@PathVariable("id") Long accountId,
			@Valid Event event, BindingResult bindingResult) {
		DSRestResponse restResponse = new DSRestResponse();
		restResponse.setResponse(new DSResponse());
		if (bindingResult.hasErrors()) {
			restResponse.getResponse().setStatus(1);
		}
		event.setCalendars(new ArrayList<Calendar>());
		Calendar calender = Account.findAccount(accountId).getCalendar();
		calender.getEvents().add(event);
		event.persist();
		List<Object> data = new ArrayList<Object>();
		data.add(event);
		restResponse.getResponse().setData(data);
		return restResponse;
	}

	@RequestMapping(value = "/{id}/calendar/events", params = { "_operationType=update" })
	@ResponseBody
	public DSRestResponse updateEvent(@PathVariable("id") Long accountId,
			Long id, @Valid Event event, BindingResult bindingResult) {
		DSRestResponse restResponse = new DSRestResponse();
		restResponse.setResponse(new DSResponse());
		event.setId(id);
		if (bindingResult.hasErrors()) {
			restResponse.getResponse().setStatus(1);
		}
		Event managedEvent = event.merge();
		List<Object> data = new ArrayList<Object>();
		data.add(managedEvent);
		restResponse.getResponse().setData(data);
		return restResponse;
	}

	@RequestMapping(value = "/{id}/calendar/events", params = { "_operationType=remove" })
	@ResponseBody
	public DSRestResponse deleteEvent(@PathVariable("id") Long accountId,
			Long id) {
		DSRestResponse restResponse = new DSRestResponse();
		restResponse.setResponse(new DSResponse());
		Event event = Event.findEvent(id);
		for (Calendar calendar : event.getCalendars()) {
			calendar.getEvents().remove(event);
		}
		event.remove();
		List<Object> data = new ArrayList<Object>();
		data.add(event);
		restResponse.getResponse().setData(data);
		return restResponse;
	}

	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		List<Account> lst = null;
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			lst = accountService.findAccountEntries(firstResult, sizeNo);
			float nrOfPages = (float) accountService.countAllAccounts()
					/ sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			lst = accountService.findAllAccounts();
		}

		for (int i = 0; i < lst.size(); i++) {
			if (lst.get(i).getStatus().equals(AccountStatus.DELETED))
				lst.remove(i);
		}

		uiModel.addAttribute("accounts", lst);
		return "accounts/list";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		Account account = accountService.findAccount(id);
		account.setEmail((new Date()).toString());
		account.setStatus(AccountStatus.DELETED);
		account.merge();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/accounts";
	}
}
