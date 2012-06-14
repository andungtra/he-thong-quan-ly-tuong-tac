package org.hcmus.tis.controller;

import java.util.List;

import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.repository.WorkItemStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/projects/workitemstatuses")
@Controller
@RooWebScaffold(path = "workitemstatuses", formBackingObject = WorkItemStatus.class)
public class WorkItemStatusController {
	@RequestMapping(value="/listJSON")
	@ResponseBody
	public List<WorkItemStatus> listJSON(){
		return workItemStatusRepository.findAll();
	}
}
