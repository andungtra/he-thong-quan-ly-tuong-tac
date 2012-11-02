package org.hcmus.tis.service;

import org.apache.shiro.SecurityUtils;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
	@Autowired
	AccountRepository accountRepository;
	public Account getCurrentAccount(){
		String userName = (String)SecurityUtils.getSubject().getPrincipal();
		Account account = accountRepository.findByEmail(userName);
		return account;
	}

}
