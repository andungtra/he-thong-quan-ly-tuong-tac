// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import org.hcmus.tis.model.Attachment;
import org.hcmus.tis.model.WorkItem;

privileged aspect Attachment_Roo_JavaBean {
    
    public String Attachment.getDisplayFileName() {
        return this.displayFileName;
    }
    
    public void Attachment.setDisplayFileName(String displayFileName) {
        this.displayFileName = displayFileName;
    }
    
    public String Attachment.getRealFileName() {
        return this.realFileName;
    }
    
    public void Attachment.setRealFileName(String realFileName) {
        this.realFileName = realFileName;
    }
    
    public WorkItem Attachment.getWorkItem() {
        return this.workItem;
    }
    
    public void Attachment.setWorkItem(WorkItem workItem) {
        this.workItem = workItem;
    }
    
}