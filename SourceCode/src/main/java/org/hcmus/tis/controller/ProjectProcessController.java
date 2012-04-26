package org.hcmus.tis.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.xml.XProjectProcess;
import org.hcmus.tis.service.ProjectProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

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
    @RequestMapping(method=RequestMethod.POST, produces = "text/html")
    public String create(Model uiModel, MultipartFile multipartFile, HttpServletRequest httpServletRequest) throws IOException, JAXBException{
    	if(multipartFile == null){
    		return null;
    	}
    	byte[] templateFile = multipartFile.getBytes();
    	InputStream inputStream = multipartFile.getInputStream();
    	JAXBContext context = JAXBContext.newInstance("org.hcmus.tis.model.xml");
/*		Unmarshaller umMarshaller = context.createUnmarshaller();
		StreamSource source = new StreamSource(inputStream);
		JAXBElement<XProjectProcess> element = umMarshaller.unmarshal(source, XProjectProcess.class);
		XProjectProcess xProjectProcess = element.getValue();*/
		
		ProjectProcess projectProcess = projectProcessService.createProjectProcess(templateFile);
		uiModel.addAttribute("projectprocess", projectProcess);
        uiModel.addAttribute("itemId", projectProcess.getId());
        return "redirect:/projectprocesses/" + encodeUrlPathSegment(projectProcess.getId().toString(), httpServletRequest);
    }
}
