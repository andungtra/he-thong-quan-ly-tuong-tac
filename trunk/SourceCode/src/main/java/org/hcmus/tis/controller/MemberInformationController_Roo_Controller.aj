// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.controller;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.hcmus.tis.controller.MemberInformationController;
import org.hcmus.tis.repository.MemberInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect MemberInformationController_Roo_Controller {

    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String MemberInformationController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("memberinformation", memberInformationRepository.findOne(id));
        uiModel.addAttribute("itemId", id);
        return "memberinformations/show";
    }
      
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String MemberInformationController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, memberInformationRepository.findOne(id));
        return "memberinformations/update";
    }
    
    String MemberInformationController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
