// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.AccountDataOnDemand;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.MemberInformationDataOnDemand;
import org.hcmus.tis.model.MemberRole;
import org.hcmus.tis.model.MemberRoleDataOnDemand;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect MemberInformationDataOnDemand_Roo_DataOnDemand {
    
    declare @type: MemberInformationDataOnDemand: @Component;
    
    private Random MemberInformationDataOnDemand.rnd = new SecureRandom();
    
    private List<MemberInformation> MemberInformationDataOnDemand.data;
    
    @Autowired
    private AccountDataOnDemand MemberInformationDataOnDemand.accountDataOnDemand;
    
    @Autowired
    private MemberRoleDataOnDemand MemberInformationDataOnDemand.memberRoleDataOnDemand;
    
    @Autowired
    private ProjectDataOnDemand MemberInformationDataOnDemand.projectDataOnDemand;
    
    public MemberInformation MemberInformationDataOnDemand.getNewTransientMemberInformation(int index) {
        MemberInformation obj = new MemberInformation();
        setAccount(obj, index);
        setDeleted(obj, index);
        setMemberRole(obj, index);
        setProject(obj, index);
        return obj;
    }
    
    public void MemberInformationDataOnDemand.setAccount(MemberInformation obj, int index) {
        Account account = accountDataOnDemand.getRandomAccount();
        obj.setAccount(account);
    }
    
    public void MemberInformationDataOnDemand.setDeleted(MemberInformation obj, int index) {
        Boolean deleted = Boolean.TRUE;
        obj.setDeleted(deleted);
    }
    
    public void MemberInformationDataOnDemand.setMemberRole(MemberInformation obj, int index) {
        MemberRole memberRole = memberRoleDataOnDemand.getRandomMemberRole();
        obj.setMemberRole(memberRole);
    }
    
    public void MemberInformationDataOnDemand.setProject(MemberInformation obj, int index) {
        Project project = projectDataOnDemand.getRandomProject();
        obj.setProject(project);
    }
    
    public MemberInformation MemberInformationDataOnDemand.getSpecificMemberInformation(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        MemberInformation obj = data.get(index);
        Long id = obj.getId();
        return MemberInformation.findMemberInformation(id);
    }
    
    public MemberInformation MemberInformationDataOnDemand.getRandomMemberInformation() {
        init();
        MemberInformation obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return MemberInformation.findMemberInformation(id);
    }
    
    public boolean MemberInformationDataOnDemand.modifyMemberInformation(MemberInformation obj) {
        return false;
    }
    
    public void MemberInformationDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = MemberInformation.findMemberInformationEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'MemberInformation' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<MemberInformation>();
        for (int i = 0; i < 10; i++) {
            MemberInformation obj = getNewTransientMemberInformation(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
