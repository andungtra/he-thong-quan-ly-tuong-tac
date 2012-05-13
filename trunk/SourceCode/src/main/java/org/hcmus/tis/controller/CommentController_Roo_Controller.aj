// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.controller;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.hcmus.tis.controller.CommentController;
import org.hcmus.tis.model.Comment;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.WorkItem;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect CommentController_Roo_Controller {
    
    @RequestMapping(params = "form", produces = "text/html")
    public String CommentController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Comment());
        return "comments/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String CommentController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("comment", Comment.findComment(id));
        uiModel.addAttribute("itemId", id);
        return "comments/show";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String CommentController.update(@Valid Comment comment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, comment);
            return "comments/update";
        }
        uiModel.asMap().clear();
        comment.merge();
        return "redirect:/comments/" + encodeUrlPathSegment(comment.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String CommentController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Comment.findComment(id));
        return "comments/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String CommentController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Comment comment = Comment.findComment(id);
        comment.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/comments";
    }
    
    void CommentController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("comment_commentdate_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    void CommentController.populateEditForm(Model uiModel, Comment comment) {
        uiModel.addAttribute("comment", comment);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("memberinformations", MemberInformation.findAllMemberInformations());
        uiModel.addAttribute("workitems", WorkItem.findAllWorkItems());
    }
    
    String CommentController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
