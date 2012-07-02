package org.hcmus.tis.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Servlet Filter implementation class ProjectPermissionFilter
 */
public class ProjectPermissionFilter implements Filter {
	@Autowired
	CustomAuthorizingRealm realm;

	/**
	 * Default constructor.
	 */
	public ProjectPermissionFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		if (request instanceof HttpServletRequest) {
			Pattern pattern = Pattern.compile("/projects/\\d+");
			String url = ((HttpServletRequest) request).getRequestURI();
			Matcher matcher = pattern.matcher(url);
			if (matcher.find()) {
				String matchedString = matcher.group();
				String projectIdString = matchedString.substring(matchedString.indexOf("/", 2) + 1);
				Long projectId = Long.decode(projectIdString);
				Subject subject = SecurityUtils.getSubject();
				if(subject != null){
					Long preProjectId = (Long)subject.getSession().getAttribute("projectid");
					if(preProjectId == null || preProjectId != projectId){
						subject.getSession().setAttribute("projectid", projectId);
						realm.clearCachedAuthorizationInfo(subject.getPrincipals());
					}
				}
			}else{
				Subject subject = SecurityUtils.getSubject();
				if(subject != null && subject.getSession().getAttribute("projectid") != null){
					subject.getSession().removeAttribute("projectid");
					realm.clearCachedAuthorizationInfo(subject.getPrincipals());
				}
			}
			// pass the request along the filter chain
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
