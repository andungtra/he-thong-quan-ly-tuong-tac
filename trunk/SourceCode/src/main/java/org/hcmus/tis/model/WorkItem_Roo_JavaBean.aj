// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.Date;
import javax.xml.bind.JAXBContext;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Priority;
import org.hcmus.tis.model.WorkItem;
import org.hcmus.tis.model.WorkItemContainer;
import org.hcmus.tis.model.WorkItemStatus;
import org.hcmus.tis.model.WorkItemType;
import org.hcmus.tis.repository.WorkItemHistoryRepository;
import org.hcmus.tis.repository.WorkItemRepository;

privileged aspect WorkItem_Roo_JavaBean {
    
    public WorkItemRepository WorkItem.getWorkItemRepository() {
        return this.workItemRepository;
    }
    
    public void WorkItem.setWorkItemRepository(WorkItemRepository workItemRepository) {
        this.workItemRepository = workItemRepository;
    }
    
    public WorkItemHistoryRepository WorkItem.getWorkItemHistoryRepository() {
        return this.workItemHistoryRepository;
    }
    
    public void WorkItem.setWorkItemHistoryRepository(WorkItemHistoryRepository workItemHistoryRepository) {
        this.workItemHistoryRepository = workItemHistoryRepository;
    }
    
    public JAXBContext WorkItem.getJaxbContext() {
        return this.jaxbContext;
    }
    
    public void WorkItem.setJaxbContext(JAXBContext jaxbContext) {
        this.jaxbContext = jaxbContext;
    }
    
    public String WorkItem.getTitle() {
        return this.title;
    }
    
    public void WorkItem.setTitle(String title) {
        this.title = title;
    }
    
    public String WorkItem.getDescription() {
        return this.description;
    }
    
    public void WorkItem.setDescription(String description) {
        this.description = description;
    }
    
    public Date WorkItem.getDateCreated() {
        return this.dateCreated;
    }
    
    public void WorkItem.setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public Date WorkItem.getDateLastEdit() {
        return this.dateLastEdit;
    }
    
    public void WorkItem.setDateLastEdit(Date dateLastEdit) {
        this.dateLastEdit = dateLastEdit;
    }
    
    public MemberInformation WorkItem.getUserLastEdit() {
        return this.userLastEdit;
    }
    
    public void WorkItem.setUserLastEdit(MemberInformation userLastEdit) {
        this.userLastEdit = userLastEdit;
    }
    
    public String WorkItem.getAdditionalFields() {
        return this.additionalFields;
    }
    
    public void WorkItem.setAdditionalFields(String additionalFields) {
        this.additionalFields = additionalFields;
    }
    
    public Priority WorkItem.getPriority() {
        return this.priority;
    }
    
    public void WorkItem.setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public WorkItemContainer WorkItem.getWorkItemContainer() {
        return this.workItemContainer;
    }
    
    public void WorkItem.setWorkItemContainer(WorkItemContainer workItemContainer) {
        this.workItemContainer = workItemContainer;
    }
    
    public WorkItemType WorkItem.getWorkItemType() {
        return this.workItemType;
    }
    
    public void WorkItem.setWorkItemType(WorkItemType workItemType) {
        this.workItemType = workItemType;
    }
    
    public MemberInformation WorkItem.getAuthor() {
        return this.author;
    }
    
    public void WorkItem.setAuthor(MemberInformation author) {
        this.author = author;
    }
    
    public MemberInformation WorkItem.getAsignee() {
        return this.asignee;
    }
    
    public void WorkItem.setAsignee(MemberInformation asignee) {
        this.asignee = asignee;
    }
    
    public WorkItemStatus WorkItem.getStatus() {
        return this.status;
    }
    
    public void WorkItem.setStatus(WorkItemStatus status) {
        this.status = status;
    }
    
    public Date WorkItem.getDueDate() {
        return this.dueDate;
    }
    
    public void WorkItem.setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
}
