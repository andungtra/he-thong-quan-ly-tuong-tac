// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.controller;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.hcmus.tis.controller.WorkItemController;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.model.WorkItemType;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect WorkItemController_Roo_Controller {
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String WorkItemController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("workitem", WorkItem.findWorkItem(id));
        uiModel.addAttribute("itemId", id);
        return "workitems/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String WorkItemController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("workitems", WorkItem.findWorkItemEntries(firstResult, sizeNo));
            float nrOfPages = (float) WorkItem.countWorkItems() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("workitems", WorkItem.findAllWorkItems());
        }
        addDateTimeFormatPatterns(uiModel);
        return "workitems/list";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String WorkItemController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        WorkItem workItem = WorkItem.findWorkItem(id);
        workItem.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/workitems";
    }
    
    void WorkItemController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("workItem_datecreated_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("workItem_duedate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    void WorkItemController.populateEditForm(Model uiModel, WorkItem workItem) {
        uiModel.addAttribute("workItem", workItem);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("memberinformations", MemberInformation.findAllMemberInformations());
        uiModel.addAttribute("prioritys", Priority.findAllPrioritys());
        uiModel.addAttribute("workitemcontainers", WorkItemContainer.findAllWorkItemContainers());
        uiModel.addAttribute("workitemstatuses", WorkItemStatus.findAllWorkItemStatuses());
        uiModel.addAttribute("workitemtypes", WorkItemType.findAllWorkItemTypes());
    }
    
    String WorkItemController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
