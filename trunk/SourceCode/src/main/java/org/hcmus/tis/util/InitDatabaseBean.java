package org.hcmus.tis.util;

import javax.annotation.PostConstruct;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Permission;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.model.WorkItemType;

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
			adminAccount.setFirstName("admin");
			adminAccount.setLastName("admin");
			adminAccount.setIsAdmin(true);
			adminAccount.setStatus(AccountStatus.ACTIVE);
			adminAccount.setPassword("827ccb0eea8a706c4c34a16891f84e7b");
			adminAccount.persist();
		}
		if(ProjectProcess.countProjectProcesses()==0){
			ProjectProcess defaultProcess = new ProjectProcess();
			defaultProcess.setDescription("Des");
			defaultProcess.setIsDeleted(false);
			defaultProcess.setName("Scrum");
			
			
			String a = new String("<?xml version=\"1.0\"?>" +
					"<xProjectProcess identity=\"scrum\" name=\"Scrum\" xmlns=\"http://www.w3schools.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.w3schools.com ProcessTemplateSchema.xsd\">" +
						"<xWorkItems>" +
							"<xWorkItem name=\"Task\" refName=\"task\">" +
								"<xPreDefinedFields></xPreDefinedFields>" +
								"<xAddionalFields></xAddionalFields>" +
							"</xWorkItem>" +
							"<xWorkItem name=\"User story\" refName=\"userStory\">" +
								"<xPreDefinedFields></xPreDefinedFields>" +
									"<xAddionalFields>" +
									"<xField refName=\"storyPoint\" name=\"Story point\" type=\"digits\">" +
									"</xField>" +
									"<xField refName=\"difficulty\" name=\"Difficulty\" type=\"digits\"></xField>" +
								"</xAddionalFields>" +
							"</xWorkItem>" +
							"<xWorkItem name=\"Bug\" refName=\"bug\">" +
								"<xPreDefinedFields></xPreDefinedFields>" +
								"<xAddionalFields>" +
									"<xField name=\"Severity\" type=\"string\" refName=\"severity\">" +
										"<xChoices>" + 
											"<xChoice>Critical</xChoice>" +
											"<xChoice>Normal</xChoice>" +
											"<xChoice>Minor</xChoice>" +
										"</xChoices>" +
									"</xField>" +
									"<xField name=\"Environment\" type=\"string\" refName=\"environment\">" +
										"<xDefaultValue>My environment</xDefaultValue>" +
									"</xField>" +
									"<xField name=\"type\" type=\"number\" refName=\"type\" requied=\"true\"></xField>" +
								"</xAddionalFields>" +
							"</xWorkItem>" +
						"</xWorkItems>" +
						"<xDescription>Des</xDescription>" +
					"</xProjectProcess>");
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
			w1.persist();
			
			WorkItemType w2 = new WorkItemType();
			w2.setAdditionalFieldsDefine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<xAdditionalFields xmlns=\"http://www.w3schools.com\"><xField ref=\"storyPoint\"/><xField ref=\"difficulty\"/></xAdditionalFields>");
			w2.setName("User story");
			w2.setRefName("userStory");
			w2.setVersion(0);
			w2.setProjectProcess(ProjectProcess.findProjectProcess((long) 1));
			w2.persist();
			
			WorkItemType w3 = new WorkItemType();
			w3.setAdditionalFieldsDefine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<xAdditionalFields xmlns=\"http://www.w3schools.com\"><xField ref=\"severity\"/><xField ref=\"environment\"/><xField ref=\"type\"/></xAdditionalFields>");
			w3.setName("Bug");
			w3.setRefName("bug");
			w3.setVersion(0);
			w3.setProjectProcess(ProjectProcess.findProjectProcess((long) 1));
			w3.persist();		
		}
	}
}
