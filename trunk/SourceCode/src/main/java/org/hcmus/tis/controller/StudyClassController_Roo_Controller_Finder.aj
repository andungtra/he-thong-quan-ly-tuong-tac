// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.controller;

import org.hcmus.tis.controller.StudyClassController;
import org.hcmus.tis.model.StudyClass;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect StudyClassController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByNameLike", "form" }, method = RequestMethod.GET)
    public String StudyClassController.findStudyClassesByNameLikeForm(Model uiModel) {
        return "studyclasses/findStudyClassesByNameLike";
    }
    
    @RequestMapping(params = "find=ByNameLike", method = RequestMethod.GET)
    public String StudyClassController.findStudyClassesByNameLike(@RequestParam("name") String name, Model uiModel) {
        uiModel.addAttribute("studyclasses", StudyClass.findStudyClassesByNameLike(name).getResultList());
        return "studyclasses/list";
    }
    
}
