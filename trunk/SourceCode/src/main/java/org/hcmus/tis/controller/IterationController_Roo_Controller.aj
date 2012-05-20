// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.controller;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.hcmus.tis.controller.IterationController;
import org.hcmus.tis.model.Iteration;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.service.IterationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect IterationController_Roo_Controller {
    
    @Autowired
    IterationService IterationController.iterationService;
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String IterationController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("iteration", iterationService.findIteration(id));
        uiModel.addAttribute("itemId", id);
        return "iterations/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String IterationController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("iterations", iterationService.findIterationEntries(firstResult, sizeNo));
            float nrOfPages = (float) iterationService.countAllIterations() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("iterations", iterationService.findAllIterations());
        }
        return "iterations/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String IterationController.update(@Valid Iteration iteration, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, iteration);
            return "iterations/update";
        }
        uiModel.asMap().clear();
        iterationService.updateIteration(iteration);
        return "redirect:/iterations/" + encodeUrlPathSegment(iteration.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String IterationController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, iterationService.findIteration(id));
        return "iterations/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String IterationController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Iteration iteration = iterationService.findIteration(id);
        iterationService.deleteIteration(iteration);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/iterations";
    }
    
    String IterationController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
