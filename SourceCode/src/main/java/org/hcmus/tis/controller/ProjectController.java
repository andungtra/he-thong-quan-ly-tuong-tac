package org.hcmus.tis.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hcmus.tis.dto.AttributeValueDTO;
import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.NonEditableEvent;
import org.hcmus.tis.dto.SearchConditionsDTO;
import org.hcmus.tis.dto.SiteMapItem;
import org.hcmus.tis.dto.datatables.DSResponse;
import org.hcmus.tis.dto.datatables.DSRestResponse;
import org.hcmus.tis.dto.datatables.ProjectDTO;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.Event;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.ProjectStatus;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemActivity;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemHistory;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.repository.EventRepository;
import org.hcmus.tis.repository.IterationRepository;
import org.hcmus.tis.repository.MemberInformationRepository;
import org.hcmus.tis.repository.ProjectRepository;
import org.hcmus.tis.repository.StudyClassRepository;
import org.hcmus.tis.repository.WorkItemActivityRepository;
import org.hcmus.tis.repository.WorkItemHistoryRepository;
import org.hcmus.tis.repository.WorkItemRepository;
import org.hcmus.tis.repository.WorkItemStatusRepository;
import org.hcmus.tis.service.ProjectProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
public class ProjectController {
	@Autowired
	private ProjectProcessService projectProcessService;
	@Autowired
	private StudyClassRepository studyClassRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private WorkItemStatusRepository workItemStatusRepository;
	@Autowired
	private IterationRepository iterationRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private WorkItemHistoryRepository workItemHistoryRepository;

	public IterationRepository getIterationRepository() {
		return iterationRepository;
	}

	public void setIterationRepository(IterationRepository iterationRepository) {
		this.iterationRepository = iterationRepository;
	}

	public MemberInformationRepository getMemberInformationRepository() {
		return memberInformationRepository;
	}

	public void setMemberInformationRepository(
			MemberInformationRepository memberInformationRepository) {
		this.memberInformationRepository = memberInformationRepository;
	}

	@Autowired
	private MemberInformationRepository memberInformationRepository;

	public WorkItemStatusRepository getWorkItemStatusRepository() {
		return workItemStatusRepository;
	}

	public void setWorkItemStatusRepository(
			WorkItemStatusRepository workItemStatusRepository) {
		this.workItemStatusRepository = workItemStatusRepository;
	}

	public ProjectProcessService getProjectProcessService() {
		return projectProcessService;
	}

	public void setProjectProcessService(
			ProjectProcessService projectProcessService) {
		this.projectProcessService = projectProcessService;
	}

	public StudyClassRepository getStudyClassRepository() {
		return studyClassRepository;
	}

	public void setStudyClassRepository(
			StudyClassRepository studyClassRepository) {
		this.studyClassRepository = studyClassRepository;
	}

	public EventRepository getEventRepository() {
		return eventRepository;
	}

	public void setEventRepository(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		Project project = new Project();
		project.setStatus(ProjectStatus.OPEN);
		populateEditForm(uiModel, project);
		List<String[]> dependencies = new ArrayList<String[]>();
		if (projectProcessService.countAllProjectProcesses() == 0) {
			dependencies.add(new String[] { "projectprocess",
					"projectprocesses" });
		}
		uiModel.addAttribute("dependencies", dependencies);
		return "projects/create";
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	@RequiresPermissions("project:create")
	public String create(@Valid Project project, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, project);
			return "projects/create";
		}
		project.setStatus(ProjectStatus.OPEN);
		uiModel.asMap().clear();
		projectRepository.save(project);
		return "redirect:projects?recentAction=created&recentId=" + project.getId();
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(
			@PathVariable("id") Long id,
			Model uiModel,
			@RequestParam(value = "keepupdate", required = false) Boolean keepUpdate, String recentAction) {
		populateEditForm(uiModel, projectRepository.findOne(id));
		uiModel.addAttribute("keepupdate", keepUpdate);
		if(keepUpdate != null && keepUpdate){
			uiModel.addAttribute("recentAction", recentAction);
		}
		return "projects/update";
	}

