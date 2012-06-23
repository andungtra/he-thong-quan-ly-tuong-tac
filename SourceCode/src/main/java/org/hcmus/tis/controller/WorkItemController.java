package org.hcmus.tis.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.SearchConditionsDTO;
import org.hcmus.tis.dto.datatables.WorkItemDTO;
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
import org.hcmus.tis.repository.AccountRepository;
import org.hcmus.tis.repository.AttachmentRepository;
import org.hcmus.tis.repository.IterationRepository;
import org.hcmus.tis.repository.MemberInformationRepository;
import org.hcmus.tis.repository.PriorityRepository;
import org.hcmus.tis.repository.ProjectRepository;
import org.hcmus.tis.repository.WorkItemHistoryRepository;
import org.hcmus.tis.repository.WorkItemRepository;
import org.hcmus.tis.repository.WorkItemStatusRepository;
import org.hcmus.tis.repository.WorkItemTypeRepository;
import org.hcmus.tis.service.EmailService;
import org.hcmus.tis.util.NotifyAboutWorkItemTask;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

@RequestMapping("/projects/{projectId}/workitems")
@Controller
@RooWebScaffold(path = "workitems", formBackingObject = WorkItem.class)
public class WorkItemController {
	@Autowired
	private TaskExecutor taskExecutor;
	@Autowired
	private EmailService emailService;
	@Autowired
	WorkItemStatusRepository workItemStatusRepository;
	@Autowired
	private MemberInformationRepository memberInformationRepository;
	@Autowired
	private PriorityRepository priorityRepository;
	@Autowired
	WorkItemTypeRepository workItemTypeRepository;
	@Autowired
	AttachmentRepository attachmentRepository;
	@Autowired
	WorkItemRepository workItemRepository;
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("workitem", workItemRepository.findOne(id));
        uiModel.addAttribute("itemId", id);
        return "workitems/show";
    }
	@Autowired
	private IterationRepository iterationRepository;
	public IterationRepository getIterationRepository() {
		return iterationRepository;
	}
	public void setIterationRepository(IterationRepository iterationRepository) {
		this.iterationRepository = iterationRepository;
	}
	public AttachmentRepository getAttachmentRepository() {
		return attachmentRepository;
	}
	public void setAttachmentRepository(AttachmentRepository attachmentRepository) {
		this.attachmentRepository = attachmentRepository;
	}
	public AccountRepository getAccountRepository() {
		return accountRepository;
	}
    public WorkItemStatusRepository getWorkItemStatusRepository() {
		return workItemStatusRepository;
	}
	public void setWorkItemStatusRepository(
			WorkItemStatusRepository workItemStatusRepository) {
		this.workItemStatusRepository = workItemStatusRepository;
	}
	public PriorityRepository getPriorityRepository() {
		return priorityRepository;
	}
	public WorkItemTypeRepository getWorkItemTypeRepository() {
		return workItemTypeRepository;
	}
	public void setWorkItemTypeRepository(
			WorkItemTypeRepository workItemTypeRepository) {
		this.workItemTypeRepository = workItemTypeRepository;
	}
	public void setPriorityRepository(PriorityRepository priorityRepository) {
		this.priorityRepository = priorityRepository;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	private WorkItemHistoryRepository workItemHistoryRepository;
	@Autowired
	private AccountRepository accountRepository;
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	@RequiresPermissions("workitem:update")
	public String update(@Valid WorkItem workItem, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest, @PathVariable("projectId") Long projectId)
			throws JAXBException {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, workItem);
			return "workitems/update";
		}
		WorkItem inDatabaseWorkItem = workItemRepository.findOne(workItem.getId());
		workItem.setSubcribers(inDatabaseWorkItem.getSubcribers());
		workItem.getSubcribers().remove(workItem.getAsignee());
		WorkItemType workItemType = workItemTypeRepository.findOne(workItem
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
		Project project = projectRepository.findOne(projectId);
		MemberInformation member = memberInformationRepository.findByAccountAndProjectAndDeleted(acc, project, false);
		workItem.setUserLastEdit(member);
		uiModel.asMap().clear();
		workItemRepository.save(workItem);
		taskExecutor.execute(new NotifyAboutWorkItemTask(workItem, "updated", emailService));
		/*return "redirect:/projects/"
				+ workItem.getWorkItemContainer().getParentProjectOrMyself()
						.getId()
				+ "/workitems/"
				+ encodeUrlPathSegment(workItem.getId().toString(),
						httpServletRequest);*/
		
		return "redirect:/projects/"
		+ workItem.getWorkItemContainer().getParentProjectOrMyself().getId()
		+ "/task";
	}

	@RequestMapping(value = "/{workItemId}", params = "subscribe")
	public String subscribe(@PathVariable("projectId") Long projectId,
			@PathVariable("workItemId") Long workItemId,
			HttpServletRequest httpServletRequest) {
		Account account = accountRepository.getByEmail(
				(String) SecurityUtils.getSubject().getPrincipal());
		Project project = projectRepository.findOne(projectId);
		MemberInformation member = memberInformationRepository.findByAccountAndProjectAndDeleted(account, project, false);
		WorkItem workItem = workItemRepository.findOne(workItemId);
		if (!workItem.getSubcribers().contains(member) && workItem.getAuthor() != member && workItem.getAsignee() != member) {
			workItem.getSubcribers().add(member);
			workItemRepository.save(workItem);
		}
		return "redirect:/projects/"
				+ workItem.getWorkItemContainer().getParentProjectOrMyself()
						.getId()
				+ "/workitems/"
				+ encodeUrlPathSegment(workItem.getId().toString(),
						httpServletRequest) + "?form";

	}

	public MemberInformationRepository getMemberInformationRepository() {
		return memberInformationRepository;
	}
	public void setMemberInformationRepository(
			MemberInformationRepository memberInformationRepository) {
		this.memberInformationRepository = memberInformationRepository;
	}
	@RequestMapping(params = "form", produces = "text/html")
	@RequiresPermissions("workitem:create")
	public String createForm(@PathVariable("projectId") Long projectId,
			Long workItemTypeId, String redirectUrl, Model uiModel) throws NotPermissionException {
		WorkItem workItem = new WorkItem();
		Project project = projectRepository.findOne(projectId);
		workItem.setWorkItemContainer(project);
		WorkItemType workItemType = workItemTypeRepository
				.findOne(workItemTypeId);
		workItem.setWorkItemType(workItemType);
		populateEditForm(uiModel, workItem);
		List<String[]> dependencies = new ArrayList<String[]>();
		uiModel.addAttribute("projectId", projectId);
		String name = (String) SecurityUtils.getSubject().getPrincipal();
		Account loginAccount = accountRepository.getByEmail(name);
		MemberInformation memberInformation =null;
		
		try {
			memberInformation= memberInformationRepository.findByAccountAndProjectAndDeleted(loginAccount, project, false);
		}catch(Exception ex){}
		if (memberInformation == null) {
			//throw new NotPermissionException();
			//uiModel.addAttribute("project", projectRepository.findOne(projectId));
			uiModel.addAttribute("itemId", projectId);
			uiModel.addAttribute("workItemTypes", projectRepository.findOne(projectId)
					.getProjectProcess().getWorkItemTypes());
			uiModel.addAttribute("error","You are not member of the project !");
			return "projects/tasks";
		}
		uiModel.addAttribute("memberInformationId", memberInformation.getId());
		if (priorityRepository.count() == 0) {
			dependencies.add(new String[] { "priority", "prioritys" });
		}
		if ( workItemStatusRepository.count() == 0) {
			dependencies.add(new String[] { "workitemstatus",
					"workitemstatuses" });
		}
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("dependencies", dependencies);

		return "workitems/create";
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	@RequiresPermissions("workitem:read")
	public String updateForm(@PathVariable("projectId") Long projectId,
			@PathVariable("id") Long id, Model uiModel) {
		WorkItem workItem = workItemRepository.findOne(id);
		populateEditForm(uiModel, workItem);
		Project project = projectRepository.findOne(projectId);
		Account account = accountRepository.getByEmail(
				(String) SecurityUtils.getSubject().getPrincipal());
		MemberInformation member =memberInformationRepository.findByAccountAndProjectAndDeleted(account, project, false);
		boolean involved =  (workItem.getAuthor() == member || workItem.getAsignee() == member);
		boolean subscribed = workItem.getSubcribers().contains(member);
		uiModel.addAttribute("subscribed", subscribed);
		uiModel.addAttribute("involved", involved);
		return "workitems/update";
	}

	void populateEditForm(Model uiModel, WorkItem workItem) {
		uiModel.addAttribute("workItem", workItem);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("memberinformations",memberInformationRepository.findByProjectAndDeleted(workItem.getWorkItemContainer().getParentProjectOrMyself(), false));
		uiModel.addAttribute("prioritys", priorityRepository.findAll());
		workItem.setWorkItemContainer(WorkItemContainer
				.findWorkItemContainer(workItem.getWorkItemContainer().getId()));
		Project project = workItem.getWorkItemContainer()
				.getParentProjectOrMyself();
		Collection<Iteration> iterations = iterationRepository.findByAncestor(project);
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
				workItemStatusRepository.findAll());
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
			populateEditForm(uiModel, workItem);
			return "workitems/create";
		}
		WorkItemType workItemType = workItemTypeRepository.findOne(workItem
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
		Project project = projectRepository.findOne(projectId);
		MemberInformation member = memberInformationRepository.findByAccountAndProjectAndDeleted(acc, project, false);
		workItem.setUserLastEdit(member);
		uiModel.asMap().clear();
		workItemRepository.save(workItem);
		if (attachmentIds != null) {
			for (Long attachmentId : attachmentIds) {
				Attachment attachment = attachmentRepository.findOne(attachmentId);
				attachment.setWorkItem(workItem);
				attachmentRepository.flush();
			}
		}
		/*return "redirect:/projects/"
				+ projectId
				+ "/workitems/"
				+ encodeUrlPathSegment(workItem.getId().toString(),
						httpServletRequest);*/
		
		return "redirect:/projects/"
				+ projectId
				+ "/task";
	}

	public WorkItemRepository getWorkItemRepository() {
		return workItemRepository;
	}
	public void setWorkItemRepository(WorkItemRepository workItemRepository) {
		this.workItemRepository = workItemRepository;
	}
	@RequestMapping(params = {"iDisplayStart", "iDisplayLength",
			"sEcho", "sSearch"})
	@ResponseBody
	@RequiresPermissions("workitem:list")
	public DtReply listWorkItemByProject(
			@PathVariable("projectId") Long projectId, int iDisplayStart,
			int iDisplayLength, String sEcho, String sSearch, @Valid SearchConditionsDTO searchCondition) {
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		Project project = projectRepository.findOne(projectId);
		if(searchCondition.getContainer() == null){
			searchCondition.setContainer(project);
		}
		reply.setiTotalRecords((int) workItemRepository.countBy(null, searchCondition));
		reply.setiTotalDisplayRecords((int) workItemRepository.countBy(sSearch, searchCondition));
		Collection<WorkItem> workItems = workItemRepository.findBy(sSearch, searchCondition, iDisplayStart, iDisplayLength);

		for (WorkItem workItem : workItems) {
			WorkItemDTO workItemDto = new WorkItemDTO();
			workItemDto.DT_RowId = workItem.getId();
			workItemDto.setlName("<a href='/TIS/projects/" + projectId
					+ "/workitems/" + workItem.getId() + "?form'>"
					+ workItem.getTitle() + "</a>");
			workItemDto.setsStatus(workItem.getStatus().getName());
			if(!(workItem.getWorkItemContainer() instanceof Project)){
				workItemDto.setsIteration(workItem.getWorkItemContainer().getName());
			}
			workItemDto.setsType(workItem.getWorkItemType().getName());
			workItemDto.setPriority(workItem.getPriority().getName());
			if(workItem.getWorkItemContainer() instanceof Iteration)
				workItemDto.setIteration(workItem.getWorkItemContainer().getName());
			else
				workItemDto.setIteration("");
			reply.getAaData().add(workItemDto);
		}
		return reply;
	}

	public ProjectRepository getProjectRepository() {
		return projectRepository;
	}
	public void setProjectRepository(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}
	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute("date_format","dd-MM-yyyy HH:mm");
	}

	@RequiresPermissions("workitem:read")
	@RequestMapping(value = "/{id}/history", produces = "text/html")
	public String history(Model uiModel,@PathVariable("id") Long id) {
		Pageable pageable = new PageRequest(0, 10, Direction.DESC, "dateLastEdit");
		WorkItem workItem = workItemRepository.findOne(id);
		List<WorkItemHistory> history = workItemHistoryRepository.findByWorkItem(workItem, pageable).getContent();
		uiModel.addAttribute("history", history);
		uiModel.addAttribute("workItemId", id);
		return "workitems/history";
	}

	@RequestMapping(value = "/{workItemId}", params = "unSubscribe")
	public String unSubscribe(@PathVariable("projectId") Long projectId,
			@PathVariable("workItemId") Long workItemId,
			HttpServletRequest httpServletRequest){
		Account account = accountRepository.getByEmail(
				(String) SecurityUtils.getSubject().getPrincipal());
		Project project = projectRepository.findOne(projectId);
		MemberInformation member = memberInformationRepository.findByAccountAndProjectAndDeleted(account, project, false);
		WorkItem workItem = workItemRepository.findOne(workItemId);
		if (workItem.getSubcribers().contains(member)) {
			workItem.getSubcribers().remove(member);
			workItemRepository.save(workItem);
		}
		return "redirect:/projects/"
				+ workItem.getWorkItemContainer().getParentProjectOrMyself()
						.getId()
				+ "/workitems/"
				+ encodeUrlPathSegment(workItem.getId().toString(),
						httpServletRequest) + "?form";

	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@RequestMapping(value = "/query")
	@ResponseBody
	public List filter(){
		return null;
	}
}
