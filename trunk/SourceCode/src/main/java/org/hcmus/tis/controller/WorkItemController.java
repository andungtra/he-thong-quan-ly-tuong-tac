package org.hcmus.tis.controller;

import org.hcmus.tis.model.WorkItem;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/workitems")
@Controller
@RooWebScaffold(path = "workitems", formBackingObject = WorkItem.class)
public class WorkItemController {
}
