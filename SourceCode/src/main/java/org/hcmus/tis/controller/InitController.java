package org.hcmus.tis.controller;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.model.ApplicationRole;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Permission;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.model.WorkItemType;
import org.hcmus.tis.repository.AccountRepository;
import org.hcmus.tis.repository.ApplicationRoleRepository;
import org.hcmus.tis.repository.MemberRoleRepository;
import org.hcmus.tis.repository.PermissionRepository;
import org.hcmus.tis.repository.PriorityRepository;
import org.hcmus.tis.repository.WorkItemStatusRepository;
import org.hcmus.tis.repository.WorkItemTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class InitController {
	@Autowired
	PriorityRepository priorityRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	WorkItemStatusRepository workItemStatusRepository;
	@Autowired
	WorkItemTypeRepository workItemTypeRepository;
	@Autowired
	PermissionRepository permissionRepository;
	@Autowired
	ApplicationRoleRepository appRoleRepository;
	@Autowired
	MemberRoleRepository memberRoleRepository;

	@RequestMapping(value = "/init", method = RequestMethod.GET, produces = "text/html")
	public String init() {
		String statusNames[] = { "New", "In Process", "Resolved", "Closed",
				"Rejected" };
		Boolean closeds[] = { false, false, true, true, true };
		if (workItemStatusRepository.count() == 0) {
			for (int index = 0; index < statusNames.length; ++index) {
				WorkItemStatus status = new WorkItemStatus();
				status.setClosed(closeds[index]);
				status.setName(statusNames[index]);
				workItemStatusRepository.save(status);
			}
		}
		String priorityNames[] = { "High", "Medium", "Low", "Urgent",
				"Immediate" };
		if (priorityRepository.count() == 0) {
			for (String name : priorityNames) {
				Priority priority = new Priority();
				priority.setName(name);
				priorityRepository.save(priority);
			}
		}
		/*String memberRoleNames[] = { "Project Manager", "Project Member" };
		String memberRoleRefName[] = { "projectmanager", "projectmember" };
		if (MemberRole.countMemberRoles() == 0) {
			for (int index = 0; index < memberRoleNames.length; ++index) {
				MemberRole role = new MemberRole();
				role.setName(memberRoleNames[index]);
				role.setRefName(memberRoleRefName[index]);
				role.persist();
			}
		}*/
		// if (Permission.countPermissions() == 0) {
		// String permissionNames[] = { "project:update", "project:create",
		// "project:read", "project:list", "workitem:*" };
		// for (String permissionName : permissionNames) {
		// Permission permission = new Permission();
		// permission.setRefName(permissionName);
		// permission.persist();
		// }
		// }
		ApplicationRole userRole = new ApplicationRole();
		String projectManagerPer[] = {"project:read", "project:list", "project:update", "project:create", "workitem:*"};
		String projectMemberPer[] = {"workitem:*"};
		String adminPer[] = {"project:*", "studyclass:*", "workitem:read", "workitem:list"};
		ApplicationRole admin = new ApplicationRole();
		admin.setName("administrator");
		ApplicationRole user = new ApplicationRole();
		user.setName("user");
		MemberRole projectManager = new MemberRole();
		projectManager.setName("project manager");
		projectManager.setRefName("projectmanager");
		MemberRole projectMember = new MemberRole();
		projectMember.setName("project member");
		projectMember.setRefName("projectmember");
		initAppRolePermission(adminPer, admin);
		appRoleRepository.save(user);
		initMemberRolePermission(projectManagerPer, projectManager);
		initMemberRolePermission(projectMemberPer, projectMember);
		if (accountRepository.count() == 0) {
			Account adminAccount = new Account();
			adminAccount.setEmail("admin@yahoo.com");
			adminAccount.setFirstName("admin");
			adminAccount.setLastName("admin");
			adminAccount.setRole(admin);
			adminAccount.setStatus(AccountStatus.ACTIVE);
			adminAccount.setPassword("827ccb0eea8a706c4c34a16891f84e7b");
			accountRepository.save(adminAccount);
		}
		if (ProjectProcess.countProjectProcesses() == 0) {
			ProjectProcess defaultProcess = new ProjectProcess();
			defaultProcess.setDescription("Des");
			defaultProcess.setIsDeleted(false);
			defaultProcess.setName("Scrum");

			String a = new String(
					"<?xml version=\"1.0\"?>"
							+ "<xProjectProcess identity=\"scrum\" name=\"Scrum\" xmlns=\"http://www.w3schools.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3schools.com ProcessTemplateSchema.xsd\">"
							+ "<xWorkItems>"
							+ "<xWorkItem name=\"Task\" refName=\"task\">"
							+ "<xPreDefinedFields></xPreDefinedFields>"
							+ "<xAddionalFields></xAddionalFields>"
							+ "</xWorkItem>"
							+ "<xWorkItem name=\"User story\" refName=\"userStory\">"
							+ "<xPreDefinedFields></xPreDefinedFields>"
							+ "<xAddionalFields>"
							+ "<xField refName=\"storyPoint\" name=\"Story point\" type=\"digits\">"
							+ "</xField>"
							+ "<xField refName=\"difficulty\" name=\"Difficulty\" type=\"digits\"></xField>"
							+ "</xAddionalFields>"
							+ "</xWorkItem>"
							+ "<xWorkItem name=\"Bug\" refName=\"bug\">"
							+ "<xPreDefinedFields></xPreDefinedFields>"
							+ "<xAddionalFields>"
							+ "<xField name=\"Severity\" type=\"string\" refName=\"severity\">"
							+ "<xChoices>"
							+ "<xChoice>Critical</xChoice>"
							+ "<xChoice>Normal</xChoice>"
							+ "<xChoice>Minor</xChoice>"
							+ "</xChoices>"
							+ "</xField>"
							+ "<xField name=\"Environment\" type=\"string\" refName=\"environment\">"
							+ "<xDefaultValue>My environment</xDefaultValue>"
							+ "</xField>"
							+ "<xField name=\"type\" type=\"number\" refName=\"type\" requied=\"true\"></xField>"
							+ "</xAddionalFields>" + "</xWorkItem>"
							+ "</xWorkItems>"
							+ "<xDescription>Des</xDescription>"
							+ "</xProjectProcess>");
			defaultProcess.setProcessTemplateFile(a.getBytes());
			defaultProcess.setUniqueName("scrum");
			defaultProcess.setVersion(0);
			defaultProcess.persist();

			WorkItemType w1 = new WorkItemType();
			w1.setAdditionalFieldsDefine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<xAdditionalFields xmlns=\"http://www.w3schools.com\"/>");
			w1.setName("Task");
			w1.setRefName("task");
			w1.setVersion(0);
			w1.setProjectProcess(ProjectProcess.findProjectProcess((long) 1));
			workItemTypeRepository.save(w1);

			WorkItemType w2 = new WorkItemType();
			w2.setAdditionalFieldsDefine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<xAdditionalFields xmlns=\"http://www.w3schools.com\"><xField ref=\"storyPoint\"/><xField ref=\"difficulty\"/></xAdditionalFields>");
			w2.setName("User story");
			w2.setRefName("userStory");
			w2.setVersion(0);
			w2.setProjectProcess(ProjectProcess.findProjectProcess((long) 1));
			workItemTypeRepository.save(w2);

			WorkItemType w3 = new WorkItemType();
			w3.setAdditionalFieldsDefine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<xAdditionalFields xmlns=\"http://www.w3schools.com\"><xField ref=\"severity\"/><xField ref=\"environment\"/><xField ref=\"type\"/></xAdditionalFields>");
			w3.setName("Bug");
			w3.setRefName("bug");
			w3.setVersion(0);
			w3.setProjectProcess(ProjectProcess.findProjectProcess((long) 1));
			workItemTypeRepository.save(w3);
		}
		return "login";
	}

	private void initAppRolePermission(String[] permissionString,
			ApplicationRole transientRole) {
		transientRole.setPermissions(new HashSet<Permission>());
		for(String per : permissionString){
			if(permissionRepository.findByRefName(per).size() == 0){
				Permission permission = new Permission();
				permission.setRefName(per);
				permissionRepository.save(permission);
				transientRole.getPermissions().add(permission);
			}
		}
		appRoleRepository.save(transientRole);
	}
	private void initMemberRolePermission(String[] permissionString,
			MemberRole transientRole) {
		transientRole.setPermissions(new HashSet<Permission>());
		for(String per : permissionString){
			if(permissionRepository.findByRefName(per).size() == 0){
				Permission permission = new Permission();
				permission.setRefName(per);
				permissionRepository.save(permission);
				transientRole.getPermissions().add(permission);
			}
		}
		memberRoleRepository.save(transientRole);
	}
}
