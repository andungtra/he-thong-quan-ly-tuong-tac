package org.hcmus.tis.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hcmus.tis.dto.DtReply;
import org.hcmus.tis.dto.ProjectDTO;
import org.hcmus.tis.dto.WorkItemDTO;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.StudyClass;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
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
    public String create(@Valid Project project, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, project);
            return "projects/create";
        }
        uiModel.asMap().clear();
        project.persist();
        uiModel.addAttribute("projectId", project.getId());
        //return "projects/gotoproject";
        uiModel.addAttribute("projects", Project.findAllProjects());
        return "projects/list";
    }

    void populateEditForm(Model uiModel, Project project) {
        uiModel.addAttribute("project", project);
        uiModel.addAttribute("memberinformations", MemberInformation.findAllMemberInformations());
        uiModel.addAttribute("studyclasses", StudyClass.findAllStudyClasses());
        uiModel.addAttribute("workitemcontainers", WorkItemContainer.findAllWorkItemContainers());
        uiModel.addAttribute("projectProcesses", ProjectProcess.findAllProjectProcesses());
    }

    @RequestMapping(value = "ID/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("itemId", id);
        Project p = Project.findProject(id);
        uiModel.addAttribute("itemName", p.getName());
        return "projects/show";
    }

    @RequestMapping(params = "find=quickFind", method = RequestMethod.GET)
    public String findProjectsQuickly(@RequestParam("query") String name, Model uiModel, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        int sizeNo = size == null ? 10 : size.intValue();
        int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
        long totalResult = 0;
        if (!name.isEmpty()) {
            uiModel.addAttribute("projects", Project.findProjectsByNameLike(name, firstResult, sizeNo).getResultList());
            totalResult = Project.countProjectsByNameLike(name);
        } else {
            uiModel.addAttribute("projects", Project.findProjectEntries(firstResult, sizeNo));
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
        uiModel.addAttribute("itemId", id);
        return "projects/overview";
    }

    @RequestMapping(value = "/{id}/task", produces = "text/html")
    public String task(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("project", Project.findProject(id));
        uiModel.addAttribute("itemId", id);
        uiModel.addAttribute("workItemTypes", Project.findProject(id).getProjectProcess().getWorkItemTypes());
        return "projects/task";
    }

    @RequestMapping(value = "/{id}/wiki", produces = "text/html")
    public String wiki(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("project", Project.findProject(id));
        uiModel.addAttribute("itemId", id);
        return "projects/wiki";
    }

    @RequestMapping(value = "/{id}/members", produces = "text/html")
    public String listMembers(@PathVariable("id") Long id, Model uiModel) {
        Set<MemberInformation> memberInformations = Project.findProject(id).getMemberInformations();
        uiModel.addAttribute("memberinformations", memberInformations);
        uiModel.addAttribute("projectId", id);
        return "projects/member";
    }

    @RequestMapping(value = "/{id}/calendar", produces = "text/html")
    public String calendar(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("project", Project.findProject(id));
        uiModel.addAttribute("itemId", id);
        return "projects/calendar";
    }
    
    @RequestMapping(value = "mList", params = { "iDisplayStart", "iDisplayLength", "sEcho" })
	@ResponseBody
	public DtReply mList( int iDisplayStart,int iDisplayLength, String sEcho) {		
		DtReply reply = new DtReply();
		reply.setsEcho(sEcho);
		reply.setiTotalRecords((int) Project.countProjects());
		reply.setiTotalDisplayRecords((int)  Project.countProjects());
		List<Project> list = Project.findProjectEntries(iDisplayStart,iDisplayLength);
		for (Project item : list) {
			ProjectDTO dto = new ProjectDTO();
			dto.DT_RowId=item.getId();
			dto.setName(item.getName());
			if(item.getParentContainer()!=null)
				dto.setParentContainer(item.getParentContainer().getName());
			dto.setDescription(item.getDescription());
					
			reply.getAaData().add(dto);
		}		
		return reply;
	}
}
