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
import org.hcmus.tis.model.ProjectProcess;
import org.hcmus.tis.model.ProjectProcessDataOnDemand;
import org.hcmus.tis.service.ProjectProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect ProjectProcessDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ProjectProcessDataOnDemand: @Component;
    
    private Random ProjectProcessDataOnDemand.rnd = new SecureRandom();
    
    private List<ProjectProcess> ProjectProcessDataOnDemand.data;
    
    @Autowired
    ProjectProcessService ProjectProcessDataOnDemand.projectProcessService;
    
    public ProjectProcess ProjectProcessDataOnDemand.getNewTransientProjectProcess(int index) {
        ProjectProcess obj = new ProjectProcess();
        setDescription(obj, index);
        setIsDeleted(obj, index);
        setName(obj, index);
        setProcessTemplateFile(obj, index);
        setUniqueName(obj, index);
        return obj;
    }
    
    public void ProjectProcessDataOnDemand.setDescription(ProjectProcess obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void ProjectProcessDataOnDemand.setIsDeleted(ProjectProcess obj, int index) {
        Boolean isDeleted = true;
        obj.setIsDeleted(isDeleted);
    }
    
    public void ProjectProcessDataOnDemand.setName(ProjectProcess obj, int index) {
        String name = "name_" + index;
        if (name.length() > 50) {
            name = name.substring(0, 50);
        }
        obj.setName(name);
    }
    
    public void ProjectProcessDataOnDemand.setProcessTemplateFile(ProjectProcess obj, int index) {
        byte[] processTemplateFile = String.valueOf(index).getBytes();
        obj.setProcessTemplateFile(processTemplateFile);
    }
    
    public void ProjectProcessDataOnDemand.setUniqueName(ProjectProcess obj, int index) {
        String uniqueName = "uniqueName_" + index;
        obj.setUniqueName(uniqueName);
    }
    
    public ProjectProcess ProjectProcessDataOnDemand.getSpecificProjectProcess(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ProjectProcess obj = data.get(index);
        Long id = obj.getId();
        return projectProcessService.findProjectProcess(id);
    }
    
    public ProjectProcess ProjectProcessDataOnDemand.getRandomProjectProcess() {
        init();
        ProjectProcess obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return projectProcessService.findProjectProcess(id);
    }
    
    public boolean ProjectProcessDataOnDemand.modifyProjectProcess(ProjectProcess obj) {
        return false;
    }
    
    public void ProjectProcessDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = projectProcessService.findProjectProcessEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ProjectProcess' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ProjectProcess>();
        for (int i = 0; i < 10; i++) {
            ProjectProcess obj = getNewTransientProjectProcess(i);
            try {
                projectProcessService.saveProjectProcess(obj);
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