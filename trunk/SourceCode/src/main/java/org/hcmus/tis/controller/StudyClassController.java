package org.hcmus.tis.controller;

import org.hcmus.tis.model.StudyClass;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/studyclasses")
@Controller
@RooWebScaffold(path = "studyclasses", formBackingObject = StudyClass.class)
public class StudyClassController {
}
