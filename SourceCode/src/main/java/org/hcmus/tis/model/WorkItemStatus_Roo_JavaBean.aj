// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import org.hcmus.tis.model.WorkItemStatus;

privileged aspect WorkItemStatus_Roo_JavaBean {
    
    public String WorkItemStatus.getName() {
        return this.name;
    }
    
    public void WorkItemStatus.setName(String name) {
        this.name = name;
    }
    
    public boolean WorkItemStatus.isClosed() {
        return this.closed;
    }
    
    public void WorkItemStatus.setClosed(boolean closed) {
        this.closed = closed;
    }
    
}