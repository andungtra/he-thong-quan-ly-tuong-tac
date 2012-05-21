package org.hcmus.tis.model;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findIterationsByParentContainer" })
public class Iteration extends WorkItemContainer {
	public Project getParentProjectOrMyself(){
		WorkItemContainer parentProject = this.getParentContainer();
		while(parentProject.getClass() != Project.class){
			parentProject = parentProject.getParentContainer();
		}
		return (Project) parentProject;
	}
}
