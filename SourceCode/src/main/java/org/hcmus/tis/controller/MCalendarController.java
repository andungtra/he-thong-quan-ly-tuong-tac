package org.hcmus.tis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/calendars")
@Controller
public class MCalendarController {
	@RequestMapping(value = "test", produces="text/html" )
	public String test(){
		return "calendar/show";
	}
}
