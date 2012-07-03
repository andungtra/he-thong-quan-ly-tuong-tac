package org.hcmus.tis.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

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
public class UpdateWorkitemNotification extends WorkItemNotification {

	public UpdateWorkitemNotification(WorkItem workItem, EmailService emailService) {
		super(workItem, emailService);

	}
	protected void sendNotify(MemberInformation member) {
		Map model = new HashMap();
		String subject = workItem.getUserLastEdit().getAccount().getFullName() + " updated " + workItem.getWorkItemType().getName() + " " + workItem.getTitle();
		model.put("workitem", getWorkItem());
		model.put("link", Setting.getServerURL() +  servletContext.getContextPath());
		model.put("dueDate", workItem.getDueDate()== null ? "" : workItem.getDueDate());
		try {
			emailService.sendEmail(member.getAccount().getEmail(),
					"workitem-notification.vm", model, subject);
		} catch (SendMailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
