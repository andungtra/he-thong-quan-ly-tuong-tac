// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.StudyClass;

privileged aspect Project_Roo_JavaBean {
    
    public String Project.getName() {
        return this.name;
    }
    
    public void Project.setName(String name) {
        this.name = name;
    }
    
    public String Project.getDescription() {
        return this.description;
    }
    
    public void Project.setDescription(String description) {
        this.description = description;
    }
    
    public StudyClass Project.getStudyClass() {
        return this.studyClass;
    }
    
    public void Project.setStudyClass(StudyClass studyClass) {
        this.studyClass = studyClass;
    }
    
}