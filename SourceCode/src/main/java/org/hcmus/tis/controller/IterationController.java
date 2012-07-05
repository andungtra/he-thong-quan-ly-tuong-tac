package org.hcmus.tis.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.repository.IterationRepository;
import org.hcmus.tis.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/projects/{projectid}/iterations")
@Controller
@RooWebScaffold(path = "iterations", formBackingObject = Iteration.class)
public class IterationController {
	@Autowired
	private IterationRepository iterationRepository;
	@Autowired
	private ProjectRepository projectRepository;
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(@PathVariable("projectid") Long projectId, Model uiModel) {
    	Project project = projectRepository.findOne(projectId);
    	Iteration iteration = new Iteration();
    	iteration.setParentContainer(project);
        populateEditForm(uiModel, iteration, project);
        return "iterations/create";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@PathVariable("projectid") Long projectId, @Valid Iteration iteration, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, iteration, new Project());
            return "iterations/update";
        }
        uiModel.asMap().clear();
        if(iteration.getParentContainer() == null){
        	Project project = projectRepository.findOne(projectId);
        	iteration.setParentContainer(project);
        }
        iterationRepository.save(iteration);
        return "redirect:/projects/" + encodeUrlPathSegment(iteration.getParentProjectOrMyself().getId().toString(), httpServletRequest) + "/iterations";
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("projectid") Long projectId, @PathVariable("id") Long id, Model uiModel) {
    	Project project = projectRepository.findOne(projectId);
        populateEditForm(uiModel, iterationRepository.findOne(id), project);
        return "iterations/update";
    }
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@PathVariable("projectid")  Long projectId, @Valid Iteration iteration, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
        	Project project = projectRepository.findOne(projectId);
            populateEditForm(uiModel, iteration, project);
            return "iterations/create";
        }
        uiModel.asMap().clear();
        if(iteration.getParentContainer() == null){
        	Project project = projectRepository.findOne(projectId);
        	iteration.setParentContainer(project);
        }
        iterationRepository.save(iteration);
        return "redirect:/projects/" + encodeUrlPathSegment(iteration.getParentProjectOrMyself().getId().toString(), httpServletRequest) + "/iterations";
    }
    public ProjectRepository getProjectRepository() {
		return projectRepository;
	}

	public void setProjectRepository(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	void populateEditForm(Model uiModel, Iteration iteration, Project project) {
        uiModel.addAttribute("iteration", iteration);
        Collection<Iteration> iterations = iterationRepository.findByAncestor(project);
        iterations.remove(iteration);
        uiModel.addAttribute("iterations", iterations);
        uiModel.addAttribute("projectId", project.getId());
    }
	 void populateEditForm(Model uiModel, Iteration iteration) {
	    }
	public IterationRepository getIterationRepository() {
		return iterationRepository;
	}

	public void setIterationRepository(IterationRepository iterationRepository) {
		this.iterationRepository = iterationRepository;
	}
}
