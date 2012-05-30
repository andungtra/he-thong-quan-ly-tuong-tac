package org.hcmus.tis.service;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.hcmus.tis.util.VelocityMimeMessagePreparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService  {
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private VelocityEngine velocityEngine;

	@Override
	public void sendEmail(String destEmail, String templateFile, Map model, String subject) throws SendMailException{
		// TODO Auto-generated method stub
		VelocityMimeMessagePreparator preparator = new VelocityMimeMessagePreparator(destEmail, templateFile, model, subject);
		preparator.setVelocityEngine(velocityEngine);
		try
		{
		this.getMailSender().send(preparator);
		}catch(MailSendException ex){
			throw new SendMailException(ex);
		}
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

}
