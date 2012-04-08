package org.hcmus.tis.service;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.hcmus.tis.service.EmailService.SendMailException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailServiceImplRealTest {
	private JavaMailSenderImpl mailSender;
	VelocityEngine engine ;
	@Before
	public void setUp() throws Exception {
		engine = new VelocityEngine();
		Properties props = new Properties();
		engine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH,
				"./src/test/");
		engine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, false);
		engine.init();

		mailSender = new JavaMailSenderImpl();
		mailSender.setUsername("0812642@gmail.com");
		mailSender.setPassword("abcxyz123");
		mailSender.setHost("smtp.gmail.com");
		mailSender.setDefaultEncoding("utf-8");
		mailSender.setPort(587);
		mailSender.setProtocol("smtp");
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		mailSender.setJavaMailProperties(properties);
	}

	@After
	public void tearDown() throws Exception {
	}
	public void testSendRealMail() throws SendMailException {
		EmailServiceImpl aut = new EmailServiceImpl();
		String destEmail = "abcxyz2357@yahoo.com";
		String templateFile = "account-confirmation.vm";
		String link = "http://www.tis.com/test";
		Map model = new HashMap();
		model.put("link", link);

		aut.setMailSender(mailSender);
		aut.setVelocityEngine(engine);
		aut.sendEmail(destEmail, templateFile, model, "Welcome to TIS");
	}
	
	//@Test(expected=SendMailException.class)
	public void testSendRealMailInvalidEmail() throws SendMailException {
		EmailServiceImpl aut = new EmailServiceImpl();
		String destEmail = "a";
		String templateFile = "account-confirmation.vm";
		String link = "http://www.tis.com/test";
		Map model = new HashMap();
		model.put("link", link);

		aut.setMailSender(mailSender);
		aut.setVelocityEngine(engine);
		aut.sendEmail(destEmail, templateFile, model, "Welcome to TIS");
	}
}
