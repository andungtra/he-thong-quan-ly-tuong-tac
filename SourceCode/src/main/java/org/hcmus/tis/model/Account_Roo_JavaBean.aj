// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import org.hcmus.tis.model.Account;

privileged aspect Account_Roo_JavaBean {
    
    public String Account.getFirstName() {
        return this.firstName;
    }
    
    public void Account.setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String Account.getLastName() {
        return this.lastName;
    }
    
    public void Account.setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String Account.getEmail() {
        return this.email;
    }
    
    public void Account.setEmail(String email) {
        this.email = email;
    }
    
    public String Account.getPassword() {
        return this.password;
    }
    
    public void Account.setPassword(String password) {
        this.password = password;
    }
    
    public Boolean Account.getIsAdmin() {
        return this.isAdmin;
    }
    
    public void Account.setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public Boolean Account.getIsEnable() {
        return this.isEnable;
    }
    
    public void Account.setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }
    
}
