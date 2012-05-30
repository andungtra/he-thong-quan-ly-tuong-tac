package org.hcmus.tis.util;

import javax.annotation.PostConstruct;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Permission;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.WorkItemStatus;

public class InitDatabaseBean {
	@PostConstruct
	public void init() {
		String statusNames[] = { "New", "In Process", "Resolved", "Closed",
				"Rejected" };
		if (WorkItemStatus.countWorkItemStatuses() == 0) {
			for (String name : statusNames) {
				WorkItemStatus status = new WorkItemStatus();
				status.setName(name);
				status.persist();
			}
		}
		String priorityNames[] = { "High", "Medium", "Low", "Urgent",
				"Immediate" };
		if (Priority.countPrioritys() == 0) {
			for (String name : priorityNames) {
				Priority priority = new Priority();
				priority.setName(name);
				priority.persist();
			}
		}
		String memberRoleNames[] = { "Project Manager", "Project Member" };
		String memberRoleRefName[] = { "projectmanager", "projectmember" };
		if (MemberRole.countMemberRoles() == 0) {
			for (int index = 0; index < memberRoleNames.length; ++index) {
				MemberRole role = new MemberRole();
				role.setName(memberRoleNames[index]);
				role.setRefName(memberRoleRefName[index]);
				role.persist();
			}
		}
		if (Permission.countPermissions() == 0) {
			String permissionNames[] = { "project:update", "project:create",
					"project:read", "project:list", "workitem:*" };
			for (String permissionName : permissionNames) {
				Permission permission = new Permission();
				permission.setRefName(permissionName);
				permission.persist();
			}
		}
		if (Account.countAccounts() == 0) {
			Account adminAccount = new Account();
			adminAccount.setEmail("admin@yahoo.com");
			adminAccount.setFirstName("first name");
			adminAccount.setLastName("last name");
			adminAccount.setIsAdmin(true);
			adminAccount.setIsEnable(true);
			adminAccount.setStatus(AccountStatus.ACTIVE);
			adminAccount.setPassword("827ccb0eea8a706c4c34a16891f84e7b");
			adminAccount.persist();
		}
	}

}
