package org.hcmus.tis.controller;

import org.hcmus.tis.model.MemberInformation;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/memberinformations")
@Controller
@RooWebScaffold(path = "memberinformations", formBackingObject = MemberInformation.class)
public class MemberInformationController {
}
