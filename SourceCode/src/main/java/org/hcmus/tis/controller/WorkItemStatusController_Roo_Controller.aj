// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.controller;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.hcmus.tis.controller.WorkItemStatusController;
import org.hcmus.tis.model.WorkItemStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect WorkItemStatusController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String WorkItemStatusController.create(@Valid WorkItemStatus workItemStatus, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, workItemStatus);
            return "workitemstatuses/create";
        }
        uiModel.asMap().clear();
        workItemStatus.persist();
        return "redirect:/workitemstatuses/" + encodeUrlPathSegment(workItemStatus.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String WorkItemStatusController.createForm(Model uiModel) {
        populateEditForm(uiModel, new WorkItemStatus());
        return "workitemstatuses/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String WorkItemStatusController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("workitemstatus", WorkItemStatus.findWorkItemStatus(id));
        uiModel.addAttribute("itemId", id);
        return "workitemstatuses/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String WorkItemStatusController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("workitemstatuses", WorkItemStatus.findWorkItemStatusEntries(firstResult, sizeNo));
            float nrOfPages = (float) WorkItemStatus.countWorkItemStatuses() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("workitemstatuses", WorkItemStatus.findAllWorkItemStatuses());
        }
        return "workitemstatuses/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String WorkItemStatusController.update(@Valid WorkItemStatus workItemStatus, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, workItemStatus);
            return "workitemstatuses/update";
        }
        uiModel.asMap().clear();
        workItemStatus.merge();
        return "redirect:/workitemstatuses/" + encodeUrlPathSegment(workItemStatus.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String WorkItemStatusController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, WorkItemStatus.findWorkItemStatus(id));
        return "workitemstatuses/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String WorkItemStatusController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        WorkItemStatus workItemStatus = WorkItemStatus.findWorkItemStatus(id);
        workItemStatus.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/workitemstatuses";
    }
    
    void WorkItemStatusController.populateEditForm(Model uiModel, WorkItemStatus workItemStatus) {
        uiModel.addAttribute("workItemStatus", workItemStatus);
    }
    
    String WorkItemStatusController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
