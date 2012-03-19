package org.hcmus.tis.controller;

import org.hcmus.tis.model.Project;
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
	 @RequestMapping(params = "find=ByNameLike", method = RequestMethod.GET)
	    public String findProjectsByNameLike(@RequestParam("name") String name, Model uiModel) {
		 if(!name.isEmpty())
		 {
	     uiModel.addAttribute("projects", Project.findProjectsByNameLike(name).getResultList());
		 }else{
			 uiModel.addAttribute("projects", Project.findAllProjects());
		 }
	     return "projects/list";
	 }

}
