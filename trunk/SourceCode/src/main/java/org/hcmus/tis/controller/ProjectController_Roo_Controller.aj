// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hcmus.tis.controller.ProjectController;
import org.hcmus.tis.model.Project;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect ProjectController_Roo_Controller {
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ProjectController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Project());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (projectProcessService.countAllProjectProcesses() == 0) {
            dependencies.add(new String[] { "projectprocess", "projectprocesses" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "projects/create";
    }
    
    @RequestMapping(produces = "text/html")
    public String ProjectController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("projects", Project.findProjectEntries(firstResult, sizeNo));
            float nrOfPages = (float) Project.countProjects() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("projects", Project.findAllProjects());
        }
        return "projects/list";
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ProjectController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Project.findProject(id));
        return "projects/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ProjectController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Project project = Project.findProject(id);
        project.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/projects";
    }
    
    String ProjectController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
