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
    
    
}