	void populateEditForm(Model uiModel, Project project) {
		uiModel.addAttribute("project", project);
		uiModel.addAttribute("studyclasses",
				studyClassRepository.findByDeleted(false));
		List<Project> projects = projectRepository.findByStatusNot(ProjectStatus.DELETED);
		projects.remove(project);
		uiModel.addAttribute("workitemcontainers",
				projects);
		List<ProjectProcess> tempt = ProjectProcess.findAllProjectProcesses();
		List<ProjectProcess> projectProcesses = new ArrayList<ProjectProcess>();
		for(ProjectProcess process : tempt){
			if(process.isIsDeleted() == false){
				projectProcesses.add(process);
			}
		}
		uiModel.addAttribute("projectprocesses",
				projectProcesses);
		uiModel.addAttribute("projectstatuses",
				Arrays.asList(ProjectStatus.values()));
	}

	@RequestMapping(value = "ID/{id}", produces = "text/html")
	@RequiresPermissions("project:read")
	public String show(@PathVariable("id") Long id, Model uiModel) {
		Project p = projectRepository.findOne(id);
		uiModel.addAttribute("project", p);
		return "projects/show";
	}

	@RequestMapping(value = "{id}", produces = "text/html")
	@RequiresPermissions("project:read")
	String showhomepage(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("projectId", id);
		List<SiteMapItem> siteMapItems = new ArrayList<SiteMapItem>();
		Project project = projectRepository.findOne(id);
		WorkItemContainer currentContainer = project;
		int num = 0;
		while (currentContainer != null && num < 3) {
			SiteMapItem item = new SiteMapItem();
			item.setName(currentContainer.getName());
			item.setUrl("/projects/" + currentContainer.getId());
			siteMapItems.add(item);

			if (currentContainer.equals(currentContainer.getParentContainer()))
				currentContainer = null;
			else
				currentContainer = currentContainer.getParentContainer();
			num++;
		}
		Collections.reverse(siteMapItems);
		uiModel.addAttribute("siteMapItems", siteMapItems);
		return "projects/homepage";
	}

	@Autowired
	WorkItemRepository workItemRepository;
	@Autowired
	WorkItemActivityRepository workItemActivityRepository;

	@RequestMapping(value = "/{id}/overview", produces = "text/html")
	@RequiresPermissions("project:read")
	public String overview(@PathVariable("id") Long id, Model uiModel)
			throws IOException {
		uiModel.addAttribute("project", projectRepository.findOne(id));

		Calendar cal = Calendar.getInstance();

		long now = cal.get(Calendar.DAY_OF_YEAR);
		ArrayList<WorkItem> overdues = new ArrayList<WorkItem>();
		ArrayList<WorkItem> indues = new ArrayList<WorkItem>();
		Project project = projectRepository.findOne(id);
		int pageSize = (int) workItemRepository.countByAncestorContainter(project,
				false);
		if(pageSize <= 0){
			pageSize = 1;
		}
		Pageable workItemPageable = new PageRequest(0,pageSize
				, new Sort(Direction.ASC, "dueDate"));
		List<WorkItem> workItemsList = workItemRepository.findByAncestor(
				project, false, workItemPageable);

		for (WorkItem workItem : workItemsList) {
			if (workItem.getDueDate() != null
					&& !workItem.getStatus().getName().equals("Closed")
					&& !workItem.getStatus().getName().equals("Resolved")) {
				Calendar dueTime = Calendar.getInstance();
				dueTime.setTime(workItem.getDueDate());
				long due = dueTime.get(Calendar.DAY_OF_YEAR);
				if (due < now) {
					if (overdues.size() < 10)
						overdues.add(workItem);
				}

				else if (due - now < 7)
					indues.add(workItem);
			}
		}
		List<WorkItemStatus> statuses = workItemStatusRepository.findAll();
		Object[][] listStatus = new Object[statuses.size()][2];
		for (int index = 0; index < statuses.size(); ++index) {
			listStatus[index][0] = statuses.get(index).getName();
			listStatus[index][1] = workItemRepository
					.countByAncestorContainerAndStatus(project,
							statuses.get(index));
		}
		Pageable pageable = new PageRequest(0, 10);
		List<WorkItemActivity> workItemActivities = workItemActivityRepository.findByProject(project, new PageRequest(0, 1000)).getContent();
		uiModel.addAttribute("listHistorys", workItemActivities);

		uiModel.addAttribute("overdues", overdues);
		uiModel.addAttribute("indues", indues);
		uiModel.addAttribute("listStatus", listStatus);
		uiModel.addAttribute("itemId", id);
		return "projects/overview";
	}

