// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.Collection;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.Permission;

privileged aspect Permission_Roo_JavaBean {
    
    public String Permission.getRefName() {
        return this.refName;
    }
    
    public void Permission.setRefName(String refName) {
        this.refName = refName;
    }
    
    public Collection<MemberRole> Permission.getMemberRoles() {
        return this.memberRoles;
    }
    
    public void Permission.setMemberRoles(Collection<MemberRole> memberRoles) {
        this.memberRoles = memberRoles;
    }
    
}
