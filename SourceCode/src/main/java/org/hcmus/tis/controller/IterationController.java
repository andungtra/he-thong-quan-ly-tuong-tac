package org.hcmus.tis.controller;

import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItemContainer;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/projects/{projectid}/iterations")
@Controller
@RooWebScaffold(path = "iterations", formBackingObject = Iteration.class)
public class IterationController {
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(@PathVariable("projectid") Long projectId, Model uiModel) {
    	Project project = Project.findProject(projectId);
    	Iteration iteration = new Iteration();
    	iteration.setParentContainer(project);
        populateEditForm(uiModel, iteration, project);
        return "iterations/create";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Iteration iteration, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, iteration, new Project());
            return "iterations/update";
        }
        uiModel.asMap().clear();
        iterationService.updateIteration(iteration);
        return "redirect:/iterations/" + encodeUrlPathSegment(iteration.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("projectid") Long projectId, @PathVariable("id") Long id, Model uiModel) {
    	Project project = Project.findProject(projectId);
        populateEditForm(uiModel, Iteration.findIteration(id), project);
        return "iterations/update";
    }
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@PathVariable("projectid")  Long projectId, @Valid Iteration iteration, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	Project project = Project.findProject(projectId);
            populateEditForm(uiModel, iteration, project);
            return "iterations/create";
        }
        uiModel.asMap().clear();
        if(iteration.getParentContainer() == null){
        	Project project = Project.findProject(projectId);
        	iteration.setParentContainer(project);
        }
        iteration.persist();
        return "redirect:/projects/" + encodeUrlPathSegment(iteration.getParentProjectOrMyself().getId().toString(), httpServletRequest) + "/roadmap";
    }
    void populateEditForm(Model uiModel, Iteration iteration, Project project) {
        uiModel.addAttribute("iteration", iteration);
        Collection<Iteration> iterations = Iteration.getdescendantIterations(project);
        uiModel.addAttribute("iterations", iterations);
        uiModel.addAttribute("projectId", project.getId());
    }
}
