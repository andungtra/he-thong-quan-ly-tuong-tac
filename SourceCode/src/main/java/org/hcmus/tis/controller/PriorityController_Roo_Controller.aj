// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.controller;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.hcmus.tis.controller.PriorityController;
import org.hcmus.tis.model.Priority;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect PriorityController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String PriorityController.create(@Valid Priority priority, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, priority);
            return "prioritys/create";
        }
        uiModel.asMap().clear();
        priority.persist();
        return "redirect:/prioritys/" + encodeUrlPathSegment(priority.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String PriorityController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Priority());
        return "prioritys/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String PriorityController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("priority", Priority.findPriority(id));
        uiModel.addAttribute("itemId", id);
        return "prioritys/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String PriorityController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("prioritys", Priority.findPriorityEntries(firstResult, sizeNo));
            float nrOfPages = (float) Priority.countPrioritys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("prioritys", Priority.findAllPrioritys());
        }
        return "prioritys/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String PriorityController.update(@Valid Priority priority, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, priority);
            return "prioritys/update";
        }
        uiModel.asMap().clear();
        priority.merge();
        return "redirect:/prioritys/" + encodeUrlPathSegment(priority.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String PriorityController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Priority.findPriority(id));
        return "prioritys/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String PriorityController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Priority priority = Priority.findPriority(id);
        priority.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/prioritys";
    }
    
    void PriorityController.populateEditForm(Model uiModel, Priority priority) {
        uiModel.addAttribute("priority", priority);
    }
    
    String PriorityController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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