// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Project;

privileged aspect MemberInformation_Roo_JavaBean {
    
    public Account MemberInformation.getAccount() {
        return this.account;
    }
    
    public void MemberInformation.setAccount(Account account) {
        this.account = account;
    }
    
    public Project MemberInformation.getProject() {
        return this.project;
    }
    
    public void MemberInformation.setProject(Project project) {
        this.project = project;
    }
    
    public MemberRole MemberInformation.getMemberRole() {
        return this.memberRole;
    }
    
    public void MemberInformation.setMemberRole(MemberRole memberRole) {
        this.memberRole = memberRole;
    }
    
    public Boolean MemberInformation.getDeleted() {
        return this.deleted;
    }
    
    public void MemberInformation.setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    
}
