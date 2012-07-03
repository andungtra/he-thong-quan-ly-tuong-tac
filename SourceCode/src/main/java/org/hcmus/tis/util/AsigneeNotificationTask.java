package org.hcmus.tis.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.hcmus.tis.model.Comment;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.service.EmailService;
import org.hcmus.tis.service.Setting;
import org.hcmus.tis.service.EmailService.SendMailException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;
@Configurable
public class AsigneeNotificationTask extends WorkItemNotification {

	public AsigneeNotificationTask(WorkItem workItem, 
			EmailService emailService) {
		super(workItem, emailService);
		this.setWorkItem(workItem);
	}

	protected void sendNotify(MemberInformation member) {
		Map model = new HashMap();
		String subject = workItem.getWorkItemType().getName() + " " + workItem.getTitle() + " was asigned to you";
		model.put("workitem", getWorkItem());
		model.put("link", Setting.getServerURL() +  servletContext.getContextPath());
		model.put("dueDate", workItem.getDueDate()== null ? "" : workItem.getDueDate());
		try {
			emailService.sendEmail(member.getAccount().getEmail(),
					"asignee-notification.vm", model, subject);
		} catch (SendMailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
