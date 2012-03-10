package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


public class HelloWorldController implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		// TODO Auto-generated method stub
		String Mess = "Hello World!";
		 
		  ModelAndView modelAndView = new ModelAndView("hello");
		  modelAndView.addObject("message", Mess);
		 
		  return modelAndView;
	}

}
