// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.Date;
import org.hcmus.tis.model.Event;

privileged aspect Event_Roo_JavaBean {
    
    public String Event.getName() {
        return this.name;
    }
    
    public void Event.setName(String name) {
        this.name = name;
    }
    
    public String Event.getDescription() {
        return this.description;
    }
    
    public void Event.setDescription(String description) {
        this.description = description;
    }
    
    public Date Event.getStartDate() {
        return this.startDate;
    }
    
    public void Event.setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date Event.getEndDate() {
        return this.endDate;
    }
    
    public void Event.setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
}
