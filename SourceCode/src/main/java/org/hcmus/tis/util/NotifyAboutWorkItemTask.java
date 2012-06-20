package org.hcmus.tis.util;

import java.util.HashMap;
import java.util.Map;

import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.service.EmailService;
import org.hcmus.tis.service.EmailService.SendMailException;

public class NotifyAboutWorkItemTask implements Runnable {
	private EmailService emailService;
	private WorkItem workItem;
	private String action;

	public NotifyAboutWorkItemTask(WorkItem workItem, String action,
			EmailService emailService) {
		this.setWorkItem(workItem);
		this.setAction(action);
		this.setEmailService(emailService);
	}

	@Override
	public void run() {
		sendNotify(workItem.getAuthor());
		if (workItem.getAsignee() != null && workItem.getAuthor() != workItem.getAsignee()) {
			sendNotify(workItem.getAsignee());
		}
		for (MemberInformation member : getWorkItem().getSubcribers()) {
			sendNotify(member);
		}

	}

	private void sendNotify(MemberInformation member) {
		Map model = new HashMap();
		model.put("member", workItem.getUserLastEdit().getAccount().getFirstName() + " "
				+ workItem.getUserLastEdit().getAccount().getLastName());
		model.put("action", getAction());
		model.put("workitem", getWorkItem().getId());
		model.put("time", getWorkItem().getDateLastEdit());
		try {
			emailService.sendEmail(member.getAccount().getEmail(),
					"workitem-notification.vm", model, "");
		} catch (SendMailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
