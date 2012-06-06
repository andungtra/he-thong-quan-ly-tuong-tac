package org.hcmus.tis.model;

import java.util.Collection;
import java.util.HashSet;

import org.hcmus.tis.dto.SearchConditionsDTO;
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
	public static Collection<Iteration> getdescendantIterations (WorkItemContainer parent){
		Collection<Iteration> iterations = new HashSet<Iteration>();
		for(WorkItemContainer workItemContainer : parent.getChildren()){
			if(workItemContainer instanceof Iteration && !iterations.contains(workItemContainer)){
				iterations.add((Iteration) workItemContainer);
				iterations.addAll(getdescendantIterations(workItemContainer));
			}
		}
		return iterations;		
	}
	
	public int getTotalTasks(){
		SearchConditionsDTO condition = new SearchConditionsDTO();
		condition.setContainer(this);		
		return (int) WorkItem.getTotalRecord(condition);
	}
	
	public int getOpenTasks(){
		return (int) WorkItem.countOpenWorkItemInParent(this);
	}
}
