package org.hcmus.tis.service;

import java.util.List;

import org.hcmus.tis.model.Account;

public interface AccountDAO {
    public Account getNguoiDungByUserName(String userName);
    public Account getNguoiDungByEmail(String email);  
    public List<Account> getNguoiDungs(Account nguoidung);
}