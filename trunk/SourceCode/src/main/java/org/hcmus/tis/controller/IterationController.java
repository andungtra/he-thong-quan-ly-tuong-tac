package org.hcmus.tis.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/iterations")
@Controller
@RooWebScaffold(path = "iterations", formBackingObject = Iteration.class)
public class IterationController {
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(@RequestParam("parentId") Long parentId, Model uiModel) {
    	WorkItemContainer container = WorkItemContainer.findWorkItemContainer(parentId);
    	Iteration iteration = new Iteration();
    	iteration.setParentContainer(container);
        populateEditForm(uiModel, iteration);
        return "iterations/create";
    }
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Iteration iteration, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, iteration);
            return "iterations/create";
        }
        uiModel.asMap().clear();
        iterationService.saveIteration(iteration);
        return "redirect:/projects/" + encodeUrlPathSegment(iteration.getParentContainer().getId().toString(), httpServletRequest) + "/roadmap";
    }
    void populateEditForm(Model uiModel, Iteration iteration) {
        uiModel.addAttribute("iteration", iteration);
    }
}
