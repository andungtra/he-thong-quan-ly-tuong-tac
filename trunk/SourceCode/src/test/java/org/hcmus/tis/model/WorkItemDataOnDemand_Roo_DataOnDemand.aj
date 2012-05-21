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
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.MemberInformationDataOnDemand;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.PriorityDataOnDemand;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemDataOnDemand;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.model.WorkItemStatusDataOnDemand;
import org.hcmus.tis.model.WorkItemType;
import org.hcmus.tis.model.WorkItemTypeDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect WorkItemDataOnDemand_Roo_DataOnDemand {
    
    declare @type: WorkItemDataOnDemand: @Component;
    
    private Random WorkItemDataOnDemand.rnd = new SecureRandom();
    
    private List<WorkItem> WorkItemDataOnDemand.data;
    
    @Autowired
    private MemberInformationDataOnDemand WorkItemDataOnDemand.memberInformationDataOnDemand;
    
    @Autowired
    private PriorityDataOnDemand WorkItemDataOnDemand.priorityDataOnDemand;
    
    @Autowired
    private WorkItemStatusDataOnDemand WorkItemDataOnDemand.workItemStatusDataOnDemand;
    
    @Autowired
    private WorkItemTypeDataOnDemand WorkItemDataOnDemand.workItemTypeDataOnDemand;
    
    public WorkItem WorkItemDataOnDemand.getNewTransientWorkItem(int index) {
        WorkItem obj = new WorkItem();
        setAdditionalFields(obj, index);
        setAsignee(obj, index);
        setAuthor(obj, index);
        setDateCreated(obj, index);
        setDateLastEdit(obj, index);
        setDescription(obj, index);
        setDueDate(obj, index);
        setPriority(obj, index);
        setStatus(obj, index);
        setTitle(obj, index);
        setWorkItemContainer(obj, index);
        setWorkItemType(obj, index);
        return obj;
    }
    
    public void WorkItemDataOnDemand.setAdditionalFields(WorkItem obj, int index) {
        String additionalFields = "additionalFields_" + index;
        obj.setAdditionalFields(additionalFields);
    }
    
    public void WorkItemDataOnDemand.setAsignee(WorkItem obj, int index) {
        MemberInformation asignee = memberInformationDataOnDemand.getRandomMemberInformation();
        obj.setAsignee(asignee);
    }
    
    public void WorkItemDataOnDemand.setAuthor(WorkItem obj, int index) {
        MemberInformation author = memberInformationDataOnDemand.getRandomMemberInformation();
        obj.setAuthor(author);
    }
    
    public void WorkItemDataOnDemand.setDateCreated(WorkItem obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void WorkItemDataOnDemand.setDateLastEdit(WorkItem obj, int index) {
        Date dateLastEdit = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateLastEdit(dateLastEdit);
    }
    
    public void WorkItemDataOnDemand.setDescription(WorkItem obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void WorkItemDataOnDemand.setDueDate(WorkItem obj, int index) {
        Date dueDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDueDate(dueDate);
    }
    
    public void WorkItemDataOnDemand.setPriority(WorkItem obj, int index) {
        Priority priority = priorityDataOnDemand.getRandomPriority();
        obj.setPriority(priority);
    }
    
    public void WorkItemDataOnDemand.setStatus(WorkItem obj, int index) {
        WorkItemStatus status = workItemStatusDataOnDemand.getRandomWorkItemStatus();
        obj.setStatus(status);
    }
    
    public void WorkItemDataOnDemand.setTitle(WorkItem obj, int index) {
        String title = "title_" + index;
        if (title.length() > 50) {
            title = title.substring(0, 50);
        }
        obj.setTitle(title);
    }
    
    public void WorkItemDataOnDemand.setWorkItemType(WorkItem obj, int index) {
        WorkItemType workItemType = workItemTypeDataOnDemand.getRandomWorkItemType();
        obj.setWorkItemType(workItemType);
    }
    
    public WorkItem WorkItemDataOnDemand.getSpecificWorkItem(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        WorkItem obj = data.get(index);
        Long id = obj.getId();
        return WorkItem.findWorkItem(id);
    }
    
    public WorkItem WorkItemDataOnDemand.getRandomWorkItem() {
        init();
        WorkItem obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return WorkItem.findWorkItem(id);
    }
    
    public boolean WorkItemDataOnDemand.modifyWorkItem(WorkItem obj) {
        return false;
    }
    
    public void WorkItemDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = WorkItem.findWorkItemEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'WorkItem' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<WorkItem>();
        for (int i = 0; i < 10; i++) {
            WorkItem obj = getNewTransientWorkItem(i);
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
