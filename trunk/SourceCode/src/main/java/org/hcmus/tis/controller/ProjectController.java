package org.hcmus.tis.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.ArrayUtils;
import org.apache.velocity.runtime.directive.Foreach;
import org.hcmus.tis.dto.DSRestResponse;
import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.DSResponse;
import org.hcmus.tis.dto.NonEditableEvent;
import org.hcmus.tis.dto.ProjectDTO;
import org.hcmus.tis.dto.SiteMapItem;
import org.hcmus.tis.dto.WorkItemDTO;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.Event;
import org.hcmus.tis.model.EventTest;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.ProjectStatus;
import org.hcmus.tis.model.StudyClass;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemHistory;
import org.hcmus.tis.model.WorkItemHistoryType;
import org.hcmus.tis.service.ProjectProcessService;
import org.hcmus.tis.util.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/projects")
@Controller
@RooWebScaffold(path = "projects", formBackingObject = Project.class)
@RooWebFinder
public class ProjectController {
	@Autowired
	private ProjectProcessService projectProcessService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Project project, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, project);
			return "projects/create";
		}
		uiModel.asMap().clear();
		project.persist();
		uiModel.addAttribute("projectId", project.getId());
		// return "projects/gotoproject";
		uiModel.addAttribute("projects", Project.findAllProjects());
		return "projects/list";
	}

	void populateEditForm(Model uiModel, Project project) {
		uiModel.addAttribute("project", project);
		uiModel.addAttribute("memberinformations",
				MemberInformation.findAllMemberInformations());
		uiModel.addAttribute("studyclasses", StudyClass.findAllStudyClasses());
		uiModel.addAttribute("workitemcontainers",
				WorkItemContainer.findAllWorkItemContainers());
		uiModel.addAttribute("projectprocesses",
				ProjectProcess.findAllProjectProcesses());
		List<ProjectProcess> test = ProjectProcess.findAllProjectProcesses();
		int size = test.size();
	}

	@RequestMapping(value = "ID/{id}", produces = "text/html")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("itemId", id);
		Project p = Project.findProject(id);
		uiModel.addAttribute("project", p);
		uiModel.addAttribute("itemName", p.getName());
		return "projects/show";
	}

	@RequestMapping(value = "{id}", produces = "text/html")
	public String showhomepage(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("itemId", id);
		List<SiteMapItem> siteMapItems = new ArrayList<SiteMapItem>();
		Project project = Project.findProject(id);
		WorkItemContainer currentContainer = project;
		while(currentContainer  != null){
			SiteMapItem item = new SiteMapItem();
			item.setName(currentContainer.getName());
			item.setUrl("/projects/" + currentContainer.getId());
			siteMapItems.add(item);
			currentContainer = currentContainer.getParentContainer();
		}
		Collections.reverse(siteMapItems);
		uiModel.addAttribute("siteMapItems", siteMapItems);
		return "projects/homepage";
	}

	@RequestMapping(params = "find=quickFind", method = RequestMethod.GET)
	public String findProjectsQuickly(@RequestParam("query") String name,
			Model uiModel,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		int sizeNo = size == null ? 10 : size.intValue();
		int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
		long totalResult = 0;
		if (!name.isEmpty()) {
			uiModel.addAttribute("projects",
					Project.findProjectsByNameLike(name, firstResult, sizeNo)
							.getResultList());
			totalResult = Project.countProjectsByNameLike(name);
		} else {
			uiModel.addAttribute("projects",
					Project.findProjectEntries(firstResult, sizeNo));
			totalResult = Project.countProjects();
		}
		Collection<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(new Parameter("find", "quickFind"));
		parameters.add(new Parameter("query", name));
		uiModel.addAttribute("parameters", parameters);
		int maxPage = (int) (totalResult / sizeNo);
		if (totalResult % sizeNo != 0) {
			++maxPage;
		}
		uiModel.addAttribute("maxPages", maxPage);
		uiModel.addAttribute("query", name);
		return "projects/list";
	}

	public String findProjectsByNameLike(String name, Model uiModel) {
		return "projects/list";
	}

	@RequestMapping(value = "/{id}/overview", produces = "text/html")
	public String overview(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("project", Project.findProject(id));

		Calendar cal = Calendar.getInstance();
		// int curWeek = cal.get(Calendar.WEEK_OF_MONTH);
		long now = cal.get(Calendar.DAY_OF_YEAR);
		ArrayList<WorkItem> overdues = new ArrayList<WorkItem>();
		ArrayList<WorkItem> indues = new ArrayList<WorkItem>();
		List<WorkItem> workItemsList = WorkItem.findAllWorkItems();
		for (WorkItem workItem : workItemsList) {
			if (workItem.getWorkItemContainer().getId().equals(id)) {
				if (workItem.getDueDate() != null) {

					Calendar dueTime = Calendar.getInstance();
					dueTime.setTime(workItem.getDueDate());
					long due = dueTime.get(Calendar.DAY_OF_YEAR);
					if (due < now) {
						if (overdues.size() < 10)
							overdues.add(workItem);
					}

					else if(due-now<7)
						indues.add(workItem);
				}
			}
		}

		List<WorkItemHistory> listHistorys = WorkItemHistory
				.findAllWorkItemHistorysInProject(id, 10);
		uiModel.addAttribute("listHistorys", listHistorys);
		uiModel.addAttribute("overdues", overdues);
		uiModel.addAttribute("indues", indues);
		uiModel.addAttribute("itemId", id);
		return "projects/overview";
	}

	@RequestMapping(value = "/{id}/task", produces = "text/html")
	public String task(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("project", Project.findProject(id));
		uiModel.addAttribute("itemId", id);
		uiModel.addAttribute("workItemTypes", Project.findProject(id)
				.getProjectProcess().getWorkItemTypes());
		return "projects/task";
	}

	@RequestMapping(value = "/{id}/roadmap", produces = "text/html")
	public String getRoadmap(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("project", Project.findProject(id));
		uiModel.addAttribute("itemId", id);
		return "projects/roadmap";
	}

	@RequestMapping(value = "/{id}/members", produces = "text/html")
	public String listMembers(@PathVariable("id") Long id, Model uiModel) {
		Set<MemberInformation> memberInformations = Project.findProject(id)
				.getMemberInformations();
		uiModel.addAttribute("memberinformations", memberInformations);
		uiModel.addAttribute("projectId", id);
		return "projects/member";
	}

	@RequestMapping(value = "mList", params = { "iDisplayStart",
			"iDisplayLength", "sEcho", "sSearch" })
	@ResponseBody
	public DtReply mList(int iDisplayStart, int iDisplayLength, String sEcho, String sSearch) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);		
		List<Project> list = Project.findProjectEntries(iDisplayStart,iDisplayLength,sSearch );
		for (Project item : list) {
			if (item.getStatus() != ProjectStatus.DELETED) {
				ProjectDTO dto = new ProjectDTO();
				dto.DT_RowId = item.getId();
				dto.setName(item.getName());
				if (item.getParentContainer() != null)
					dto.setParentContainer(item.getParentContainer().getName());
				dto.setDescription(item.getDescription());

				reply.getAaData().add(dto);
			}
		}
		reply.setiTotalRecords(reply.getAaData().size());
		return reply;
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Project project, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest,
			HttpSession session) {
		Account acc = (Account) session.getAttribute("account");
		List<MemberInformation> listInfo = MemberInformation
				.findMemberInformationsByProject(project);
		MemberInformation info = null;
		for (MemberInformation memberInformation : listInfo) {
			if (memberInformation.getAccount().equals(acc)) {
				info = memberInformation;
				break;
			}
		}
		if (acc.getIsAdmin()==true ||( info!=null && info.getMemberRole().getId() == 1)) {
			if (bindingResult.hasErrors()) {
				populateEditForm(uiModel, project);
				return "projects/update";
			}
			uiModel.asMap().clear();
			project.merge();
			uiModel.addAttribute("projects", Project.findAllProjects());
			return "projects/list";
		} else {
			uiModel.addAttribute("error","You don't have authority !!!");
			populateEditForm(uiModel, project);
			return "projects/update";
		}
	}

	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		List<Project> lst = null;
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			lst = Project.findProjectEntries(firstResult, sizeNo);

			float nrOfPages = (float) Project.countProjects() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			lst = Project.findAllProjects();
		}

		for (int i = 0; i < lst.size(); i++) {
			if (lst.get(i).getStatus() != null
					&& lst.get(i).getStatus().equals(ProjectStatus.DELETED))
				lst.remove(i);
		}

		uiModel.addAttribute("projects", lst);
		return "projects/list";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		Project project = Project.findProject(id);
		project.setStatus(ProjectStatus.DELETED);
		project.merge();
		// project.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/projects";
	}

	@RequestMapping(value = "/{id}/dumpcalendar")
	public String showDumpCalendar(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("id", id);
		return "projects/dumpcalendar";
	}

	@RequestMapping(value = "/{id}/calendar")
	public String showCalendar(@PathVariable("id") Long id,
			@RequestParam("encodedmemberids") String encodedMemberIds,
			Model uiModel) {
		uiModel.addAttribute("id", id);
		uiModel.addAttribute("encodedmemberids", encodedMemberIds);
		return "projects/calendar";
	}

	@RequestMapping(value = "/{id}/calendar/{encodedmemberids}/events")
	@ResponseBody
	public DSRestResponse getEvents(@PathVariable("id") Long id,
			@PathVariable("encodedmemberids") String encodedMemberIds) {
		DSRestResponse restResponse = new DSRestResponse();
		restResponse.setResponse(new DSResponse());
		Project project = Project.findProject(id);
		restResponse.getResponse().setData(new ArrayList<Object>());
		for (Event event : project.getCalendar().getEvents()) {
			restResponse.getResponse().getData().add(event);
		}
		for (Event event : project.getEventsOfMembers()) {
			NonEditableEvent nonEditableEvent = new NonEditableEvent();
			nonEditableEvent.setId(event.getId());
			int membersNumber = 0;
			String memberNames = "";

			for (org.hcmus.tis.model.Calendar calendar : event.getCalendars()) {
				if (calendar.getAccount() != null) {
					for (MemberInformation memberInformation : project
							.getMemberInformations()) {
						if (memberInformation.getAccount().getCalendar()
								.getId() == calendar.getId()) {
							membersNumber++;
							memberNames = memberNames
									+ memberInformation.getAccount()
											.getFirstName()
									+ " "
									+ memberInformation.getAccount()
											.getLastName() + ",";
							break;
						}
					}
				}
			}
			nonEditableEvent
					.setName(String.valueOf(membersNumber) + " members");
			nonEditableEvent.setDescription(memberNames);
			nonEditableEvent.setStartDate(event.getStartDate());
			nonEditableEvent.setEndDate(event.getEndDate());
			restResponse.getResponse().getData().add(nonEditableEvent);
		}
		restResponse.getResponse().setStatus(0);
		return restResponse;
	}

	@RequestMapping(value = "/{id}/calendar/{encodedmemberids}/events", params = { "_operationType=add" })
	@ResponseBody
	public DSRestResponse creatEvent(@PathVariable("id") Long projectId,
			@Valid Event event, BindingResult bindingResult) {
		DSRestResponse restResponse = new DSRestResponse();
		restResponse.setResponse(new DSResponse());
		Project project = Project.findProject(projectId);
		project.getCalendar().getEvents().add(event);
		for (MemberInformation memberInformation : project
				.getMemberInformations()) {
			memberInformation.getAccount().getCalendar().getEvents().add(event);
		}
		event.persist();
		restResponse.getResponse().setData(new ArrayList<Object>());
		restResponse.getResponse().getData().add(event);
		restResponse.getResponse().setStatus(0);

		return restResponse;
	}

	@RequestMapping(value = "/{id}/calendar/{encodedmemberids}/events", params = { "_operationType=update" })
	@ResponseBody
	public DSRestResponse updateEvent(@PathVariable("id") Long projectId,
			Long id, @Valid Event event, BindingResult bindingResult) {
		DSRestResponse restResonse = new DSRestResponse();
		restResonse.setResponse(new DSResponse());
		event.setId(id);
		Event resultEvent = event.merge();

		restResonse.getResponse().setStatus(0);
		restResonse.getResponse().setData(new ArrayList<Object>());
		restResonse.getResponse().getData().add(resultEvent);
		return restResonse;
	}

	@RequestMapping(value = "/{id}/calendar/{encodedmemberids}/events", params = { "_operationType=remove" })
	@ResponseBody
	public DSRestResponse deleteEvent(@PathVariable("id") Long accountId,
			Long id) {
		DSRestResponse restResponse = new DSRestResponse();
		restResponse.setResponse(new DSResponse());
		Event event = Event.findEvent(id);
		for (org.hcmus.tis.model.Calendar calendar : event.getCalendars()) {
			calendar.getEvents().remove(event);
		}
		event.remove();
		restResponse.getResponse().setStatus(0);
		restResponse.getResponse().setData(new ArrayList<Object>());
		restResponse.getResponse().getData().add(event);
		return restResponse;
	}
	public void createWorkItemContainer(Long projectId){
		
	}
}
