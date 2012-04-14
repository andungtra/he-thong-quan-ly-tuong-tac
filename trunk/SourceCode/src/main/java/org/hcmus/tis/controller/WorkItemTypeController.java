package org.hcmus.tis.controller;

import org.hcmus.tis.model.WorkItemType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/workitemtypes")
@Controller
@RooWebScaffold(path = "workitemtypes", formBackingObject = WorkItemType.class)
public class WorkItemTypeController {
}
