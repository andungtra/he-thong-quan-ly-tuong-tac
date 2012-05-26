import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.util.WebUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class WebAuthorizationAspect {

    @Before("@target(org.springframework.stereotype.Controller) && @annotation(requiresRoles)")
    public void assertRolesAuthorized(JoinPoint jp, RequiresRoles requiresRoles) {
        SecurityUtils.getSubject().checkRoles(requiresRoles.value());
    }
    @Before("@target(org.springframework.stereotype.Controller) && @annotation(requiresPermissions)")
    public void assertPermissiontAuthorizled(JoinPoint jp, RequiresPermissions requiresPermissions) {
    	ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        try{
        	  SecurityUtils.getSubject().checkPermissions(requiresPermissions.value());
        }catch (AuthorizationException e) {
			WebUtils.saveRequest(request);
			throw e;
		}
      
    }
}