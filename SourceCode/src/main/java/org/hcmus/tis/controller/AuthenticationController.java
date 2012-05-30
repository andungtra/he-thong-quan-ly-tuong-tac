package org.hcmus.tis.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController {
	//for development mode
	volatile private static boolean completeInit = false;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(HttpServletRequest request, HttpServletRequest post,
			String username, String password, HttpServletResponse response)
			throws IOException {
		//for development mode only
		if (!completeInit) {
			MemberRole projectManager = MemberRole.findMemberRole((long) 1);
			Permission permission = Permission.findPermission((long) 1);
			projectManager.getPermissions().add(permission);
			permission = Permission.findPermission((long) 3);
			projectManager.getPermissions().add(permission);
			permission = Permission.findPermission((long) 5);
			projectManager.getPermissions().add(permission);
			projectManager.flush();

			MemberRole projectMember = MemberRole.findMemberRole((long) 2);
			permission = Permission.findPermission((long) 3);
			projectMember.getPermissions().add(permission);
			permission = Permission.findPermission((long) 5);
			projectMember.getPermissions().add(permission);
			projectMember.flush();
			completeInit = true;
		}
		//finish development mode
		UsernamePasswordToken token = new UsernamePasswordToken(username,
				password);
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
		WebUtils.redirectToSavedRequest(request, response, "/accounts/home");
		SecurityUtils
				.getSubject()
				.getSession()
				.setAttribute(
						"account",
						Account.findAccountsByEmailEquals(username)
								.getSingleResult());
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = "text/html")
	public String loginForm() {
		return "login";
	}
}
