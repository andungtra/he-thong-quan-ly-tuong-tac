package org.hcmus.tis.service;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.service.EmailService.SendMailException;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { org.hcmus.tis.model.Account.class })
public interface AccountService {
    public void createAccount(Account account) throws DuplicateException, SendMailException;


}
