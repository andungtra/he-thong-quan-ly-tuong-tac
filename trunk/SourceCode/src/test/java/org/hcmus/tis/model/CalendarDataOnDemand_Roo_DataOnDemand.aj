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
import org.hcmus.tis.model.Calendar;
import org.hcmus.tis.model.CalendarDataOnDemand;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.ProjectDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect CalendarDataOnDemand_Roo_DataOnDemand {
    
    declare @type: CalendarDataOnDemand: @Component;
    
    private Random CalendarDataOnDemand.rnd = new SecureRandom();
    
    private List<Calendar> CalendarDataOnDemand.data;
    
    @Autowired
    private AccountDataOnDemand CalendarDataOnDemand.accountDataOnDemand;
    
    @Autowired
    private ProjectDataOnDemand CalendarDataOnDemand.projectDataOnDemand;
    
    public Calendar CalendarDataOnDemand.getNewTransientCalendar(int index) {
        Calendar obj = new Calendar();
        setAccount(obj, index);
        setProject(obj, index);
        return obj;
    }
    
    public void CalendarDataOnDemand.setAccount(Calendar obj, int index) {
        Account account = accountDataOnDemand.getSpecificAccount(index);
        obj.setAccount(account);
    }
    
    public void CalendarDataOnDemand.setProject(Calendar obj, int index) {
        Project project = projectDataOnDemand.getSpecificProject(index);
        obj.setProject(project);
    }
    
    public Calendar CalendarDataOnDemand.getSpecificCalendar(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Calendar obj = data.get(index);
        Long id = obj.getId();
        return Calendar.findCalendar(id);
    }
    
    public Calendar CalendarDataOnDemand.getRandomCalendar() {
        init();
        Calendar obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Calendar.findCalendar(id);
    }
    
    public boolean CalendarDataOnDemand.modifyCalendar(Calendar obj) {
        return false;
    }
    
    public void CalendarDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Calendar.findCalendarEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Calendar' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Calendar>();
        for (int i = 0; i < 10; i++) {
            Calendar obj = getNewTransientCalendar(i);
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
