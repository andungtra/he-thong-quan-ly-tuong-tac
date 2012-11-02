package org.hcmus.tis.service;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemActivity;
import org.hcmus.tis.model.WorkItemCreation;
import org.hcmus.tis.model.WorkItemUpdation;
import org.hcmus.tis.model.WorkItemVersion;
import org.hcmus.tis.repository.AccountRepository;
import org.hcmus.tis.repository.WorkItemActivityRepository;
import org.hcmus.tis.repository.WorkItemRepository;
import org.hcmus.tis.repository.WorkItemVersionRepository;
import org.hcmus.tis.util.AsigneeNotificationTask;
import org.hcmus.tis.util.UpdateWorkitemNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;


public class WorkItemServiceImpl implements WorkItemService {
	@Autowired
	private EmailService emailService;
	@Autowired
	private TaskExecutor taskExecutor;
	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	@Autowired
	private SecurityService securityService;
	@Autowired
	private WorkItemVersionRepository workItemVersionRepository;
	public WorkItemVersionRepository getWorkItemVersionRepository() {
		return workItemVersionRepository;
	}
	public void setWorkItemVersionRepository(
			WorkItemVersionRepository workItemVersionRepository) {
		this.workItemVersionRepository = workItemVersionRepository;
	}
	@Autowired
	private WorkItemActivityRepository workItemActivityRepository;
	@Autowired
	private AccountRepository accountRepository;
	public void save(WorkItem workItem){
		WorkItemActivity workItemActivity = null;
		if(workItem.getId() != null){
			workItemActivity = new WorkItemUpdation();
			WorkItemUpdation updation = (WorkItemUpdation)workItemActivity;
			updation.setPreVersion(workItem.getHeadVersion());
			
		}else{
			workItemActivity = new WorkItemCreation();
		}
		workItemRepository.save(workItem);
		WorkItemVersion currentVersion = workItem.getCurrentVersion();
		workItemVersionRepository.save(currentVersion);
		workItem.setHeadVersion(currentVersion);
		workItemRepository.save(workItem);
		Account account = securityService.getCurrentAccount();
		workItemActivity.setAccount(account);
		workItemActivity.setDate(new Date());
		workItemActivity.setProject(workItem.getWorkItemContainer().getParentProjectOrMyself());
		workItemActivity.setResultVersion(currentVersion);
		workItemActivityRepository.save(workItemActivity);
		if(workItemActivity instanceof WorkItemCreation){
			if (workItem.getAsignee() != null) {
				taskExecutor.execute(new AsigneeNotificationTask(workItem,
						emailService));
			}
		}else{
			
		}
		
	}
	public WorkItemActivityRepository getWorkItemActivityRepository() {
		return workItemActivityRepository;
	}
	public void setWorkItemActivityRepository(
			WorkItemActivityRepository workItemActivityRepository) {
		this.workItemActivityRepository = workItemActivityRepository;
	}
	public SecurityService getSecurityService() {
		return securityService;
	}
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	public WorkItemRepository getWorkItemRepository(){
		return workItemRepository;
	}
	public void setWorkItemRepository(WorkItemRepository workItemRepository){
		this.workItemRepository = workItemRepository;
	}
}
