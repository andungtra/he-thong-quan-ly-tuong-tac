package org.hcmus.tis.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.WorkItemDTO;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.Attachment;
import org.hcmus.tis.model.Field;
import org.hcmus.tis.model.FieldDefine;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemHistory;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.model.WorkItemType;
import org.hcmus.tis.model.xml.ObjectFactory;
import org.hcmus.tis.model.xml.XAdditionalFieldsImpl;
import org.hcmus.tis.model.xml.XFieldImpl;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/projects/{projectId}/workitems")
@Controller
@RooWebScaffold(path = "workitems", formBackingObject = WorkItem.class)
public class WorkItemController {
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	@RequiresPermissions("workitem:update")
	public String update(@Valid WorkItem workItem, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest)
			throws JAXBException {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, workItem);
			return "workitems/update";
		}
		WorkItem inDatabaseWorkItem = WorkItem.findWorkItem(workItem.getId());
		workItem.setSubcribers(inDatabaseWorkItem.getSubcribers());
		WorkItemType workItemType = WorkItemType.findWorkItemType(workItem
				.getWorkItemType().getId());
		List<Field> fields = new ArrayList<Field>();
		String attachmentIds[] = httpServletRequest
				.getParameterValues("attachment");
		for (FieldDefine fieldDefine : workItemType.getAdditionalFieldDefines()) {
			Field field = new Field();
			field.setName(fieldDefine.getRefName());
			field.setValue(httpServletRequest.getParameter(fieldDefine
					.getRefName()));
			fields.add(field);
		}
		workItem.setAdditionFiels(fields);

		Date date = new Date();
		workItem.setDateLastEdit(date);
		Account acc = (Account) SecurityUtils.getSubject().getSession()
				.getAttribute("account");
		workItem.setUserLastEdit(acc);
		uiModel.asMap().clear();
		workItem.merge();
		return "redirect:/projects/"
				+ workItem.getWorkItemContainer().getParentProjectOrMyself()
						.getId()
				+ "/workitems/"
				+ encodeUrlPathSegment(workItem.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{workItemId}", params = "subscribe")
	public String subscribe(@PathVariable("projectId") Long projectId,
			@PathVariable("workItemId") Long workItemId,
			HttpServletRequest httpServletRequest) {
		Account account = Account.findAccountsByEmailEquals(
				(String) SecurityUtils.getSubject().getPrincipal())
				.getSingleResult();
		Project project = Project.findProject(projectId);
		MemberInformation member = MemberInformation
				.findMemberInformationsByAccountAndProject(account, project)
				.getSingleResult();
		WorkItem workItem = WorkItem.findWorkItem(workItemId);
		if (!workItem.getSubcribers().contains(member)) {
			workItem.getSubcribers().add(member);
			workItem.flush();
		}
		return "redirect:/projects/"
				+ workItem.getWorkItemContainer().getParentProjectOrMyself()
						.getId()
				+ "/workitems/"
				+ encodeUrlPathSegment(workItem.getId().toString(),
						httpServletRequest) + "?form";

	}

	@RequestMapping(params = "form", produces = "text/html")
	@RequiresPermissions("workitem:create")
	public String createForm(@PathVariable("projectId") Long projectId,
			Long workItemTypeId, String redirectUrl, Model uiModel,
			Principal principal) throws NotPermissionException {
		WorkItem workItem = new WorkItem();
		Project project = Project.findProject(projectId);
		workItem.setWorkItemContainer(project);
		WorkItemType workItemType = WorkItemType
				.findWorkItemType(workItemTypeId);
		workItem.setWorkItemType(workItemType);
		populateEditFormCustomly(uiModel, workItem);
		List<String[]> dependencies = new ArrayList<String[]>();
		uiModel.addAttribute("projectId", projectId);
		String name = principal.getName();
		Account loginAccount = Account.findAccountsByEmailEquals(name)
				.getSingleResult();
		MemberInformation memberInformation = MemberInformation
				.findMemberInformationsByAccountAndProject(loginAccount,
						project).getSingleResult();
		if (memberInformation == null) {
			throw new NotPermissionException();
		}
		uiModel.addAttribute("memberInformationId", memberInformation.getId());
		if (Priority.countPrioritys() == 0) {
			dependencies.add(new String[] { "priority", "prioritys" });
		}
		if (WorkItemStatus.countWorkItemStatuses() == 0) {
			dependencies.add(new String[] { "workitemstatus",
					"workitemstatuses" });
		}
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("dependencies", dependencies);

		return "workitems/create";
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	@RequiresPermissions("workitem:update")
	public String updateForm(@PathVariable("projectId") Long projectId,
			@PathVariable("id") Long id, Model uiModel) {
		WorkItem workItem = WorkItem.findWorkItem(id);
		populateEditFormCustomly(uiModel, workItem);
		Project project = Project.findProject(projectId);
		Account account = Account.findAccountsByEmailEquals(
				(String) SecurityUtils.getSubject().getPrincipal())
				.getSingleResult();
		MemberInformation member = MemberInformation
				.findMemberInformationsByAccountAndProject(account, project)
				.getSingleResult();
		boolean subscribed = workItem.getSubcribers().contains(member);
		uiModel.addAttribute("subscribed", subscribed);
		return "workitems/update";
	}

	void populateEditFormCustomly(Model uiModel, WorkItem workItem) {
		uiModel.addAttribute("workItem", workItem);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("memberinformations",
				MemberInformation.findAllMemberInformations());
		uiModel.addAttribute("prioritys", Priority.findAllPrioritys());
		workItem.setWorkItemContainer(WorkItemContainer
				.findWorkItemContainer(workItem.getWorkItemContainer().getId()));
		Project project = workItem.getWorkItemContainer()
				.getParentProjectOrMyself();
		List<Iteration> iterations = Iteration.findIterationsByParentContainer(
				project).getResultList();
		List<WorkItemContainer> containers = new ArrayList<WorkItemContainer>();
		Project dumpProject = new Project();
		dumpProject.setId(project.getId());
		dumpProject.setName("Please choose ...");
		containers.add(dumpProject);
		for (Iteration iteration : iterations) {
			containers.add(iteration);
		}
		uiModel.addAttribute("workitemcontainers", containers);
		uiModel.addAttribute("workitemstatuses",
				WorkItemStatus.findAllWorkItemStatuses());
		uiModel.addAttribute("workItemType", workItem.getWorkItemType());
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	@RequiresPermissions("workitem:create")
	public String create(
			@Valid WorkItem workItem,
			BindingResult bindingResult,
			@PathVariable("projectId") Long projectId,
			Model uiModel,
			@RequestParam(value = "attachment", required = false) Long[] attachmentIds,
			HttpServletRequest httpServletRequest) throws JAXBException {
		if (bindingResult.hasErrors()) {
			populateEditFormCustomly(uiModel, workItem);
			return "workitems/create";
		}
		WorkItemType workItemType = WorkItemType.findWorkItemType(workItem
				.getWorkItemType().getId());
		List<Field> fields = new ArrayList<Field>();
		for (FieldDefine fieldDefine : workItemType.getAdditionalFieldDefines()) {
			Field field = new Field();
			field.setName(fieldDefine.getRefName());
			field.setValue(httpServletRequest.getParameter(fieldDefine
					.getRefName()));
			fields.add(field);
		}
		workItem.setAdditionFiels(fields);

		Date date = new Date();
		workItem.setDateLastEdit(date);
		Account acc = (Account) SecurityUtils.getSubject().getSession()
				.getAttribute("account");
		workItem.setUserLastEdit(acc);
		uiModel.asMap().clear();
		workItem.persist();
		if (attachmentIds != null) {
			for (Long attachmentId : attachmentIds) {
				Attachment attachment = Attachment.findAttachment(attachmentId);
				attachment.setWorkItem(workItem);
				attachment.flush();
			}
		}
		return "redirect:/projects/"
				+ projectId
				+ "/workitems/"
				+ encodeUrlPathSegment(workItem.getId().toString(),
						httpServletRequest);

	}

	@RequestMapping(params = { "projectId", "iDisplayStart", "iDisplayLength",
			"sEcho", "sSearch", "sSearch_0", "sSearch_1", "sSearch_2",
			"sSearch_3" })
	@ResponseBody
	@RequiresPermissions("workitem:list")
	public DtReply listWorkItemByProject(
			@PathVariable("projectId") Long projectId, int iDisplayStart,
			int iDisplayLength, String sEcho, int iSortCol_0,
			String sSortDir_0, String sSearch, String sSearch_0,
			String sSearch_1, String sSearch_2, String sSearch_3) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		Project project = Project.findProject(projectId);
		reply.setiTotalRecords((int) WorkItem.countWorkItemByProject(project));
		reply.setiTotalDisplayRecords((int) WorkItem
				.countWorkItemByProject(project));
		List<WorkItem> workItems = WorkItem.findWorkItems(project,
				iDisplayStart, iDisplayLength, sSearch, sSearch_0, sSearch_1,
				sSearch_2, sSearch_3);

		for (WorkItem workItem : workItems) {
			WorkItemDTO workItemDto = new WorkItemDTO();
			workItemDto.DT_RowId = workItem.getId();
			workItemDto.setlName("<a href='/TIS/projects/" + projectId
					+ "/workitems/" + workItem.getId() + "?form'>"
					+ workItem.getTitle() + "</a>");
			workItemDto.setsStatus(workItem.getStatus().getName());
			workItemDto.setsType(workItem.getWorkItemType().getName());
			workItemDto.setPriority(workItem.getPriority().getName());
			reply.getAaData().add(workItemDto);
		}
		reply.setiTotalRecords(reply.getAaData().size());
		return reply;
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute(
				"workItem_datecreated_date_format",
				DateTimeFormat.patternForStyle("M-",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"workItem_datelastedit_date_format",
				DateTimeFormat.patternForStyle("M-",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"workItem_duedate_date_format",
				DateTimeFormat.patternForStyle("SS",
						LocaleContextHolder.getLocale()));
	}

	@RequiresPermissions("workitem:read")
	@RequestMapping(value = "/${id}/history", produces = "text/html")
	public String history(Model uiModel, Long id) {
		List<WorkItemHistory> history = WorkItemHistory
				.findAllWorkItemHistorysOfWorkItem(id, 10);
		uiModel.addAttribute("history", history);
		uiModel.addAttribute("workItemId", id);
		return "workitems/history";
	}

	@RequestMapping(value = "/{workItemId}", params = "unSubscribe")
	public String unSubscribe(@PathVariable("projectId") Long projectId,
			@PathVariable("workItemId") Long workItemId,
			HttpServletRequest httpServletRequest){
		Account account = Account.findAccountsByEmailEquals(
				(String) SecurityUtils.getSubject().getPrincipal())
				.getSingleResult();
		Project project = Project.findProject(projectId);
		MemberInformation member = MemberInformation
				.findMemberInformationsByAccountAndProject(account, project)
				.getSingleResult();
		WorkItem workItem = WorkItem.findWorkItem(workItemId);
		if (workItem.getSubcribers().contains(member)) {
			workItem.getSubcribers().remove(member);
			workItem.flush();
		}
		return "redirect:/projects/"
				+ workItem.getWorkItemContainer().getParentProjectOrMyself()
						.getId()
				+ "/workitems/"
				+ encodeUrlPathSegment(workItem.getId().toString(),
						httpServletRequest) + "?form";

	}
}