	@RequestMapping(value = "/{id}/workitems", produces = "text/html")
	public String task(@PathVariable("id") Long id, Model uiModel,
			String recentAction, Long recentWorkItemId) {
		// uiModel.addAttribute("project", projectRepository.findOne(id));
		uiModel.addAttribute("itemId", id);
		if (recentAction != null && recentWorkItemId != null) {
			uiModel.addAttribute("recentActivity", recentAction);
			uiModel.addAttribute("recentWorkItem",
					workItemRepository.findOne(recentWorkItemId));
		}
		uiModel.addAttribute("workItemTypes", projectRepository.findOne(id)
				.getProjectProcess().getWorkItemTypes());
		return "projects/tasks";
	}

	@RequestMapping(value = "/{id}/advancedsearch", produces = "text/html")
	public String advancedSearch(@PathVariable("id") Long id, Model uiModel,
			@Valid SearchConditionsDTO searchCondition) {
		Project project = projectRepository.findOne(id);
		uiModel.addAttribute("project", project);
		uiModel.addAttribute("itemId", id);
		uiModel.addAttribute("statuses", workItemStatusRepository.findAll());
		uiModel.addAttribute("searchcondition", searchCondition);
		uiModel.addAttribute("members", memberInformationRepository
				.findByProjectAndDeleted(project, false));
		uiModel.addAttribute("iterations",
				iterationRepository.findByAncestor(project));
		uiModel.addAttribute("workItemTypes", projectRepository.findOne(id)
				.getProjectProcess().getWorkItemTypes());
		ArrayList<AttributeValueDTO> params = new ArrayList<AttributeValueDTO>();
		if (searchCondition.getStatus() != null) {
			params.add(new AttributeValueDTO("status", searchCondition
					.getStatus().getId().toString()));
		}
		if (searchCondition.getOwner() != null) {
			params.add(new AttributeValueDTO("owner", searchCondition
					.getOwner().getId().toString()));
		}
		if (searchCondition.getAsignee() != null) {
			params.add(new AttributeValueDTO("asignee", searchCondition
					.getAsignee().getId().toString()));
		}
		if (searchCondition.getContainer() != null) {
			params.add(new AttributeValueDTO("container", searchCondition
					.getContainer().getId().toString()));
		}
		if (searchCondition.getTitleDescription() != null) {
			params.add(new AttributeValueDTO("titleDescription",
					searchCondition.getTitleDescription()));
		}
		if (searchCondition.getClosed() != null) {
			params.add(new AttributeValueDTO("closed", searchCondition
					.getClosed().toString()));
		}
		uiModel.addAttribute("searchparams", params);
		return "projects/advancedtasks";
	}

	public ProjectRepository getProjectRepository() {
		return projectRepository;
	}

