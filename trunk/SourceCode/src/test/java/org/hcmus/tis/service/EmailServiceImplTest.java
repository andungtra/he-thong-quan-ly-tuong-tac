package org.hcmus.tis.service;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.hcmus.tis.service.EmailService.SendMailException;
import org.hcmus.tis.util.VelocityMimeMessagePreparator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;
@RunWith(PowerMockRunner.class)
@PrepareForTest(VelocityEngineUtils.class)
public class EmailServiceImplTest {

	@Test
	public void testSendEmail() throws SendMailException {
		EmailServiceImpl aut = new EmailServiceImpl();
		String destEmail = "destEmail";
		String templateFile = "fileName";
		Map model = Mockito.mock(Map.class);
		
		VelocityEngine mockEngine = Mockito.mock(VelocityEngine.class);
		JavaMailSender mockMailSender  = Mockito.mock(JavaMailSender.class);
		aut.setMailSender(mockMailSender);
		aut.setVelocityEngine(mockEngine);
		
		PowerMockito.mockStatic(VelocityEngineUtils.class);
		PowerMockito.when(VelocityEngineUtils.mergeTemplateIntoString(Mockito.any(VelocityEngine.class),  Mockito.anyString(), Mockito.any(Map.class))).thenReturn("email");
		
		aut.sendEmail(destEmail, templateFile, model, "subject");
		Mockito.verify(mockMailSender).send(Mockito.any(MimeMessagePreparator.class));
	}
	
	

}

