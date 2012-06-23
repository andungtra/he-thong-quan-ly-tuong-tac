package org.hcmus.tis.util;

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
import org.hcmus.tis.repository.AccountRepository;
import org.hcmus.tis.repository.MemberInformationRepository;
import org.hcmus.tis.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomAuthorizingRealm extends AuthorizingRealm {
	@Autowired
	AuthorityRepository authorizingRepository;

	public AuthorityRepository getAccountRepository() {
		return authorizingRepository;
	}

	public void setAccountRepository(AuthorityRepository accountRepository) {
		this.authorizingRepository = accountRepository;
	}

	public CustomAuthorizingRealm() {
		super(new HashedCredentialsMatcher("MD5"));
	}

	@Override
	public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		try {
			String email = (String) arg0.getPrimaryPrincipal();
			Account account = authorizingRepository.getByEmail(email);
			if (account != null) {
				SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
				info.setRoles(new HashSet<String>());
				info.addStringPermission("account:*:"
						+ account.getId().toString());
				info.getRoles().add(account.getRole().getName());
				for (Permission permission : account.getRole().getPermissions()) {
					info.addStringPermission(permission.getRefName());

				}
				if (SecurityUtils.getSubject() != null
						&& SecurityUtils.getSubject().getSession() != null) {
					Long projectId = (Long) SecurityUtils.getSubject()
							.getSession().getAttribute("projectid");
					if (projectId != null) {
						Project project = authorizingRepository
								.findProject(projectId);
						if (authorizingRepository.countByAccountAndProjectAndDeleted(account, project, false) > 0) {
							MemberInformation member = authorizingRepository
									.findByAccountAndProjectAndDeleted(account,
											project, false);
							info.getRoles().add(
									member.getMemberRole().getRefName());
							for (Permission permission : member.getMemberRole()
									.getPermissions()) {
								info.addStringPermission(permission
										.getRefName());
							}
						}
					}

				}

				return info;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0)
			throws AuthenticationException {
		try {
			if (arg0 instanceof UsernamePasswordToken) {
				UsernamePasswordToken userNamePasswordToken = (UsernamePasswordToken) arg0;
				Account account = authorizingRepository
						.getByEmail(userNamePasswordToken.getUsername());
				return new SimpleAuthenticationInfo(account.getEmail(),
						account.getPassword(), this.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

}
