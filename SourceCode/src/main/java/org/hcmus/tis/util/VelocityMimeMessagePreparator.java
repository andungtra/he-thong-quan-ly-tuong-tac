package org.hcmus.tis.util;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class VelocityMimeMessagePreparator implements MimeMessagePreparator {
	private String destEmail;
	private String templateFile;
	private Map<?, ?> model;
	private String subject;
	private VelocityEngine velocityEngine;
	public VelocityMimeMessagePreparator(String destEmail, String templateFile, Map<?, ?> model, String subject){
		this.setDestEmail(destEmail);
		this.setTemplateFile(templateFile);
		this.setModel(model);
		this.subject = subject;
	}
	@Override
	public void prepare(MimeMessage mineMessage) throws Exception {
		// TODO Auto-generated method stub
		MimeMessageHelper message = new MimeMessageHelper(mineMessage);
		message.setTo(getDestEmail());
		message.setSubject(subject);
		message.setFrom("webmaster@csonth.gov.uk"); // could be
													// parameterized...
		String text = VelocityEngineUtils.mergeTemplateIntoString(
				getVelocityEngine(), getTemplateFile(),
				getModel());
		message.setText(text, true);
	}
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	public String getTemplateFile() {
		return templateFile;
	}
	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}
	public String getDestEmail() {
		return destEmail;
	}
	public void setDestEmail(String destEmail) {
		this.destEmail = destEmail;
	}
	public Map<?, ?> getModel() {
		return model;
	}
	public void setModel(Map<?, ?> model) {
		this.model = model;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

}
