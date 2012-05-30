package org.hcmus.tis.service;

import java.util.HashSet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Permission;
import org.hcmus.tis.model.Project;

public class CustomAuthorizingRealm extends AuthorizingRealm {
	public CustomAuthorizingRealm() {
		super(new HashedCredentialsMatcher("MD5"));
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		try {
			String email = (String) arg0.getPrimaryPrincipal();
			Account account = Account.findAccountsByEmailEquals(email)
					.getSingleResult();
			if (account != null) {
				SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
				info.setRoles(new HashSet<String>());
				info.addStringPermission("account:*:"
						+ account.getId().toString());
				if (account.getIsAdmin()) {
					info.getRoles().add("administrator");
					info.addStringPermission("*:*");
					return info;
				}
				if (SecurityUtils.getSubject() != null
						&& SecurityUtils.getSubject().getSession() != null) {
					Long projectId = (Long) SecurityUtils.getSubject()
							.getSession().getAttribute("projectid");
					Project project = Project.findProject(projectId);
					if (project != null) {
						MemberInformation member = MemberInformation
								.findMemberInformationsByAccountAndProject(
										account, project).getSingleResult();
						info.getRoles()
								.add(member.getMemberRole().getRefName());
						for (Permission permission : member.getMemberRole()
								.getPermissions()) {
							info.addStringPermission(permission.getRefName());
						}
					}

				}

				return info;
			}
		} catch (Exception ex) {

		}
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken arg0) throws AuthenticationException {
		try{
		if (arg0 instanceof UsernamePasswordToken) {
			UsernamePasswordToken userNamePasswordToken = (UsernamePasswordToken) arg0;
			Account account = Account.findAccountsByEmailEquals(
					userNamePasswordToken.getUsername()).getSingleResult();
			return new SimpleAuthenticationInfo(account.getEmail(),
					account.getPassword(), this.getName());
		}
		}catch (Exception e) {
		}
		return null;
	}

	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

}
