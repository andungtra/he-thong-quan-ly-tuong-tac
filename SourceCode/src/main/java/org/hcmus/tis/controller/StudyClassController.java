package org.hcmus.tis.controller;

import java.util.List;
import org.hcmus.tis.model.StudyClass;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/studyclasses")
@Controller
@RooWebScaffold(path = "studyclasses", formBackingObject = StudyClass.class)
@RooWebFinder
public class StudyClassController {

    @RequestMapping(params = "find=quickFind", method = RequestMethod.GET)
    public String findStudyClassesQuickly(@RequestParam("query") String query, Model uiModel) {
        if (query.isEmpty()) {
            uiModel.addAttribute("studyclasses", StudyClass.findAllStudyClasses());
        } else {
            uiModel.addAttribute("studyclasses", StudyClass.findStudyClassesByNameLike(query).getResultList());
        }
        uiModel.addAttribute("query", query);
        return "studyclasses/list";
    }

    @RequestMapping(value = "/ID/{id}", produces = "text/html")
    public String showClass(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("studyclass", StudyClass.findStudyClass(id));
        uiModel.addAttribute("itemId", id);
        return "studyclasses/show";
    }
}
