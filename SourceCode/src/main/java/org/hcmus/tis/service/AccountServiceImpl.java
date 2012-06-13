package org.hcmus.tis.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountStatus;
import org.hcmus.tis.repository.AccountRepository;
import org.hcmus.tis.service.EmailService.SendMailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
@Service
public class AccountServiceImpl implements AccountService, ServletContextAware  {
	@Autowired
	private EmailService emailService;
	@Autowired
	private AccountRepository accountRepository;
	public AccountRepository getAccountRepository() {
		return accountRepository;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public void createAccount(Account account) throws DuplicateException, SendMailException {
		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();
		account.setPassword(uuidString);
		account.setStatus(AccountStatus.INACTIVE);
		if (accountRepository.countByEmail(account.getEmail()) > 0) {
			throw new DuplicateException();
		}
		accountRepository.save(account);
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
