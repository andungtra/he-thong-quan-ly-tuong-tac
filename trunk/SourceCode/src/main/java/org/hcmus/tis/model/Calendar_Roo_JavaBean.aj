// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.Collection;
import org.hcmus.tis.model.Account;
import org.hcmus.tis.model.Calendar;
import org.hcmus.tis.model.Event;
import org.hcmus.tis.model.Project;

privileged aspect Calendar_Roo_JavaBean {
    
    public Collection<Event> Calendar.getEvents() {
        return this.events;
    }
    
    public void Calendar.setEvents(Collection<Event> events) {
        this.events = events;
    }
    
    public Account Calendar.getAccount() {
        return this.account;
    }
    
    public void Calendar.setAccount(Account account) {
        this.account = account;
    }
    
    public Project Calendar.getProject() {
        return this.project;
    }
    
    public void Calendar.setProject(Project project) {
        this.project = project;
    }
    
}