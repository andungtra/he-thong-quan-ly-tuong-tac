package org.hcmus.tis.controller;

import org.hcmus.tis.model.ProjectProcess;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/projectprocesses")
@Controller
@RooWebScaffold(path = "projectprocesses", formBackingObject = ProjectProcess.class)
public class ProjectProcessController {

    @RequestMapping(value = "/ID/{id}", produces = "text/html")
    public String showProcess(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("projectprocess", ProjectProcess.findProjectProcess(id));
        uiModel.addAttribute("itemId", id);
        return "projectprocesses/show";
    }
}
