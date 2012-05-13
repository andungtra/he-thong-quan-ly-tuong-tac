package org.hcmus.tis.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.WorkItemStatus;

/**
 * Servlet implementation class StartupServlet
 */
public class StartupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartupServlet() {
        super();
        String statusNames[] = {"New", "In Process", "Resolved", "Closed", "Rejected"};
        if(WorkItemStatus.countWorkItemStatuses() == 0){
        	for(String name : statusNames){
        		WorkItemStatus status = new WorkItemStatus();
        		status.setName(name);
        		status.persist();
        	}
        }
        String priorityNames[] = {"High", "Medium", "Low", "Urgent", "Immediate"};
        if(Priority.countPrioritys() == 0){
        	for(String name : priorityNames){
        		Priority priority = new Priority();
        		priority.setName(name);
        		priority.persist();
        	}
        }
        String memberRoleNames[] = {"Project Manager", "Project Member"};
        if(MemberRole.countMemberRoles() == 0){
        	for(String name : memberRoleNames){
        		MemberRole role = new MemberRole();
        		role.setName(name);
        		role.persist();
        	}
        }
        if(Account.countAccounts() == 0){
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
