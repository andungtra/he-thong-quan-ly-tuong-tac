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
import org.hcmus.tis.model.Attachment;
import org.hcmus.tis.model.AttachmentDataOnDemand;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect AttachmentDataOnDemand_Roo_DataOnDemand {
    
    declare @type: AttachmentDataOnDemand: @Component;
    
    private Random AttachmentDataOnDemand.rnd = new SecureRandom();
    
    private List<Attachment> AttachmentDataOnDemand.data;
    
    @Autowired
    private WorkItemDataOnDemand AttachmentDataOnDemand.workItemDataOnDemand;
    
    public Attachment AttachmentDataOnDemand.getNewTransientAttachment(int index) {
        Attachment obj = new Attachment();
        setDisplayFileName(obj, index);
        setRealFileName(obj, index);
        setWorkItem(obj, index);
        return obj;
    }
    
    public void AttachmentDataOnDemand.setDisplayFileName(Attachment obj, int index) {
        String displayFileName = "displayFileName_" + index;
        obj.setDisplayFileName(displayFileName);
    }
    
    public void AttachmentDataOnDemand.setRealFileName(Attachment obj, int index) {
        String realFileName = "realFileName_" + index;
        obj.setRealFileName(realFileName);
    }
    
    public void AttachmentDataOnDemand.setWorkItem(Attachment obj, int index) {
        WorkItem workItem = workItemDataOnDemand.getRandomWorkItem();
        obj.setWorkItem(workItem);
    }
    
    public Attachment AttachmentDataOnDemand.getSpecificAttachment(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Attachment obj = data.get(index);
        Long id = obj.getId();
        return Attachment.findAttachment(id);
    }
    
    public Attachment AttachmentDataOnDemand.getRandomAttachment() {
        init();
        Attachment obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Attachment.findAttachment(id);
    }
    
    public boolean AttachmentDataOnDemand.modifyAttachment(Attachment obj) {
        return false;
    }
    
    public void AttachmentDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Attachment.findAttachmentEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Attachment' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Attachment>();
        for (int i = 0; i < 10; i++) {
            Attachment obj = getNewTransientAttachment(i);
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