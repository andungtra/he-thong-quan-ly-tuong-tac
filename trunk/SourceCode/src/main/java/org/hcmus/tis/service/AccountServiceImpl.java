package org.hcmus.tis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.TypedQuery;
import javax.servlet.ServletContext;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.service.EmailService.SendMailException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.context.ServletContextAware;

public class AccountServiceImpl implements AccountService, ServletContextAware  {
	@Autowired
	private EmailService emailService;

	public void createAccount(Account account) throws DuplicateException, SendMailException {
		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();
		account.setPassword(uuidString);
		account.setStatus(AccountStatus.INACTIVE);
		if (Account.findAccountsByEmailEquals(account.getEmail())
				.getResultList().size() > 0) {
			throw new DuplicateException();
		}
		account.persist();
		Map model = new HashMap();
		model.put("link", Setting.getServerURL() +  servletContext.getContextPath() + "/accounts/" + account.getId().toString() + "?activeKey=" + account.getPassword());
		emailService.sendEmail(account.getEmail(), "account-confirmation.vm",
				model, "welcome to TIS");
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	private ServletContext servletContext;
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		
	}
}
