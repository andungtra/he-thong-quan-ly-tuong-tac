package org.hcmus.tis.controller;

import java.util.ArrayList;
import java.util.Collection;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.util.Parameter;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/projects")
@Controller
@RooWebScaffold(path = "projects", formBackingObject = Project.class)
@RooWebFinder
public class ProjectController {

    @RequestMapping(params = "find=quickFind", method = RequestMethod.GET)
    public String findProjectsQuickly(@RequestParam("query") String name, Model uiModel, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size) {
        int sizeNo = size == null ? 10 : size.intValue();
        int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
        long totalResult = 0;
        if (!name.isEmpty()) {
            uiModel.addAttribute("projects", Project.findProjectsByNameLike(name, firstResult, sizeNo).getResultList());
            totalResult = Project.countProjectsByNameLike(name);
        } else {
            uiModel.addAttribute("projects", Project.findProjectEntries(firstResult, sizeNo));
            totalResult = Project.countProjects();
        }
        Collection<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("find", "quickFind"));
        parameters.add(new Parameter("query", name));
        uiModel.addAttribute("parameters", parameters);
        int maxPage = (int) (totalResult / sizeNo);
        if (totalResult % sizeNo != 0) {
            ++maxPage;
        }
        uiModel.addAttribute("maxPages", maxPage);
        uiModel.addAttribute("query", name);
        return "projects/list";
    }

    public String findProjectsByNameLike(String name, Model uiModel) {
        return "projects/list";
    }
}
