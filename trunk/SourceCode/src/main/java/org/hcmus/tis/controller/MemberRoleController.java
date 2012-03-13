package org.hcmus.tis.controller;

import org.hcmus.tis.model.MemberRole;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/memberroles")
@Controller
@RooWebScaffold(path = "memberroles", formBackingObject = MemberRole.class)
public class MemberRoleController {
}
