package org.hcmus.tis.controller;

import org.hcmus.tis.model.Iteration;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/iterations")
@Controller
@RooWebScaffold(path = "iterations", formBackingObject = Iteration.class)
public class IterationController {
}