	public void setProjectRepository(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	@RequestMapping(value = "/{id}/iterations", produces = "text/html")
	public String getPlan(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("project", projectRepository.findOne(id));
		uiModel.addAttribute("itemId", id);
		return "projects/roadmap";
	}

	@RequestMapping(value = "/{id}/memberinformations", produces = "text/html")
	public String listMembers(@PathVariable("id") Long id, Model uiModel,String recentAction, Long recentMemberId) {
		Set<MemberInformation> memberInformations = projectRepository.findOne(
				id).getMemberInformations();
		uiModel.addAttribute("memberinformations", memberInformations);
		uiModel.addAttribute("projectId", id);
		if(recentAction != null && recentMemberId != null){
			uiModel.addAttribute("recentAction", recentAction);
			uiModel.addAttribute("recentMember", memberInformationRepository.findOne(recentMemberId));
		}
		return "projects/member";
	}

	@RequestMapping(value = "mList", params = { "iDisplayStart",
			"iDisplayLength", "sEcho", "sSearch" })
	@ResponseBody
	public DtReply mList(int iDisplayStart, int iDisplayLength, String sEcho,
			String sSearch, HttpServletRequest request) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		Pageable pageable = new PageRequest(iDisplayStart / iDisplayLength,
				iDisplayLength);
		Page<Project> page = projectRepository.findByNameLikeAndStatusNot("%"
				+ sSearch + "%", ProjectStatus.DELETED, pageable);
		List<Project> list = page.getContent();
		for (Project item : list) {
			ProjectDTO dto = new ProjectDTO();
			dto.DT_RowId = item.getId();
			dto.setName("<a href='" + request.getContextPath() + "/projects/"
					+ item.getId() + "?goto=true'>" + item.getName() + "</a>");

			if (item.getParentContainer() != null)
				dto.setParentContainer(item.getParentContainer().getName());
			String s;
			if (item.getDescription() != null
					&& item.getDescription().length() > 50)
				s = item.getDescription().substring(0, 49) + " ...";
			else
				s = item.getDescription();
			dto.setDescription(s);

			reply.getAaData().add(dto);
		}
		reply.setiTotalDisplayRecords((int) page.getTotalElements());
		reply.setiTotalRecords((int) projectRepository
				.countByStatusNot(ProjectStatus.DELETED));
		return reply;
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	@RequiresPermissions("project:update")
	public String update(
			@Valid Project project,
			@RequestParam(value = "keepupdate", required = false) Boolean update,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, HttpSession session) {
		if (project.getStatus() == ProjectStatus.DELETED) {
			List<MemberInformation> listInfo = memberInformationRepository
					.findByProjectAndDeleted(project, false);
			for (MemberInformation memberInformation : listInfo) {
				memberInformation.setDeleted(true);
				memberInformationRepository.save(memberInformation);
			}
			org.hcmus.tis.model.Calendar calendar  = projectRepository.findOne(project.getId()).getCalendar();
			for(Event event : calendar.getEvents()){
				for (org.hcmus.tis.model.Calendar calendar2 : event.getCalendars()) {
					calendar2.getEvents().remove(event);
				}
				eventRepository.delete(event);
			}
		}
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, project);
			return "projects/update";
		}
		uiModel.asMap().clear();
		projectRepository.save(project);
		if (update != null && update == true) {
			if(project.getStatus() == ProjectStatus.DELETED){
				uiModel.addAttribute("url", "/accounts/home");
				return "redirect";
			}
			return "redirect:/projects/" + project.getId()
					+ "?form&keepupdate=true&recentAction=updated";
		}
		return "redirect:/projects?recentAction=updated&recentId=" + project.getId();

	}

	@RequestMapping(produces = "text/html")
	@RequiresPermissions("project:list")
	public String list(String recentAction,  Long recentId, Model uiModel) {
		if(recentAction != null && recentId != null){
			uiModel.addAttribute("recentAction", recentAction);
			uiModel.addAttribute("recentProject", projectRepository.findOne(recentId));
		}
		return "projects/list";
	}

	/**
	 * @param id
	 * @param page
	 * @param size
	 * @param uiModel
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	@RequiresPermissions("project:delete")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		Project project = projectRepository.findOne(id);
		project.setStatus(ProjectStatus.DELETED);
		projectRepository.save(project);
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
		Project project = projectRepository.findOne(id);
		restResponse.getResponse().setData(new ArrayList<Object>());
		for (Event event : project.getCalendar().getEvents()) {
			restResponse.getResponse().getData().add(event);
		}
		List<MemberInformation> projectMembers = memberInformationRepository.findByProjectAndDeleted(project, false);
		for (Event event : project.getEventsOfMembers()) {
			NonEditableEvent nonEditableEvent = new NonEditableEvent();
			nonEditableEvent.setId(event.getId());
			int membersNumber = 0;
			String memberNames = "";

			for (org.hcmus.tis.model.Calendar calendar : event.getCalendars()) {
				if (calendar.getAccount() != null) {
					for (MemberInformation memberInformation : projectMembers) {
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
		Project project = projectRepository.findOne(projectId);
		project.getCalendar().getEvents().add(event);
		for (MemberInformation memberInformation : project
				.getMemberInformations()) {
			memberInformation.getAccount().getCalendar().getEvents().add(event);
		}
		eventRepository.save(event);
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
		eventRepository.save(event);
		Event resultEvent = eventRepository.findOne(event.getId());

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
		Event event = eventRepository.findOne(id);
		for (org.hcmus.tis.model.Calendar calendar : event.getCalendars()) {
			calendar.getEvents().remove(event);
		}
		eventRepository.delete(event);
		restResponse.getResponse().setStatus(0);
		restResponse.getResponse().setData(new ArrayList<Object>());
		restResponse.getResponse().getData().add(event);
		return restResponse;
	}

	/*
	 * @RequestMapping(value = "/workitems/{workItemId}/history", produces =
	 * "text/html") public String history(Model uiModel,
	 * @PathVariable("workItemId") Long id) { List<WorkItemHistory> history =
	 * WorkItemHistory .findAllWorkItemHistorysOfWorkItem(id, 10);
	 * uiModel.addAttribute("history", history);
	 * uiModel.addAttribute("workItemId", id); return "workitems/history"; }
	 */
}
