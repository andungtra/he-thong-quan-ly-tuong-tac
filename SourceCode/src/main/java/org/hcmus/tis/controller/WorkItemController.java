package org.hcmus.tis.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.WorkItemDto;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.model.WorkItemType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/workitems")
@Controller
@RooWebScaffold(path = "workitems", formBackingObject = WorkItem.class)
public class WorkItemController {

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Long projectId, Long workItemTypeId,
			String redirectUrl, Model uiModel) {
		WorkItem workItem = new WorkItem();
		Project project = new Project();
		project.setId(projectId);
		workItem.setWorkItemContainer(project);
		populateEditFormCustomly(uiModel, workItem, (long) 1);
		List<String[]> dependencies = new ArrayList<String[]>();
		uiModel.addAttribute("workItemTypeId", workItemTypeId);
		uiModel.addAttribute("projectId", projectId);
		uiModel.addAttribute("memberInformationId", (long) 1);
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

	void populateEditFormCustomly(Model uiModel, WorkItem workItem,
			Long projectId) {
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
	}

	@RequestMapping(value = "listWorkItemByProject", params = { "projectId",
			"iDisplayStart", "iDisplayLength", "sEcho" })
	@ResponseBody
	public DtReply listWorkItemByProject(Long projectId, int iDisplayStart,
			int iDisplayLength, String sEcho) {
		for (int index = 0; index < 100000000; ++index) {

		}
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		reply.setiTotalRecords((int) WorkItem.countWorkItems());
		reply.setiTotalDisplayRecords((int) WorkItem.countWorkItems());
		List<WorkItem> workItems = WorkItem.findWorkItemEntries(iDisplayStart,
				iDisplayLength);
		for (WorkItem workItem : workItems) {
			WorkItemDto workItemDto = new WorkItemDto();
			workItemDto.setlName("<a href='/TIS/workitems/" + workItem.getId()
					+ "'>" + workItem.getTitle() + "</a>");
			workItemDto.setsStatus(workItem.getStatus().getName());
			workItemDto.setsType(workItem.getWorkItemType().getName());
			reply.getAaData().add(workItemDto);
		}
		return reply;
	}
}
