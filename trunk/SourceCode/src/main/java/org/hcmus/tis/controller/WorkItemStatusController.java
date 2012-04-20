package org.hcmus.tis.controller;

import org.hcmus.tis.model.WorkItemStatus;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/workitemstatuses")
@Controller
@RooWebScaffold(path = "workitemstatuses", formBackingObject = WorkItemStatus.class)
public class WorkItemStatusController {
}
