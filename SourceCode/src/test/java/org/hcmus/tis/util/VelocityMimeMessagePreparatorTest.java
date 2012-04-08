package org.hcmus.tis.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.mockito.Mockito;

public class VelocityMimeMessagePreparatorTest {

	@Test
	public void testPrepare() throws Exception {
		String email = "a@yahoo.com";
		String templateFile = "account-confirmation.vm";
		Map model  = new HashMap();
		VelocityEngine engine = new VelocityEngine();
		VelocityMimeMessagePreparator aut = new VelocityMimeMessagePreparator(email, templateFile, model, "subject");
		Properties props = new Properties(); 
		engine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, "./src/test/");
		engine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, false);
		engine.init();
		aut.setVelocityEngine(engine);
		Properties properties = new Properties();
		Session session = Session.getDefaultInstance(properties);
		MimeMessage mimeMessage = new MimeMessage(session);
		
		aut.prepare(mimeMessage);
	}

}
