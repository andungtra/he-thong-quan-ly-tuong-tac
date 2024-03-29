// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.hcmus.tis.model.Comment;
import org.hcmus.tis.model.CommentDataOnDemand;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.MemberInformationDataOnDemand;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect CommentDataOnDemand_Roo_DataOnDemand {
    
    declare @type: CommentDataOnDemand: @Component;
    
    private Random CommentDataOnDemand.rnd = new SecureRandom();
    
    private List<Comment> CommentDataOnDemand.data;
    
    @Autowired
    private MemberInformationDataOnDemand CommentDataOnDemand.memberInformationDataOnDemand;
    
    @Autowired
    private WorkItemDataOnDemand CommentDataOnDemand.workItemDataOnDemand;

    
}
