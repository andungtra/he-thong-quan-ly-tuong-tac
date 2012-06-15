package org.hcmus.tis.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Transient;

import org.hcmus.tis.dto.SearchConditionsDTO;
import org.hcmus.tis.repository.WorkItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
@Configurable
public class Iteration extends WorkItemContainer {
	@Autowired
	@Transient
	WorkItemRepository workItemRepository;
	public Project getParentProjectOrMyself(){
		WorkItemContainer parentProject = this.getParentContainer();
		while(parentProject.getClass() != Project.class){
			parentProject = parentProject.getParentContainer();
		}
		return (Project) parentProject;
	}
/*	public static Collection<Iteration> getdescendantIterations (WorkItemContainer parent){
		Collection<Iteration> iterations = new HashSet<Iteration>();
		for(WorkItemContainer workItemContainer : parent.getChildren()){
			if(workItemContainer instanceof Iteration && !iterations.contains(workItemContainer)){
				iterations.add((Iteration) workItemContainer);
				iterations.addAll(getdescendantIterations(workItemContainer));
			}
		}
		return iterations;		
	}*/
	
	public int getTotalTasks(){	
		return (int) workItemRepository.countByAncestorContainter(this, null);
	}
	
	public int getOpenTasks(){
		return (int)workItemRepository.countByAncestorContainter(this, false);
	}
}
