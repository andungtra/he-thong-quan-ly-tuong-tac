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

import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.WorkItemDTO;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.Field;
import org.hcmus.tis.model.FieldDefine;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.model.WorkItemType;
import org.hcmus.tis.model.xml.ObjectFactory;
import org.hcmus.tis.model.xml.XAdditionalFieldsImpl;
import org.hcmus.tis.model.xml.XFieldImpl;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/workitems")
@Controller
@RooWebScaffold(path = "workitems", formBackingObject = WorkItem.class)
public class WorkItemController {
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid WorkItem workItem, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws JAXBException {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, workItem);
            return "workitems/update";
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
		
        uiModel.asMap().clear();
        workItem.merge();
        return "redirect:/workitems/" + encodeUrlPathSegment(workItem.getId().toString(), httpServletRequest);
    }
	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Long projectId, Long workItemTypeId,
			String redirectUrl, Model uiModel, Principal principal)
			throws NotPermissionException {
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
		uiModel.addAttribute("dependencies", dependencies);
		return "workitems/create";
	}
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
    	WorkItem workItem = WorkItem.findWorkItem(id);
        populateEditFormCustomly(uiModel, workItem);
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
	public String create(@Valid WorkItem workItem, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest)
			throws JAXBException {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, workItem);
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
		
		uiModel.asMap().clear();
		workItem.persist();
		return "redirect:/workitems/"
				+ encodeUrlPathSegment(workItem.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "listWorkItemByProject", params = { "projectId",
			"iDisplayStart", "iDisplayLength", "sEcho" })
	@ResponseBody
	public DtReply listWorkItemByProject(Long projectId, int iDisplayStart,
			int iDisplayLength, String sEcho, int iSortCol_0,
			String sSortDir_0, String sSearch) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		reply.setiTotalRecords((int) WorkItem.countWorkItems());
		reply.setiTotalDisplayRecords((int) WorkItem.countWorkItems());
		List<WorkItem> workItems = WorkItem.findWorkItemEntries(iDisplayStart,
				iDisplayLength);
		for (WorkItem workItem : workItems) {
			if (workItem.getWorkItemContainer().getId().equals(projectId)) {
				WorkItemDTO workItemDto = new WorkItemDTO();
				workItemDto.DT_RowId = workItem.getId();
				workItemDto.setlName("<a href='/TIS/workitems/"
						+ workItem.getId() + "?form'>" + workItem.getTitle()
						+ "</a>");
				workItemDto.setsStatus(workItem.getStatus().getName());
				workItemDto.setsType(workItem.getWorkItemType().getName());
				reply.getAaData().add(workItemDto);
			}
		}
		return reply;
	}
}
