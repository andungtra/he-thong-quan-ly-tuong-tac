package org.hcmus.tis.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.util.Parameter;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/projects")
@Controller
@RooWebScaffold(path = "projects", formBackingObject = Project.class)
@RooWebFinder
public class ProjectController {
	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("project", Project.findProject(id));
        uiModel.addAttribute("itemId", id);
        return "projects/show";
    }
	@RequestMapping(value = "/testlayout")
	public String testProjectLayout(){
		return "projects/testlayout";
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
        return "projects/member";
    }
    
    @RequestMapping(value = "/{id}/calendar", produces = "text/html")
    public String calendar(@PathVariable("id") Long id, Model uiModel) {
    	uiModel.addAttribute("project", Project.findProject(id));
        uiModel.addAttribute("itemId", id);
        return "projects/calendar";
    }
    
    @RequestMapping(value = "/{id}/activity", produces = "text/html")
    public String activity(@PathVariable("id") Long id, Model uiModel) {
    	uiModel.addAttribute("project", Project.findProject(id));
        uiModel.addAttribute("itemId", id);
        return "projects/activity";
    }
}
