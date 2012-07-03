package org.hcmus.tis.util;

import javax.servlet.ServletContext;

import org.hcmus.tis.model.Comment;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.service.EmailService;
import org.springframework.web.context.ServletContextAware;

public abstract class WorkItemNotification implements Runnable,
		ServletContextAware {
	protected EmailService emailService;
	protected ServletContext servletContext;
	protected WorkItem workItem;

	public WorkItemNotification(WorkItem workItem, EmailService emailService) {
		this.setWorkItem(workItem);
		this.setEmailService(emailService);
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public WorkItem getWorkItem() {
		return workItem;
	}

	public void setWorkItem(WorkItem workItem) {
		this.workItem = workItem;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;

	}

	@Override
	public void run() {
		sendNotify(workItem.getAuthor());
		if (workItem.getAsignee() != null
				&& workItem.getAuthor() != workItem.getAsignee()) {
			sendNotify(workItem.getAsignee());
		}
		if (workItem.getSubcribers() != null) {
			for (MemberInformation member : getWorkItem().getSubcribers()) {
				sendNotify(member);
			}
		}

	};

	protected abstract void sendNotify(MemberInformation member);
}
