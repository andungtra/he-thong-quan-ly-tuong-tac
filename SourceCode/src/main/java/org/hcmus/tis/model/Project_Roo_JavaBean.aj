// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.hcmus.tis.model;

import java.util.Set;
import org.hcmus.tis.model.MemberInformation;
import org.hcmus.tis.model.Project;
import org.hcmus.tis.model.StudyClass;

privileged aspect Project_Roo_JavaBean {
    
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
    
    public Set<MemberInformation> Project.getMemberInformations() {
        return this.memberInformations;
    }
    
    public void Project.setMemberInformations(Set<MemberInformation> memberInformations) {
        this.memberInformations = memberInformations;
    }
    
}
